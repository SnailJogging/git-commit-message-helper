# Development Guide

Git Commit Message Helper 开发指南，适合想要贡献代码或了解插件内部实现的开发者。

---

## 📋 目录

- [环境要求](#环境要求)
- [项目结构](#项目结构)
- [构建与运行](#构建与运行)
- [测试指南](#测试指南)
- [调试技巧](#调试技巧)
- [技术实现](#技术实现)
- [发布流程](#发布流程)

---

## 🔧 环境要求

### 必需环境

- **JDK**: 17 或更高版本
- **IntelliJ IDEA**: 2023.2 或更高版本（推荐使用最新版）
- **Gradle**: 8.5+（项目自带 Gradle Wrapper）
- **Git**: 2.0+

### 推荐工具

- **IntelliJ IDEA Ultimate**（用于插件开发）
- **Postman** 或 **curl**（用于测试 AI API）

### 系统要求

- **macOS**: 12.0+
- **Windows**: 10/11
- **Linux**: Ubuntu 20.04+ 或其他主流发行版

---

## 📁 项目结构

```
Git.Commit.Message.Helper/
├── src/
│   ├── main/
│   │   ├── java/com/github/gitcommithelper/
│   │   │   ├── action/              # 用户操作（Actions）
│   │   │   │   └── GenerateCommitMessageAction.java
│   │   │   ├── ai/                  # AI 提供者框架
│   │   │   │   ├── AIProvider.java          # AI 接口
│   │   │   │   ├── AIProviderFactory.java   # 工厂类
│   │   │   │   ├── AIProviderType.java      # 类型枚举
│   │   │   │   ├── BaseAIProvider.java      # 抽象基类
│   │   │   │   ├── OpenAIProvider.java      # OpenAI 实现
│   │   │   │   ├── ClaudeProvider.java      # Claude 实现
│   │   │   │   ├── DeepSeekProvider.java    # DeepSeek 实现
│   │   │   │   ├── GLMProvider.java         # 智谱 AI 实现
│   │   │   │   ├── QwenProvider.java        # 阿里通义实现
│   │   │   │   ├── OllamaProvider.java      # Ollama 实现
│   │   │   │   └── AzureOpenAIProvider.java # Azure 实现
│   │   │   ├── model/               # 数据模型
│   │   │   │   ├── CommitMessage.java
│   │   │   │   └── FileChangeInfo.java
│   │   │   ├── service/             # 业务逻辑服务
│   │   │   │   ├── SmartCommitMessageGenerator.java  # AI 增强生成器
│   │   │   │   ├── CommitMessageGenerator.java       # 规则生成器
│   │   │   │   └── GitAnalysisService.java          # Git 分析
│   │   │   ├── settings/            # 设置管理
│   │   │   │   └── PluginSettings.java
│   │   │   ├── ui/                  # 用户界面
│   │   │   │   ├── SettingsPanel.java
│   │   │   │   └── SettingsConfigurable.java
│   │   │   └── validator/           # 验证器
│   │   │       └── CommitMessageValidator.java
│   │   └── resources/
│   │       └── META-INF/
│   │           └── plugin.xml       # 插件配置
│   └── test/                        # 单元测试
│       └── java/com/github/gitcommithelper/
│           ├── model/
│           └── validator/
├── build.gradle.kts                 # Gradle 构建配置
├── gradle.properties                # Gradle 属性
└── settings.gradle.kts              # Gradle 设置

```

---

## 🚀 构建与运行

### 1. 克隆项目

```bash
git clone https://github.com/yourusername/Git.Commit.Message.Helper.git
cd Git.Commit.Message.Helper
```

### 2. 构建插件

```bash
# 使用 Gradle Wrapper 构建
./gradlew buildPlugin

# Windows
gradlew.bat buildPlugin
```

**构建产物位置**: `build/distributions/Git.Commit.Message.Helper-1.0.0.zip`

### 3. 运行开发环境

```bash
# 启动带有插件的 IDEA 实例
./gradlew runIde
```

这会启动一个新的 IntelliJ IDEA 实例，插件已自动加载，适合开发和调试。

### 4. 清理构建文件

```bash
./gradlew clean
```

---

## 🧪 测试指南

### 运行所有测试

```bash
./gradlew test
```

### 运行特定测试

```bash
# 运行单个测试类
./gradlew test --tests "CommitMessageValidatorTest"

# 运行特定测试方法
./gradlew test --tests "CommitMessageValidatorTest.testValidMessage"
```

### 查看测试报告

测试完成后，查看报告：
```bash
open build/reports/tests/test/index.html  # macOS
start build/reports/tests/test/index.html # Windows
```

### 测试覆盖率

```bash
./gradlew test jacocoTestReport
open build/reports/jacoco/test/html/index.html
```

---

## 🎯 快速测试插件功能

### 方法 1: 在开发环境中测试

1. 运行 `./gradlew runIde`
2. 在新打开的 IDEA 中打开一个 Git 项目
3. 修改一些文件
4. 打开 Git Commit 窗口（`Ctrl+K` / `Cmd+K`）
5. 点击 ⚡ "Generate Commit Message" 按钮
6. 验证生成的提交信息

### 方法 2: 安装到本地 IDEA

1. 构建插件：`./gradlew buildPlugin`
2. 打开你的主 IntelliJ IDEA
3. 进入 `Settings` → `Plugins` → ⚙️ → `Install Plugin from Disk`
4. 选择 `build/distributions/Git.Commit.Message.Helper-1.0.0.zip`
5. 重启 IDEA
6. 按照 [USER_GUIDE.md](USER_GUIDE.md) 测试功能

### 测试清单

完成以下测试确保功能正常：

#### 基础功能测试
- [ ] 插件在 Plugins 列表中可见且已启用
- [ ] 设置页面可以正常打开
- [ ] Git 提交窗口中可以看到生成按钮
- [ ] 点击生成按钮能弹出对话框
- [ ] 生成的提交信息格式正确（`type(scope): subject`）
- [ ] "Copy to Clipboard" 功能正常

#### 规则分析模式测试
- [ ] 修改 `.java` 文件 → 生成 `feat` 或 `fix`
- [ ] 修改 `.md` 文件 → 生成 `docs`
- [ ] 修改测试文件 → 生成 `test`
- [ ] 修改 `build.gradle.kts` → 生成 `build`

#### AI 增强模式测试（需配置 AI）
- [ ] 配置 AI Provider 并测试连接
- [ ] AI 生成的信息更详细准确
- [ ] AI 失败时自动降级到规则模式
- [ ] 超时设置有效（默认 30 秒）

#### 验证功能测试
- [ ] 提交正确格式无警告
- [ ] 提交错误格式显示错误提示
- [ ] 提交警告格式显示警告提示
- [ ] 可以在警告对话框选择继续或取消

#### 多语言测试
- [ ] 切换到中文模式，生成中文 subject
- [ ] 切换到英文模式，生成英文 subject
- [ ] 语言设置即时生效，无需重启

#### 高级功能测试
- [ ] 直接填充功能：生成的信息自动填入输入框
- [ ] 选择性分析：仅分析选中的文件

---

## 🐛 调试技巧

### 1. 启用调试模式

在 `runIde` 时启用调试：

```bash
./gradlew runIde --debug-jvm
```

然后在 IDE 中创建 Remote Debug Configuration：
- Host: `localhost`
- Port: `5005`

### 2. 查看插件日志

#### 开发环境日志

当运行 `./gradlew runIde` 时，日志直接输出到控制台。

#### 已安装插件的日志

**macOS**:
```bash
tail -f ~/Library/Logs/JetBrains/IntelliJIdea*/idea.log
```

**Windows**:
```
%USERPROFILE%\.IntelliJIdea2024.1\system\log\idea.log
```

**Linux**:
```bash
tail -f ~/.IntelliJIdea2024.1/system/log/idea.log
```

### 3. 添加调试日志

在代码中使用 IntelliJ 平台的日志系统：

```java
import com.intellij.openapi.diagnostic.Logger;

public class MyClass {
    private static final Logger LOG = Logger.getInstance(MyClass.class);

    public void myMethod() {
        LOG.info("Info message");
        LOG.warn("Warning message");
        LOG.error("Error message", exception);
        LOG.debug("Debug message");  // 仅在 debug 模式下显示
    }
}
```

### 4. 断点调试

在 IntelliJ IDEA 中：
1. 在代码行号处点击设置断点
2. 运行 `./gradlew runIde`
3. 当执行到断点时，程序会暂停
4. 使用调试器查看变量、调用栈等

### 5. 性能分析

使用 IntelliJ 的 Profiler：
```bash
./gradlew runIde --args="--profile"
```

---

## 🛠️ 技术实现详解

### AI 提供者框架

#### 架构设计

```
AIProvider (接口)
    ↑
BaseAIProvider (抽象类)
    ↑
    ├── OpenAIProvider
    ├── ClaudeProvider
    ├── DeepSeekProvider
    ├── GLMProvider
    ├── QwenProvider
    ├── OllamaProvider
    └── AzureOpenAIProvider
```

#### 核心接口

```java
public interface AIProvider {
    String generateCommitMessage(String diff, List<FileChangeInfo> fileChanges)
        throws AIProviderException;

    boolean isAvailable();

    void testConnection() throws AIProviderException;

    String getName();
}
```

#### 添加新的 AI 提供商

1. 在 `AIProviderType` 枚举中添加新类型
2. 创建新的 Provider 类继承 `BaseAIProvider`
3. 实现 `sendRequest` 和 `parseResponse` 方法
4. 在 `AIProviderFactory` 中添加创建逻辑
5. 在 `SettingsPanel` 中添加 UI 选项

**示例**：

```java
public class MyAIProvider extends BaseAIProvider {

    @Override
    protected String sendRequest(String prompt) throws AIProviderException {
        String endpoint = settings.getApiEndpoint();
        String apiKey = settings.getApiKey();

        // 构建 HTTP 请求
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(endpoint))
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(buildRequestBody(prompt)))
            .timeout(Duration.ofSeconds(settings.getTimeoutSeconds()))
            .build();

        // 发送请求并获取响应
        HttpResponse<String> response = httpClient.send(request,
            HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    @Override
    protected String parseResponse(String response) throws AIProviderException {
        // 解析 JSON 响应，提取生成的文本
        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        return json.get("result").getAsString();
    }
}
```

### 智能生成流程

```
用户点击生成按钮
    ↓
GenerateCommitMessageAction.actionPerformed()
    ↓
SmartCommitMessageGenerator.generateCommitMessage()
    ↓
检查 AI 是否启用
    ↓ YES
创建 AI Provider (AIProviderFactory)
    ↓
获取文件变更 (GitAnalysisService)
    ↓
获取 Git diff (git diff --cached)
    ↓
构建 Prompt (BaseAIProvider.buildPrompt())
    ↓
调用 AI API (Provider.sendRequest())
    ↓
解析响应 (Provider.parseResponse())
    ↓
成功？ → 返回 AI 结果
    ↓ NO (或 AI 未启用)
降级到规则分析模式
    ↓
CommitMessageGenerator.generateTemplate()
    ↓
返回基础生成结果
```

### 设置持久化

使用 IntelliJ 平台的 `PersistentStateComponent`：

```java
@State(
    name = "GitCommitMessageHelperSettings",
    storages = @Storage("GitCommitMessageHelper.xml")
)
public class PluginSettings implements PersistentStateComponent<PluginSettings> {
    // 设置字段
    public String apiKey = "";
    public String model = "gpt-4-turbo";
    // ...

    @Override
    public PluginSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull PluginSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
```

配置文件位置：
- **macOS**: `~/Library/Application Support/JetBrains/IntelliJIdea*/options/GitCommitMessageHelper.xml`
- **Windows**: `%APPDATA%\JetBrains\IntelliJIdea*\options\GitCommitMessageHelper.xml`
- **Linux**: `~/.config/JetBrains/IntelliJIdea*/options/GitCommitMessageHelper.xml`

### Git 集成

使用 IntelliJ 平台的 Git API：

```java
// 获取文件变更
ChangeListManager changeListManager = ChangeListManager.getInstance(project);
Collection<Change> changes = changeListManager.getAllChanges();

// 获取 Git diff
ProcessBuilder pb = new ProcessBuilder("git", "diff", "--cached");
pb.directory(new File(project.getBasePath()));
Process process = pb.start();

BufferedReader reader = new BufferedReader(
    new InputStreamReader(process.getInputStream()));
String line;
StringBuilder diff = new StringBuilder();
while ((line = reader.readLine()) != null) {
    diff.append(line).append("\n");
}
```

---

## 📦 发布流程

### 1. 版本号管理

在 `build.gradle.kts` 中更新版本号：

```kotlin
group = "com.github.gitcommithelper"
version = "1.1.0"  // 更新这里
```

遵循语义化版本规范：
- **主版本**（Major）：不兼容的 API 修改
- **次版本**（Minor）：向下兼容的功能性新增
- **修订号**（Patch）：向下兼容的问题修正

### 2. 更新 CHANGELOG

在 [CHANGELOG.md](CHANGELOG.md) 中记录本次发布的所有变更：

```markdown
## [1.1.0] - 2026-01-18

### 新增
- AI 增强生成功能
- 多语言支持

### 改进
- 提升生成质量

### 修复
- 修复某个 bug
```

### 3. 构建发布版本

```bash
# 清理旧的构建文件
./gradlew clean

# 构建插件
./gradlew buildPlugin

# 验证插件
./gradlew verifyPlugin
```

### 4. 测试发布版本

```bash
# 在开发环境中测试
./gradlew runIde

# 或安装到本地 IDEA 测试
# Settings → Plugins → Install Plugin from Disk
# 选择 build/distributions/Git.Commit.Message.Helper-1.1.0.zip
```

### 5. 发布到 JetBrains Marketplace

#### 准备工作

1. 在 [JetBrains Marketplace](https://plugins.jetbrains.com/) 注册账号
2. 创建新插件或选择现有插件
3. 获取 **Publish Token**

#### 配置环境变量

```bash
# macOS/Linux
export PUBLISH_TOKEN="your-token-here"

# Windows
set PUBLISH_TOKEN=your-token-here
```

#### 发布插件

```bash
./gradlew publishPlugin
```

或在 `build.gradle.kts` 中配置：

```kotlin
intellijPlatform {
    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
    }
}
```

#### 手动上传

1. 访问 [JetBrains Plugin Repository](https://plugins.jetbrains.com/plugin/add)
2. 填写插件信息
3. 上传 `build/distributions/Git.Commit.Message.Helper-1.1.0.zip`
4. 等待审核（通常 1-3 个工作日）

### 6. 创建 GitHub Release

```bash
# 打标签
git tag -a v1.1.0 -m "Release version 1.1.0"
git push origin v1.1.0

# 在 GitHub 上创建 Release
# 1. 访问 GitHub 仓库的 Releases 页面
# 2. 点击 "Draft a new release"
# 3. 选择标签 v1.1.0
# 4. 填写 Release notes（可以从 CHANGELOG 复制）
# 5. 上传构建的插件 zip 文件
# 6. 发布
```

---

## 🔐 签名插件（可选）

为插件添加数字签名以增强安全性：

### 生成证书

```bash
# 生成私钥和证书
openssl req -x509 -newkey rsa:4096 -keyout private_key.pem \
    -out certificate.pem -days 365 -nodes
```

### 配置签名

在 `build.gradle.kts` 中：

```kotlin
intellijPlatform {
    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }
}
```

### 设置环境变量

```bash
export CERTIFICATE_CHAIN=$(cat certificate.pem)
export PRIVATE_KEY=$(cat private_key.pem)
export PRIVATE_KEY_PASSWORD="your-password"
```

### 签名并构建

```bash
./gradlew signPlugin
./gradlew buildPlugin
```

---

## 📊 性能优化建议

### 1. 减少 AI API 调用

```java
// 实现缓存机制
private Map<String, CommitMessage> cache = new ConcurrentHashMap<>();

public CommitMessage generateCommitMessage(Project project) {
    String diffHash = calculateDiffHash(project);

    if (cache.containsKey(diffHash)) {
        LOG.info("Using cached result");
        return cache.get(diffHash);
    }

    CommitMessage result = generateWithAI(project);
    cache.put(diffHash, result);
    return result;
}
```

### 2. 后台任务执行

```java
ProgressManager.getInstance().run(new Task.Backgroundable(project, "Generating Commit Message") {
    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        indicator.setText("Analyzing changes...");
        // 执行耗时操作
    }
});
```

### 3. 限制 Diff 大小

```java
private String truncateDiff(String diff, int maxLength) {
    if (diff.length() <= maxLength) {
        return diff;
    }
    return diff.substring(0, maxLength) + "\n... (diff truncated)";
}
```

---

## 🤝 贡献指南

请参阅 [CONTRIBUTING.md](CONTRIBUTING.md) 了解如何贡献代码。

---

## 📞 获取帮助

- **问题反馈**: [GitHub Issues](https://github.com/yourusername/Git.Commit.Message.Helper/issues)
- **功能建议**: [GitHub Discussions](https://github.com/yourusername/Git.Commit.Message.Helper/discussions)
- **开发文档**: [IntelliJ Platform SDK](https://plugins.jetbrains.com/docs/intellij/welcome.html)

---

**Happy Coding!** 🚀
