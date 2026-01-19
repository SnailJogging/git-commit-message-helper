package com.github.gitcommithelper.ai;

import com.github.gitcommithelper.model.FileChangeInfo;
import com.github.gitcommithelper.settings.PluginSettings;
import com.github.gitcommithelper.util.SSLUtil;
import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;
import java.util.List;

/**
 * Base class for AI providers with common functionality
 */
public abstract class BaseAIProvider implements AIProvider {

    protected final PluginSettings settings;

    protected BaseAIProvider(PluginSettings settings) {
        this.settings = settings;
    }

    @Override
    public boolean isAvailable() {
        String apiKey = getApiKey();
        String endpoint = getEndpoint();
        return apiKey != null && !apiKey.trim().isEmpty()
                && endpoint != null && !endpoint.trim().isEmpty();
    }

    @Override
    public int getMaxTokens() {
        return settings.getMaxTokens();
    }

    @Override
    public int getTimeout() {
        return settings.getTimeoutSeconds() * 1000;
    }

    protected String getApiKey() {
        return settings.getApiKey();
    }

    protected String getEndpoint() {
        return settings.getApiEndpoint();
    }

    protected String getModel() {
        return settings.getModel();
    }

    protected double getTemperature() {
        return settings.getTemperature();
    }

    /**
     * Builds the prompt for AI to generate commit message
     */
    protected String buildPrompt(@NotNull String diffContent, @NotNull List<FileChangeInfo> fileChanges) {
        StringBuilder prompt = new StringBuilder();

        // Use custom prompt template if available
        String customPrompt = settings.getPromptTemplate();
        if (customPrompt != null && !customPrompt.trim().isEmpty()) {
            prompt.append(customPrompt).append("\n\n");
        } else {
            prompt.append(getDefaultPrompt()).append("\n\n");
        }

        // Add file changes summary
        prompt.append("Files changed:\n");
        for (FileChangeInfo file : fileChanges) {
            prompt.append("- ").append(file.getChangeType()).append(": ")
                    .append(file.getFileName()).append("\n");
        }
        prompt.append("\n");

        // Add diff content (truncated if too long)
        prompt.append("Git diff:\n```\n");
        String truncatedDiff = truncateDiff(diffContent, 4000);
        prompt.append(truncatedDiff);
        prompt.append("\n```\n\n");

        prompt.append("Generate ONLY the commit message following Conventional Commits format (type(scope): subject). ");
        prompt.append("Do not include any explanation or additional text.");

        return prompt.toString();
    }

    /**
     * Gets the default prompt template based on language setting
     */
    protected String getDefaultPrompt() {
        String language = settings.getMessageLanguage();

        // Chinese prompt (default)
        if ("zh-CN".equals(language) || "zh".equals(language)) {
            return "你是一个 Git 提交信息专家。分析以下代码变更，生成简洁、有意义的提交信息，遵循 Conventional Commits 规范。\n\n" +
                    "格式：type(scope): subject\n\n" +
                    "类型：feat（新功能）, fix（修复）, docs（文档）, style（格式）, refactor（重构）, test（测试）, chore（杂项）, build（构建）, ci（持续集成）\n\n" +
                    "规则：\n" +
                    "- 主题应清晰描述【改了什么】和【为什么改】\n" +
                    "- type 和 scope 使用英文小写，subject 使用中文\n" +
                    "- 不要以句号结尾\n" +
                    "- 要具体说明业务逻辑或功能变化\n" +
                    "- 整个消息不超过 100 个字符\n" +
                    "- 示例：feat(user): 添加用户名验证以防止 li 开头的用户名冲突\n" +
                    "- 示例：fix(auth): 修复登录时的空指针异常";
        }

        // English prompt
        return "You are a Git commit message expert. Analyze the following code changes and generate a concise, " +
                "meaningful commit message following the Conventional Commits specification.\n\n" +
                "Format: type(scope): subject\n\n" +
                "Types: feat, fix, docs, style, refactor, test, chore, build, ci\n\n" +
                "Rules:\n" +
                "- Subject should be clear and describe WHAT changed and WHY\n" +
                "- Use lowercase for subject\n" +
                "- No period at the end\n" +
                "- Be specific about the business logic or functionality changed\n" +
                "- Maximum 72 characters for the entire message";
    }

    /**
     * Truncates diff content to fit within token limits
     */
    protected String truncateDiff(String diff, int maxChars) {
        if (diff.length() <= maxChars) {
            return diff;
        }
        return diff.substring(0, maxChars) + "\n... (diff truncated)";
    }

    /**
     * Extracts the commit message from AI response
     */
    protected String extractCommitMessage(String response) {
        if (response == null || response.trim().isEmpty()) {
            return null;
        }

        // Remove common prefixes/suffixes that AI might add
        String cleaned = response.trim()
                .replaceAll("^(Commit message:|Message:|Here's the commit message:)\\s*", "")
                .replaceAll("^[\"'`]|[\"'`]$", "")
                .trim();

        // Take only the first line if multi-line
        int newlineIndex = cleaned.indexOf('\n');
        if (newlineIndex > 0) {
            cleaned = cleaned.substring(0, newlineIndex).trim();
        }

        return cleaned;
    }

    /**
     * Configures SSL certificate handling for HttpURLConnection
     * <p>
     * If trustAllCertificates is enabled in settings, this will configure
     * the connection to trust all SSL certificates, which is useful in
     * corporate proxy environments (e.g., mitmproxy).
     * </p>
     *
     * @param connection the HttpURLConnection to configure
     */
    protected void configureSSL(HttpURLConnection connection) {
        if (settings.isTrustAllCertificates()) {
            SSLUtil.trustAllCertificates(connection);
        }
    }
}
