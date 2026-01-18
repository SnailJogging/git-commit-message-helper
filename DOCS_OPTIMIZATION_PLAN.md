# 文档优化方案

## 📊 当前问题

现有文档共 **25 个**，存在以下问题：

1. **高度重复**：多个文档描述相同内容
2. **结构混乱**：缺乏清晰的文档层次
3. **命名不一致**：中英文混用，大小写不统一
4. **维护困难**：更新一个功能需要修改多个文档

---

## 🎯 优化目标

1. **精简数量**：从 25 个减少到 **8-10 个核心文档**
2. **清晰分类**：用户文档、开发文档、项目管理文档分离
3. **统一风格**：统一命名规范和结构
4. **易于维护**：每个功能只在一个地方描述

---

## 📋 文档分类与合并方案

### ✅ 保留并优化的核心文档

#### 1. **README.md** ⭐ 最重要
**目标读者**：所有用户
**内容**：
- 项目简介
- 核心特性
- 快速开始（安装 + 基本使用）
- 文档导航（链接到其他文档）
- 徽章（版本、构建状态、许可证）

**合并来源**：
- 当前 README.md
- QUICK_START.md 的核心内容
- PROJECT_SUMMARY.md 的特性部分

---

#### 2. **INSTALLATION.md**
**目标读者**：新用户
**内容**：
- 从 JetBrains Marketplace 安装
- 从磁盘安装（本地构建）
- 系统要求
- 故障排除

**合并来源**：
- 当前 INSTALLATION.md
- install-guide.md（删除）
- QUICK_START.md 的安装部分

---

#### 3. **USER_GUIDE.md**（新建，替代多个用户文档）
**目标读者**：插件用户
**内容**：
- 基本使用方法
- AI 配置（所有 AI 提供商）
- 多语言支持（中文/英文）
- 高级功能（选中文件分析、自动填充）
- 故障排除
- 最佳实践

**合并来源**：
- USAGE_GUIDE.md
- AI_SETUP_GUIDE.md
- CHINESE_AI_SETUP.md
- MULTILANG_SUPPORT.md
- PLUGIN_DEMO.md（演示部分）
- QUICK_TEST.md（测试方法部分）
- FEATURE_UPDATE_v1.1.md（功能描述部分）

**结构**：
```markdown
# Git Commit Message Helper - 用户指南

## 快速开始
- 基本使用
- 第一次生成提交信息

## AI 配置
### 配置步骤
### 支持的 AI 提供商
- OpenAI GPT
- Claude (Anthropic)
- DeepSeek
- 智谱 AI (GLM)
- 阿里通义千问
- Azure OpenAI
- Ollama (本地)
- 自定义 Endpoint

### 国内用户推荐配置
- 智谱 AI 配置
- 通义千问配置
- DeepSeek 配置

## 多语言支持
- 中文模式（默认）
- 英文模式
- 如何切换

## 高级功能
- 直接填充提交信息
- 仅分析选中文件
- 自定义 Prompt

## 故障排除
## 最佳实践
## 常见问题
```

---

#### 4. **CHANGELOG.md**
**目标读者**：所有用户
**内容**：
- 按版本记录所有变更
- 遵循 Keep a Changelog 格式

**合并来源**：
- 当前 CHANGELOG.md
- UPDATE_SUMMARY.md（作为某个版本的详细说明）
- FEATURE_UPDATE_v1.1.md（作为 v1.1 的变更日志）

---

#### 5. **CONTRIBUTING.md**
**目标读者**：贡献者
**内容**：
- 如何贡献代码
- 开发环境设置
- 代码规范
- 提交 PR 流程
- 代码审查标准

**合并来源**：
- 当前 CONTRIBUTING.md
- 开发流程.md

---

#### 6. **ARCHITECTURE.md**
**目标读者**：开发者
**内容**：
- 项目架构设计
- 核心组件说明
- 技术栈
- 扩展点

**保持不变**：当前内容已经很好

---

#### 7. **DEVELOPMENT.md**（新建）
**目标读者**：开发者
**内容**：
- 构建和运行
- 测试指南
- 调试技巧
- 发布流程

**合并来源**：
- TESTING_GUIDE.md
- QUICK_TEST.md（开发者测试部分）
- IMPLEMENTATION_SUMMARY.md（技术实现部分）
- 开发流程.md（开发步骤）

---

#### 8. **FAQ.md**（新建）
**目标读者**：所有用户
**内容**：
- 常见问题及解答
- 从各个文档中提取

**提取自**：
- AI_SETUP_GUIDE.md 的故障排除
- CHINESE_AI_SETUP.md 的常见问题
- MULTILANG_SUPPORT.md 的常见问题

---

### ❌ 删除的文档

以下文档内容已合并到其他文档，建议删除：

1. **QUICK_START.md** → 合并到 README.md
2. **USAGE_GUIDE.md** → 合并到新的 USER_GUIDE.md
3. **AI_SETUP_GUIDE.md** → 合并到 USER_GUIDE.md
4. **CHINESE_AI_SETUP.md** → 合并到 USER_GUIDE.md
5. **MULTILANG_SUPPORT.md** → 合并到 USER_GUIDE.md
6. **PLUGIN_DEMO.md** → 合并到 USER_GUIDE.md
7. **QUICK_TEST.md** → 合并到 USER_GUIDE.md 和 DEVELOPMENT.md
8. **TESTING_GUIDE.md** → 合并到 DEVELOPMENT.md
9. **FEATURE_UPDATE_v1.1.md** → 合并到 CHANGELOG.md
10. **UPDATE_SUMMARY.md** → 合并到 CHANGELOG.md
11. **install-guide.md** → 合并到 INSTALLATION.md
12. **开发流程.md** → 合并到 CONTRIBUTING.md 和 DEVELOPMENT.md
13. **IMPLEMENTATION_SUMMARY.md** → 合并到 ARCHITECTURE.md 和 DEVELOPMENT.md

### 🗂️ 移到文档目录的文件（项目管理相关）

这些文档是项目管理/内部使用的，建议移到 `docs/internal/` 目录：

1. **PROJECT_SUMMARY.md** → `docs/internal/`
2. **PROJECT_STATUS.md** → `docs/internal/`
3. **PROJECT_COMPLETE.md** → `docs/internal/`
4. **NEXT_STEPS.md** → `docs/internal/`
5. **FILE_LIST.md** → `docs/internal/`
6. **GITHUB_SETUP.md** → `docs/internal/`
7. **PUSH_TO_GITHUB.md** → `docs/internal/`

---

## 📁 优化后的文档结构

```
Git.Commit.Message.Helper/
├── README.md                    # 项目主页，所有人的入口
├── INSTALLATION.md              # 安装指南
├── USER_GUIDE.md                # 完整用户手册（合并了 7 个文档）
├── CHANGELOG.md                 # 版本变更历史
├── CONTRIBUTING.md              # 贡献指南
├── ARCHITECTURE.md              # 架构设计
├── DEVELOPMENT.md               # 开发指南
├── FAQ.md                       # 常见问题
├── LICENSE                      # 许可证
└── docs/
    └── internal/                # 内部项目管理文档
        ├── PROJECT_SUMMARY.md
        ├── PROJECT_STATUS.md
        ├── FILE_LIST.md
        └── ...
```

**从 25 个减少到 8 个核心文档 + 7 个内部文档**

---

## 🔄 合并执行计划

### Phase 1: 创建新文档

1. **创建 USER_GUIDE.md**
   - 合并 7 个用户相关文档的内容
   - 重新组织结构，去重
   - 统一格式和风格

2. **创建 DEVELOPMENT.md**
   - 合并开发相关文档
   - 包含构建、测试、调试

3. **创建 FAQ.md**
   - 从各文档提取常见问题
   - 分类整理

### Phase 2: 更新现有文档

1. **更新 README.md**
   - 简化，聚焦项目介绍
   - 添加导航链接

2. **更新 INSTALLATION.md**
   - 合并 install-guide.md

3. **更新 CHANGELOG.md**
   - 添加 v1.1 的完整变更
   - 合并 UPDATE_SUMMARY.md 的内容

4. **更新 CONTRIBUTING.md**
   - 合并开发流程

### Phase 3: 清理

1. 删除已合并的 13 个文档
2. 创建 `docs/internal/` 目录
3. 移动 7 个内部文档

---

## ✅ 优化后的好处

1. **用户友好**
   - 清晰的文档导航
   - 一个地方找到所有用户信息（USER_GUIDE.md）

2. **开发者友好**
   - 开发相关信息集中
   - 架构和实现分离

3. **易于维护**
   - 减少重复内容
   - 更新功能只需修改一个文档

4. **专业形象**
   - 结构清晰
   - 命名统一
   - 符合开源项目标准

---

## 📝 实施建议

### 立即执行（高优先级）

1. ✅ 创建 **USER_GUIDE.md**（最重要）
2. ✅ 更新 **README.md**
3. ✅ 更新 **CHANGELOG.md**

### 第二阶段

4. 创建 **DEVELOPMENT.md**
5. 创建 **FAQ.md**
6. 更新 **CONTRIBUTING.md**

### 第三阶段

7. 删除冗余文档
8. 整理内部文档

---

## 🎯 下一步行动

是否要我帮您：

1. **立即创建优化后的 USER_GUIDE.md**？
2. **更新 README.md 使其更简洁**？
3. **更新 CHANGELOG.md 包含所有新功能**？

请告诉我从哪个开始！
