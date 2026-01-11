package com.github.gitcommithelper.validator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for CommitMessageValidator
 */
public class CommitMessageValidatorTest {

    private CommitMessageValidator validator;

    @Before
    public void setUp() {
        validator = new CommitMessageValidator();
    }

    @Test
    public void testValidCommitMessage() {
        String message = "feat: add user authentication";
        CommitMessageValidator.ValidationResult result = validator.validate(message);

        assertTrue("Message should be valid", result.isValid());
        assertFalse("Should have no warnings", result.hasWarnings());
    }

    @Test
    public void testValidCommitMessageWithScope() {
        String message = "fix(auth): resolve login issue";
        CommitMessageValidator.ValidationResult result = validator.validate(message);

        assertTrue("Message should be valid", result.isValid());
    }

    @Test
    public void testValidCommitMessageWithBreakingChange() {
        String message = "feat(api)!: change authentication method";
        CommitMessageValidator.ValidationResult result = validator.validate(message);

        assertTrue("Message should be valid", result.isValid());
    }

    @Test
    public void testInvalidEmptyMessage() {
        String message = "";
        CommitMessageValidator.ValidationResult result = validator.validate(message);

        assertFalse("Empty message should be invalid", result.isValid());
        assertTrue("Should have errors", !result.getErrors().isEmpty());
    }

    @Test
    public void testInvalidMessageFormat() {
        String message = "this is not a valid commit message";
        CommitMessageValidator.ValidationResult result = validator.validate(message);

        assertFalse("Invalid format should fail", result.isValid());
    }

    @Test
    public void testMessageWithPeriodWarning() {
        String message = "feat: add new feature.";
        CommitMessageValidator.ValidationResult result = validator.validate(message);

        assertTrue("Message should be valid", result.isValid());
        assertTrue("Should have warning about period", result.hasWarnings());
    }

    @Test
    public void testMessageWithUppercaseWarning() {
        String message = "feat: Add new feature";
        CommitMessageValidator.ValidationResult result = validator.validate(message);

        assertTrue("Message should be valid", result.isValid());
        assertTrue("Should have warning about uppercase", result.hasWarnings());
    }

    @Test
    public void testShortSubjectWarning() {
        String message = "feat: fix";
        CommitMessageValidator.ValidationResult result = validator.validate(message);

        assertTrue("Message should be valid", result.isValid());
        assertTrue("Should have warning about short subject", result.hasWarnings());
    }

    @Test
    public void testLongHeaderWarning() {
        String message = "feat: this is a very long commit message subject that exceeds the recommended maximum length of 72 characters";
        CommitMessageValidator.ValidationResult result = validator.validate(message);

        assertTrue("Message should be valid", result.isValid());
        assertTrue("Should have warning about length", result.hasWarnings());
    }

    @Test
    public void testMessageWithBody() {
        String message = "feat: add user authentication\n\nThis commit adds a new user authentication system using JWT tokens.";
        CommitMessageValidator.ValidationResult result = validator.validate(message);

        assertTrue("Message with body should be valid", result.isValid());
    }

    @Test
    public void testMessageWithoutBlankLineWarning() {
        String message = "feat: add feature\nThis is the body without blank line";
        CommitMessageValidator.ValidationResult result = validator.validate(message);

        assertTrue("Message should be valid", result.isValid());
        assertTrue("Should have warning about missing blank line", result.hasWarnings());
    }

    @Test
    public void testIsValidFormat() {
        assertTrue(validator.isValidFormat("feat: add feature"));
        assertTrue(validator.isValidFormat("fix(auth): resolve issue"));
        assertTrue(validator.isValidFormat("docs: update readme"));
        assertFalse(validator.isValidFormat("invalid message"));
        assertFalse(validator.isValidFormat(""));
    }

    @Test
    public void testExtractType() {
        assertEquals("feat", validator.extractType("feat: add feature"));
        assertEquals("fix", validator.extractType("fix(auth): resolve issue"));
        assertEquals("docs", validator.extractType("docs: update readme"));
        assertNull(validator.extractType("invalid message"));
        assertNull(validator.extractType(""));
    }

    @Test
    public void testAllCommitTypes() {
        String[] types = {"feat", "fix", "docs", "style", "refactor", "perf", "test", "build", "ci", "chore", "revert"};

        for (String type : types) {
            String message = type + ": test message for " + type;
            CommitMessageValidator.ValidationResult result = validator.validate(message);
            assertTrue("Type " + type + " should be valid", result.isValid());
        }
    }

    @Test
    public void testUnknownTypeWarning() {
        String message = "unknown: this is an unknown type";
        CommitMessageValidator.ValidationResult result = validator.validate(message);

        assertTrue("Message should be valid", result.isValid());
        assertTrue("Should have warning about unknown type", result.hasWarnings());
        assertTrue("Warning should mention unknown type",
                result.getWarningMessage().contains("Unknown commit type"));
    }
}
