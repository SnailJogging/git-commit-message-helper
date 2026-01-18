package com.github.gitcommithelper.ai;

import com.github.gitcommithelper.settings.PluginSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Factory for creating AI provider instances
 */
public class AIProviderFactory {

    /**
     * Creates an AI provider based on the current settings
     */
    @Nullable
    public static AIProvider createProvider(@NotNull PluginSettings settings) {
        AIProviderType type = settings.getAiProviderType();

        if (type == null || type == AIProviderType.NONE) {
            return null;
        }

        switch (type) {
            case OPENAI:
            case AZURE_OPENAI:
            case CUSTOM:
                return new OpenAIProvider(settings);
            case CLAUDE:
                return new ClaudeProvider(settings);
            case DEEPSEEK:
                return new DeepSeekProvider(settings);
            case ZHIPU:
            case QWEN:
            case OLLAMA:
                return new OpenAIProvider(settings); // These use OpenAI-compatible API
            default:
                return null;
        }
    }
}
