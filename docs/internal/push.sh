#!/bin/bash

echo "🚀 正在推送代码到 GitHub..."
echo ""
echo "如果推送失败，请尝试以下方法："
echo ""
echo "方法1: 使用 GitHub Desktop (推荐)"
echo "  1. 下载: open https://desktop.github.com/"
echo "  2. 登录后选择 'Add Local Repository'"
echo "  3. 选择此目录并点击 'Publish'"
echo ""
echo "方法2: 使用 GitHub CLI"
echo "  brew install gh"
echo "  gh auth login"
echo "  git push -u origin main"
echo ""
echo "方法3: 手动上传"
echo "  访问: https://github.com/SnailJogging/git-commit-message-helper/upload"
echo "  拖拽所有文件上传"
echo ""

# 尝试推送
git push -u origin main

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ 推送成功！"
    echo "🔗 访问您的仓库: https://github.com/SnailJogging/git-commit-message-helper"
else
    echo ""
    echo "❌ 推送失败，请使用上述替代方法"
fi
