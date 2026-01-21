# 常见问题解答（FAQ）

Git Commit Message Helper 插件常见问题与解决方案。

---

## 📋 目录

- [安装与配置](#安装与配置)
- [基础使用](#基础使用)
- [AI 功能](#ai-功能)
- [多语言支持](#多语言支持)
- [故障排除](#故障排除)
- [性能与优化](#性能与优化)

---

## 安装与配置

### Q1: 插件支持哪些 IntelliJ IDEA 版本？

**A**: 插件支持 IntelliJ IDEA 2023.2 - 2025.2 版本，包括：
- IntelliJ IDEA Community Edition
- IntelliJ IDEA Ultimate Edition

已测试版本：
- ✅ 2023.2.5
- ✅ 2024.1.4 (Community Edition)
- ✅ 2024.2
- ✅ 2025.1

### Q2: 如何安装插件？

**A**: 有两种安装方式：

**方式 1：从 JetBrains Marketplace 安装（推荐）**
1. 打开 `Settings/Preferences` → `Plugins`
2. 点击 `Marketplace` 标签
3. 搜索 "Git Commit Message Helper"
4. 点击 `Install`
5. 重启 IDEA

**方式 2：从磁盘安装**
1. 下载插件 `.zip` 文件
2. 打开 `Settings/Preferences` → `Plugins`
3. 点击 ⚙️ → `Install Plugin from Disk`
4. 选择下载的 `.zip` 文件
5. 重启 IDEA

详见 [INSTALLATION.md](INSTALLATION.md)

### Q3: 安装后在哪里找到插件功能？

**A**: 插件集成在以下位置：

1. **Git Commit 窗口**：
   - 打开方式：`Ctrl+K` (Windows/Linux) 或 `Cmd+K` (macOS)
   - 工具栏中有 ⚡ "Generate Commit Message" 按钮

2. **设置页面**：
   - `Settings/Preferences` → `Tools` → `Git Commit Message Helper`

### Q4: 插件需要联网吗？

**A**: 取决于使用模式：

- **规则分析模式**：不需要联网，完全离线工作
- **AI 增强模式**：需要联网（除非使用 Ollama 本地模型）

---

## 基础使用

### Q5: 如何生成提交信息？

**A**:

1. 修改并保存文件
2. 打开 Git Commit 窗口（`Ctrl+K` / `Cmd+K`）
3. 勾选要提交的文件
4. 点击工具栏的 ⚡ "Generate Commit Message" 按钮
5. 查看生成的提交信息
6. 点击 "Copy to Clipboard" 或使用直接填充功能

详见 [USER_GUIDE.md - 基本使用](USER_GUIDE.md#基本使用)

### Q6: 生成的提交信息是什么格式？

**A**: 插件遵循 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```
<type>(<scope>): <subject>
```

**示例**：
```
feat(user): add username validation
fix(auth): resolve login null pointer exception
docs(readme): update installation guide
```

**类型（type）**：
- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 杂项任务
- `build`: 构建系统
- `ci`: CI/CD 配置

### Q7: 可以只分析部分文件吗？

**A**: 可以！这是 v1.1 的新功能。

在 Git Commit 窗口中：
1. 仅勾选想要分析的文件
2. 点击 ⚡ 生成按钮
3. 插件只会分析选中的文件

这在大型变更集中特别有用。

### Q8: 提交验证可以关闭吗？

**A**: 可以。

在 `Settings` → `Git Commit Message Helper` 中：
- 取消勾选 "Enable commit message validation"
- 点击 "Apply"

关闭后，插件不会验证提交信息格式。

---

## AI 功能

### Q9: 如何启用 AI 增强功能？

**A**:

1. 打开 `Settings` → `Git Commit Message Helper`
2. 勾选 "Enable AI-enhanced generation"
3. 从 "AI Provider" 下拉框选择 AI 提供商
4. 填写 API Key 和其他必要配置
5. 点击 "Test Connection" 验证
6. 点击 "Apply" 保存

详见 [USER_GUIDE.md - AI 配置](USER_GUIDE.md#ai-配置)

### Q10: 支持哪些 AI 提供商？

**A**: 插件支持 7 种主流 AI 提供商：

| 提供商 | 推荐场景 | 配置难度 | 成本 |
|--------|---------|---------|------|
| 🇨🇳 **智谱 AI (GLM)** | 国内用户首选 | ⭐ 简单 | 低 |
| 🇨🇳 **DeepSeek** | 高性价比 | ⭐ 简单 | 极低 |
| 🇨🇳 **阿里通义千问** | 企业用户 | ⭐ 简单 | 低 |
| 🌍 **OpenAI GPT** | 国际用户 | ⭐⭐ 中等 | 中 |
| 🌍 **Claude** | 高质量生成 | ⭐⭐ 中等 | 中 |
| 🏠 **Ollama** | 完全离线 | ⭐⭐⭐ 较难 | 免费 |
| ☁️ **Azure OpenAI** | 企业级 | ⭐⭐⭐ 较难 | 高 |

### Q11: 国内用户推荐使用哪个 AI？

**A**: 推荐按以下顺序选择：

1. **智谱 AI (GLM-4)** - 首选
   - ✅ 免费额度充足
   - ✅ 响应速度快
   - ✅ 注册简单
   - ✅ 国内网络稳定

2. **DeepSeek** - 备选
   - ✅ 成本极低（0.001 元/千 tokens）
   - ✅ 性能优秀
   - ✅ 专为代码优化

3. **阿里通义千问** - 企业
   - ✅ 企业级可靠性
   - ✅ 阿里云生态集成

详见 [USER_GUIDE.md - 国内用户推荐配置](USER_GUIDE.md#国内用户推荐配置)

### Q12: 如何获取 API Key？

**A**: 每个 AI 提供商的获取方式不同：

**智谱 AI**:
1. 访问 https://open.bigmodel.cn/
2. 注册并登录
3. 进入 "API Keys" 页面
4. 点击 "创建 API Key"

**DeepSeek**:
1. 访问 https://platform.deepseek.com/
2. 注册并登录
3. 充值少量金额（10 元足够用很久）
4. 创建 API Key

**OpenAI**:
1. 访问 https://platform.openai.com/
2. 注册并登录
3. 进入 "API Keys"
4. 点击 "Create new secret key"

更多详见 [USER_GUIDE.md - AI 配置步骤](USER_GUIDE.md#配置步骤)

### Q13: AI 生成失败怎么办？

**A**: 插件有自动降级机制：

1. **启用降级（推荐）**：
   - 在设置中勾选 "Fallback to basic mode if AI fails"
   - AI 失败时自动使用规则分析模式

2. **排查原因**：
   - 检查 API Key 是否正确
   - 检查网络连接
   - 查看 Timeout 设置（建议 30 秒）
   - 查看 IDEA 日志获取详细错误

3. **常见错误**：
   - **401 Unauthorized**: API Key 错误
   - **429 Too Many Requests**: API 调用超限
   - **Timeout**: 网络慢或超时设置太短

### Q14: AI 生成很慢怎么办？

**A**: 优化方法：

1. **增加 Timeout**：
   - 设置中将 Timeout 调整为 30-60 秒

2. **切换更快的提供商**：
   - DeepSeek: 2-5 秒
   - 智谱 AI: 3-6 秒
   - OpenAI: 3-8 秒

3. **使用本地模型**：
   - 安装 Ollama
   - 下载轻量级模型（如 qwen:7b）
   - 虽然初始加载慢，但后续很快

4. **启用缓存**：
   - 勾选 "Enable caching"
   - 相似的 diff 会直接返回缓存结果

### Q15: AI 生成的信息不够准确怎么办？

**A**: 优化建议：

1. **调整 Temperature**：
   - 降低到 0.1-0.2（更确定性的输出）
   - 默认 0.3 已经较低

2. **自定义 Prompt**：
   - 在设置中填写 "Custom Prompt Template"
   - 添加项目特定的上下文或规则

3. **更换模型**：
   - OpenAI: 使用 `gpt-4-turbo` 而非 `gpt-3.5`
   - Claude: 使用 `claude-3-5-sonnet` 而非 `claude-3-haiku`

4. **确保 Stage 文件**：
   - 先 `git add` 文件再生成
   - 这样 AI 能分析更准确的 diff

### Q16: Claude API 一直报错 401？

**A**: Claude API 有特殊认证要求：

1. **不要用 Anthropic Console 的 API Key**：
   - Anthropic Console（console.anthropic.com）的 Key 不能用于 API

2. **获取正确的 API Key**：
   - 访问 https://console.anthropic.com/settings/keys
   - 确认在 "API Keys" 标签页
   - 创建新的 API Key

3. **检查 API Endpoint**：
   - 默认：`https://api.anthropic.com/v1/messages`
   - 不要修改（除非使用代理）

4. **检查 API 版本**：
   - 插件使用的是最新 Messages API
   - 确保 Claude API Key 支持此版本

### Q17: 如何使用 Ollama 本地模型？

**A**: Ollama 完全离线，无需 API Key：

1. **安装 Ollama**：
   - 访问 https://ollama.com/
   - 下载并安装

2. **下载模型**：
   ```bash
   ollama pull qwen:7b    # 中文优化
   ollama pull llama2     # 英文优化
   ollama pull deepseek-coder  # 代码优化
   ```

3. **启动 Ollama 服务**：
   ```bash
   ollama serve
   ```
   默认监听 `http://localhost:11434`

4. **配置插件**：
   - AI Provider: 选择 "Ollama (Local)"
   - API Endpoint: `http://localhost:11434`
   - Model: 输入模型名称（如 `qwen:7b`）
   - API Key: 留空

5. **开始使用**：
   - 第一次会较慢（加载模型）
   - 后续请求会很快

详见 [USER_GUIDE.md - Ollama 配置](USER_GUIDE.md#ollama-本地模型)

---

## 多语言支持

### Q18: 如何切换提交信息的语言？

**A**:

1. 打开 `Settings` → `Git Commit Message Helper`
2. 找到 "Message Language" 下拉框
3. 选择 "中文 (Chinese)" 或 "English"
4. 点击 "Apply"
5. 立即生效，无需重启

### Q19: 中文模式和英文模式有什么区别？

**A**:

**中文模式（默认）**：
```
feat(user): 添加用户名验证以防止 li 开头的用户名冲突
fix(auth): 修复登录时的空指针异常
docs(readme): 更新项目安装说明
```
- `type` 和 `scope` 仍为英文（保持兼容性）
- `subject` 使用中文
- 更适合中文团队

**英文模式**：
```
feat(user): add username validation to prevent 'li' prefix conflicts
fix(auth): fix null pointer exception during login
docs(readme): update project installation guide
```
- 完全英文
- 适合国际化项目

### Q20: 可以在同一项目中混用中英文吗？

**A**: 技术上可以，但**不建议**：

- 同一个项目应保持提交信息风格一致
- 如果团队成员变化，可以统一调整语言设置
- 建议在项目开始时确定语言，并在团队内统一

### Q21: type 和 scope 可以用中文吗？

**A**: **不建议**，原因：

1. **工具兼容性**：
   - 许多工具（changelog 生成器、commit lint）依赖英文 type
   - 英文 type 是 Conventional Commits 规范

2. **国际标准**：
   - `feat`、`fix` 等是约定俗成的标准
   - 保持这部分英文有助于跨团队协作

3. **推荐做法**：
   - `type` 和 `scope`: 英文
   - `subject`: 根据团队偏好选择中文或英文

### Q22: 规则分析模式支持中文吗？

**A**: **不支持**。

- 规则分析模式（非 AI）仅生成英文
- 中文功能需要启用 AI 增强模式
- 这是因为规则模式基于文件名和类型推断，无法理解语义

### Q23: 自定义 Prompt 会覆盖语言设置吗？

**A**: **是的**。

- 如果设置了 "Custom Prompt Template"，会忽略语言设置
- 自定义 Prompt 完全由用户控制
- 如果想使用语言切换功能，请清空自定义 Prompt

---

## 故障排除

### Q24: 生成按钮不显示？

**A**: 排查步骤：

1. **确认插件已启用**：
   - `Settings` → `Plugins`
   - 搜索 "Git Commit Message Helper"
   - 确保已勾选

2. **确认在 Git 提交窗口**：
   - 按 `Ctrl+K` / `Cmd+K` 打开提交窗口
   - 不是普通的编辑器窗口

3. **确认项目是 Git 仓库**：
   - 底部状态栏应显示 Git 分支名
   - 如果没有，运行 `git init`

4. **重启 IDEA**：
   - `File` → `Invalidate Caches / Restart`
   - 选择 "Invalidate and Restart"

### Q25: 点击生成按钮没有反应？

**A**: 可能原因：

1. **没有选中文件**：
   - 在 Commit 窗口中至少勾选一个文件

2. **没有文件变更**：
   - 确保有已修改但未提交的文件
   - 运行 `git status` 查看

3. **插件加载失败**：
   - 查看 IDEA 日志：`Help` → `Show Log in Finder/Explorer`
   - 搜索 "GitCommitMessageHelper" 或 "ERROR"

4. **权限问题**：
   - 确保 IDEA 有读取 Git 仓库的权限

### Q26: 验证功能不工作？

**A**: 检查：

1. **验证是否启用**：
   - `Settings` → `Git Commit Message Helper`
   - 确保 "Enable commit message validation" 已勾选

2. **CheckinHandler 注册**：
   - 查看 `plugin.xml` 中是否正确注册
   - 重新安装插件

3. **冲突插件**：
   - 其他 Git 相关插件可能冲突
   - 尝试禁用其他提交信息插件

### Q27: IDEA 未识别为 Git 项目？

**A**: 解决方法：

**方法 1**：通过 IDEA 启用
1. `VCS` → `Enable Version Control Integration...`
2. 选择 `Git`
3. 点击 `OK`

**方法 2**：通过命令行
```bash
cd your-project
git init  # 如果还没有初始化
```

**方法 3**：重新打开项目
1. `File` → `Close Project`
2. 重新打开项目文件夹

### Q28: 如何查看插件日志？

**A**:

**macOS**:
```bash
tail -f ~/Library/Logs/JetBrains/IntelliJIdea*/idea.log
```

**Windows**:
```
%USERPROFILE%\.IntelliJIdea2024.1\system\log\idea.log
```

**Linux**:
```bash
tail -f ~/.IntelliJIdea2024.1/system/log/idea.log
```

**通过 IDEA**:
- `Help` → `Show Log in Finder` (macOS)
- `Help` → `Show Log in Explorer` (Windows/Linux)

### Q29: 企业内网环境如何配置代理？

**A**: 如果您在企业内网，需要配置 IDEA 代理来访问外部 AI 服务。

**配置步骤**：

1. **打开代理设置**
   - `File` → `Settings` → `Appearance & Behavior` → `System Settings` → `HTTP Proxy`

2. **选择代理模式**

   **方式 1：自动检测（推荐）**
   - 选择 **"Auto-detect proxy settings"**
   - IDEA 会自动使用系统代理配置

   **方式 2：手动配置**
   - 选择 **"Manual proxy configuration"**
   - 填入代理信息：
     ```
     Host: xx.xxx.xx.xxx
     Port: xxxx
     ```
   - 如果需要认证：
     - 勾选 **"Proxy authentication"**
     - 填入用户名和密码

3. **测试并应用**
   - 点击 **"Check connection"** 测试连接
   - 输入测试 URL（如 `https://api.openai.com`）
   - 点击 **"Apply"** 和 **"OK"**
   - **重启 IDEA**（重要！）

**注意事项**：
- 国内 AI（智谱、DeepSeek、阿里通义）通常不需要代理
- 国外 AI（OpenAI、Claude、Azure）在国内通常需要代理
- 本地 Ollama 完全不需要网络
- 如遇问题，联系 IT 部门确认企业代理配置

### Q30: 配置代理后仍然无法连接 AI 服务？

**A**: 尝试以下排查步骤：

1. **检查代理配置**
   - 确认 Host 和 Port 正确
   - 确认用户名密码正确（如需要认证）
   - 联系 IT 部门核实代理地址

2. **测试代理连接**
   - 在代理设置中点击 "Check connection"
   - 使用目标 API 地址测试（如 `https://api.openai.com`）

3. **检查 SSL 证书**
   - 企业代理可能拦截 HTTPS 请求
   - 在插件设置中勾选 **"Trust all certificates"**（仅用于开发环境）

4. **尝试直连测试**
   - 暂时关闭代理
   - 使用手机热点测试是否为代理问题

5. **查看详细日志**
   - `Help` → `Show Log in Finder/Explorer`
   - 搜索 "Connection" 或 "Proxy" 相关错误

6. **使用国内 AI**
   - 如代理问题无法解决
   - 考虑切换到国内 AI 提供商（智谱、DeepSeek、阿里通义）
   - 或使用本地 Ollama 完全离线工作

---

## 性能与优化

### Q31: 插件会拖慢 IDEA 吗？

**A**: **不会**。

- 插件只在用户主动点击生成按钮时工作
- 后台任务执行，不阻塞 UI
- 内存占用小（< 10MB）
- 规则分析模式响应极快（< 1 秒）

### Q32: AI 模式生成需要多久？

**A**: 取决于 AI 提供商：

| 提供商 | 生成时间 | 成功率 | 准确度 |
|--------|---------|--------|--------|
| 规则分析 | < 1 秒 | 99%+ | 中等 |
| OpenAI GPT-4 | 3-8 秒 | 95%+ | 高 |
| DeepSeek | 2-5 秒 | 95%+ | 高 |
| Claude | 3-6 秒 | 95%+ | 高 |
| 智谱 AI | 3-6 秒 | 95%+ | 高 |
| 阿里通义 | 3-7 秒 | 95%+ | 高 |
| Ollama (本地) | 5-15 秒 | 99%+ | 中高 |

### Q33: 如何加快生成速度？

**A**: 优化方法：

1. **使用更快的 AI 提供商**：
   - DeepSeek 通常最快

2. **减小 diff 大小**：
   - 仅选中需要提交的文件
   - 避免一次提交大量文件

3. **启用缓存**：
   - 勾选 "Enable caching"
   - 相似 diff 会命中缓存

4. **使用本地模型**：
   - Ollama 无网络延迟
   - 硬件性能决定速度

5. **降低 Max Tokens**：
   - 减少到 100-200
   - 提交信息通常不需要太多 tokens

### Q34: 插件会收集我的代码吗？

**A**: **不会**。

**隐私说明**：
1. **规则分析模式**：完全本地，不传输任何数据
2. **AI 模式**：
   - 仅将 git diff 发送给选择的 AI 提供商
   - 不会发送到插件开发者
   - 不会存储或上传到任何第三方
3. **Ollama 模式**：完全本地，零网络传输

**建议**：
- 敏感项目使用规则分析模式或 Ollama
- 公开项目可以安全使用云端 AI

---

## 其他问题

### Q35: 插件是开源的吗？

**A**: **是的**。

- GitHub: https://github.com/yourusername/Git.Commit.Message.Helper
- License: MIT
- 欢迎贡献代码和反馈问题

### Q36: 如何报告 Bug 或提出建议？

**A**:

**报告 Bug**:
1. 访问 [GitHub Issues](https://github.com/yourusername/Git.Commit.Message.Helper/issues)
2. 点击 "New Issue"
3. 提供以下信息：
   - IDEA 版本
   - 插件版本
   - 重现步骤
   - 错误截图或日志

**功能建议**:
1. 访问 [GitHub Discussions](https://github.com/yourusername/Git.Commit.Message.Helper/discussions)
2. 描述你的需求和使用场景

### Q37: 插件支持其他 JetBrains IDE 吗？

**A**: 目前仅支持 IntelliJ IDEA。

计划中：
- ⏳ PyCharm
- ⏳ WebStorm
- ⏳ Android Studio

欢迎在 GitHub 投票支持你需要的 IDE。

### Q38: 可以自定义提交类型吗？

**A**: **可以**。

在 `Settings` → `Git Commit Message Helper` 中：
1. 找到 "Custom Commit Types" 文本框
2. 输入自定义类型，用逗号分隔
3. 例如：`feature,bugfix,hotfix,release`
4. 点击 "Apply"

自定义类型会在生成和验证时被识别。

### Q39: 插件有快捷键吗？

**A**: 当前版本没有独立快捷键。

**使用方式**：
- 打开 Commit 窗口：`Ctrl+K` / `Cmd+K`
- 然后点击 ⚡ 按钮

**计划中**：
- 下个版本可能添加快捷键配置

### Q40: 如何卸载插件？

**A**:

1. 打开 `Settings/Preferences` → `Plugins`
2. 找到 "Git Commit Message Helper"
3. 点击右侧的 ⚙️
4. 选择 "Uninstall"
5. 重启 IDEA

**清理配置文件**（可选）：
- macOS: `~/Library/Application Support/JetBrains/IntelliJIdea*/options/GitCommitMessageHelper.xml`
- Windows: `%APPDATA%\JetBrains\IntelliJIdea*\options\GitCommitMessageHelper.xml`
- Linux: `~/.config/JetBrains/IntelliJIdea*/options/GitCommitMessageHelper.xml`

---

## 🤝 获取更多帮助

如果上述 FAQ 没有解决你的问题：

- **用户手册**: [USER_GUIDE.md](USER_GUIDE.md)
- **开发文档**: [DEVELOPMENT.md](DEVELOPMENT.md)
- **GitHub Issues**: [提交问题](https://github.com/yourusername/Git.Commit.Message.Helper/issues)
- **GitHub Discussions**: [参与讨论](https://github.com/yourusername/Git.Commit.Message.Helper/discussions)

---

**希望这些解答能帮到你！** 🎉
