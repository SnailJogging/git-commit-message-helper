package com.github.gitcommithelper.validator;

import com.github.gitcommithelper.model.CommitType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator for commit messages following Conventional Commits specification
 */
public class CommitMessageValidator {

    // Regex pattern for Conventional Commits
    // Format: type(scope)!: subject
    private static final Pattern COMMIT_PATTERN = Pattern.compile(
            "^(\\w+)(\\(([\\w\\-\\.]+)\\))?(!)?: (.+)$"
    );

    private static final int MAX_SUBJECT_LENGTH = 72;
    private static final int MAX_LINE_LENGTH = 100;

    /**
     * Validates a commit message and returns validation results
     */
    public ValidationResult validate(String message) {
        ValidationResult result = new ValidationResult();

        if (message == null || message.trim().isEmpty()) {
            result.addError("Commit message cannot be empty");
            return result;
        }

        String[] lines = message.split("\n");
        String header = lines[0];

        // Validate header format
        validateHeader(header, result);

        // Validate body if present
        if (lines.length > 1) {
            validateBody(lines, result);
        }

        return result;
    }

    /**
     * Validates the commit message header
     */
    private void validateHeader(String header, ValidationResult result) {
        Matcher matcher = COMMIT_PATTERN.matcher(header);

        if (!matcher.matches()) {
            result.addError("Header does not follow Conventional Commits format. " +
                    "Expected format: type(scope): subject");
            return;
        }

        // Validate type
        String type = matcher.group(1);
        if (CommitType.fromString(type) == null) {
            result.addWarning("Unknown commit type: " + type + ". " +
                    "Common types are: feat, fix, docs, style, refactor, test, chore");
        }

        // Validate subject
        String subject = matcher.group(5);
        if (subject.isEmpty()) {
            result.addError("Subject cannot be empty");
        } else {
            validateSubject(subject, result);
        }

        // Validate header length
        if (header.length() > MAX_SUBJECT_LENGTH) {
            result.addWarning("Header exceeds " + MAX_SUBJECT_LENGTH + " characters. " +
                    "Current length: " + header.length());
        }
    }

    /**
     * Validates the commit message subject
     */
    private void validateSubject(String subject, ValidationResult result) {
        // Subject should not end with a period
        if (subject.endsWith(".")) {
            result.addWarning("Subject should not end with a period");
        }

        // Subject should start with lowercase (common convention)
        if (Character.isUpperCase(subject.charAt(0))) {
            result.addWarning("Subject should start with a lowercase letter");
        }

        // Subject should be descriptive (at least 3 words or 10 characters)
        if (subject.length() < 10) {
            result.addWarning("Subject seems too short. Be more descriptive.");
        }
    }

    /**
     * Validates the commit message body
     */
    private void validateBody(String[] lines, ValidationResult result) {
        // Line 2 should be blank
        if (lines.length > 1 && !lines[1].trim().isEmpty()) {
            result.addWarning("Second line should be blank to separate header from body");
        }

        // Check line lengths in body
        for (int i = 2; i < lines.length; i++) {
            if (lines[i].length() > MAX_LINE_LENGTH) {
                result.addWarning("Line " + (i + 1) + " exceeds " + MAX_LINE_LENGTH + " characters");
            }
        }
    }

    /**
     * Quick validation to check if message follows basic format
     */
    public boolean isValidFormat(String message) {
        if (message == null || message.trim().isEmpty()) {
            return false;
        }

        String header = message.split("\n")[0];
        return COMMIT_PATTERN.matcher(header).matches();
    }

    /**
     * Extracts the commit type from a message
     */
    public String extractType(String message) {
        if (message == null || message.isEmpty()) {
            return null;
        }

        Matcher matcher = COMMIT_PATTERN.matcher(message.split("\n")[0]);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * Result class containing validation errors and warnings
     */
    public static class ValidationResult {
        private final List<String> errors = new ArrayList<>();
        private final List<String> warnings = new ArrayList<>();

        public void addError(String error) {
            errors.add(error);
        }

        public void addWarning(String warning) {
            warnings.add(warning);
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        public boolean hasWarnings() {
            return !warnings.isEmpty();
        }

        public List<String> getErrors() {
            return errors;
        }

        public List<String> getWarnings() {
            return warnings;
        }

        public String getErrorMessage() {
            if (errors.isEmpty()) {
                return null;
            }
            return String.join("\n", errors);
        }

        public String getWarningMessage() {
            if (warnings.isEmpty()) {
                return null;
            }
            return String.join("\n", warnings);
        }

        public String getAllMessages() {
            List<String> allMessages = new ArrayList<>();
            if (!errors.isEmpty()) {
                allMessages.add("Errors:");
                allMessages.addAll(errors);
            }
            if (!warnings.isEmpty()) {
                if (!allMessages.isEmpty()) {
                    allMessages.add("");
                }
                allMessages.add("Warnings:");
                allMessages.addAll(warnings);
            }
            return String.join("\n", allMessages);
        }
    }
}
