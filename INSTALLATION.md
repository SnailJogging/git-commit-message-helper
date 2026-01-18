# 安装指南

## 插件安装

插件文件位置：`build/distributions/git-commit-message-helper-1.0.0.zip`

### 方法 1：从磁盘安装（推荐）

1. 打开 IntelliJ IDEA
2. 打开设置：
   - Mac: `IntelliJ IDEA` → `Settings...` (⌘,)
   - Windows/Linux: `File` → `Settings` (Ctrl+Alt+S)
3. 导航到 `Plugins`
4. 点击齿轮图标 ⚙️ → `Install Plugin from Disk...`
5. 选择 `build/distributions/git-commit-message-helper-1.0.0.zip`
6. 点击 `OK`
7. 重启 IDEA

### 方法 2：解压安装

1. 解压 `git-commit-message-helper-1.0.0.zip`
2. 将解压后的文件夹复制到 IDEA 插件目录：
   - Mac: `~/Library/Application Support/JetBrains/IntelliJIdea{version}/plugins/`
   - Windows: `%APPDATA%\JetBrains\IntelliJIdea{version}\plugins\`
   - Linux: `~/.local/share/JetBrains/IntelliJIdea{version}/plugins/`
3. 重启 IDEA

## 验证安装

1. 重启 IDEA 后，进入 `Settings` → `Plugins`
2. 在已安装列表中搜索 "Git Commit Message Helper"
3. 确认插件已启用（勾选框已选中）

## 首次配置

### 基础使用（无需配置）

插件默认使用规则分析模式，无需任何配置即可使用：

1. 修改代码
2. 打开 Git 提交窗口（⌘K 或 Ctrl+K）
3. 点击 ⚡ "Generate Commit Message" 按钮
4. 复制生成的提交信息

### AI 增强配置（可选）

如果想使用 AI 生成更准确的提交信息，请参考 [AI_SETUP_GUIDE.md](AI_SETUP_GUIDE.md)

快速配置步骤：

1. 进入 `Settings` → `Tools` → `Git Commit Message Helper`
2. 勾选 "Enable AI-enhanced generation"
3. 选择 AI Provider（如 OpenAI、Claude、DeepSeek）
4. 填写 API Key
5. 点击 "Test Connection" 验证
6. 点击 "Apply" 保存

## 使用指南

### 生成提交信息

1. 修改代码并暂存（stage）要提交的文件
2. 打开 Git 提交窗口：
   - Mac: ⌘K
   - Windows/Linux: Ctrl+K
3. 点击提交信息框工具栏中的 ⚡ "Generate Commit Message" 按钮
4. 等待生成（规则模式：即时，AI 模式：3-10 秒）
5. 在弹出对话框中查看生成的提交信息
6. 点击 "Copy to Clipboard" 复制
7. 粘贴到提交信息框，根据需要调整
8. 提交

### 验证功能

插件还会在提交前验证提交信息格式：

- ✅ 符合 Conventional Commits 规范的信息会直接通过
- ⚠️ 有警告的信息会提示是否继续
- ❌ 格式错误的信息会提示修改

可以在设置中关闭验证功能。

## 支持的 IDEA 版本

- IntelliJ IDEA 2023.2 - 2025.2
- 支持 Community 和 Ultimate 版本

## 系统要求

- Java 17 或更高版本
- Git 已安装并配置
- （AI 模式）网络连接（除非使用本地 Ollama）

## 卸载

1. 进入 `Settings` → `Plugins`
2. 找到 "Git Commit Message Helper"
3. 点击齿轮图标 ⚙️ → `Uninstall`
4. 重启 IDEA

## 故障排除

### 插件未出现在列表中

- 确认 IDEA 版本兼容（2023.2-2025.2）
- 检查插件文件是否完整
- 尝试重新安装

### 生成按钮未显示

- 确认在 Git 提交窗口中（不是普通编辑器）
- 检查插件是否已启用
- 尝试重启 IDEA

### AI 功能不工作

- 检查网络连接
- 验证 API Key 是否正确
- 查看详细配置指南：[AI_SETUP_GUIDE.md](AI_SETUP_GUIDE.md)

## 更多信息

- AI 配置详细指南：[AI_SETUP_GUIDE.md](AI_SETUP_GUIDE.md)
- 插件演示文档：[PLUGIN_DEMO.md](PLUGIN_DEMO.md)
- GitHub 仓库：https://github.com/SnailJogging/git-commit-message-helper

---

安装完成后，开始享受智能化的 Git 提交体验！🚀
