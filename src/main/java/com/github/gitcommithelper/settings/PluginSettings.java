package com.github.gitcommithelper.settings;

import com.github.gitcommithelper.ai.AIProviderType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Persistent settings for the Git Commit Message Helper plugin
 */
@State(
        name = "GitCommitMessageHelperSettings",
        storages = @Storage("GitCommitMessageHelper.xml")
)
public class PluginSettings implements PersistentStateComponent<PluginSettings> {

    // Validation settings
    public boolean enableValidation = true;
    public boolean showWarnings = true;

    // Generation settings
    public boolean enableAutoGeneration = true;
    public int maxSubjectLength = 72;
    public String customTypes = "";

    // AI settings
    public boolean enableAI = false;
    public String aiProviderType = AIProviderType.NONE.name();

    // Per-provider API keys (new implementation)
    public Map<String, String> providerApiKeys = new HashMap<>();

    // Legacy single API key (kept for backward compatibility)
    @Deprecated
    public String apiKey = "";

    public String apiEndpoint = "";
    public String model = "gpt-4-turbo";
    public int maxTokens = 500;
    public double temperature = 0.3;
    public int timeoutSeconds = 30;
    public boolean enableCaching = true;
    public boolean fallbackToBasic = true;
    public boolean includeFileContent = false;
    public String promptTemplate = "";
    public String messageLanguage = "zh-CN"; // Default to Chinese
    public boolean trustAllCertificates = true; // Default enabled for corporate proxy convenience

    public static PluginSettings getInstance() {
        return ApplicationManager.getApplication().getService(PluginSettings.class);
    }

    @Nullable
    @Override
    public PluginSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull PluginSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    // Getters and setters

    public boolean isEnableValidation() {
        return enableValidation;
    }

    public void setEnableValidation(boolean enableValidation) {
        this.enableValidation = enableValidation;
    }

    public boolean isShowWarnings() {
        return showWarnings;
    }

    public void setShowWarnings(boolean showWarnings) {
        this.showWarnings = showWarnings;
    }

    public boolean isEnableAutoGeneration() {
        return enableAutoGeneration;
    }

    public void setEnableAutoGeneration(boolean enableAutoGeneration) {
        this.enableAutoGeneration = enableAutoGeneration;
    }

    public int getMaxSubjectLength() {
        return maxSubjectLength;
    }

    public void setMaxSubjectLength(int maxSubjectLength) {
        this.maxSubjectLength = maxSubjectLength;
    }

    public String getCustomTypes() {
        return customTypes;
    }

    public void setCustomTypes(String customTypes) {
        this.customTypes = customTypes;
    }

    public boolean isEnableAI() {
        return enableAI;
    }

    public void setEnableAI(boolean enableAI) {
        this.enableAI = enableAI;
    }

    public AIProviderType getAiProviderType() {
        try {
            return AIProviderType.valueOf(aiProviderType);
        } catch (Exception e) {
            return AIProviderType.NONE;
        }
    }

    public void setAiProviderType(AIProviderType type) {
        this.aiProviderType = type.name();
    }

    /**
     * Get API key for current provider (new implementation)
     */
    public String getApiKey() {
        AIProviderType currentType = getAiProviderType();
        if (currentType == AIProviderType.NONE) {
            return "";
        }

        String key = providerApiKeys.get(currentType.name());
        if (key != null && !key.isEmpty()) {
            return key;
        }

        // Backward compatibility: if no per-provider key exists, return legacy key
        return apiKey != null ? apiKey : "";
    }

    /**
     * Set API key for current provider (new implementation)
     */
    public void setApiKey(String apiKey) {
        AIProviderType currentType = getAiProviderType();
        if (currentType != AIProviderType.NONE) {
            if (providerApiKeys == null) {
                providerApiKeys = new HashMap<>();
            }
            providerApiKeys.put(currentType.name(), apiKey != null ? apiKey : "");
        }
        // Also update legacy field for backward compatibility
        this.apiKey = apiKey;
    }

    /**
     * Get API key for specific provider
     */
    public String getApiKeyForProvider(AIProviderType providerType) {
        if (providerType == AIProviderType.NONE) {
            return "";
        }
        if (providerApiKeys == null) {
            providerApiKeys = new HashMap<>();
        }
        return providerApiKeys.getOrDefault(providerType.name(), "");
    }

    /**
     * Set API key for specific provider
     */
    public void setApiKeyForProvider(AIProviderType providerType, String apiKey) {
        if (providerType == AIProviderType.NONE) {
            return;
        }
        if (providerApiKeys == null) {
            providerApiKeys = new HashMap<>();
        }
        providerApiKeys.put(providerType.name(), apiKey != null ? apiKey : "");
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public boolean isEnableCaching() {
        return enableCaching;
    }

    public void setEnableCaching(boolean enableCaching) {
        this.enableCaching = enableCaching;
    }

    public boolean isFallbackToBasic() {
        return fallbackToBasic;
    }

    public void setFallbackToBasic(boolean fallbackToBasic) {
        this.fallbackToBasic = fallbackToBasic;
    }

    public boolean isIncludeFileContent() {
        return includeFileContent;
    }

    public void setIncludeFileContent(boolean includeFileContent) {
        this.includeFileContent = includeFileContent;
    }

    public String getPromptTemplate() {
        return promptTemplate;
    }

    public void setPromptTemplate(String promptTemplate) {
        this.promptTemplate = promptTemplate;
    }

    public String getMessageLanguage() {
        return messageLanguage;
    }

    public void setMessageLanguage(String messageLanguage) {
        this.messageLanguage = messageLanguage;
    }

    public boolean isTrustAllCertificates() {
        return trustAllCertificates;
    }

    public void setTrustAllCertificates(boolean trustAllCertificates) {
        this.trustAllCertificates = trustAllCertificates;
    }
}
