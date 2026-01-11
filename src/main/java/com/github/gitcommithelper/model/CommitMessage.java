package com.github.gitcommithelper.model;

/**
 * Model representing a structured commit message
 */
public class CommitMessage {
    private CommitType type;
    private String scope;
    private String subject;
    private String body;
    private String footer;
    private boolean breakingChange;

    public CommitMessage() {
    }

    public CommitMessage(CommitType type, String subject) {
        this.type = type;
        this.subject = subject;
    }

    public CommitType getType() {
        return type;
    }

    public void setType(CommitType type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public boolean isBreakingChange() {
        return breakingChange;
    }

    public void setBreakingChange(boolean breakingChange) {
        this.breakingChange = breakingChange;
    }

    /**
     * Formats the commit message according to Conventional Commits specification
     */
    public String format() {
        StringBuilder sb = new StringBuilder();

        // Type
        sb.append(type.getType());

        // Scope (optional)
        if (scope != null && !scope.isEmpty()) {
            sb.append("(").append(scope).append(")");
        }

        // Breaking change indicator
        if (breakingChange) {
            sb.append("!");
        }

        // Subject
        sb.append(": ").append(subject);

        // Body (optional)
        if (body != null && !body.isEmpty()) {
            sb.append("\n\n").append(body);
        }

        // Footer (optional)
        if (footer != null && !footer.isEmpty()) {
            sb.append("\n\n").append(footer);
        }

        return sb.toString();
    }

    /**
     * Parses a commit message string into a CommitMessage object
     */
    public static CommitMessage parse(String message) {
        CommitMessage commitMessage = new CommitMessage();

        if (message == null || message.isEmpty()) {
            return commitMessage;
        }

        String[] parts = message.split("\n\n", 3);
        String header = parts[0];

        // Parse header
        parseHeader(header, commitMessage);

        // Parse body if present
        if (parts.length > 1) {
            commitMessage.setBody(parts[1]);
        }

        // Parse footer if present
        if (parts.length > 2) {
            commitMessage.setFooter(parts[2]);
        }

        return commitMessage;
    }

    private static void parseHeader(String header, CommitMessage commitMessage) {
        // Check for breaking change indicator
        if (header.contains("!:")) {
            commitMessage.setBreakingChange(true);
            header = header.replace("!", "");
        }

        // Extract type and scope
        int colonIndex = header.indexOf(":");
        if (colonIndex > 0) {
            String typeAndScope = header.substring(0, colonIndex);
            String subject = header.substring(colonIndex + 1).trim();

            commitMessage.setSubject(subject);

            // Check for scope
            if (typeAndScope.contains("(")) {
                int scopeStart = typeAndScope.indexOf("(");
                int scopeEnd = typeAndScope.indexOf(")");
                if (scopeEnd > scopeStart) {
                    String type = typeAndScope.substring(0, scopeStart);
                    String scope = typeAndScope.substring(scopeStart + 1, scopeEnd);
                    commitMessage.setType(CommitType.fromString(type));
                    commitMessage.setScope(scope);
                }
            } else {
                commitMessage.setType(CommitType.fromString(typeAndScope));
            }
        }
    }
}
