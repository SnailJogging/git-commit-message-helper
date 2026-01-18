package com.github.gitcommithelper.service;

import com.github.gitcommithelper.ai.AIProvider;
import com.github.gitcommithelper.ai.AIProviderException;
import com.github.gitcommithelper.ai.AIProviderFactory;
import com.github.gitcommithelper.model.CommitMessage;
import com.github.gitcommithelper.model.FileChangeInfo;
import com.github.gitcommithelper.settings.PluginSettings;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Smart commit message generator with AI enhancement and fallback mechanism
 */
public class SmartCommitMessageGenerator {

    private static final Logger LOG = Logger.getInstance(SmartCommitMessageGenerator.class);

    private final CommitMessageGenerator basicGenerator;
    private final GitAnalysisService gitAnalysisService;
    private final PluginSettings settings;

    public SmartCommitMessageGenerator() {
        this.basicGenerator = new CommitMessageGenerator();
        this.gitAnalysisService = GitAnalysisService.getInstance();
        this.settings = PluginSettings.getInstance();
    }

    /**
     * Generates commit message using AI if enabled, with fallback to basic generator
     */
    @NotNull
    public CommitMessage generateCommitMessage(@NotNull Project project) {
        return generateCommitMessage(project, null);
    }

    /**
     * Generates commit message for selected changes, or all changes if selectedChanges is null
     */
    @NotNull
    public CommitMessage generateCommitMessage(@NotNull Project project, @Nullable Collection<Change> selectedChanges) {
        // Check if AI is enabled
        if (!settings.isEnableAI()) {
            LOG.info("AI generation disabled, using basic generator");
            return basicGenerator.generateTemplate(project);
        }

        try {
            // Try AI-enhanced generation
            CommitMessage aiMessage = generateWithAI(project, selectedChanges);
            if (aiMessage != null) {
                return aiMessage;
            }
        } catch (Exception e) {
            LOG.warn("AI generation failed", e);
        }

        // Fallback to basic generator
        if (settings.isFallbackToBasic()) {
            LOG.info("Falling back to basic generator");
            return basicGenerator.generateTemplate(project);
        }

        // If fallback is disabled, return error message
        CommitMessage errorMessage = new CommitMessage();
        errorMessage.setSubject("AI generation failed");
        return errorMessage;
    }

    @Nullable
    private CommitMessage generateWithAI(@NotNull Project project, @Nullable Collection<Change> selectedChanges) throws AIProviderException {
        // Create AI provider
        AIProvider provider = AIProviderFactory.createProvider(settings);
        if (provider == null || !provider.isAvailable()) {
            LOG.warn("AI provider not available");
            return null;
        }

        // Get file changes
        List<FileChangeInfo> fileChanges = gitAnalysisService.analyzeFileChanges(project, selectedChanges);
        if (fileChanges.isEmpty()) {
            LOG.info("No file changes detected");
            return null;
        }

        // Get git diff for selected files
        String diff = getGitDiff(project, selectedChanges);
        if (diff == null || diff.trim().isEmpty()) {
            LOG.warn("Failed to get git diff");
            return null;
        }

        // Generate commit message using AI
        LOG.info("Generating commit message with " + provider.getName());
        String aiGeneratedMessage = provider.generateCommitMessage(diff, fileChanges);

        if (aiGeneratedMessage == null || aiGeneratedMessage.trim().isEmpty()) {
            LOG.warn("AI provider returned empty message");
            return null;
        }

        // Parse the AI-generated message
        return parseCommitMessage(aiGeneratedMessage);
    }

    /**
     * Gets the git diff for staged changes
     */
    @Nullable
    private String getGitDiff(@NotNull Project project, @Nullable Collection<Change> selectedChanges) {
        try {
            List<String> command = new ArrayList<>();
            command.add("git");
            command.add("diff");
            command.add("--cached");

            // If specific changes are selected, add their file paths
            if (selectedChanges != null && !selectedChanges.isEmpty()) {
                command.add("--");
                for (Change change : selectedChanges) {
                    String filePath = getFilePathFromChange(change);
                    if (filePath != null) {
                        // Make path relative to git root
                        String relativePath = filePath.replace(project.getBasePath() + "/", "");
                        command.add(relativePath);
                    }
                }
            }

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new java.io.File(project.getBasePath()));
            Process process = pb.start();

            StringBuilder diff = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    diff.append(line).append("\n");
                }
            }

            process.waitFor();

            // If no staged changes, get unstaged diff
            if (diff.length() == 0) {
                command = new ArrayList<>();
                command.add("git");
                command.add("diff");

                // Add file paths for selected changes
                if (selectedChanges != null && !selectedChanges.isEmpty()) {
                    command.add("--");
                    for (Change change : selectedChanges) {
                        String filePath = getFilePathFromChange(change);
                        if (filePath != null) {
                            String relativePath = filePath.replace(project.getBasePath() + "/", "");
                            command.add(relativePath);
                        }
                    }
                }

                pb = new ProcessBuilder(command);
                pb.directory(new java.io.File(project.getBasePath()));
                process = pb.start();

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        diff.append(line).append("\n");
                    }
                }

                process.waitFor();
            }

            return diff.toString();

        } catch (Exception e) {
            LOG.error("Failed to get git diff", e);
            return null;
        }
    }

    /**
     * Gets file path from a Change object
     */
    @Nullable
    private String getFilePathFromChange(@NotNull Change change) {
        if (change.getAfterRevision() != null) {
            return change.getAfterRevision().getFile().getPath();
        } else if (change.getBeforeRevision() != null) {
            return change.getBeforeRevision().getFile().getPath();
        }
        return null;
    }

    /**
     * Parses AI-generated commit message into CommitMessage object
     */
    @NotNull
    private CommitMessage parseCommitMessage(@NotNull String message) {
        CommitMessage commitMessage = new CommitMessage();

        // Expected format: type(scope): subject
        String trimmed = message.trim();

        // Extract type
        int colonIndex = trimmed.indexOf(':');
        if (colonIndex > 0) {
            String typePart = trimmed.substring(0, colonIndex).trim();
            String subject = trimmed.substring(colonIndex + 1).trim();

            // Extract scope if present
            int openParen = typePart.indexOf('(');
            int closeParen = typePart.indexOf(')');

            if (openParen > 0 && closeParen > openParen) {
                String type = typePart.substring(0, openParen).trim();
                String scope = typePart.substring(openParen + 1, closeParen).trim();

                commitMessage.setType(parseType(type));
                commitMessage.setScope(scope);
            } else {
                commitMessage.setType(parseType(typePart));
            }

            commitMessage.setSubject(subject);
        } else {
            // If parsing fails, use the whole message as subject
            commitMessage.setSubject(trimmed);
            commitMessage.setType(com.github.gitcommithelper.model.CommitType.CHORE);
        }

        return commitMessage;
    }

    /**
     * Parses commit type string to CommitType enum
     */
    private com.github.gitcommithelper.model.CommitType parseType(String typeStr) {
        try {
            return com.github.gitcommithelper.model.CommitType.valueOf(typeStr.toUpperCase());
        } catch (Exception e) {
            return com.github.gitcommithelper.model.CommitType.CHORE;
        }
    }

    /**
     * Generates multiple suggestions if possible
     */
    @NotNull
    public List<CommitMessage> generateSuggestions(@NotNull Project project) {
        CommitMessage message = generateCommitMessage(project);
        return List.of(message);
    }
}
