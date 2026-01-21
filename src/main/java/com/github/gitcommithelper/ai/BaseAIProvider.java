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
            return "你是一个 Git 提交信息专家。你的任务是分析代码变更，生成能够准确描述【业务逻辑变化】的提交信息。\n\n" +
                    "# 核心原则\n" +
                    "提交信息应该让团队成员理解这次变更对业务功能、用户体验或系统行为产生了什么影响，而不仅仅是代码层面做了什么修改。\n\n" +
                    "# 分析步骤\n" +
                    "1. **识别变更内容**：仔细查看所有文件的变更，包括新增、修改、删除的代码\n" +
                    "2. **理解变更关系**：分析不同文件的变更之间的关联，识别完整的功能变更\n" +
                    "3. **推断变更意图**：从代码变更推断出业务目标、问题修复或功能改进\n" +
                    "4. **形成描述策略**：\n" +
                    "   - 如果是新增业务功能 → 描述添加了什么能力\n" +
                    "   - 如果是修复问题 → 描述修复了什么具体问题及其影响\n" +
                    "   - 如果是优化改进 → 描述改进了什么体验或性能\n" +
                    "   - 如果是多处关联变更 → 找出核心业务目标进行概括\n\n" +
                    "# 变更类别识别\n" +
                    "- **数据层变更**（模型/实体）→ 关注数据结构变化对业务的影响\n" +
                    "- **业务逻辑变更**（服务/控制器）→ 关注业务规则和流程的变化\n" +
                    "- **接口变更**（API/端点）→ 关注对外提供能力的变化\n" +
                    "- **交互变更**（UI/前端）→ 关注用户体验和交互流程的变化\n" +
                    "- **基础设施变更**（配置/构建）→ 关注系统能力和运维的变化\n\n" +
                    "# 优秀示例\n\n" +
                    "## 场景1：新增业务功能\n" +
                    "- ✅ `feat(user): 添加用户名验证以防止 li 开头的用户名冲突`\n" +
                    "- ✅ `feat(order): 支持订单批量导出为 Excel 文件`\n" +
                    "- ✅ `feat(payment): 集成支付宝支付渠道`\n" +
                    "- ❌ `feat(user): 添加 username 字段和验证逻辑` (过于技术化)\n" +
                    "- ❌ `feat: 添加新功能` (过于笼统)\n\n" +
                    "## 场景2：修复业务问题\n" +
                    "- ✅ `fix(auth): 修复退出登录后 session 未清理导致的权限混乱`\n" +
                    "- ✅ `fix(cart): 修复购物车商品数量更新时的价格计算错误`\n" +
                    "- ✅ `fix(search): 修复搜索结果为空时的空指针异常`\n" +
                    "- ❌ `fix(auth): 修复空指针` (未说明影响)\n" +
                    "- ❌ `fix: 修复 bug` (过于笼统)\n\n" +
                    "## 场景3：优化改进\n" +
                    "- ✅ `perf(list): 优化商品列表查询性能，支持百万级数据分页`\n" +
                    "- ✅ `refactor(cache): 重构缓存策略以减少 Redis 连接数`\n" +
                    "- ✅ `style(form): 统一表单验证错误提示样式`\n" +
                    "- ❌ `perf: 优化查询` (未说明优化什么)\n" +
                    "- ❌ `refactor: 代码重构` (过于笼统)\n\n" +
                    "## 场景4：数据模型变更\n" +
                    "- ✅ `feat(user): 添加用户手机号字段以支持短信登录`\n" +
                    "- ✅ `feat(product): 扩展商品属性支持多规格 SKU`\n" +
                    "- ❌ `feat(user): 添加 phone 字段` (未说明业务目的)\n\n" +
                    "## 场景5：API 接口变更\n" +
                    "- ✅ `feat(api): 新增批量删除用户接口以支持后台管理`\n" +
                    "- ✅ `feat(api): 订单查询接口支持按状态筛选`\n" +
                    "- ❌ `feat(api): 添加新接口` (未说明用途)\n\n" +
                    "## 场景6：配置和依赖\n" +
                    "- ✅ `build(deps): 升级 Spring Boot 至 3.2 以支持虚拟线程`\n" +
                    "- ✅ `chore(config): 配置生产环境数据库连接池以提升并发性能`\n" +
                    "- ❌ `build: 更新依赖` (未说明目的)\n\n" +
                    "# 复杂场景处理\n\n" +
                    "**场景A：多文件关联变更（添加新功能）**\n" +
                    "变更：User.java 添加 email 字段 + UserService.java 添加邮箱验证 + UserController.java 添加注册接口\n" +
                    "- ✅ `feat(user): 支持邮箱注册并发送验证邮件`\n" +
                    "- ❌ `feat(user): 添加邮箱字段和验证逻辑` (仅描述代码变更)\n\n" +
                    "**场景B：跨层次修复**\n" +
                    "变更：OrderService.java 修改库存检查逻辑 + Order.java 添加状态字段\n" +
                    "- ✅ `fix(order): 修复高并发下的库存超卖问题`\n" +
                    "- ❌ `fix(order): 修改库存检查并添加状态字段` (仅描述代码变更)\n\n" +
                    "**场景C：重构但不改变功能**\n" +
                    "变更：拆分大型 Service 类，提取工具方法\n" +
                    "- ✅ `refactor(service): 拆分订单服务以提升代码可维护性`\n" +
                    "- ❌ `refactor: 重构代码` (过于笼统)\n\n" +
                    "**场景D：同时包含多个独立变更**\n" +
                    "变更：添加日志 + 修复权限 bug + 调整格式\n" +
                    "- ✅ `fix(auth): 修复管理员权限校验失败导致的访问拒绝` (选择最重要的)\n" +
                    "- ❌ `chore: 添加日志修复 bug 调整格式` (混杂多个变更)\n\n" +
                    "# 格式规范\n" +
                    "**格式**：`type(scope): subject`\n\n" +
                    "**类型（type）**：\n" +
                    "- feat: 新功能\n" +
                    "- fix: 修复问题\n" +
                    "- docs: 文档变更\n" +
                    "- style: 代码格式（不影响功能）\n" +
                    "- refactor: 重构（不改变功能）\n" +
                    "- perf: 性能优化\n" +
                    "- test: 测试相关\n" +
                    "- build: 构建系统或依赖\n" +
                    "- ci: CI/CD 配置\n" +
                    "- chore: 其他杂项\n\n" +
                    "**范围（scope）**：模块名，如 user, order, auth, api\n\n" +
                    "**主题（subject）**：\n" +
                    "- type 和 scope 使用英文小写\n" +
                    "- subject 使用中文，清晰描述业务影响\n" +
                    "- 不以句号结尾\n" +
                    "- 整体不超过 100 个字符\n" +
                    "- 优先描述【做了什么】和【为什么/解决什么问题】\n\n" +
                    "# 反面示例（避免）\n" +
                    "- ❌ `update code` - 过于笼统\n" +
                    "- ❌ `fix bug` - 未说明修复了什么\n" +
                    "- ❌ `feat(user): 修改 User.java` - 只描述文件变更\n" +
                    "- ❌ `feat(user): 添加 validation 方法` - 只描述代码变更\n" +
                    "- ❌ `feat(user): update user logic and add new field` - 使用英文且过于技术化\n" +
                    "- ❌ `用户功能更新` - 缺少类型和范围\n" +
                    "- ❌ `feat: 实现需求 #1234` - 缺少具体描述\n" +
                    "- ❌ `feat(user): 添加用户名验证，修复登录bug，更新文档` - 混合多个变更\n" +
                    "- ❌ `修复了一个很严重的问题` - 缺少具体性\n" +
                    "- ❌ `feat(user): 为 User 类添加了 email 属性和对应的 getter/setter 方法` - 过于冗长和技术化";
        }

        // English prompt
        return "You are a Git commit message expert. Your task is to analyze code changes and generate commit messages that accurately describe **business logic changes**.\n\n" +
                "# Core Principle\n" +
                "Commit messages should help team members understand how this change affects business functionality, user experience, or system behavior, not just what code-level modifications were made.\n\n" +
                "# Analysis Steps\n" +
                "1. **Identify Changes**: Carefully review all file changes, including added, modified, and deleted code\n" +
                "2. **Understand Relationships**: Analyze connections between changes in different files to identify complete feature changes\n" +
                "3. **Infer Intent**: Deduce business goals, problem fixes, or feature improvements from code changes\n" +
                "4. **Form Description Strategy**:\n" +
                "   - If adding business feature → describe what capability was added\n" +
                "   - If fixing problem → describe what specific issue was fixed and its impact\n" +
                "   - If optimizing → describe what experience or performance was improved\n" +
                "   - If multiple related changes → identify core business goal for summary\n\n" +
                "# Change Category Recognition\n" +
                "- **Data Layer Changes** (models/entities) → focus on how data structure changes affect business\n" +
                "- **Business Logic Changes** (services/controllers) → focus on business rule and process changes\n" +
                "- **API Changes** (endpoints) → focus on changes to external capabilities\n" +
                "- **Interaction Changes** (UI/frontend) → focus on user experience and interaction flow changes\n" +
                "- **Infrastructure Changes** (config/build) → focus on system capability and operational changes\n\n" +
                "# Excellent Examples\n\n" +
                "## Scenario 1: Adding Business Feature\n" +
                "- ✅ `feat(user): add username validation to prevent conflicts with li-prefix usernames`\n" +
                "- ✅ `feat(order): support batch export of orders to Excel`\n" +
                "- ✅ `feat(payment): integrate Alipay payment channel`\n" +
                "- ❌ `feat(user): add username field and validation logic` (too technical)\n" +
                "- ❌ `feat: add new feature` (too vague)\n\n" +
                "## Scenario 2: Fixing Business Issues\n" +
                "- ✅ `fix(auth): fix session not cleared after logout causing permission confusion`\n" +
                "- ✅ `fix(cart): fix price calculation error when updating item quantity`\n" +
                "- ✅ `fix(search): fix null pointer exception when search results are empty`\n" +
                "- ❌ `fix(auth): fix null pointer` (impact not stated)\n" +
                "- ❌ `fix: fix bug` (too vague)\n\n" +
                "## Scenario 3: Optimization and Improvement\n" +
                "- ✅ `perf(list): optimize product list query to support million-record pagination`\n" +
                "- ✅ `refactor(cache): refactor cache strategy to reduce Redis connections`\n" +
                "- ✅ `style(form): standardize form validation error message styles`\n" +
                "- ❌ `perf: optimize query` (what was optimized not stated)\n" +
                "- ❌ `refactor: code refactoring` (too vague)\n\n" +
                "## Scenario 4: Data Model Changes\n" +
                "- ✅ `feat(user): add phone number field to support SMS login`\n" +
                "- ✅ `feat(product): extend product attributes to support multi-spec SKU`\n" +
                "- ❌ `feat(user): add phone field` (business purpose not stated)\n\n" +
                "## Scenario 5: API Interface Changes\n" +
                "- ✅ `feat(api): add batch delete users endpoint for admin dashboard`\n" +
                "- ✅ `feat(api): support filtering by status in order query endpoint`\n" +
                "- ❌ `feat(api): add new endpoint` (purpose not stated)\n\n" +
                "## Scenario 6: Configuration and Dependencies\n" +
                "- ✅ `build(deps): upgrade Spring Boot to 3.2 to support virtual threads`\n" +
                "- ✅ `chore(config): configure production database connection pool for better concurrency`\n" +
                "- ❌ `build: update dependencies` (purpose not stated)\n\n" +
                "# Complex Scenario Handling\n\n" +
                "**Scenario A: Multi-file Related Changes (Adding Feature)**\n" +
                "Changes: User.java adds email field + UserService.java adds email validation + UserController.java adds registration endpoint\n" +
                "- ✅ `feat(user): support email registration with verification email`\n" +
                "- ❌ `feat(user): add email field and validation logic` (only describes code changes)\n\n" +
                "**Scenario B: Cross-layer Fix**\n" +
                "Changes: OrderService.java modifies inventory check logic + Order.java adds status field\n" +
                "- ✅ `fix(order): fix inventory overselling under high concurrency`\n" +
                "- ❌ `fix(order): modify inventory check and add status field` (only describes code changes)\n\n" +
                "**Scenario C: Refactoring Without Functional Change**\n" +
                "Changes: Split large Service class, extract utility methods\n" +
                "- ✅ `refactor(service): split order service to improve maintainability`\n" +
                "- ❌ `refactor: refactor code` (too vague)\n\n" +
                "**Scenario D: Multiple Independent Changes**\n" +
                "Changes: Add logging + fix permission bug + adjust formatting\n" +
                "- ✅ `fix(auth): fix admin permission check failure causing access denial` (choose most important)\n" +
                "- ❌ `chore: add logging fix bug adjust format` (mixing multiple changes)\n\n" +
                "# Format Specification\n" +
                "**Format**: `type(scope): subject`\n\n" +
                "**Type**:\n" +
                "- feat: new feature\n" +
                "- fix: bug fix\n" +
                "- docs: documentation\n" +
                "- style: code formatting (no functional change)\n" +
                "- refactor: refactoring (no functional change)\n" +
                "- perf: performance optimization\n" +
                "- test: testing\n" +
                "- build: build system or dependencies\n" +
                "- ci: CI/CD configuration\n" +
                "- chore: other miscellaneous\n\n" +
                "**Scope**: module name, e.g., user, order, auth, api\n\n" +
                "**Subject**:\n" +
                "- Use lowercase for type and scope\n" +
                "- Use lowercase for subject\n" +
                "- No period at the end\n" +
                "- Maximum 72 characters total\n" +
                "- Prioritize describing **what was done** and **why/what problem it solves**\n\n" +
                "# Anti-patterns (Avoid)\n" +
                "- ❌ `update code` - too vague\n" +
                "- ❌ `fix bug` - doesn't state what was fixed\n" +
                "- ❌ `feat(user): modify User.java` - only describes file change\n" +
                "- ❌ `feat(user): add validation method` - only describes code change\n" +
                "- ❌ `feat(user): 添加用户验证` - mixing languages\n" +
                "- ❌ `user feature update` - missing type and scope\n" +
                "- ❌ `feat: implement requirement #1234` - lacks specific description\n" +
                "- ❌ `feat(user): add username validation, fix login bug, update docs` - mixing multiple changes\n" +
                "- ❌ `fixed a critical issue` - lacks specificity\n" +
                "- ❌ `feat(user): added email property and corresponding getter/setter methods to User class` - too verbose and technical";
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
