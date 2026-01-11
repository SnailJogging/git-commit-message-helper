package com.github.gitcommithelper.model;

/**
 * Model representing information about a changed file
 */
public class FileChangeInfo {
    private String filePath;
    private String fileName;
    private String fileExtension;
    private ChangeType changeType;
    private int linesAdded;
    private int linesDeleted;

    public enum ChangeType {
        ADDED,
        MODIFIED,
        DELETED,
        RENAMED
    }

    public FileChangeInfo(String filePath, ChangeType changeType) {
        this.filePath = filePath;
        this.changeType = changeType;
        parseFileName();
    }

    private void parseFileName() {
        if (filePath != null) {
            int lastSlash = filePath.lastIndexOf('/');
            this.fileName = lastSlash >= 0 ? filePath.substring(lastSlash + 1) : filePath;

            int lastDot = fileName.lastIndexOf('.');
            this.fileExtension = lastDot >= 0 ? fileName.substring(lastDot + 1) : "";
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
        parseFileName();
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public int getLinesAdded() {
        return linesAdded;
    }

    public void setLinesAdded(int linesAdded) {
        this.linesAdded = linesAdded;
    }

    public int getLinesDeleted() {
        return linesDeleted;
    }

    public void setLinesDeleted(int linesDeleted) {
        this.linesDeleted = linesDeleted;
    }
}
