# Git Commit Message Helper

[![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)](https://github.com/SnailJogging/git-commit-message-helper/releases/latest)
[![Platform](https://img.shields.io/badge/platform-IntelliJ%20IDEA-green.svg)](https://www.jetbrains.com/idea/)
[![License](https://img.shields.io/badge/license-MIT-orange.svg)](LICENSE)
[![Download](https://img.shields.io/badge/download-latest-success.svg)](https://github.com/SnailJogging/git-commit-message-helper/releases/latest)

一个强大的 IntelliJ IDEA 插件，帮助开发者自动生成规范化的 Git 提交信息，支持 AI 增强和多语言。

A powerful IntelliJ IDEA plugin that helps developers automatically generate standardized Git commit messages with AI enhancement and multi-language support.

---

## ✨ 核心特性 | Key Features

- **🤖 AI 增强生成** - 支持 7 种主流 AI 提供商（OpenAI、DeepSeek、智谱 AI、阿里通义、Claude、Ollama、Azure）
- **🌏 多语言支持** - 中文/英文提交信息自由切换，默认中文
- **📋 规则验证** - 自动验证提交信息格式，符合 Conventional Commits 规范
- **🎯 智能分析** - 分析代码变更，生成精准的 type、scope 和 subject
- **⚡ 直接填充** - 一键将生成的提交信息填充到输入框
- **🎨 选择性分析** - 仅分析选中的文件，提高生成速度
- **🔧 高度可配置** - 自定义 Prompt、模型参数、API endpoint

---

## 🚀 快速开始 | Quick Start

### 1. 安装插件

**从 GitHub Release 下载**（推荐）：
- 访问 [Releases 页面](https://github.com/SnailJogging/git-commit-message-helper/releases/latest)
- 下载 `git-commit-message-helper-1.0.0.zip`
- `Settings/Preferences` → `Plugins` → ⚙️ → `Install Plugin from Disk`
- 选择下载的 zip 文件并重启 IDEA

**从 JetBrains Marketplace 安装**（即将上线）：
- `Settings/Preferences` → `Plugins` → `Marketplace`
- 搜索 "Git Commit Message Helper"
- 点击 `Install`

详细安装说明请参阅 [INSTALLATION.md](INSTALLATION.md)

### 2. 配置 AI（可选但推荐）

`Settings/Preferences` → `Tools` → `Git Commit Message Helper`

**国内用户推荐**：
- **智谱 AI**（GLM-4）- 免费额度充足
- **DeepSeek** - 性价比高
- **阿里通义千问** - 稳定可靠

详细配置指南请参阅 [USER_GUIDE.md](USER_GUIDE.md#ai-配置)

### 3. 开始使用

1. 在 IDEA 中修改代码并保存
2. 打开 Git Commit 窗口（`Ctrl+K` / `Cmd+K`）
3. 点击工具栏的 **"Generate AI Message"** 按钮
4. 插件自动分析代码变更并生成提交信息
5. 点击 **"Commit"** 提交

---

## 🎯 支持的 AI 提供商 | AI Providers

| 提供商 | 模型示例 | 推荐场景 | 配置难度 |
|--------|---------|---------|---------|
| 🇨🇳 **智谱 AI** | GLM-4 | 国内用户首选 | ⭐ 简单 |
| 🇨🇳 **DeepSeek** | deepseek-chat | 高性价比 | ⭐ 简单 |
| 🇨🇳 **阿里通义** | qwen-turbo | 企业用户 | ⭐ 简单 |
| 🌍 **OpenAI** | GPT-4 Turbo | 国际用户 | ⭐⭐ 中等 |
| 🌍 **Claude** | Claude 3.5 Sonnet | 高质量生成 | ⭐⭐ 中等 |
| 🏠 **Ollama** | llama2/qwen | 本地部署 | ⭐⭐⭐ 较难 |
| ☁️ **Azure OpenAI** | 企业版 GPT | 企业用户 | ⭐⭐⭐ 较难 |

---

## 🌏 多语言提交信息 | Multi-language Support

插件支持生成**中文**或**英文**提交信息，默认为中文。

### 中文模式（默认）

```
feat(user): 添加用户名验证以防止 li 开头的用户名冲突
fix(auth): 修复登录时的空指针异常
docs(readme): 更新项目安装说明
```

### 英文模式

```
feat(user): add username validation to prevent 'li' prefix conflicts
fix(auth): fix null pointer exception during login
docs(readme): update project installation guide
```

**切换语言**：
`Settings` → `Git Commit Message Helper` → `Message Language` → 选择 `中文 (Chinese)` 或 `English`

详细说明请参阅 [USER_GUIDE.md](USER_GUIDE.md#多语言支持)

---

## 📚 文档导航 | Documentation

- **[USER_GUIDE.md](USER_GUIDE.md)** - 完整用户手册（基本使用、AI 配置、高级功能、故障排除）
- **[INSTALLATION.md](INSTALLATION.md)** - 详细安装指南
- **[CHANGELOG.md](CHANGELOG.md)** - 版本变更历史
- **[CONTRIBUTING.md](CONTRIBUTING.md)** - 贡献指南（如何参与开发）
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - 架构设计文档（开发者）

---

## 💡 使用示例 | Examples

### 场景 1：添加新功能

**代码变更**：
```java
public void validateUsername(String username) {
    if (username.startsWith("li")) {
        throw new IllegalArgumentException("Username cannot start with 'li'");
    }
}
```

**生成的提交信息**（中文模式）：
```
feat(user): 添加用户名验证以防止 li 开头的用户名冲突
```

### 场景 2：修复 Bug

**代码变更**：修复了登录时的 NullPointerException

**生成的提交信息**（中文模式）：
```
fix(auth): 修复登录时的空指针异常
```

### 场景 3：更新文档

**代码变更**：更新了 README.md 的安装说明

**生成的提交信息**（中文模式）：
```
docs(readme): 更新项目安装说明
```

---

## ⚙️ 系统要求 | Requirements

- **IntelliJ IDEA**: 2023.2 - 2025.2（支持 Community 和 Ultimate 版本）
- **JDK**: 17 或更高版本
- **Git**: 已安装并配置

**已测试版本**：
- ✅ IntelliJ IDEA 2023.2.5
- ✅ IntelliJ IDEA 2024.1.4 (Community Edition)
- ✅ IntelliJ IDEA 2024.2

---

## 🛠️ 开发 | Development

### 构建插件

```bash
./gradlew buildPlugin
```

### 运行插件（开发模式）

```bash
./gradlew runIde
```

### 运行测试

```bash
./gradlew test
```

详细开发指南请参阅 [CONTRIBUTING.md](CONTRIBUTING.md)

---

## 🤝 贡献 | Contributing

欢迎贡献代码、报告问题或提出建议！

- 提交 Issue：[GitHub Issues](https://github.com/yourusername/Git.Commit.Message.Helper/issues)
- 提交 PR：请参阅 [CONTRIBUTING.md](CONTRIBUTING.md)
- 讨论功能：[GitHub Discussions](https://github.com/yourusername/Git.Commit.Message.Helper/discussions)

---

## 📄 许可证 | License

本项目采用 MIT 许可证，详见 [LICENSE](LICENSE) 文件。

---

## 📞 联系方式 | Contact

- **问题反馈**: [GitHub Issues](https://github.com/yourusername/git-commit-message-helper/issues)
- **参考资源**: [Conventional Commits](https://www.conventionalcommits.org/)

---

## ⭐ Star History

如果这个插件对你有帮助，请给项目一个 Star ⭐

---

**🎉 开始使用 Git Commit Message Helper，让提交信息更规范、更清晰！**
