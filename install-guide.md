# Git Commit Message Helper - 安装测试指南

## 🎉 插件已成功打包！

**文件位置**: `build/distributions/git-commit-message-helper-1.0.0.zip`
**文件大小**: 1.5 MB

---

## 📥 安装方法

### 方法1：安装到现有的 IntelliJ IDEA（推荐用于测试）

1. **打开 IntelliJ IDEA**

2. **进入插件设置**
   - macOS: `IntelliJ IDEA` → `Settings` → `Plugins` (或快捷键 `⌘,`)
   - Windows/Linux: `File` → `Settings` → `Plugins` (或快捷键 `Ctrl+Alt+S`)

3. **安装插件**
   - 点击齿轮图标 ⚙️ 
   - 选择 `Install Plugin from Disk...`
   - 导航到文件位置：
     ```
     /Users/liebedich/StudySpace/Git.Commit.Message.Helper/build/distributions/
     ```
   - 选择 `git-commit-message-helper-1.0.0.zip`
   - 点击 `OK`

4. **重启 IDEA**
   - 在弹出的对话框中点击 `Restart IDE`
   - 或者手动重启 IDEA

5. **验证安装成功**
   - 重启后，打开 `Settings` → `Plugins`
   - 在搜索框输入 "Git Commit Message Helper"
   - 确认插件出现在已安装列表中
   - 确认复选框已勾选（插件已启用）

---

### 方法2：启动测试 IDE 实例

直接启动一个包含插件的 IDEA 测试实例：

```bash
cd /Users/liebedich/StudySpace/Git.Commit.Message.Helper
./gradlew runIde
```

这会启动一个新的 IntelliJ IDEA 窗口，插件已自动加载。

**注意**：
- 首次启动可能需要下载依赖，请耐心等待
- 这个测试实例与您的正常 IDEA 设置是隔离的

---

## 🧪 测试插件功能

### 1. 测试自动生成功能

1. **打开或创建一个 Git 项目**
   - 如果没有，可以初始化一个测试项目：
     ```bash
     mkdir ~/test-git-project
     cd ~/test-git-project
     git init
     echo "# Test Project" > README.md
     git add README.md
     ```

2. **在 IDEA 中打开项目**
   - `File` → `Open` → 选择 `~/test-git-project`

3. **修改一些文件**
   - 编辑 README.md 或创建新文件
   - 例如：添加一些内容到 README.md

4. **打开 Git 提交窗口**
   - 快捷键：`⌘K` (Mac) 或 `Ctrl+K` (Windows/Linux)
   - 或菜单：`Git` → `Commit...`

5. **使用生成功能**
   - 在提交信息框的工具栏中，找到 ⚡ 闪电图标按钮
   - 按钮名称：**"Generate Commit Message"**
   - 点击按钮

6. **查看生成的提交信息**
   - 会弹出一个对话框显示生成的提交信息
   - 显示提交类型和作用域（如果有）
   - 点击 "Copy to Clipboard" 复制到剪贴板
   - 粘贴到提交信息框中

### 2. 测试自动验证功能

1. **输入不规范的提交信息**
   ```
   update files
   ```

2. **尝试提交**
   - 点击 `Commit` 按钮

3. **查看验证结果**
   - 应该会弹出错误对话框
   - 显示格式错误信息
   - 选择是否继续或取消

4. **输入正确格式的提交信息**
   ```
   docs: update README
   ```

5. **再次提交**
   - 应该验证通过，可以正常提交

### 3. 测试设置页面

1. **打开设置**
   - `Settings` → `Tools` → `Git Commit Message Helper`

2. **查看配置选项**
   - ✓ Enable commit message validation
   - ✓ Enable automatic message generation
   - ✓ Show warnings for commit messages
   - 📝 Maximum subject length
   - 📝 Custom Commit Types

3. **修改设置并应用**

---

## 🔍 故障排除

### 插件没有显示

**检查1：确认插件已安装**
- `Settings` → `Plugins`
- 搜索 "Git Commit Message Helper"
- 确认已在列表中

**检查2：确认插件已启用**
- 插件旁边的复选框应该被勾选
- 如果未勾选，勾选后重启 IDEA

**检查3：查看日志**
- `Help` → `Show Log in Finder` (Mac) 或 `Show Log in Explorer` (Windows)
- 查找相关错误信息

### 生成按钮不可见

**检查1：确认是 Git 项目**
- 确保当前项目已初始化 Git
- 运行 `git status` 确认

**检查2：有文件变动**
- 确保有未提交的文件变动
- 修改或创建一些文件

**检查3：工具栏显示**
- 右键点击提交对话框的工具栏
- 选择 `Customize Toolbar...`
- 确认操作可见

### 验证功能不工作

**检查1：确认设置已启用**
- `Settings` → `Tools` → `Git Commit Message Helper`
- 确认 "Enable commit message validation" 已勾选

**检查2：输入的格式**
- 确保测试的提交信息确实不符合格式
- 尝试完全随机的文本

---

## 📊 预期的测试结果

### ✅ 成功指标

- [ ] 插件出现在插件列表中
- [ ] 生成按钮在 Git 提交窗口中可见
- [ ] 点击生成按钮后显示对话框
- [ ] 生成的提交信息格式正确
- [ ] 不规范的提交信息会触发验证错误
- [ ] 设置页面可以正常打开和配置
- [ ] 所有功能在测试项目中正常工作

### ⚠️ 可能的问题

1. **首次加载慢**
   - 正常现象，需要初始化插件
   - 等待几秒钟

2. **生成的信息不够准确**
   - 这是预期的，因为是基于简单规则
   - 用户可以手动调整

3. **某些提交类型未识别**
   - 可以在设置中添加自定义类型

---

## 📝 测试清单

### 基础功能测试

- [ ] 插件成功安装
- [ ] 插件在 IDEA 中可见
- [ ] 生成按钮出现在提交窗口
- [ ] 点击生成按钮有响应
- [ ] 生成的信息可以复制

### 验证功能测试

- [ ] 空提交信息被拒绝
- [ ] 不规范格式显示错误
- [ ] 规范格式通过验证
- [ ] 警告信息正确显示

### 设置功能测试

- [ ] 设置页面可以打开
- [ ] 各项配置可以修改
- [ ] 配置可以保存
- [ ] 修改配置后生效

### 边界情况测试

- [ ] 非 Git 项目正确处理
- [ ] 无文件变动时的行为
- [ ] 大量文件变动的性能
- [ ] 特殊字符在提交信息中

---

## 🚀 测试通过后的下一步

1. **收集测试反馈**
   - 记录任何 bug 或问题
   - 记录改进建议

2. **性能测试**
   - 在大型项目中测试
   - 观察响应速度

3. **用户体验评估**
   - 界面是否友好
   - 操作是否直观
   - 提示是否清晰

4. **准备发布**
   - 修复发现的 bug
   - 优化用户体验
   - 准备发布文档

---

## 📞 需要帮助？

如果在测试过程中遇到问题：

1. 查看项目文档：
   - `README.md` - 项目介绍
   - `QUICK_START.md` - 快速开始
   - `USAGE_GUIDE.md` - 使用指南

2. 查看日志文件：
   - IDEA 日志：`Help` → `Show Log`
   - 查找 "gitcommithelper" 相关信息

3. 重新构建插件：
   ```bash
   ./gradlew clean buildPlugin
   ```

---

**祝测试顺利！** 🎉
