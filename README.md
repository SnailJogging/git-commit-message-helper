# Git Commit Message Helper

一个 IntelliJ IDEA 插件，帮助开发者编写规范化的 Git 提交信息，遵循 [Conventional Commits](https://www.conventionalcommits.org/) 规范。

## 功能特性

### 🚀 主要功能

- **自动生成提交信息模板**：根据文件变动智能生成符合规范的提交信息
- **实时格式校验**：在提交前自动验证提交信息格式
- **智能类型推断**：基于修改的文件类型自动推荐合适的提交类型
- **作用域建议**：根据文件路径智能建议提交作用域
- **多种提交类型支持**：支持所有标准的 Conventional Commits 类型

### 📋 支持的提交类型

| 类型 | 描述 |
|------|------|
| `feat` | 新功能 |
| `fix` | Bug 修复 |
| `docs` | 文档更新 |
| `style` | 代码格式调整（不影响代码含义） |
| `refactor` | 代码重构 |
| `perf` | 性能优化 |
| `test` | 测试相关 |
| `build` | 构建系统或依赖更新 |
| `ci` | CI/CD 配置更新 |
| `chore` | 其他不修改 src 或 test 文件的更改 |
| `revert` | 回滚之前的提交 |

## 安装

### 从 JetBrains Marketplace 安装（即将上线）

1. 打开 IntelliJ IDEA
2. 进入 `File` > `Settings` > `Plugins`
3. 搜索 "Git Commit Message Helper"
4. 点击 `Install` 安装

### 从源码构建

```bash
# 克隆仓库
git clone https://github.com/yourusername/git-commit-message-helper.git
cd git-commit-message-helper

# 构建插件
./gradlew buildPlugin

# 生成的插件文件位于 build/distributions/
```

## 使用方法

### 生成提交信息

1. 在 Git 提交对话框中，点击工具栏上的 ⚡ 图标
2. 插件会自动分析当前的文件变动
3. 生成的提交信息会显示在对话框中
4. 可以选择复制到剪贴板或直接使用

### 自动验证

插件会在提交前自动验证提交信息：

- ✅ 格式正确：允许提交
- ⚠️ 有警告：询问是否继续
- ❌ 格式错误：询问是否强制提交

### 配置选项

进入 `File` > `Settings` > `Tools` > `Git Commit Message Helper` 可以配置：

- 启用/禁用提交信息验证
- 启用/禁用自动生成
- 显示/隐藏警告
- 自定义主题行最大长度
- 添加自定义提交类型

## 提交信息格式

### 基本格式

```
<type>(<scope>): <subject>

<body>

<footer>
```

### 示例

```
feat(auth): add user authentication

Implements JWT-based authentication system with refresh tokens.
Includes login, logout, and token refresh endpoints.

Fixes #123
```

### 破坏性变更

```
feat(api)!: change authentication method

BREAKING CHANGE: Old API key authentication is removed.
Use JWT tokens instead.
```

## 开发

### 环境要求

- JDK 17+
- IntelliJ IDEA 2023.2+
- Gradle 8.0+

### 开发命令

```bash
# 运行插件开发环境
./gradlew runIde

# 运行测试
./gradlew test

# 构建插件
./gradlew buildPlugin

# 验证插件
./gradlew verifyPlugin
```

### 项目结构

```
src/
├── main/
│   ├── java/com/github/gitcommithelper/
│   │   ├── action/          # 用户操作
│   │   ├── model/           # 数据模型
│   │   ├── service/         # 业务逻辑
│   │   ├── ui/              # 用户界面
│   │   └── validator/       # 验证器
│   └── resources/
│       └── META-INF/
│           └── plugin.xml   # 插件配置
└── test/                    # 单元测试
```

## 技术栈

- Java 17
- IntelliJ Platform SDK
- Git4Idea (IntelliJ Git 插件 API)
- JUnit 4 (测试)

## 贡献

欢迎贡献！请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feat/amazing-feature`)
3. 提交更改 (`git commit -m 'feat: add amazing feature'`)
4. 推送到分支 (`git push origin feat/amazing-feature`)
5. 开启 Pull Request

请确保：
- 代码遵循项目的编码规范
- 添加适当的测试
- 更新相关文档

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 致谢

- [Conventional Commits](https://www.conventionalcommits.org/) - 提交信息规范
- [IntelliJ Platform SDK](https://plugins.jetbrains.com/docs/intellij/) - 插件开发文档

## 联系方式

- 问题反馈：[GitHub Issues](https://github.com/yourusername/git-commit-message-helper/issues)
- 邮箱：support@example.com

---

⭐ 如果这个项目对你有帮助，请给个 Star！
