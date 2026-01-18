package com.github.gitcommithelper.ai;

import com.github.gitcommithelper.model.FileChangeInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Interface for AI-powered commit message generation providers
 */
public interface AIProvider {

    /**
     * Gets the name of this AI provider
     */
    @NotNull
    String getName();

    /**
     * Checks if this provider is properly configured and available
     */
    boolean isAvailable();

    /**
     * Tests the connection to the AI service
     * @return null if successful, error message otherwise
     */
    @Nullable
    String testConnection();

    /**
     * Generates a commit message based on git diff and file changes
     *
     * @param diffContent The git diff content
     * @param fileChanges List of changed files with metadata
     * @return Generated commit message, or null if generation failed
     * @throws AIProviderException if an error occurs during generation
     */
    @Nullable
    String generateCommitMessage(@NotNull String diffContent, @NotNull List<FileChangeInfo> fileChanges)
            throws AIProviderException;

    /**
     * Gets the maximum number of tokens this provider can handle
     */
    int getMaxTokens();

    /**
     * Gets the timeout for API calls in milliseconds
     */
    int getTimeout();
}
