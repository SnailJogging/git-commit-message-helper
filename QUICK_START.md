# 快速开始指南

## 5 分钟快速上手

### 步骤 1：构建项目（1 分钟）

```bash
# 确保你有 JDK 17+
java -version

# 克隆项目（如果还没有）
cd /Users/liebedich/StudySpace/Git.Commit.Message.Helper

# 构建插件
./gradlew buildPlugin
```

构建成功后，插件文件位于：`build/distributions/git-commit-message-helper-1.0.0.zip`

### 步骤 2：安装插件（1 分钟）

#### 方法 A：在开发模式下运行

```bash
# 启动带有插件的 IntelliJ IDEA
./gradlew runIde
```

这会启动一个新的 IDEA 实例，插件已自动加载。

#### 方法 B：手动安装到现有 IDEA

1. 打开 IntelliJ IDEA
2. 进入 `File` → `Settings` → `Plugins`
3. 点击齿轮图标 ⚙️ → `Install Plugin from Disk...`
4. 选择 `build/distributions/git-commit-message-helper-1.0.0.zip`
5. 重启 IDEA

### 步骤 3：第一次使用（3 分钟）

#### 测试自动生成功能

1. **打开任意项目**（或创建一个测试项目）

2. **修改一些文件**
   ```bash
   # 创建一个测试文件
   echo "# Test" > README.md
   ```

3. **打开 Git 提交窗口**
   - 快捷键：`Cmd + K` (Mac) 或 `Ctrl + K` (Windows/Linux)

4. **点击生成按钮** ⚡
   - 在提交信息框上方工具栏找到闪电图标
   - 点击 "Generate Commit Message"

5. **查看生成的提交信息**
   - 插件会分析你的文件变动
   - 显示生成的提交信息对话框
   - 点击 "Copy to Clipboard" 复制

6. **粘贴并提交**
   - 将提交信息粘贴到提交框
   - 点击 "Commit" 提交

#### 测试自动验证功能

1. **输入一个不规范的提交信息**
   ```
   update files
   ```

2. **尝试提交**
   - 插件会显示错误提示
   - 说明格式不正确

3. **修改为正确格式**
   ```
   docs: update README
   ```

4. **再次提交**
   - 验证通过，提交成功！

## 常见使用场景

### 场景 1：添加新功能

```bash
# 创建新文件
touch src/main/java/com/example/UserService.java

# 提交时生成的信息：
# feat: add user service
```

### 场景 2：修复 Bug

```bash
# 修改现有文件
vim src/main/java/com/example/AuthService.java

# 提交时生成的信息：
# fix: resolve login issue
```

### 场景 3：更新文档

```bash
# 修改 README
vim README.md

# 提交时生成的信息：
# docs: update README
```

### 场景 4：添加测试

```bash
# 创建测试文件
touch src/test/java/com/example/UserServiceTest.java

# 提交时生成的信息：
# test: add tests
```

## 配置插件（可选）

### 访问设置页面

`File` → `Settings` → `Tools` → `Git Commit Message Helper`

### 推荐配置

✅ **启用提交信息验证** - 确保所有提交都符合规范
✅ **启用自动生成** - 快速生成提交信息
✅ **显示警告** - 帮助改进提交质量
📝 **主题行长度**: 72 字符（默认）

## 测试项目

### 运行单元测试

```bash
./gradlew test
```

查看测试报告：`build/reports/tests/test/index.html`

### 验证插件

```bash
./gradlew verifyPlugin
```

## 开发和调试

### 启用调试模式

```bash
./gradlew runIde --debug-jvm
```

然后在你的 IDE 中创建一个 Remote JVM Debug 配置，连接到 localhost:5005

### 查看日志

插件日志位于 IDEA 的日志文件中：
- Mac: `~/Library/Logs/JetBrains/IntelliJIdea{version}/idea.log`
- Windows: `%USERPROFILE%\.IntelliJIdea{version}\system\log\idea.log`
- Linux: `~/.IntelliJIdea{version}/system/log/idea.log`

### 热重载

修改代码后：
1. 重新构建：`./gradlew buildPlugin`
2. 在测试 IDEA 中：`File` → `Invalidate Caches / Restart`

## 故障排除

### 问题：构建失败

**解决方案**：
```bash
# 清理并重新构建
./gradlew clean buildPlugin
```

### 问题：插件未显示

**检查**：
1. 确认插件已安装：`Settings` → `Plugins`
2. 确认插件已启用（勾选框已选中）
3. 重启 IDEA

### 问题：生成按钮不可见

**检查**：
1. 确保在 Git 项目中
2. 确保有文件变动
3. 检查工具栏设置：右键工具栏 → `Customize Toolbar`

### 问题：验证不工作

**检查**：
1. 进入设置确认验证功能已启用
2. 确保提交信息格式正确
3. 查看日志文件获取详细错误

## 性能基准

在标准项目中的预期性能：

| 操作 | 耗时 | 文件数 |
|------|------|--------|
| 分析文件变动 | < 100ms | < 50 |
| 生成提交信息 | < 50ms | - |
| 验证提交信息 | < 10ms | - |

如果性能不佳，请报告 Issue。

## 下一步

### 深入学习

- 📖 阅读 [完整使用指南](USAGE_GUIDE.md)
- 🏗️ 了解 [架构设计](ARCHITECTURE.md)
- 📋 查看 [开发流程](开发流程.md)

### 贡献代码

- 🐛 [报告问题](https://github.com/yourusername/git-commit-message-helper/issues)
- 💡 [提交功能建议](https://github.com/yourusername/git-commit-message-helper/discussions)
- 🔧 [参与开发](https://github.com/yourusername/git-commit-message-helper/pulls)

### 分享反馈

我们期待听到你的反馈！

- ⭐ 如果喜欢，请给项目点个 Star
- 💬 在社交媒体分享
- 📧 发送邮件到 support@example.com

## 快速命令参考

```bash
# 构建插件
./gradlew buildPlugin

# 运行测试
./gradlew test

# 启动开发环境
./gradlew runIde

# 验证插件
./gradlew verifyPlugin

# 清理构建
./gradlew clean

# 查看所有任务
./gradlew tasks
```

---

**开始享受规范化的 Git 提交吧！** 🚀

有问题？查看 [FAQ](USAGE_GUIDE.md#常见问题) 或 [提交 Issue](https://github.com/yourusername/git-commit-message-helper/issues)
