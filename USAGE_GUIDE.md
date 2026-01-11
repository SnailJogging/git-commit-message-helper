# Git Commit Message Helper 使用指南

## 快速开始

### 1. 安装插件

安装完成后，插件会自动集成到 IntelliJ IDEA 的 Git 提交界面中。

### 2. 第一次使用

#### 方式一：自动生成提交信息

1. 修改一些文件
2. 打开 Git 提交窗口 (`Cmd/Ctrl + K`)
3. 点击提交信息框上方工具栏的 ⚡ **"Generate Commit Message"** 按钮
4. 插件会分析你的文件变动并生成建议的提交信息
5. 点击 "Copy to Clipboard" 复制生成的信息到提交框

#### 方式二：手动编写并验证

1. 在提交信息框中手动输入提交信息
2. 点击 "Commit" 按钮
3. 插件会自动验证你的提交信息格式
4. 如果格式有问题，会显示错误或警告提示

## 详细功能说明

### 提交信息格式

插件遵循 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```
<type>(<scope>): <subject>

<body>

<footer>
```

#### 必填部分
- **type**: 提交类型（必填）
- **subject**: 简短描述（必填）

#### 可选部分
- **scope**: 影响范围（可选）
- **body**: 详细描述（可选）
- **footer**: 额外信息，如关联的 issue（可选）

### 提交类型详解

| 类型 | 何时使用 | 示例 |
|------|---------|------|
| **feat** | 添加新功能 | `feat(auth): add login functionality` |
| **fix** | 修复 bug | `fix(api): resolve null pointer exception` |
| **docs** | 文档变更 | `docs: update README with installation guide` |
| **style** | 代码格式调整 | `style: format code with prettier` |
| **refactor** | 代码重构 | `refactor(service): simplify user validation logic` |
| **perf** | 性能优化 | `perf(db): optimize query performance` |
| **test** | 测试相关 | `test(auth): add unit tests for login` |
| **build** | 构建系统或依赖 | `build: update gradle to 8.5` |
| **ci** | CI/CD 配置 | `ci: add GitHub Actions workflow` |
| **chore** | 其他杂项 | `chore: update dependencies` |
| **revert** | 回滚提交 | `revert: revert "feat: add login"` |

### 智能类型推断

插件会根据修改的文件类型自动推荐提交类型：

| 文件类型 | 推荐类型 |
|---------|---------|
| `*.md`, `README`, `docs/` | `docs` |
| `*Test.java`, `*.test.ts`, `*.spec.js` | `test` |
| `package.json`, `pom.xml`, `build.gradle` | `build` |
| `.github/`, `.gitlab-ci.yml`, `Jenkinsfile` | `ci` |
| 新增文件较多 | `feat` |
| 修改现有文件 | `fix` |

### 作用域（Scope）建议

作用域表示本次提交影响的范围，插件会自动分析：

- **单一目录修改**：使用目录名作为作用域
  - 例如：修改 `src/auth/` 目录下的文件 → `feat(auth): ...`

- **多个相关文件**：使用公共模块名
  - 例如：修改多个认证相关文件 → `fix(auth): ...`

- **无明显作用域**：可以省略
  - 例如：`docs: update README`

### 提交信息验证

#### 错误级别（阻止提交）

以下情况会显示错误：

- ❌ 提交信息为空
- ❌ 格式不符合 `type: subject` 或 `type(scope): subject`
- ❌ 缺少主题描述

#### 警告级别（建议修改）

以下情况会显示警告，但允许提交：

- ⚠️ 使用未知的提交类型
- ⚠️ 主题以大写字母开头（建议小写）
- ⚠️ 主题以句号结尾（建议不加）
- ⚠️ 主题过短（少于 10 个字符）
- ⚠️ 标题行过长（超过 72 个字符）
- ⚠️ 正文行过长（超过 100 个字符）
- ⚠️ 标题和正文之间缺少空行

## 最佳实践

### ✅ 好的提交信息示例

```
feat(auth): add JWT authentication

Implement JSON Web Token based authentication system.
- Add login endpoint
- Add token validation middleware
- Add refresh token mechanism

Closes #123
```

```
fix(api): resolve race condition in data fetch

Fixed an issue where concurrent requests could cause
data inconsistency.

Fixes #456
```

```
docs: update API documentation

Add examples for authentication endpoints.
```

### ❌ 不好的提交信息示例

```
Update files
```
> 缺少类型和具体描述

```
fix bug
```
> 描述过于简单，没有说明修复了什么

```
FEAT: Add new feature.
```
> 类型应小写，主题不应以大写开头，不应以句号结尾

```
feat(authentication-module-and-user-management): add very long feature description that exceeds the recommended limit
```
> 作用域过长，主题行过长

## 高级用法

### 破坏性变更（Breaking Changes）

当提交包含不兼容的 API 变更时：

```
feat(api)!: change authentication endpoint

BREAKING CHANGE: The /auth endpoint now requires
a different request format. Update your API calls
to use the new format.
```

### 多行正文

使用正文提供更多上下文：

```
refactor(database): optimize query performance

Replaced N+1 queries with batch loading.
This improves page load time by approximately 60%.

Technical details:
- Implemented DataLoader pattern
- Added query result caching
- Optimized database indexes
```

### 关联 Issue

在 footer 中引用相关 issue：

```
fix(login): resolve password validation bug

Fixes #123
Closes #124
See also #125
```

## 配置选项

### 访问设置

`File` → `Settings` → `Tools` → `Git Commit Message Helper`

### 可配置项

1. **启用提交信息验证**
   - 开启：提交前自动验证格式
   - 关闭：不进行验证

2. **启用自动生成**
   - 开启：可以使用生成按钮
   - 关闭：隐藏生成功能

3. **显示警告**
   - 开启：显示所有警告
   - 关闭：只显示错误

4. **最大主题长度**
   - 默认：72 字符
   - 可调整：20-200 字符

5. **自定义提交类型**
   - 添加团队特定的提交类型
   - 每行一个类型

## 常见问题

### Q: 如何禁用自动验证？

A: 进入设置页面，取消勾选 "Enable commit message validation"。

### Q: 生成的提交信息不准确怎么办？

A: 生成的信息是基于文件变动的智能推测，你可以：
1. 手动编辑生成的信息
2. 完全手写提交信息
3. 通过配置自定义规则

### Q: 可以为不同项目使用不同的规则吗？

A: 当前版本的设置是全局的。未来版本会支持项目级别的配置。

### Q: 如何添加自定义的提交类型？

A: 在设置页面的 "Custom Commit Types" 文本框中，每行添加一个类型名称。

### Q: 插件会影响提交性能吗？

A: 插件只在以下情况下运行：
- 点击生成按钮时
- 提交前验证时

对正常的代码编辑没有任何影响。

## 快捷键

| 操作 | 快捷键 |
|------|--------|
| 打开提交窗口 | `Cmd/Ctrl + K` |
| 提交 | `Cmd/Ctrl + Enter` |

## 获取帮助

- 📖 完整文档：[README.md](README.md)
- 🐛 报告问题：[GitHub Issues](https://github.com/yourusername/git-commit-message-helper/issues)
- 💡 功能建议：欢迎提交 Issue 或 Pull Request

## 更新日志

### v1.0.0 (当前版本)

- ✨ 初始发布
- ✅ 提交信息自动生成
- ✅ 格式验证
- ✅ 智能类型推断
- ✅ 作用域建议

---

享受更规范的 Git 提交体验！ 🎉
