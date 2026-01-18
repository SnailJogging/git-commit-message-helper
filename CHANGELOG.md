# 更新日志

所有关于本项目的重要变更都会记录在此文件中。

本项目遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

## [未发布]

### 计划中的功能
- [ ] 支持项目级别的配置
- [ ] 提交历史分析功能
- [ ] 支持更多提交规范（Angular、Vue 等）
- [ ] 提交信息模板管理
- [ ] 团队规范共享功能

## [1.1.0] - 2026-01-18

### 新增
- ✨ **AI 增强生成功能** - 重大升级
  - 支持 7 种主流 AI 提供商：
    - OpenAI GPT (GPT-4 Turbo, GPT-4, GPT-3.5)
    - DeepSeek (deepseek-chat, deepseek-coder)
    - 智谱 AI / GLM (glm-4, glm-3-turbo)
    - 阿里通义千问 (qwen-turbo, qwen-plus, qwen-max)
    - Claude / Anthropic (Claude 3.5 Sonnet, Claude 3 Opus/Haiku)
    - Ollama (本地部署，支持 llama2, qwen 等模型)
    - Azure OpenAI (企业版 GPT)
  - 智能分析代码变更，生成高质量提交信息
  - 自动识别变更类型、影响范围和业务逻辑
  - 支持自定义 Prompt 模板
  - 可配置模型参数（temperature, max_tokens, timeout）
  - 支持缓存机制，提高响应速度
  - AI 失败时自动降级到规则生成模式

- 🌏 **多语言提交信息支持**
  - 支持中文/英文提交信息自由切换
  - 默认中文模式（适合国内团队）
  - 中文模式：`type(scope): 中文描述`（type/scope 保持英文，subject 使用中文）
  - 英文模式：`type(scope): english description`（完全英文）
  - Settings 界面一键切换语言
  - 语言设置即时生效，无需重启

- ⚡ **直接填充功能**
  - 生成的提交信息直接填充到 Commit Message 输入框
  - 无需手动复制粘贴，提高效率
  - 支持覆盖已有内容（有确认提示）

- 🎨 **选择性文件分析**
  - 仅分析 Commit Dialog 中选中的文件
  - 忽略未勾选的文件，提高分析精度
  - 适合大型变更集中只提交部分文件的场景
  - 减少无关文件对提交信息质量的干扰

- 🔧 **增强的设置界面**
  - 新增 AI 配置面板
    - AI Provider 选择下拉框（8 个选项：None + 7 种 AI）
    - API Key 输入（支持密码模式）
    - API Endpoint 配置（支持自定义和预设）
    - 模型选择（根据 Provider 自动更新）
    - 高级参数配置（Max Tokens, Temperature, Timeout）
    - 缓存和降级开关
  - 新增语言选择下拉框（中文/English）
  - 新增自定义 Prompt Template 文本框
  - 改进的 UI 布局和分组
  - 实时参数验证

### 改进
- 🚀 提升了提交信息生成质量
  - AI 模式能理解复杂的代码逻辑变更
  - 更准确的 type 和 scope 推断
  - 更清晰的 subject 描述（包含"改了什么"和"为什么改"）

- 📊 优化了设置持久化
  - 所有设置保存在 `GitCommitMessageHelper.xml`
  - 支持跨项目配置共享
  - 敏感信息（API Key）安全存储

- 🎯 改进了用户体验
  - 更友好的错误提示信息
  - AI 请求失败时的详细错误说明
  - 国内 AI 提供商的特别说明和推荐

### 文档
- 📖 新增 **USER_GUIDE.md** - 完整用户手册
  - 快速开始指南
  - 详细的 AI 配置步骤（每个提供商）
  - 国内用户推荐配置（智谱、DeepSeek、通义）
  - 多语言支持说明
  - 高级功能使用方法
  - 故障排除指南
  - 最佳实践建议
  - 详细的 FAQ

- 📖 新增 **MULTILANG_SUPPORT.md** - 多语言功能专项文档
- 📖 新增 **DOCS_OPTIMIZATION_PLAN.md** - 文档优化方案
- 📖 更新 **README.md** - 全新设计
  - 添加徽章（版本、平台、许可证）
  - 简化结构，聚焦核心特性
  - 添加 AI 提供商对比表
  - 添加使用示例
  - 添加文档导航链接

### 技术栈更新
- 新增依赖：无（使用标准 HTTP 客户端）
- JSON 解析：使用 IntelliJ 平台内置的 JSON 库
- HTTP 请求：使用 Java 11+ HttpClient

### 已知问题
- ⚠️ Claude API 需要单独的 API Key（非 Anthropic Console 的 Key）
- ⚠️ Ollama 需要本地运行服务（默认 http://localhost:11434）
- ⚠️ Azure OpenAI 需要 Azure 订阅和部署

## [1.0.0] - 2026-01-11

### 新增
- ✨ 提交信息自动生成功能
  - 基于文件变动智能生成提交类型
  - 自动推断作用域
  - 生成描述性的主题行
- ✨ 提交信息格式验证
  - 支持 Conventional Commits 规范
  - 实时错误和警告提示
  - 可配置的验证规则
- ✨ 智能文件分析
  - 根据文件扩展名推断提交类型
  - 支持常见项目结构识别
  - 分析文件变更类型（新增、修改、删除）
- ✨ 用户界面
  - Git 提交对话框集成
  - 生成按钮快速访问
  - 友好的错误提示对话框
- ✨ 设置页面
  - 启用/禁用验证功能
  - 启用/禁用自动生成
  - 自定义主题行长度限制
  - 添加自定义提交类型
- ✨ 完整的单元测试
  - 提交信息验证器测试
  - 提交信息模型测试
  - 覆盖主要功能

### 技术栈
- Java 17
- IntelliJ Platform SDK 2023.2
- Git4Idea 插件 API
- Gradle 8.5
- JUnit 4

### 文档
- 📖 完整的 README
- 📖 详细的使用指南
- 📖 开发流程文档
- 📖 API 文档（JavaDoc）

---

## 版本说明

### 版本格式

版本号格式为 `主版本.次版本.修订号`：

- **主版本**：不兼容的 API 修改
- **次版本**：向下兼容的功能性新增
- **修订号**：向下兼容的问题修正

### 变更类型

- **新增** - 新功能
- **变更** - 现有功能的变化
- **废弃** - 即将移除的功能
- **移除** - 已移除的功能
- **修复** - Bug 修复
- **安全** - 安全相关的修复

## 贡献指南

如果你想为此项目做贡献，请：

1. 在提交 Pull Request 前，确保更新此 CHANGELOG
2. 遵循 [Keep a Changelog](https://keepachangelog.com/) 格式
3. 在 `[未发布]` 部分添加你的更改
4. 发布新版本时，维护者会将更改移到对应的版本号下

## 链接

- [首页](README.md)
- [用户指南](USER_GUIDE.md)
- [安装指南](INSTALLATION.md)
- [开发指南](DEVELOPMENT.md)
- [贡献指南](CONTRIBUTING.md)
- [常见问题](FAQ.md)
- [问题追踪](https://github.com/yourusername/git-commit-message-helper/issues)
