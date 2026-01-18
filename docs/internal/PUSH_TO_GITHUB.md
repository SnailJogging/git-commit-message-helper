# 推送到 GitHub - 操作指南

由于需要身份验证，您有以下几种方式推送代码：

## 方式 1: 使用 GitHub Desktop（最简单）

1. 下载安装 GitHub Desktop: https://desktop.github.com/
2. 登录您的 GitHub 账号
3. 点击 "Add Local Repository"
4. 选择项目目录: `/Users/liebedich/StudySpace/Git.Commit.Message.Helper`
5. 点击 "Publish repository" 按钮
6. 完成！

## 方式 2: 配置 SSH 密钥（推荐长期使用）

### 步骤 1: 查看您的公钥
```bash
cat ~/.ssh/id_rsa.pub
```

### 步骤 2: 添加到 GitHub
1. 复制上面命令输出的公钥内容
2. 访问 https://github.com/settings/ssh/new
3. Title: `MacBook` (或任何描述性名称)
4. Key: 粘贴公钥内容
5. 点击 "Add SSH key"

### 步骤 3: 测试连接
```bash
ssh -T git@github.com
```

如果看到 "Hi SnailJogging!" 说明配置成功。

### 步骤 4: 推送
```bash
git push -u origin main
```

## 方式 3: 使用 Personal Access Token

### 步骤 1: 生成 Token
1. 访问 https://github.com/settings/tokens
2. 点击 "Generate new token (classic)"
3. Note: `git-commit-helper`
4. 勾选权限: `repo` (完整)
5. 点击 "Generate token"
6. **立即复制** token（只显示一次！）

### 步骤 2: 推送时使用 Token
```bash
# 先切换回 HTTPS
git remote set-url origin https://github.com/SnailJogging/git-commit-message-helper.git

# 推送（会提示输入用户名和密码）
git push -u origin main
# Username: SnailJogging
# Password: [粘贴您的 Token]
```

或者直接在 URL 中使用 Token：
```bash
git remote set-url origin https://YOUR_TOKEN@github.com/SnailJogging/git-commit-message-helper.git
git push -u origin main
```

## 方式 4: 使用 GitHub CLI（推荐）

### 安装 GitHub CLI
```bash
brew install gh
```

### 登录并推送
```bash
gh auth login
# 按提示选择: GitHub.com -> HTTPS -> Login with a web browser

# 推送
git push -u origin main
```

---

## 当前状态

✅ Git 仓库已初始化
✅ 所有文件已提交 (33 个文件)
✅ 远程仓库已配置
⏳ 等待推送到 GitHub

## 推荐方案

**最简单**: 使用 GitHub Desktop（无需命令行）
**最安全**: 配置 SSH 密钥（一次配置，长期使用）
**最快速**: 使用 GitHub CLI（自动处理认证）

---

## 推送成功后

访问您的仓库查看: https://github.com/SnailJogging/git-commit-message-helper

您应该能看到：
- ✅ 完整的源代码
- ✅ 所有文档文件
- ✅ README.md 作为首页显示

---

选择您喜欢的方式完成推送吧！🚀
