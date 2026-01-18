# 多语言提交信息支持

## 🌏 新功能：中英文提交信息

插件现在支持生成**中文**或**英文**的提交信息！

默认语言：**中文** 🇨🇳

---

## ⚙️ 如何配置

### 步骤 1：打开设置

`Settings/Preferences` → `Tools` → `Git Commit Message Helper`

### 步骤 2：选择语言

在 AI 设置区域中，找到 **"Message Language"** 选项：

```
Message Language: [中文 (Chinese) ▼]
```

可选项：
- **中文 (Chinese)** - 默认选项
- **English**

### 步骤 3：应用设置

点击 **"Apply"** 或 **"OK"** 保存

---

## 📝 生成效果对比

### 场景：添加用户名验证

**代码变更**：
```java
public void addUser(User user) {
    if (user.getName().startsWith("li")) {
        throw new IllegalArgumentException("Username cannot start with 'li'");
    }
    users.add(user);
}
```

### 中文模式（默认）

```
feat(user): 添加用户名验证以防止 li 开头的用户名冲突
```

### 英文模式

```
feat(user): add username validation to prevent 'li' prefix conflicts
```

---

## 🎯 提交信息格式

### 中文模式

**格式**：`type(scope): 中文描述`

**说明**：
- `type` 和 `scope` 使用英文小写（遵循 Conventional Commits 规范）
- `subject`（主题）使用中文
- 更适合中文团队和项目

**示例**：
```
feat(auth): 添加用户登录功能
fix(user): 修复用户名验证的空指针异常
docs(readme): 更新项目安装文档
refactor(service): 重构用户服务以提高性能
test(user): 添加用户验证的单元测试
```

### 英文模式

**格式**：`type(scope): english description`

**说明**：
- 完全使用英文
- 适合国际化项目或英文团队

**示例**：
```
feat(auth): add user login functionality
fix(user): fix null pointer exception in username validation
docs(readme): update project installation guide
refactor(service): refactor user service for better performance
test(user): add unit tests for user validation
```

---

## 🔧 技术实现

### AI Prompt 自动切换

插件根据语言设置自动使用不同的 AI prompt：

#### 中文 Prompt（默认）

```
你是一个 Git 提交信息专家。分析以下代码变更，生成简洁、有意义的提交信息，遵循 Conventional Commits 规范。

格式：type(scope): subject

类型：feat（新功能）, fix（修复）, docs（文档）, style（格式）, refactor（重构）, test（测试）, chore（杂项）, build（构建）, ci（持续集成）

规则：
- 主题应清晰描述【改了什么】和【为什么改】
- type 和 scope 使用英文小写，subject 使用中文
- 不要以句号结尾
- 要具体说明业务逻辑或功能变化
- 整个消息不超过 100 个字符
- 示例：feat(user): 添加用户名验证以防止 li 开头的用户名冲突
- 示例：fix(auth): 修复登录时的空指针异常
```

#### 英文 Prompt

```
You are a Git commit message expert. Analyze the following code changes and generate a concise, meaningful commit message following the Conventional Commits specification.

Format: type(scope): subject

Types: feat, fix, docs, style, refactor, test, chore, build, ci

Rules:
- Subject should be clear and describe WHAT changed and WHY
- Use lowercase for subject
- No period at the end
- Be specific about the business logic or functionality changed
- Maximum 72 characters for the entire message
```

---

## 💡 使用建议

### 什么时候用中文？

✅ 团队成员都是中文使用者
✅ 项目文档和注释都是中文
✅ 希望提交信息更详细、更易读
✅ 内部项目，不对外开源

**优势**：
- 更清晰地表达业务逻辑
- 减少理解成本
- 支持更长的描述（中文字符信息密度高）

### 什么时候用英文？

✅ 国际化团队或开源项目
✅ 需要与国际开发者协作
✅ 遵循行业标准（大部分开源项目使用英文）
✅ 公司要求统一使用英文

**优势**：
- 符合国际惯例
- 更广泛的受众
- 与大多数开发工具兼容更好

---

## 🌟 最佳实践

### 推荐配置：混合模式（中文）

对于中文团队的私有项目，推荐使用中文模式：

```
feat(user): 添加邮箱验证功能
fix(order): 修复订单计算金额错误
docs(api): 更新 API 接口文档
refactor(payment): 重构支付模块以支持多种支付方式
```

**为什么推荐？**
- `type` 和 `scope` 仍使用英文，保持与工具兼容性
- `subject` 使用中文，更容易理解业务逻辑
- 兼顾规范性和可读性

---

## 🔄 随时切换

您可以随时更改语言设置：

1. 打开 Settings → Git Commit Message Helper
2. 修改 Message Language
3. 点击 Apply
4. 下次生成提交信息时立即生效

**无需重启 IDEA！**

---

## 📊 字符限制对比

| 模式 | 推荐长度 | 说明 |
|------|---------|------|
| 中文 | ≤ 100 字符 | 中文信息密度高，可以表达更多内容 |
| 英文 | ≤ 72 字符 | 遵循传统 Git 提交信息规范 |

**为什么中文可以更长？**
- 1 个中文字符 ≈ 2-3 个英文单词的信息量
- 示例：
  - 中文：`添加用户验证` (6 字符)
  - 英文：`add user validation` (21 字符)

---

## ❓ 常见问题

### Q1: 可以在同一个项目中混用中英文吗？

**A**: 可以随时切换，但建议保持一致性：
- 同一个项目最好统一使用一种语言
- 可以根据团队成员变化调整语言设置

### Q2: type 和 scope 可以用中文吗？

**A**: 不建议
- `type` 和 `scope` 建议保持英文，这是 Conventional Commits 规范
- 许多工具（如 changelog 生成器）依赖英文 type
- 只在 `subject` 部分使用中文

### Q3: 规则分析模式支持中文吗？

**A**: 不支持
- 规则分析模式（非 AI 模式）仅生成英文
- 中文功能需要启用 AI 增强模式

### Q4: 自定义 Prompt 会覆盖语言设置吗？

**A**: 是的
- 如果设置了自定义 Prompt Template，会忽略语言设置
- 如果想使用语言切换功能，请清空自定义 Prompt

---

## 🚀 开始使用

1. 更新插件到最新版本
2. 打开 Settings → Git Commit Message Helper
3. 选择 **"中文 (Chinese)"**（默认）
4. 开始享受中文提交信息！

---

**示例效果**：

```bash
$ git log --oneline

feat(user): 添加用户名验证以防止 li 开头的用户名冲突
fix(auth): 修复登录时的空指针异常
docs(readme): 更新项目安装说明
refactor(service): 重构用户服务以提高代码可维护性
```

简洁、清晰、易懂！ 🎉
