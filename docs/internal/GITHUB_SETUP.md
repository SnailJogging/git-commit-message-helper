# GitHub 仓库设置指南

## 🚀 快速开始

### 1. 在 GitHub 创建新仓库

访问 https://github.com/new 并填写：

- **Repository name**: `git-commit-message-helper`
- **Description**: `🚀 IntelliJ IDEA plugin for generating and validating Git commit messages following Conventional Commits`
- **Visibility**: ✅ Public
- **不要勾选**: ❌ Add a README file
- **不要勾选**: ❌ Add .gitignore
- **不要勾选**: ❌ Choose a license

点击 **Create repository**

### 2. 推送本地代码到 GitHub

在创建仓库后，GitHub 会显示快速设置页面。在本项目目录下运行：

```bash
# 添加远程仓库
git remote add origin https://github.com/SnailJogging/git-commit-message-helper.git

# 确认分支名称
git branch -M main

# 推送到 GitHub
git push -u origin main
```

### 3. 验证推送成功

访问 https://github.com/SnailJogging/git-commit-message-helper

您应该能看到：
- ✅ 所有源代码文件
- ✅ README.md 作为首页显示
- ✅ 33 个文件提交
- ✅ 初始提交信息

---

## 📋 推荐的仓库设置

### 添加仓库描述和标签

在 GitHub 仓库页面：

1. 点击页面右上角的 ⚙️ **Settings**
2. 在 **Description** 输入：
   ```
   IntelliJ IDEA plugin for generating and validating Git commit messages following Conventional Commits
   ```
3. 在 **Website** 输入（可选）：
   ```
   https://plugins.jetbrains.com/plugin/YOUR-PLUGIN-ID
   ```
4. 添加 **Topics**（标签）：
   - `intellij-plugin`
   - `git`
   - `commit-message`
   - `conventional-commits`
   - `java`
   - `gradle`
   - `developer-tools`

### 设置 GitHub Pages（可选）

如果您想为插件创建文档网站：

1. `Settings` → `Pages`
2. Source: `Deploy from a branch`
3. Branch: `main` / `(root)`
4. 保存

### 启用 Issues

1. `Settings` → `General`
2. 勾选 ✅ **Issues**
3. 这样用户可以报告问题和提供反馈

### 创建 Release

发布第一个版本：

1. 点击仓库页面右侧的 **Releases**
2. 点击 **Create a new release**
3. 填写信息：
   - **Tag**: `v1.0.0`
   - **Release title**: `v1.0.0 - Initial Release`
   - **Description**: 从 CHANGELOG.md 复制内容
   - **Upload files**: 上传 `build/distributions/git-commit-message-helper-1.0.0.zip`
4. 点击 **Publish release**

---

## 🔐 身份验证

如果推送时需要身份验证：

### 使用 Personal Access Token (推荐)

1. 访问 https://github.com/settings/tokens
2. 点击 **Generate new token (classic)**
3. 勾选权限：
   - ✅ `repo` (完整的仓库访问权限)
4. 生成并复制 token
5. 推送时使用：
   ```bash
   git push https://YOUR_TOKEN@github.com/SnailJogging/git-commit-message-helper.git main
   ```

### 使用 SSH (推荐)

如果您已配置 SSH：

```bash
git remote set-url origin git@github.com:SnailJogging/git-commit-message-helper.git
git push -u origin main
```

---

## 📝 后续步骤

### 1. 添加徽章到 README

在 README.md 顶部添加状态徽章：

```markdown
# Git Commit Message Helper

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![GitHub release](https://img.shields.io/github/release/SnailJogging/git-commit-message-helper.svg)](https://github.com/SnailJogging/git-commit-message-helper/releases)
[![GitHub stars](https://img.shields.io/github/stars/SnailJogging/git-commit-message-helper.svg)](https://github.com/SnailJogging/git-commit-message-helper/stargazers)
```

### 2. 设置 GitHub Actions (可选)

创建 `.github/workflows/build.yml` 自动构建和测试：

```yaml
name: Build

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    - name: Build with Gradle
      run: ./gradlew buildPlugin
    - name: Run tests
      run: ./gradlew test
```

### 3. 发布到 JetBrains Marketplace

1. 访问 https://plugins.jetbrains.com/
2. 注册或登录账户
3. 上传插件：`build/distributions/git-commit-message-helper-1.0.0.zip`
4. 填写插件信息（复制自 README）
5. 等待审核

---

## 🐛 常见问题

### 推送被拒绝

如果遇到 `failed to push some refs` 错误：

```bash
# 强制推送（仅在确认本地代码正确时）
git push -u origin main --force
```

### 远程仓库已存在文件

如果 GitHub 仓库已有 README 或其他文件：

```bash
# 先拉取远程内容
git pull origin main --allow-unrelated-histories

# 解决冲突后推送
git push -u origin main
```

### 验证失败

确保您的 GitHub 用户名和 token/密码正确：

```bash
# 查看远程仓库配置
git remote -v

# 更新远程仓库 URL
git remote set-url origin https://github.com/SnailJogging/git-commit-message-helper.git
```

---

## 📞 需要帮助？

- GitHub 文档: https://docs.github.com/
- Git 文档: https://git-scm.com/doc
- 本项目 Issues: https://github.com/SnailJogging/git-commit-message-helper/issues

---

**准备好了吗？开始推送到 GitHub！** 🚀
