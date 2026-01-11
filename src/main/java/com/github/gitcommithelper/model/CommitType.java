package com.github.gitcommithelper.model;

/**
 * Enum representing different types of git commits following Conventional Commits specification
 */
public enum CommitType {
    FEAT("feat", "A new feature"),
    FIX("fix", "A bug fix"),
    DOCS("docs", "Documentation only changes"),
    STYLE("style", "Changes that do not affect the meaning of the code"),
    REFACTOR("refactor", "A code change that neither fixes a bug nor adds a feature"),
    PERF("perf", "A code change that improves performance"),
    TEST("test", "Adding missing tests or correcting existing tests"),
    BUILD("build", "Changes that affect the build system or external dependencies"),
    CI("ci", "Changes to CI configuration files and scripts"),
    CHORE("chore", "Other changes that don't modify src or test files"),
    REVERT("revert", "Reverts a previous commit");

    private final String type;
    private final String description;

    CommitType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return type;
    }

    public static CommitType fromString(String type) {
        for (CommitType ct : CommitType.values()) {
            if (ct.type.equalsIgnoreCase(type)) {
                return ct;
            }
        }
        return null;
    }
}
