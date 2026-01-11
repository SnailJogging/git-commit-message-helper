package com.github.gitcommithelper.service;

import com.github.gitcommithelper.model.CommitMessage;
import com.github.gitcommithelper.model.CommitType;
import com.github.gitcommithelper.model.FileChangeInfo;
import com.intellij.openapi.project.Project;

import java.util.List;

/**
 * Service for generating commit message templates based on file changes
 */
public class CommitMessageGenerator {

    private final GitAnalysisService gitAnalysisService;

    public CommitMessageGenerator() {
        this.gitAnalysisService = GitAnalysisService.getInstance();
    }

    /**
     * Generates a commit message template based on project changes
     */
    public CommitMessage generateTemplate(Project project) {
        List<FileChangeInfo> fileChanges = gitAnalysisService.analyzeFileChanges(project);

        CommitMessage commitMessage = new CommitMessage();

        // Suggest commit type
        CommitType suggestedType = gitAnalysisService.suggestCommitType(fileChanges);
        commitMessage.setType(suggestedType);

        // Suggest scope
        String suggestedScope = gitAnalysisService.suggestScope(fileChanges);
        commitMessage.setScope(suggestedScope);

        // Generate subject
        String subject = generateSubject(fileChanges, suggestedType);
        commitMessage.setSubject(subject);

        return commitMessage;
    }

    /**
     * Generates a suggested subject line based on file changes
     */
    private String generateSubject(List<FileChangeInfo> fileChanges, CommitType commitType) {
        if (fileChanges.isEmpty()) {
            return "update files";
        }

        // Count change types
        long addedCount = fileChanges.stream()
                .filter(f -> f.getChangeType() == FileChangeInfo.ChangeType.ADDED)
                .count();
        long modifiedCount = fileChanges.stream()
                .filter(f -> f.getChangeType() == FileChangeInfo.ChangeType.MODIFIED)
                .count();
        long deletedCount = fileChanges.stream()
                .filter(f -> f.getChangeType() == FileChangeInfo.ChangeType.DELETED)
                .count();

        // Generate subject based on commit type and changes
        switch (commitType) {
            case FEAT:
                return generateFeatureSubject(fileChanges, addedCount);
            case FIX:
                return generateFixSubject(fileChanges, modifiedCount);
            case DOCS:
                return generateDocsSubject(fileChanges);
            case TEST:
                return generateTestSubject(fileChanges, addedCount);
            case REFACTOR:
                return generateRefactorSubject(fileChanges);
            case STYLE:
                return "update code style";
            case CHORE:
                return generateChoreSubject(fileChanges);
            case BUILD:
                return "update build configuration";
            case CI:
                return "update CI/CD configuration";
            default:
                return "update files";
        }
    }

    private String generateFeatureSubject(List<FileChangeInfo> fileChanges, long addedCount) {
        if (addedCount == 1) {
            String fileName = fileChanges.get(0).getFileName();
            return "add " + extractFeatureName(fileName);
        } else if (addedCount > 1) {
            return "add new functionality";
        }
        return "add feature";
    }

    private String generateFixSubject(List<FileChangeInfo> fileChanges, long modifiedCount) {
        if (modifiedCount == 1) {
            String fileName = fileChanges.get(0).getFileName();
            return "fix issue in " + extractFeatureName(fileName);
        }
        return "fix bugs";
    }

    private String generateDocsSubject(List<FileChangeInfo> fileChanges) {
        for (FileChangeInfo file : fileChanges) {
            String fileName = file.getFileName().toLowerCase();
            if (fileName.contains("readme")) {
                return "update README";
            }
        }
        return "update documentation";
    }

    private String generateTestSubject(List<FileChangeInfo> fileChanges, long addedCount) {
        if (addedCount > 0) {
            return "add tests";
        }
        return "update tests";
    }

    private String generateRefactorSubject(List<FileChangeInfo> fileChanges) {
        if (fileChanges.size() == 1) {
            String fileName = fileChanges.get(0).getFileName();
            return "refactor " + extractFeatureName(fileName);
        }
        return "refactor code";
    }

    private String generateChoreSubject(List<FileChangeInfo> fileChanges) {
        for (FileChangeInfo file : fileChanges) {
            String fileName = file.getFileName().toLowerCase();
            if (fileName.contains("package.json") || fileName.contains("pom.xml")
                || fileName.contains("build.gradle")) {
                return "update dependencies";
            }
        }
        return "update project files";
    }

    /**
     * Extracts a feature name from a file name
     */
    private String extractFeatureName(String fileName) {
        // Remove file extension
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            fileName = fileName.substring(0, lastDot);
        }

        // Convert camelCase or PascalCase to words
        String result = fileName.replaceAll("([A-Z])", " $1")
                .replaceAll("([a-z])([A-Z])", "$1 $2")
                .toLowerCase()
                .trim();

        // Remove common suffixes
        result = result.replaceAll("(service|controller|component|util|helper|manager)$", "").trim();

        return result.isEmpty() ? fileName : result;
    }

    /**
     * Generates multiple template suggestions
     */
    public List<CommitMessage> generateSuggestions(Project project) {
        // This could be enhanced to provide multiple suggestions
        CommitMessage template = generateTemplate(project);
        return List.of(template);
    }
}
