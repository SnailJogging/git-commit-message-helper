# SSL 证书问题解决方案 / SSL Certificate Fix

## 问题概述 / Problem Overview

### 问题描述 / Problem Description

在公司网络环境下使用 Git Commit Message Helper 插件时，所有 AI 提供商（DeepSeek、OpenAI、Claude 等）的连接测试都失败，错误信息为：

When using the Git Commit Message Helper plugin in a corporate network environment, all AI provider connection tests (DeepSeek, OpenAI, Claude, etc.) fail with the error:

```
Connection error: unable to get local issuer certificate
```

### 根本原因 / Root Cause

**问题分析**：
1. 公司网络使用 **mitmproxy** 进行 HTTPS 流量监控
2. mitmproxy 使用自签名证书替换原始服务器证书
3. Java 的 `HttpURLConnection` 严格验证 SSL 证书链
4. 自签名证书不在 Java 信任证书库中，导致验证失败

**Technical Analysis**:
1. Corporate network uses **mitmproxy** for HTTPS traffic monitoring
2. mitmproxy replaces original server certificates with self-signed certificates
3. Java's `HttpURLConnection` strictly validates SSL certificate chains
4. Self-signed certificates are not in Java's trusted certificate store, causing validation failure

### 证书链分析 / Certificate Chain Analysis

```bash
# 使用 curl 测试连接
curl -v https://api.deepseek.com/v1/models

# 输出显示证书问题
* SSL certificate problem: unable to get local issuer certificate (20)
* Closing connection 0

# 证书信息
* Server certificate:
*  subject: CN=api.deepseek.com
*  start date: Jan 19 13:37:26 2026 GMT
*  expire date: Apr 19 13:37:26 2026 GMT
*  issuer: CN=mitmproxy; O=mitmproxy  # ← 问题根源
```

## 解决方案 / Solution

### 实现方式 / Implementation Approach

添加一个**可选配置项**，允许用户在企业代理环境中信任所有 SSL 证书。

Added an **optional configuration option** allowing users to trust all SSL certificates in corporate proxy environments.

**重要警告 / IMPORTANT WARNING**:
- ⚠️ 该选项会**禁用 SSL 证书验证**
- ⚠️ 这使连接容易受到**中间人攻击**
- ⚠️ 仅在**受信任的企业网络环境**中启用
- ⚠️ This option **disables SSL certificate verification**
- ⚠️ This makes connections vulnerable to **man-in-the-middle attacks**
- ⚠️ Only enable in **trusted corporate network environments**

### 技术实现 / Technical Implementation

#### 1. SSL 工具类 / SSL Utility Class

创建 `SSLUtil.java` 实现 SSL 证书信任管理：

Created `SSLUtil.java` to implement SSL certificate trust management:

**文件位置 / File Location**: `src/main/java/com/github/gitcommithelper/util/SSLUtil.java`

**关键代码 / Key Code**:
```java
public class SSLUtil {
    private static SSLContext trustAllContext;
    private static HostnameVerifier trustAllHostnameVerifier;

    static {
        try {
            // 创建信任所有证书的 TrustManager
            // Create TrustManager that trusts all certificates
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
            };

            // 创建 SSLContext
            trustAllContext = SSLContext.getInstance("TLS");
            trustAllContext.init(null, trustAllCerts, new SecureRandom());

            // 创建 HostnameVerifier
            trustAllHostnameVerifier = (hostname, session) -> true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SSL context", e);
        }
    }

    /**
     * 配置 HttpURLConnection 以信任所有 SSL 证书
     * Configure HttpURLConnection to trust all SSL certificates
     */
    public static void trustAllCertificates(HttpURLConnection connection) {
        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
            httpsConnection.setSSLSocketFactory(trustAllContext.getSocketFactory());
            httpsConnection.setHostnameVerifier(trustAllHostnameVerifier);
        }
    }
}
```

#### 2. 设置存储 / Settings Storage

在 `PluginSettings.java` 中添加配置字段：

Added configuration field in `PluginSettings.java`:

```java
public class PluginSettings implements PersistentStateComponent<PluginSettings> {
    // ... 其他字段 / other fields ...

    public boolean trustAllCertificates = false; // Trust all SSL certificates (for corporate proxy)

    public boolean isTrustAllCertificates() {
        return trustAllCertificates;
    }

    public void setTrustAllCertificates(boolean trustAllCertificates) {
        this.trustAllCertificates = trustAllCertificates;
    }
}
```

#### 3. 基础 AI 提供商集成 / Base AI Provider Integration

在 `BaseAIProvider.java` 中添加 SSL 配置方法：

Added SSL configuration method in `BaseAIProvider.java`:

```java
protected void configureSSL(HttpURLConnection connection) {
    if (settings.isTrustAllCertificates()) {
        SSLUtil.trustAllCertificates(connection);
    }
}
```

#### 4. AI 提供商更新 / AI Provider Updates

在所有 AI 提供商中调用 SSL 配置：

Call SSL configuration in all AI providers:

**DeepSeekProvider.java**:
```java
public String testConnection() {
    try {
        URL url = new URL(getEndpoint() + "/models");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        configureSSL(conn); // ← 添加 SSL 配置 / Add SSL configuration
        // ... 其余代码 / rest of code ...
    }
}

public String generateCommitMessage(...) {
    try {
        URL url = new URL(getEndpoint() + "/chat/completions");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        configureSSL(conn); // ← 添加 SSL 配置 / Add SSL configuration
        // ... 其余代码 / rest of code ...
    }
}
```

**类似的修改应用于 / Similar changes applied to**:
- `OpenAIProvider.java`
- `ClaudeProvider.java`
- 其他所有 AI 提供商 / All other AI providers

#### 5. 用户界面 / User Interface

在 `SettingsPanel.java` 中添加复选框：

Added checkbox in `SettingsPanel.java`:

**UI 代码 / UI Code**:
```java
private JCheckBox trustAllCertificatesCheckBox;

// 在 addAISettings 方法中 / In addAISettings method:
trustAllCertificatesCheckBox = new JCheckBox("Trust all SSL certificates (for corporate proxy)");
trustAllCertificatesCheckBox.setToolTipText("WARNING: Disables SSL certificate verification. Only enable in corporate proxy environments.");
trustAllCertificatesCheckBox.addActionListener(e -> modified = true);
aiSettingsPanel.add(trustAllCertificatesCheckBox);
```

**加载设置 / Load Settings**:
```java
private void loadSettings() {
    // ... 其他设置 / other settings ...
    trustAllCertificatesCheckBox.setSelected(settings.isTrustAllCertificates());
}
```

**保存设置 / Save Settings**:
```java
private void saveToSettings(PluginSettings targetSettings) {
    // ... 其他设置 / other settings ...
    targetSettings.setTrustAllCertificates(trustAllCertificatesCheckBox.isSelected());
}
```

## 使用方法 / Usage Instructions

### 配置步骤 / Configuration Steps

1. **打开设置 / Open Settings**
   - IntelliJ IDEA → Settings → Tools → Git Commit Message Helper
   - 或 / Or: `Ctrl+Alt+S` (Windows/Linux) / `Cmd+,` (macOS)

2. **启用 AI 功能 / Enable AI Features**
   - 勾选 "Enable AI-enhanced generation"
   - Check "Enable AI-enhanced generation"

3. **配置 AI 提供商 / Configure AI Provider**
   - 选择 AI 提供商（DeepSeek、OpenAI、Claude 等）
   - Select AI Provider (DeepSeek, OpenAI, Claude, etc.)
   - 输入 API Key 和 Endpoint
   - Enter API Key and Endpoint

4. **启用 SSL 证书信任 / Enable SSL Certificate Trust**
   - ✅ 勾选 "Trust all SSL certificates (for corporate proxy)"
   - ✅ Check "Trust all SSL certificates (for corporate proxy)"
   - ⚠️ 阅读警告提示 / Read the warning tooltip

5. **测试连接 / Test Connection**
   - 点击 "Test Connection" 按钮
   - Click "Test Connection" button
   - 应该显示 "Connection successful!" / Should show "Connection successful!"

### 配置位置截图 / Configuration Location

```
Settings
└── Tools
    └── Git Commit Message Helper
        └── AI Enhancement Settings
            ├── ☑ Enable AI-enhanced generation
            ├── AI Provider: [DeepSeek ▼]
            ├── API Key: [sk-*********************]
            ├── API Endpoint: [https://api.deepseek.com]
            ├── ...
            ├── ☑ Enable caching
            ├── ☑ Fallback to basic mode if AI fails
            ├── ☑ Trust all SSL certificates (for corporate proxy) ← 这里！
            ├── Message Language: [中文 (Chinese) ▼]
            └── [Test Connection]
```

## 测试验证 / Testing Verification

### 测试环境 / Test Environment

- **网络环境 / Network Environment**: 公司代理 (mitmproxy) / Corporate proxy (mitmproxy)
- **操作系统 / OS**: macOS
- **IDE 版本 / IDE Version**: IntelliJ IDEA 2023.2.5+
- **Java 版本 / Java Version**: JDK 17+

### 测试步骤 / Test Steps

1. **未启用证书信任时 / Without Certificate Trust**:
   ```
   ❌ Connection error: unable to get local issuer certificate
   ```

2. **启用证书信任后 / With Certificate Trust**:
   ```
   ✅ Connection successful!
   ```

3. **生成提交信息测试 / Commit Message Generation Test**:
   ```
   # 修改一些文件 / Modify some files
   git add .

   # 在 IDEA 中打开 Commit 对话框 / Open Commit dialog in IDEA
   # 点击 "Generate Commit Message" / Click "Generate Commit Message"

   # 应该成功生成提交信息 / Should successfully generate commit message
   ✅ feat(ssl): 添加企业代理环境下的 SSL 证书信任配置
   ```

### 验证清单 / Verification Checklist

- [x] 构建成功无错误 / Build succeeds without errors
- [x] UI 显示新的复选框 / UI shows new checkbox
- [x] 设置能够正确保存和加载 / Settings save and load correctly
- [x] 未启用时连接失败（预期行为）/ Connection fails when disabled (expected)
- [x] 启用后连接成功 / Connection succeeds when enabled
- [x] 所有 AI 提供商都支持 / All AI providers supported
- [x] 提交信息生成正常工作 / Commit message generation works

## 安全考虑 / Security Considerations

### 风险说明 / Risk Explanation

启用 "Trust all SSL certificates" 会：

Enabling "Trust all SSL certificates" will:

1. **禁用证书验证 / Disable Certificate Verification**
   - 不再验证服务器证书的有效性
   - No longer validates server certificate validity
   - 不检查证书颁发机构 (CA)
   - Does not check Certificate Authority (CA)

2. **容易受到中间人攻击 / Vulnerable to MITM Attacks**
   - 攻击者可能拦截和修改流量
   - Attackers could intercept and modify traffic
   - 敏感数据（API Key）可能被窃取
   - Sensitive data (API Key) could be stolen

3. **仅限受信任环境 / Trusted Environments Only**
   - ✅ 公司内网环境 / Corporate intranet
   - ✅ 开发测试环境 / Development/test environment
   - ❌ 公共网络 / Public networks
   - ❌ 不受信任的网络 / Untrusted networks

### 最佳实践 / Best Practices

1. **仅在必要时启用 / Enable Only When Necessary**
   - 只在确实需要时才勾选此选项
   - Only check this option when truly needed
   - 不在公司网络时应禁用
   - Disable when not on corporate network

2. **使用环境变量 / Use Environment Variables**
   - 不要硬编码 API Key
   - Don't hardcode API Keys
   - 使用系统环境变量存储敏感信息
   - Use system environment variables for sensitive data

3. **定期审查设置 / Regular Settings Review**
   - 定期检查此选项是否仍然需要
   - Regularly check if this option is still needed
   - 当离开公司网络时禁用
   - Disable when leaving corporate network

4. **考虑导入 CA 证书 / Consider Importing CA Certificate**
   - 更安全的方案是将公司 CA 证书导入 Java 信任库
   - A safer approach is importing corporate CA certificate to Java trust store
   - 参考文档 / Reference documentation:
     ```bash
     # 导入证书到 Java keystore
     keytool -import -trustcacerts -file corporate-ca.crt \
       -alias corporateCA -keystore $JAVA_HOME/lib/security/cacerts
     ```

## 故障排除 / Troubleshooting

### 常见问题 / Common Issues

#### 1. 启用后仍然连接失败 / Still Fails After Enabling

**可能原因 / Possible Causes**:
- 设置未保存 / Settings not saved
- 需要重启 IDE / IDE restart required
- 代理配置不正确 / Proxy configuration incorrect

**解决方法 / Solutions**:
```bash
# 1. 确保设置已保存
# 1. Ensure settings are saved
Settings → Apply → OK

# 2. 重启 IntelliJ IDEA
# 2. Restart IntelliJ IDEA

# 3. 检查系统代理设置
# 3. Check system proxy settings
echo $http_proxy
echo $https_proxy
```

#### 2. 其他应用仍然可用，插件不行 / Other Apps Work, Plugin Doesn't

**原因 / Reason**:
- Java 和其他应用使用不同的证书存储
- Java and other applications use different certificate stores

**解决方法 / Solution**:
- 确保插件设置中已启用 SSL 证书信任
- Ensure SSL certificate trust is enabled in plugin settings
- 使用 curl 测试时加上 `-k` 参数类似效果
- Using curl with `-k` flag has similar effect

#### 3. 构建失败 SSL 错误 / Build Fails with SSL Error

**问题 / Issue**:
```
Could not HEAD 'https://teamcity.jetbrains.com/...'
Remote host terminated the handshake
```

**解决方法 / Solution**:
```bash
# 使用 Gradle 时信任所有证书
# Trust all certificates when using Gradle
./gradlew buildPlugin -Djavax.net.ssl.trustAll=true

# 或配置 gradle.properties
# Or configure gradle.properties
systemProp.javax.net.ssl.trustAll=true
```

## 相关文件 / Related Files

### 源代码文件 / Source Code Files

1. **SSL 工具类 / SSL Utility**
   - `src/main/java/com/github/gitcommithelper/util/SSLUtil.java`
   - 实现 SSL 证书信任逻辑 / Implements SSL certificate trust logic

2. **设置类 / Settings Class**
   - `src/main/java/com/github/gitcommithelper/settings/PluginSettings.java`
   - 存储配置选项 / Stores configuration option

3. **基础提供商 / Base Provider**
   - `src/main/java/com/github/gitcommithelper/ai/BaseAIProvider.java`
   - 提供 SSL 配置方法 / Provides SSL configuration method

4. **AI 提供商 / AI Providers**
   - `src/main/java/com/github/gitcommithelper/ai/DeepSeekProvider.java`
   - `src/main/java/com/github/gitcommithelper/ai/OpenAIProvider.java`
   - `src/main/java/com/github/gitcommithelper/ai/ClaudeProvider.java`
   - 调用 SSL 配置 / Call SSL configuration

5. **用户界面 / User Interface**
   - `src/main/java/com/github/gitcommithelper/ui/SettingsPanel.java`
   - 提供配置界面 / Provides configuration UI

### 文档文件 / Documentation Files

1. **本文档 / This Document**
   - `docs/internal/SSL_CERTIFICATE_FIX.md`
   - SSL 证书问题解决方案完整说明 / Complete explanation of SSL certificate fix

2. **用户指南 / User Guide**
   - `USER_GUIDE.md`
   - 需要更新以包含 SSL 配置说明 / Needs update to include SSL configuration instructions

3. **FAQ**
   - `FAQ.md`
   - 需要添加 SSL 相关常见问题 / Needs SSL-related FAQ entries

## 版本历史 / Version History

### v1.1.0 (计划中 / Planned)

**新增功能 / New Features**:
- ✅ 添加 SSL 证书信任配置选项 / Added SSL certificate trust configuration option
- ✅ 支持企业代理环境（mitmproxy）/ Support for corporate proxy environments (mitmproxy)
- ✅ 所有 AI 提供商支持 SSL 配置 / All AI providers support SSL configuration

**改进 / Improvements**:
- 更好的错误提示 / Better error messages
- 配置界面添加警告提示 / Warning tooltip in configuration UI
- 完整的文档说明 / Complete documentation

**修复 / Bug Fixes**:
- 修复企业代理环境下连接失败问题 / Fixed connection failures in corporate proxy environments

## 参考资料 / References

### 技术文档 / Technical Documentation

1. **Java SSL/TLS**
   - [Java Secure Socket Extension (JSSE) Reference Guide](https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html)
   - [X509TrustManager Interface](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/X509TrustManager.html)

2. **mitmproxy**
   - [mitmproxy Documentation](https://docs.mitmproxy.org/)
   - [mitmproxy Certificate Installation](https://docs.mitmproxy.org/stable/concepts-certificates/)

3. **IntelliJ Platform SDK**
   - [Plugin Settings](https://plugins.jetbrains.com/docs/intellij/settings.html)
   - [Persisting State of Components](https://plugins.jetbrains.com/docs/intellij/persisting-state-of-components.html)

### 相关问题 / Related Issues

1. **Stack Overflow**
   - [Trusting all certificates using HttpClient over HTTPS](https://stackoverflow.com/questions/2642777/trusting-all-certificates-using-httpclient-over-https)
   - [How to bypass SSL certificate checking in Java](https://stackoverflow.com/questions/1828775/how-to-bypass-ssl-certificate-checking-in-java)

2. **GitHub Issues**
   - 类似问题的讨论和解决方案
   - Similar issues and solutions

---

## 总结 / Summary

该实现提供了一个**安全、可控、用户友好**的解决方案，用于处理企业代理环境下的 SSL 证书验证问题。

This implementation provides a **secure, controllable, and user-friendly** solution for handling SSL certificate verification issues in corporate proxy environments.

**关键优势 / Key Advantages**:
- ✅ 可选配置，默认安全 / Optional configuration, secure by default
- ✅ 清晰的警告提示 / Clear warning messages
- ✅ 支持所有 AI 提供商 / Supports all AI providers
- ✅ 易于使用和配置 / Easy to use and configure
- ✅ 完整的文档说明 / Complete documentation

**使用建议 / Usage Recommendations**:
- ⚠️ 仅在受信任的企业网络环境中启用 / Enable only in trusted corporate network environments
- ⚠️ 定期审查是否仍需要此选项 / Regularly review if this option is still needed
- ⚠️ 考虑更安全的方案（导入 CA 证书）/ Consider safer alternatives (import CA certificate)

---

**最后更新 / Last Updated**: 2026-01-19
**版本 / Version**: 1.1.0 (开发中 / In Development)
**作者 / Author**: Git Commit Message Helper Team
