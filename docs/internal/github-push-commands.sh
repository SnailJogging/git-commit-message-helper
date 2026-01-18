#!/bin/bash

# 提示用户输入仓库名称
echo "请在 GitHub 上创建新仓库后，运行以下命令："
echo ""
echo "# 添加远程仓库（请将 YOUR_REPO_NAME 替换为实际的仓库名）"
echo "git remote add origin https://github.com/SnailJogging/YOUR_REPO_NAME.git"
echo ""
echo "# 推荐的仓库名: git-commit-message-helper"
echo "git remote add origin https://github.com/SnailJogging/git-commit-message-helper.git"
echo ""
echo "# 推送到 GitHub"
echo "git branch -M main"
echo "git push -u origin main"
echo ""
echo "完成后，您的项目将在 GitHub 上可见！"
