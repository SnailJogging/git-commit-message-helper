# Git Commit Message Helper - 用户指南

完整的插件使用手册，涵盖所有功能和配置。

---

## 📖 目录

- [快速开始](#快速开始)
- [基本使用](#基本使用)
- [AI 配置](#ai-配置)
- [多语言支持](#多语言支持)
- [高级功能](#高级功能)
- [故障排除](#故障排除)
- [最佳实践](#最佳实践)
- [常见问题](#常见问题)

---

## 🚀 快速开始

### 第一次使用

1. **安装插件**（参见 [INSTALLATION.md](INSTALLATION.md)）
2. **打开设置**：`Settings` → `Tools` → `Git Commit Message Helper`
3. **选择模式**：
   - 基础模式：无需配置，直接使用规则分析
   - AI 增强模式：配置 AI 提供商，获得更准确的提交信息

### 生成第一条提交信息

1. 修改代码
2. 打开提交窗口（⌘K / Ctrl+K）
3. 点击工具栏中的 ⚡ **"Generate Commit Message"** 按钮
4. 提交信息自动填充到输入框中
5. 根据需要微调，然后提交

---

## 💡 基本使用

### 两种生成模式

#### 规则分析模式（默认）

**优点**：
- ✅ 快速、即时响应
- ✅ 无需配置
- ✅ 完全离线可用
- ✅ 免费

**工作原理**：
- 分析文件变更类型（新增/修改/删除）
- 识别文件类型（Java、文档、测试等）
- 智能推断提交类型和作用域

**示例**：
```
feat(user): add new functionality
fix(service): resolve issue
docs(readme): update documentation
```

#### AI 增强模式（推荐）

**优点**：
- ✅ 理解业务逻辑
- ✅ 生成更准确的描述
- ✅ 分析代码变更的意图
- ✅ 支持多语言（中文/英文）

**工作原理**：
- AI 分析 git diff 内容
- 理解代码变更的业务含义
- 生成符合 Conventional Commits 规范的提交信息

**示例**：
```
feat(user): 添加用户名验证以防止 li 开头的用户名冲突
fix(order): 修复订单金额计算时的精度丢失问题
refactor(payment): 重构支付模块以支持多种支付方式
```

### 使用流程

```
修改代码 → 打开提交窗口 → 选择文件 → 点击⚡生成 → 自动填充 → 提交
```

---

## 🤖 AI 配置

### 启用 AI 增强模式

1. 打开设置：`Settings` → `Tools` → `Git Commit Message Helper`
2. 勾选 **"Enable AI-enhanced generation"**
3. 选择 AI 提供商
4. 配置 API Key 和其他参数
5. 点击 **"Test Connection"** 验证
6. 应用设置

---

### 支持的 AI 提供商

#### 1. OpenAI GPT

**推荐场景**：国际化团队、需要最高质量

```
AI Provider: OpenAI GPT
API Key: sk-xxxxxxxxxxxxxxxxxxxxx
API Endpoint: https://api.openai.com/v1
Model: gpt-4-turbo (或 gpt-3.5-turbo)
Max Tokens: 500
Temperature: 0.3
```

**获取 API Key**：
1. 访问 https://platform.openai.com/api-keys
2. 创建新的 API key
3. 复制并粘贴到插件设置中

**费用**：
- GPT-4 Turbo: ~$0.01/次
- GPT-3.5 Turbo: ~$0.001/次

---

#### 2. DeepSeek ⭐ 性价比之王

**推荐场景**：个人开发者、高频使用

```
AI Provider: DeepSeek
API Key: sk-xxxxxxxxxxxxxxxxxxxxx
API Endpoint: https://api.deepseek.com
Model: deepseek-chat
Max Tokens: 500
Temperature: 0.3
```

**获取 API Key**：
1. 访问 https://platform.deepseek.com/api_keys
2. 创建新的 API key
3. 复制并粘贴到插件设置中

**费用**：~¥0.001/次（最便宜！）

---

#### 3. 智谱 AI (GLM) 🇨🇳

**推荐场景**：中文项目、国内团队

```
AI Provider: 智谱 AI (GLM)
API Key: xxxxxxxxxx.xxxxxxxxxxxxxx
API Endpoint: https://open.bigmodel.cn/api/paas/v4
Model: glm-4
Max Tokens: 500
Temperature: 0.3
```

**获取 API Key**：
1. 访问 https://open.bigmodel.cn
2. 注册并实名认证
3. 控制台创建 API Key
4. 充值（最低 ¥10）

**费用**：~¥0.005/次

**特点**：
- ✅ 中文理解能力强
- ✅ 国内访问速度快
- ✅ 支持支付宝/微信支付

---

#### 4. 阿里通义千问 🇨🇳

**推荐场景**：企业用户、阿里云生态

```
AI Provider: 阿里通义千问
API Key: sk-xxxxxxxxxxxxxxxxxxxxxxxx
API Endpoint: https://dashscope.aliyuncs.com/compatible-mode/v1
Model: qwen-turbo (或 qwen-plus)
Max Tokens: 500
Temperature: 0.3
```

**获取 API Key**：
1. 访问 https://dashscope.aliyun.com
2. 使用阿里云账号登录
3. 开通 DashScope 服务
4. 创建 API Key

**费用**：
- qwen-turbo: ~¥0.002/次
- qwen-plus: ~¥0.004/次

**特点**：
- ✅ 稳定可靠
- ✅ 阿里云账号直接使用
- ✅ 企业级支持

---

#### 5. Claude (Anthropic)

**推荐场景**：需要高质量输出

```
AI Provider: Claude (Anthropic)
API Key: sk-ant-xxxxxxxxxxxxxxxxxxxxx
API Endpoint: https://api.anthropic.com
Model: claude-3-5-sonnet-20241022
Max Tokens: 500
Temperature: 0.3
```

**获取 API Key**：
1. 访问 https://console.anthropic.com/settings/keys
2. 创建新的 API key
3. 复制并粘贴到插件设置中

**费用**：~$0.003/次

---

#### 6. Ollama (本地) 💻

**推荐场景**：隐私敏感项目、完全免费

```
AI Provider: Ollama (Local)
API Key: (留空)
API Endpoint: http://localhost:11434
Model: llama2 (或其他已安装的模型)
Max Tokens: 500
Temperature: 0.3
```

**安装 Ollama**：
1. 下载：https://ollama.ai/
2. 安装模型：`ollama pull llama2`
3. 启动服务：`ollama serve`

**特点**：
- ✅ 完全免费
- ✅ 数据不出本地
- ✅ 无网络要求
- ❌ 需要一定硬件资源

---

#### 7. Azure OpenAI

**推荐场景**：企业 Azure 用户

```
AI Provider: Azure OpenAI
API Key: your-azure-api-key
API Endpoint: https://your-resource.openai.azure.com
Model: gpt-4
Max Tokens: 500
Temperature: 0.3
```

---

### AI 提供商对比

| 提供商 | 每次成本 | 质量 | 速度 | 中文支持 | 推荐度 |
|--------|---------|------|------|---------|--------|
| DeepSeek | ¥0.001 | ⭐⭐⭐⭐⭐ | ⚡⚡⚡ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| 智谱 GLM-4 | ¥0.005 | ⭐⭐⭐⭐ | ⚡⚡⚡ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| 阿里 qwen-turbo | ¥0.002 | ⭐⭐⭐⭐ | ⚡⚡⚡ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| OpenAI GPT-4 | $0.01 | ⭐⭐⭐⭐⭐ | ⚡⚡ | ⭐⭐⭐ | ⭐⭐⭐⭐ |
| Claude 3.5 | $0.003 | ⭐⭐⭐⭐⭐ | ⚡⚡ | ⭐⭐⭐ | ⭐⭐⭐⭐ |
| Ollama | 免费 | ⭐⭐⭐ | ⚡ | ⭐⭐⭐ | ⭐⭐⭐ |

**推荐选择**：
- 💰 **预算优先**：DeepSeek
- 🇨🇳 **中文项目**：智谱 AI
- 🏢 **企业用户**：阿里通义千问
- 🔒 **隐私优先**：Ollama
- 🎯 **质量优先**：OpenAI GPT-4 或 Claude

---

### 高级选项

#### 参数说明

- **Max Tokens**：生成的最大 token 数
  - 推荐值：500
  - 范围：100-2000

- **Temperature**：生成的随机性
  - 推荐值：0.3
  - 范围：0.0-1.0
  - 0.0 = 最确定，1.0 = 最随机

- **Timeout**：API 调用超时时间
  - 推荐值：30 秒
  - 范围：5-120 秒

#### 高级功能

- **Enable caching**：缓存相似变更的分析结果（加速后续生成）
- **Fallback to basic mode if AI fails**：AI 失败时自动降级到规则分析

---

### 自定义 Prompt

如果想要更个性化的提交信息，可以自定义 Prompt 模板：

**中文示例**：
```
你是一个 Git 提交信息专家。分析以下代码变更并生成提交信息。

格式：type(scope): subject

规则：
- type 和 scope 使用英文
- subject 使用中文
- 要具体说明业务逻辑变化
- 不超过 100 个字符
```

**英文示例**：
```
You are a Git commit message expert. Analyze code changes and generate commit message.

Format: type(scope): subject

Rules:
- Use lowercase
- Be specific about business logic
- Maximum 72 characters
```

点击 **"Reset to Default"** 恢复默认 Prompt。

---

## 🌏 多语言支持

插件支持生成**中文**或**英文**的提交信息。

### 默认语言

**中文** 🇨🇳（默认）

### 如何切换

1. 打开设置：`Settings` → `Tools` → `Git Commit Message Helper`
2. 找到 **"Message Language"** 选项
3. 选择语言：
   - **中文 (Chinese)** - 默认
   - **English**
4. 应用设置

**无需重启 IDEA！**

---

### 生成效果对比

#### 场景：添加用户验证

**代码变更**：
```java
public void addUser(User user) {
    if (user.getName().startsWith("li")) {
        throw new IllegalArgumentException("Username cannot start with 'li'");
    }
    users.add(user);
}
```

**中文模式（默认）**：
```
feat(user): 添加用户名验证以防止 li 开头的用户名冲突
```

**英文模式**：
```
feat(user): add username validation to prevent 'li' prefix conflicts
```

---

### 提交信息格式

#### 中文模式

**格式**：`type(scope): 中文描述`

**说明**：
- `type` 和 `scope` 使用英文小写（符合 Conventional Commits）
- `subject` 使用中文
- 更适合中文团队

**示例**：
```
feat(auth): 添加用户登录功能
fix(order): 修复订单金额计算错误
docs(readme): 更新项目安装说明
refactor(payment): 重构支付模块以支持多种支付方式
test(user): 添加用户验证的单元测试
```

#### 英文模式

**格式**：`type(scope): english description`

**说明**：
- 完全使用英文
- 适合国际化项目

**示例**：
```
feat(auth): add user login functionality
fix(order): fix order amount calculation error
docs(readme): update project installation guide
refactor(payment): refactor payment module for multiple methods
test(user): add unit tests for user validation
```

---

### 什么时候用中文？

✅ 团队成员都是中文使用者
✅ 项目文档和注释都是中文
✅ 希望提交信息更详细、更易读
✅ 内部项目，不对外开源

**优势**：
- 更清晰地表达业务逻辑
- 减少理解成本
- 支持更长的描述

### 什么时候用英文？

✅ 国际化团队或开源项目
✅ 需要与国际开发者协作
✅ 遵循行业标准
✅ 公司要求统一使用英文

**优势**：
- 符合国际惯例
- 更广泛的受众
- 工具兼容性更好

---

## ⚡ 高级功能

### 1. 直接填充提交信息

**功能**：点击生成按钮后，提交信息自动填充到提交框中，无需手动复制粘贴。

**优势**：
- ✅ 节省 60% 的操作时间
- ✅ 无需手动复制粘贴
- ✅ 一键完成

**如何使用**：
1. 打开提交窗口
2. 点击 ⚡ 生成按钮
3. 提交信息自动填充
4. 右下角显示通知气泡

**降级处理**：
- 如果无法直接填充，会自动显示对话框供手动复制

---

### 2. 仅分析选中的文件

**功能**：只分析您在提交对话框中选中的文件，而不是所有已修改的文件。

**优势**：
- ✅ 提交信息更精准
- ✅ 分批提交更方便
- ✅ 避免无关文件干扰

**如何使用**：
1. 打开提交窗口
2. **只选中要提交的文件**（重要！）
3. 点击 ⚡ 生成按钮
4. AI 只分析选中的文件

**示例场景**：
```
假设您修改了以下文件：
☑ UserService.java      (选中)
☑ UserController.java   (选中)
☐ README.md            (未选中)
☐ test/UserTest.java   (未选中)

点击生成 → 只分析 UserService.java 和 UserController.java
生成：feat(user): 添加用户管理功能
```

**分批提交示例**：
```
第一次提交（用户功能）：
☑ UserService.java
☑ UserController.java
→ feat(user): 添加用户管理功能

第二次提交（文档更新）：
☑ README.md
→ docs(readme): 更新用户模块文档

第三次提交（测试）：
☑ test/UserTest.java
→ test(user): 添加用户服务单元测试
```

---

### 3. 智能作用域识别

插件会自动识别变更的作用域：

- 根据文件路径识别模块
- 根据变更类型识别范围
- 生成准确的 scope

**示例**：
```
src/main/java/com/example/user/UserService.java
→ scope: user

src/main/java/com/example/order/payment/PaymentService.java
→ scope: payment
```

---

## 🌐 企业代理环境配置

如果您在企业内网环境中使用本插件，需要配置 IDEA 的代理设置以访问 AI 提供商的 API。

### 配置步骤

1. **打开 IDEA 代理设置**
   - 路径：`File` → `Settings` → `Appearance & Behavior` → `System Settings` → `HTTP Proxy`

2. **选择代理模式**

   **方式 1：自动检测（推荐）**
   - 选择 **"Auto-detect proxy settings"**
   - IDEA 会自动使用系统代理配置
   - 适合大多数企业环境

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

3. **测试连接**
   - 点击 **"Check connection"** 按钮
   - 输入测试 URL（如 `https://api.openai.com`）
   - 确认连接成功

4. **应用设置**
   - 点击 **"Apply"** 和 **"OK"**
   - **重启 IDEA**（重要！）

### 常见问题

**Q: 配置代理后仍无法连接？**
- 确认代理服务器地址和端口正确
- 检查用户名密码是否正确
- 联系 IT 部门确认代理配置
- 尝试关闭 SSL 证书验证（设置中的 "Trust all certificates"）

**Q: 只有部分 AI 提供商需要代理吗？**
- 国外 AI（OpenAI、Claude、Azure）通常需要代理
- 国内 AI（智谱、DeepSeek、阿里通义）通常不需要代理
- 本地 Ollama 完全不需要网络

**Q: 代理配置会影响其他功能吗？**
- 代理配置是 IDEA 全局的
- 会影响所有需要网络的插件和功能
- 建议与 IT 部门确认企业代理策略

---

## 🔧 故障排除

### 问题 1：AI 生成失败

**症状**：点击生成后提示错误

**可能原因**：
- API Key 错误
- 网络连接问题
- API 余额不足
- 超时

**解决方案**：
1. 检查 API Key 是否正确
2. 点击 "Test Connection" 测试连接
3. 检查网络连接（国外 API 可能需要代理）
4. 检查 API 余额
5. 增加 Timeout 时间（30 → 60 秒）
6. 确保勾选了 "Fallback to basic mode"

---

### 问题 2：生成速度慢

**症状**：等待时间超过 30 秒

**解决方案**：
1. 使用更快的模型
   - OpenAI: gpt-3.5-turbo 代替 gpt-4
   - 阿里: qwen-turbo 代替 qwen-plus
2. 减少 Max Tokens 值（500 → 300）
3. 启用缓存功能
4. 考虑使用 DeepSeek（最快）
5. 或使用本地 Ollama

---

### 问题 3：生成的信息不准确

**症状**：AI 生成的信息与实际变更不符

**解决方案**：
1. 调整 Temperature 值（0.3 → 0.1）
2. 确保先 stage 要提交的文件
3. 只选中相关文件（使用选中文件功能）
4. 自定义 Prompt 模板
5. 尝试不同的 AI 模型

---

### 问题 4：无法直接填充提交信息

**症状**：仍然显示对话框，无法自动填充

**解决方案**：
- 这是正常的降级行为
- 某些提交对话框不支持直接填充
- 使用对话框中的 "Copy to Clipboard" 按钮
- 手动粘贴到提交框中

---

### 问题 5：中文乱码

**症状**：生成的中文显示乱码

**解决方案**：
1. 确保 IDEA 使用 UTF-8 编码
2. 检查 "Message Language" 设置
3. 重启 IDEA
4. 如果仍有问题，切换到英文模式

---

### 问题 6：API 费用过高

**解决方案**：
1. 切换到 DeepSeek（最便宜）
2. 使用本地 Ollama（免费）
3. 降低 Max Tokens
4. 简单变更使用规则模式
5. 启用缓存减少重复调用

---

## 💡 最佳实践

### 1. 先 Stage 文件再生成

**推荐流程**：
```
修改代码 → Stage 文件 → 生成提交信息 → 提交
```

**原因**：确保 git diff 包含正确的内容

---

### 2. 复杂变更用 AI，简单变更用规则

**何时使用 AI**：
- ✅ 添加新功能
- ✅ 修复复杂 bug
- ✅ 重构代码
- ✅ 业务逻辑变更

**何时使用规则模式**：
- ✅ 修改文档
- ✅ 调整格式
- ✅ 更新依赖
- ✅ 简单的文本修改

---

### 3. 选择合适的 AI 模型

**个人开发者（成本敏感）**：
```
AI Provider: DeepSeek
Model: deepseek-chat
Max Tokens: 300
Temperature: 0.3
☑ Enable caching
☑ Fallback to basic mode
```

**团队协作（质量优先）**：
```
AI Provider: OpenAI GPT / 智谱 AI
Model: gpt-4-turbo / glm-4
Max Tokens: 500
Temperature: 0.2
☑ Enable caching
☑ Fallback to basic mode
```

**完全离线（隐私优先）**：
```
AI Provider: Ollama (Local)
Model: llama2
Max Tokens: 500
Temperature: 0.3
```

---

### 4. 生成后微调

AI 生成的信息可能需要小幅调整：

- 检查 type 是否准确
- 补充更多上下文
- 调整语言风格
- 添加 breaking changes 说明

---

### 5. 建立团队规范

统一团队的使用方式：

- 统一使用相同的 AI 配置
- 统一语言（中文/英文）
- 统一 Prompt 模板
- 定期检查提交信息质量

---

### 6. 充分利用选中文件功能

**推荐**：
- 功能性修改单独提交
- 文档更新单独提交
- 测试代码单独提交
- 重构单独提交

**避免**：
- 一次提交包含多个不相关的变更
- 混合功能代码和文档更新

---

## ❓ 常见问题

### Q1: 可以在同一个项目中混用中英文吗？

**A**: 可以随时切换，但建议保持一致性：
- 同一个项目最好统一使用一种语言
- 可以根据团队成员变化调整语言设置

---

### Q2: type 和 scope 可以用中文吗？

**A**: 不建议
- `type` 和 `scope` 建议保持英文（Conventional Commits 规范）
- 许多工具依赖英文 type
- 只在 `subject` 部分使用中文

---

### Q3: 规则分析模式支持中文吗？

**A**: 不支持
- 规则分析模式仅生成英文
- 中文功能需要启用 AI 增强模式

---

### Q4: 自定义 Prompt 会覆盖语言设置吗？

**A**: 是的
- 如果设置了自定义 Prompt，会忽略语言设置
- 想使用语言切换功能，请清空自定义 Prompt

---

### Q5: 哪个 AI 模型最好？

**A**: 取决于需求：
- **最便宜**：DeepSeek（¥0.001/次）
- **中文最好**：智谱 GLM-4
- **最稳定**：阿里通义千问
- **质量最高**：OpenAI GPT-4
- **完全免费**：Ollama（本地）

---

### Q6: 可以用于商业项目吗？

**A**: 可以
- 插件本身是开源的
- 但要注意 AI 提供商的隐私政策
- 敏感项目建议使用本地 Ollama

---

### Q7: 生成的信息会被保存吗？

**A**: 不会
- 插件不保存任何生成的提交信息
- AI 提供商可能会记录 API 调用（查看各自隐私政策）
- 使用本地 Ollama 保证数据不出本地

---

### Q8: 支持其他语言吗（如日语、韩语）？

**A**: 目前支持中文和英文
- 如有其他语言需求，可以通过自定义 Prompt 实现
- 欢迎提交 Issue 或 PR 添加更多语言

---

### Q9: 如何提高生成质量？

**A**: 以下技巧可以提高质量：
1. 使用更好的 AI 模型（GPT-4, Claude 3.5）
2. 降低 Temperature（0.3 → 0.1）
3. 只选中相关文件
4. 确保代码变更清晰
5. 自定义 Prompt 添加具体要求

---

### Q10: 遇到问题如何获取帮助？

**A**: 多种方式：
1. 查看本文档的故障排除部分
2. 查看 [FAQ.md](FAQ.md)
3. 查看 IDEA 日志：Help → Show Log in Finder
4. 提交 Issue：https://github.com/SnailJogging/git-commit-message-helper/issues

---

## 📞 支持与反馈

- 📖 完整文档：[README.md](README.md)
- 📦 安装指南：[INSTALLATION.md](INSTALLATION.md)
- 🔄 更新日志：[CHANGELOG.md](CHANGELOG.md)
- 🤝 贡献指南：[CONTRIBUTING.md](CONTRIBUTING.md)
- 🐛 问题反馈：[GitHub Issues](https://github.com/SnailJogging/git-commit-message-helper/issues)

---

**享受智能化的 Git 提交体验！** 🎉
