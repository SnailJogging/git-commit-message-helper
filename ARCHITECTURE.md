# 项目架构文档

## 概述

Git Commit Message Helper 是一个 IntelliJ IDEA 插件，采用分层架构设计，遵循职责分离原则。

## 架构图

```
┌─────────────────────────────────────────────────────────┐
│                    用户界面层 (UI)                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │SettingsPanel │  │  Action      │  │ CheckinHandler│  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
                           │
                           ↓
┌─────────────────────────────────────────────────────────┐
│                    业务逻辑层 (Service)                   │
│  ┌──────────────────┐  ┌────────────────────────────┐  │
│  │GitAnalysisService│  │CommitMessageGenerator      │  │
│  └──────────────────┘  └────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                           │
                           ↓
┌─────────────────────────────────────────────────────────┐
│                    验证层 (Validator)                     │
│  ┌──────────────────────────────────────────────────┐  │
│  │          CommitMessageValidator                   │  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                           │
                           ↓
┌─────────────────────────────────────────────────────────┐
│                    数据模型层 (Model)                     │
│  ┌──────────────┐  ┌──────────────┐  ┌─────────────┐  │
│  │ CommitType   │  │CommitMessage │  │FileChangeInfo│  │
│  └──────────────┘  └──────────────┘  └─────────────┘  │
└─────────────────────────────────────────────────────────┘
                           │
                           ↓
┌─────────────────────────────────────────────────────────┐
│               外部依赖 (External Dependencies)            │
│  ┌──────────────────┐  ┌────────────────────────────┐  │
│  │IntelliJ Platform │  │      Git4Idea API          │  │
│  └──────────────────┘  └────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

## 核心组件

### 1. 模型层 (Model)

负责定义数据结构和业务实体。

#### CommitType.java
- **职责**：定义所有支持的提交类型枚举
- **特点**：
  - 支持 11 种标准提交类型
  - 提供类型描述
  - 支持字符串转换

#### CommitMessage.java
- **职责**：表示一个完整的提交信息
- **功能**：
  - 存储提交信息的各个部分（type、scope、subject、body、footer）
  - 格式化输出符合 Conventional Commits 的字符串
  - 解析字符串为结构化对象
- **设计模式**：Builder 模式（隐式）

#### FileChangeInfo.java
- **职责**：表示文件变更信息
- **包含**：
  - 文件路径和名称
  - 文件扩展名
  - 变更类型（新增、修改、删除、重命名）
  - 代码行数变化

### 2. 服务层 (Service)

包含核心业务逻辑。

#### GitAnalysisService.java
- **职责**：分析 Git 仓库的变更
- **功能**：
  - 获取当前变更列表
  - 分析文件变动类型
  - 推断合适的提交类型
  - 建议提交作用域
  - 分析提交历史（预留）
- **设计模式**：单例模式（通过 Application Service）

#### CommitMessageGenerator.java
- **职责**：生成提交信息模板
- **功能**：
  - 基于文件变动生成完整提交信息
  - 生成描述性的主题行
  - 提供多个建议（预留）
- **依赖**：GitAnalysisService

#### CommitMessageProviderFactory.java
- **职责**：创建提交检查处理器
- **功能**：
  - 在提交前验证提交信息
  - 显示验证结果
  - 允许用户选择是否继续
- **集成点**：IntelliJ VCS 提交流程

### 3. 验证层 (Validator)

#### CommitMessageValidator.java
- **职责**：验证提交信息格式
- **功能**：
  - 检查是否符合 Conventional Commits 格式
  - 提供详细的错误和警告信息
  - 支持快速格式检查
  - 提取提交类型
- **验证规则**：
  - 必须遵循 `type(scope): subject` 格式
  - 主题行不超过 72 字符
  - 正文行不超过 100 字符
  - 主题行不以句号结尾
  - 主题行以小写开头

### 4. 用户界面层 (UI)

#### GenerateCommitMessageAction.java
- **职责**：用户触发的生成操作
- **功能**：
  - 响应工具栏按钮点击
  - 调用生成服务
  - 显示生成结果
  - 提供复制到剪贴板功能
- **设计模式**：命令模式

#### SettingsConfigurable.java
- **职责**：插件设置页面配置
- **功能**：
  - 注册设置页面
  - 管理设置的保存和加载
- **集成点**：IntelliJ Settings 系统

#### SettingsPanel.java
- **职责**：设置页面 UI
- **功能**：
  - 启用/禁用验证
  - 启用/禁用自动生成
  - 配置主题行长度
  - 自定义提交类型
- **UI 组件**：Swing

## 数据流

### 生成提交信息流程

```
用户点击生成按钮
    ↓
GenerateCommitMessageAction.actionPerformed()
    ↓
CommitMessageGenerator.generateTemplate(project)
    ↓
GitAnalysisService.analyzeFileChanges(project)
    ↓
GitAnalysisService.suggestCommitType(fileChanges)
    ↓
GitAnalysisService.suggestScope(fileChanges)
    ↓
CommitMessage.format()
    ↓
显示给用户 / 复制到剪贴板
```

### 提交验证流程

```
用户点击 Commit 按钮
    ↓
CommitMessageValidationHandler.beforeCheckin()
    ↓
CommitMessageValidator.validate(message)
    ↓
生成 ValidationResult
    ↓
显示错误/警告对话框
    ↓
用户选择继续或取消
```

## 设计模式

### 1. 单例模式 (Singleton)
- **应用**：GitAnalysisService
- **实现**：通过 IntelliJ Application Service 机制

### 2. 工厂模式 (Factory)
- **应用**：CommitMessageProviderFactory
- **目的**：创建 CheckinHandler 实例

### 3. 策略模式 (Strategy)
- **应用**：文件类型到提交类型的映射
- **实现**：在 GitAnalysisService 中的多个判断分支

### 4. 模板方法模式 (Template Method)
- **应用**：提交信息生成
- **实现**：CommitMessageGenerator 中的 generateSubject 方法

## 扩展点

### 1. IntelliJ Platform 扩展点

在 `plugin.xml` 中注册：

```xml
<!-- 提交检查处理器 -->
<vcsCheckinHandlerFactory
    implementation="...CommitMessageProviderFactory"/>

<!-- 设置页面 -->
<projectConfigurable
    instance="...SettingsConfigurable"/>

<!-- 应用服务 -->
<applicationService
    serviceImplementation="...GitAnalysisService"/>
```

### 2. Action 扩展点

```xml
<action id="...GenerateCommitMessageAction"
        class="...GenerateCommitMessageAction"
        text="Generate Commit Message"
        icon="AllIcons.Actions.Lightning">
    <add-to-group group-id="Vcs.MessageActionGroup" anchor="first"/>
</action>
```

## 依赖关系

```
UI Layer (Action, Settings)
    ↓ 依赖
Service Layer (Generator, Analysis)
    ↓ 依赖
Validator Layer (MessageValidator)
    ↓ 依赖
Model Layer (CommitType, Message, FileInfo)
    ↓ 依赖
External (IntelliJ Platform, Git4Idea)
```

## 测试架构

### 单元测试

- **CommitMessageValidatorTest**：测试验证逻辑
- **CommitMessageTest**：测试模型的解析和格式化

### 测试覆盖

- ✅ 模型层：100%
- ✅ 验证层：100%
- ⚠️ 服务层：需要 Mock（未完成）
- ⚠️ UI 层：需要 UI 测试框架（未完成）

## 性能考虑

### 1. 延迟初始化
- 服务只在需要时创建
- UI 组件按需加载

### 2. 缓存策略
- GitAnalysisService 分析结果可以缓存（待实现）
- 历史提交分析结果缓存（待实现）

### 3. 异步处理
- 文件分析在后台线程执行（待实现）
- 历史记录分析异步加载（待实现）

## 安全考虑

### 1. 输入验证
- 所有用户输入都经过验证
- 防止注入攻击（正则表达式安全）

### 2. 权限控制
- 只读取 Git 仓库信息
- 不修改任何文件或配置（除用户主动保存设置）

## 未来扩展

### 1. 插件化架构
- 支持自定义提交规范
- 提供 API 供其他插件集成

### 2. AI 集成
- 使用机器学习优化建议
- 基于项目历史学习提交模式

### 3. 团队协作
- 共享团队提交规范
- 统计和报告功能

## 技术债务

### 当前已知问题

1. **CommitMessageProviderFactory** 中的 CommitMessage 导入冲突
2. **GitAnalysisService** 历史分析功能未实现
3. 缺少集成测试
4. UI 测试覆盖不足
5. 缺少性能测试

### 改进计划

1. 添加更多单元测试
2. 实现集成测试框架
3. 优化文件分析性能
4. 添加配置持久化
5. 实现项目级别配置

## 文档维护

本文档应该在以下情况下更新：

- 添加新的核心组件
- 修改架构设计
- 引入新的设计模式
- 添加重要的扩展点
- 重大重构

---

**维护者**：开发团队
**最后更新**：2026-01-11
**版本**：1.0.0
