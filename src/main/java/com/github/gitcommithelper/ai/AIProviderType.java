package com.github.gitcommithelper.ai;

/**
 * Enumeration of supported AI provider types
 */
public enum AIProviderType {
    NONE("None (Rule-based only)", ""),
    OPENAI("OpenAI GPT", "https://api.openai.com/v1"),
    CLAUDE("Claude (Anthropic)", "https://api.anthropic.com"),
    DEEPSEEK("DeepSeek", "https://api.deepseek.com"),
    ZHIPU("智谱 AI (GLM)", "https://open.bigmodel.cn/api/paas/v4"),
    QWEN("阿里通义千问", "https://dashscope.aliyuncs.com/compatible-mode/v1"),
    AZURE_OPENAI("Azure OpenAI", ""),
    OLLAMA("Ollama (Local)", "http://localhost:11434"),
    CUSTOM("Custom Endpoint", "");

    private final String displayName;
    private final String defaultEndpoint;

    AIProviderType(String displayName, String defaultEndpoint) {
        this.displayName = displayName;
        this.defaultEndpoint = defaultEndpoint;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDefaultEndpoint() {
        return defaultEndpoint;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
