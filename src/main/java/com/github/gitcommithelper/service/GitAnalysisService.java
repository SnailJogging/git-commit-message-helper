package com.github.gitcommithelper.service;

import com.github.gitcommithelper.model.CommitType;
import com.github.gitcommithelper.model.FileChangeInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import git4idea.GitUtil;
import git4idea.history.GitHistoryUtils;
import git4idea.repo.GitRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for analyzing Git repository changes and history
 */
public class GitAnalysisService {

    public static GitAnalysisService getInstance() {
        return ApplicationManager.getApplication().getService(GitAnalysisService.class);
    }

    /**
     * Analyzes file changes in the current changelist
     */
    public List<FileChangeInfo> analyzeFileChanges(Project project) {
        List<FileChangeInfo> fileChanges = new ArrayList<>();

        ChangeListManager changeListManager = ChangeListManager.getInstance(project);
        Collection<Change> changes = changeListManager.getAllChanges();

        for (Change change : changes) {
            FileChangeInfo.ChangeType changeType = determineChangeType(change);
            String filePath = getFilePath(change);

            if (filePath != null) {
                FileChangeInfo fileInfo = new FileChangeInfo(filePath, changeType);
                fileChanges.add(fileInfo);
            }
        }

        return fileChanges;
    }

    /**
     * Suggests a commit type based on file changes
     */
    public CommitType suggestCommitType(List<FileChangeInfo> fileChanges) {
        if (fileChanges.isEmpty()) {
            return CommitType.CHORE;
        }

        Map<String, Integer> extensionCount = new HashMap<>();
        Map<FileChangeInfo.ChangeType, Integer> changeTypeCount = new HashMap<>();

        for (FileChangeInfo fileInfo : fileChanges) {
            // Count file extensions
            String ext = fileInfo.getFileExtension();
            extensionCount.put(ext, extensionCount.getOrDefault(ext, 0) + 1);

            // Count change types
            FileChangeInfo.ChangeType changeType = fileInfo.getChangeType();
            changeTypeCount.put(changeType, changeTypeCount.getOrDefault(changeType, 0) + 1);
        }

        // Determine commit type based on file extensions and patterns
        for (FileChangeInfo fileInfo : fileChanges) {
            String ext = fileInfo.getFileExtension();
            String fileName = fileInfo.getFileName().toLowerCase();

            // Documentation changes
            if (ext.equals("md") || fileName.contains("readme") || fileName.contains("doc")) {
                return CommitType.DOCS;
            }

            // Test files
            if (fileName.contains("test") || fileName.contains("spec") || ext.equals("test.js")
                || ext.equals("test.ts") || fileName.endsWith("Test.java")) {
                return CommitType.TEST;
            }

            // Build and configuration files
            if (fileName.contains("package.json") || fileName.contains("pom.xml")
                || fileName.contains("build.gradle") || fileName.contains("webpack")
                || fileName.contains(".config")) {
                return CommitType.BUILD;
            }

            // CI/CD files
            if (fileName.contains(".github") || fileName.contains(".gitlab")
                || fileName.contains("jenkins") || fileName.contains(".travis")) {
                return CommitType.CI;
            }
        }

        // If mostly new files, likely a feature
        if (changeTypeCount.getOrDefault(FileChangeInfo.ChangeType.ADDED, 0) > fileChanges.size() / 2) {
            return CommitType.FEAT;
        }

        // If mostly modified files, could be a fix or refactor
        if (changeTypeCount.getOrDefault(FileChangeInfo.ChangeType.MODIFIED, 0) > 0) {
            return CommitType.FIX;
        }

        return CommitType.CHORE;
    }

    /**
     * Analyzes commit history to find common patterns
     */
    public Map<CommitType, Integer> analyzeCommitHistory(Project project, int limit) {
        Map<CommitType, Integer> typeFrequency = new HashMap<>();

        try {
            GitRepository repository = GitUtil.getRepositoryManager(project).getRepositories().stream()
                    .findFirst()
                    .orElse(null);

            if (repository == null) {
                return typeFrequency;
            }

            // This is a simplified version - in a real implementation, you would
            // use GitHistoryUtils to fetch and analyze commit messages
            // For now, we'll return an empty map

        } catch (Exception e) {
            // Log error
        }

        return typeFrequency;
    }

    /**
     * Generates a suggested scope based on file paths
     */
    public String suggestScope(List<FileChangeInfo> fileChanges) {
        if (fileChanges.isEmpty()) {
            return null;
        }

        // Find common directory prefix
        Set<String> directories = fileChanges.stream()
                .map(FileChangeInfo::getFilePath)
                .map(this::extractDirectory)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (directories.size() == 1) {
            return directories.iterator().next();
        }

        // Find common module or package
        String commonPrefix = findCommonPrefix(new ArrayList<>(directories));
        if (commonPrefix != null && !commonPrefix.isEmpty()) {
            return commonPrefix;
        }

        return null;
    }

    private FileChangeInfo.ChangeType determineChangeType(Change change) {
        if (change.getBeforeRevision() == null) {
            return FileChangeInfo.ChangeType.ADDED;
        } else if (change.getAfterRevision() == null) {
            return FileChangeInfo.ChangeType.DELETED;
        } else {
            return FileChangeInfo.ChangeType.MODIFIED;
        }
    }

    private String getFilePath(Change change) {
        if (change.getAfterRevision() != null) {
            return change.getAfterRevision().getFile().getPath();
        } else if (change.getBeforeRevision() != null) {
            return change.getBeforeRevision().getFile().getPath();
        }
        return null;
    }

    private String extractDirectory(String filePath) {
        if (filePath == null) {
            return null;
        }

        int lastSlash = filePath.lastIndexOf('/');
        if (lastSlash > 0) {
            String dir = filePath.substring(0, lastSlash);
            // Get the last directory name
            int secondLastSlash = dir.lastIndexOf('/');
            if (secondLastSlash >= 0) {
                return dir.substring(secondLastSlash + 1);
            }
            return dir;
        }
        return null;
    }

    private String findCommonPrefix(List<String> strings) {
        if (strings.isEmpty()) {
            return null;
        }

        String prefix = strings.get(0);
        for (int i = 1; i < strings.size(); i++) {
            while (!strings.get(i).startsWith(prefix)) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) {
                    return null;
                }
            }
        }
        return prefix;
    }
}
