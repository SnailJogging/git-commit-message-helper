# 贡献指南

感谢你考虑为 Git Commit Message Helper 项目做出贡献！

## 行为准则

### 我们的承诺

为了营造一个开放和友好的环境，我们承诺让每个人都能参与到这个项目中，无论年龄、体型、残疾、种族、性别认同、经验水平、国籍、个人外貌、种族、宗教或性取向。

### 我们的标准

**积极行为示例**：
- 使用友好和包容的语言
- 尊重不同的观点和经验
- 优雅地接受建设性批评
- 关注对社区最有利的事情
- 对其他社区成员表示同情

**不可接受行为示例**：
- 使用性化的语言或图像
- 挑衅、侮辱或贬损性评论
- 公开或私下骚扰
- 未经明确许可发布他人的私人信息
- 其他在专业环境中可能被认为不适当的行为

## 如何贡献

### 报告 Bug

#### 提交 Bug 前

- 检查 [已有 Issues](https://github.com/yourusername/git-commit-message-helper/issues)，避免重复
- 确保你使用的是最新版本
- 收集相关信息（版本号、操作系统、IDEA 版本等）

#### 如何提交好的 Bug 报告

使用 Issue 模板，包含以下信息：

```markdown
**描述问题**
清晰简洁地描述问题

**重现步骤**
1. 进入 '...'
2. 点击 '...'
3. 滚动到 '...'
4. 看到错误

**期望行为**
描述你期望发生什么

**截图**
如果适用，添加截图帮助解释问题

**环境**
- OS: [例如 macOS 14.0]
- IntelliJ IDEA: [例如 2023.2.5]
- 插件版本: [例如 1.0.0]
- Java 版本: [例如 17]

**额外上下文**
添加任何其他相关信息
```

### 建议功能

#### 提交建议前

- 检查是否已有相似建议
- 确保建议符合项目范围和目标

#### 如何提交好的功能建议

```markdown
**问题描述**
这个功能解决什么问题？

**期望的解决方案**
清晰描述你希望如何实现

**可选方案**
描述你考虑过的其他方案

**额外上下文**
添加相关的截图、示例或链接
```

### 第一次贡献

不确定从哪里开始？可以查看以下 Issue：

- `good-first-issue` - 适合新手的简单问题
- `help-wanted` - 需要帮助的问题

### 提交代码

#### 开发流程

1. **Fork 项目**
   ```bash
   # 点击 GitHub 上的 Fork 按钮
   ```

2. **克隆你的 Fork**
   ```bash
   git clone https://github.com/your-username/git-commit-message-helper.git
   cd git-commit-message-helper
   ```

3. **创建分支**
   ```bash
   git checkout -b feat/amazing-feature
   ```

   分支命名规范：
   - `feat/` - 新功能
   - `fix/` - Bug 修复
   - `docs/` - 文档更新
   - `refactor/` - 代码重构
   - `test/` - 测试相关

4. **进行更改**
   - 编写代码
   - 添加测试
   - 更新文档

5. **提交更改**
   ```bash
   git add .
   git commit -m "feat: add amazing feature"
   ```

   **重要**：使用本插件推荐的提交信息格式！

6. **推送到 GitHub**
   ```bash
   git push origin feat/amazing-feature
   ```

7. **创建 Pull Request**
   - 访问你的 Fork 页面
   - 点击 "New Pull Request"
   - 填写 PR 模板

#### Pull Request 指南

**PR 标题**：使用 Conventional Commits 格式
```
feat: add user authentication
fix: resolve login issue
docs: update README
```

**PR 描述应包含**：

```markdown
## 更改内容
简要描述你的更改

## 更改类型
- [ ] Bug 修复
- [ ] 新功能
- [ ] 文档更新
- [ ] 代码重构
- [ ] 性能优化
- [ ] 测试

## 测试
描述你如何测试这些更改

## 检查清单
- [ ] 代码遵循项目风格指南
- [ ] 进行了自我审查
- [ ] 添加了必要的注释
- [ ] 更新了相关文档
- [ ] 没有产生新的警告
- [ ] 添加了测试证明修复有效或功能可用
- [ ] 所有测试通过
- [ ] 更新了 CHANGELOG.md

## 相关 Issue
Closes #(issue 编号)
```

### 代码规范

#### Java 代码风格

遵循 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)

**关键要点**：

1. **缩进**：使用 4 个空格
2. **命名**：
   - 类名：PascalCase (`CommitMessage`)
   - 方法名：camelCase (`generateTemplate`)
   - 常量：UPPER_SNAKE_CASE (`MAX_LENGTH`)
   - 包名：小写 (`com.github.gitcommithelper`)

3. **注释**：
   ```java
   /**
    * 类或方法的 JavaDoc 注释
    *
    * @param parameter 参数描述
    * @return 返回值描述
    */
   ```

4. **导入**：
   - 不使用通配符导入
   - 按字母顺序排列
   - 分组：java.*, javax.*, 第三方, 项目内部

#### 提交信息规范

使用 Conventional Commits：

```
<type>(<scope>): <subject>

<body>

<footer>
```

**类型**：
- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档
- `style`: 格式
- `refactor`: 重构
- `test`: 测试
- `chore`: 构建/工具

**示例**：
```
feat(validator): add support for custom rules

Add ability to define custom validation rules in settings.
Users can now add their own commit type patterns.

Closes #123
```

### 测试要求

#### 单元测试

- 所有新功能必须有测试覆盖
- Bug 修复应包含回归测试
- 目标代码覆盖率：80%+

#### 运行测试

```bash
# 运行所有测试
./gradlew test

# 运行特定测试
./gradlew test --tests CommitMessageValidatorTest

# 生成覆盖率报告
./gradlew jacocoTestReport
```

#### 测试命名规范

```java
@Test
public void testMethodName_Scenario_ExpectedResult() {
    // Arrange
    // Act
    // Assert
}
```

**示例**：
```java
@Test
public void testValidate_EmptyMessage_ReturnsError() {
    // ...
}
```

### 文档要求

#### 代码文档

- 所有 public 类和方法必须有 JavaDoc
- 复杂逻辑添加内联注释
- 使用清晰的变量和方法名

#### 用户文档

如果你的更改影响用户体验，更新：

- `README.md` - 如果影响主要功能
- `USER_GUIDE.md` - 如果影响使用方法
- `CHANGELOG.md` - 记录所有更改（必需）
- `FAQ.md` - 如果解决了常见问题
- `DEVELOPMENT.md` - 如果影响开发流程

### 发布流程

#### 版本号

遵循 [语义化版本](https://semver.org/)：

- `MAJOR.MINOR.PATCH`
- `1.0.0` → `1.0.1` (补丁)
- `1.0.0` → `1.1.0` (次版本)
- `1.0.0` → `2.0.0` (主版本)

#### 发布检查清单

- [ ] 所有测试通过
- [ ] 文档已更新
- [ ] CHANGELOG 已更新
- [ ] 版本号已更新
- [ ] 创建 Git tag
- [ ] 构建并发布插件

## 项目结构

```
src/
├── main/
│   ├── java/              # Java 源代码
│   │   └── com/github/gitcommithelper/
│   │       ├── action/    # 用户操作
│   │       ├── model/     # 数据模型
│   │       ├── service/   # 业务逻辑
│   │       ├── ui/        # 用户界面
│   │       └── validator/ # 验证器
│   └── resources/         # 资源文件
│       └── META-INF/
│           └── plugin.xml # 插件配置
└── test/                  # 测试代码
    └── java/
```

## 开发环境设置

详细的开发环境设置请参阅 [DEVELOPMENT.md](DEVELOPMENT.md)。

### 必需工具

- JDK 17+
- IntelliJ IDEA 2023.2+
- Git 2.0+
- Gradle 8.5+ (项目包含 wrapper)

### 推荐工具

- IntelliJ IDEA Ultimate（更好的插件开发支持）
- Postman 或 curl（测试 AI API）
- SonarLint（代码质量检查）

### 快速开始

```bash
# 1. 克隆项目
git clone https://github.com/yourusername/Git.Commit.Message.Helper.git
cd Git.Commit.Message.Helper

# 2. 构建项目
./gradlew buildPlugin

# 3. 运行开发环境
./gradlew runIde

# 4. 运行测试
./gradlew test
```

### IDE 配置

1. **代码风格**
   - 导入 `intellij-java-google-style.xml`
   - `Settings` → `Editor` → `Code Style` → `Import Scheme`

2. **Save Actions**
   - 启用 "Reformat code on save"
   - 启用 "Optimize imports on save"

3. **插件**
   - SonarLint
   - CheckStyle-IDEA
   - Lombok

## 社区

### 获取帮助

- 💬 [GitHub Discussions](https://github.com/yourusername/git-commit-message-helper/discussions)
- 🐛 [Issue Tracker](https://github.com/yourusername/git-commit-message-helper/issues)
- 📧 Email: support@example.com

### 保持联系

- ⭐ Star 项目获取更新
- 👀 Watch 仓库接收通知
- 🐦 关注项目动态

## 感谢贡献者

感谢所有为这个项目做出贡献的人！

<!-- ALL-CONTRIBUTORS-LIST:START -->
<!-- ALL-CONTRIBUTORS-LIST:END -->

## 许可证

通过贡献代码，你同意你的贡献将在 [MIT License](LICENSE) 下授权。

---

**再次感谢你的贡献！** ❤️

每一个 PR，每一个 Issue，每一个建议都让这个项目变得更好。

Happy Coding! 🚀
