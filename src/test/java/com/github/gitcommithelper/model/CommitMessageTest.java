package com.github.gitcommithelper.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for CommitMessage model
 */
public class CommitMessageTest {

    @Test
    public void testFormatSimpleMessage() {
        CommitMessage message = new CommitMessage(CommitType.FEAT, "add user authentication");
        String formatted = message.format();

        assertEquals("feat: add user authentication", formatted);
    }

    @Test
    public void testFormatMessageWithScope() {
        CommitMessage message = new CommitMessage(CommitType.FIX, "resolve login issue");
        message.setScope("auth");
        String formatted = message.format();

        assertEquals("fix(auth): resolve login issue", formatted);
    }

    @Test
    public void testFormatMessageWithBreakingChange() {
        CommitMessage message = new CommitMessage(CommitType.FEAT, "change API structure");
        message.setScope("api");
        message.setBreakingChange(true);
        String formatted = message.format();

        assertEquals("feat(api)!: change API structure", formatted);
    }

    @Test
    public void testFormatMessageWithBody() {
        CommitMessage message = new CommitMessage(CommitType.FEAT, "add new feature");
        message.setBody("This is a detailed description of the feature.");
        String formatted = message.format();

        assertEquals("feat: add new feature\n\nThis is a detailed description of the feature.", formatted);
    }

    @Test
    public void testFormatMessageWithFooter() {
        CommitMessage message = new CommitMessage(CommitType.FIX, "fix critical bug");
        message.setFooter("Fixes #123");
        String formatted = message.format();

        assertEquals("fix: fix critical bug\n\nFixes #123", formatted);
    }

    @Test
    public void testFormatCompleteMessage() {
        CommitMessage message = new CommitMessage(CommitType.FEAT, "add user authentication");
        message.setScope("auth");
        message.setBreakingChange(true);
        message.setBody("Implements JWT-based authentication system.");
        message.setFooter("BREAKING CHANGE: Old authentication method removed");
        String formatted = message.format();

        String expected = "feat(auth)!: add user authentication\n\n" +
                "Implements JWT-based authentication system.\n\n" +
                "BREAKING CHANGE: Old authentication method removed";

        assertEquals(expected, formatted);
    }

    @Test
    public void testParseSimpleMessage() {
        String messageStr = "feat: add user authentication";
        CommitMessage message = CommitMessage.parse(messageStr);

        assertEquals(CommitType.FEAT, message.getType());
        assertEquals("add user authentication", message.getSubject());
        assertNull(message.getScope());
        assertFalse(message.isBreakingChange());
    }

    @Test
    public void testParseMessageWithScope() {
        String messageStr = "fix(auth): resolve login issue";
        CommitMessage message = CommitMessage.parse(messageStr);

        assertEquals(CommitType.FIX, message.getType());
        assertEquals("auth", message.getScope());
        assertEquals("resolve login issue", message.getSubject());
    }

    @Test
    public void testParseMessageWithBreakingChange() {
        String messageStr = "feat(api)!: change authentication method";
        CommitMessage message = CommitMessage.parse(messageStr);

        assertEquals(CommitType.FEAT, message.getType());
        assertEquals("api", message.getScope());
        assertEquals("change authentication method", message.getSubject());
        assertTrue(message.isBreakingChange());
    }

    @Test
    public void testParseMessageWithBody() {
        String messageStr = "feat: add new feature\n\nThis is the body of the commit message.";
        CommitMessage message = CommitMessage.parse(messageStr);

        assertEquals(CommitType.FEAT, message.getType());
        assertEquals("add new feature", message.getSubject());
        assertEquals("This is the body of the commit message.", message.getBody());
    }

    @Test
    public void testParseCompleteMessage() {
        String messageStr = "feat(auth): add user authentication\n\n" +
                "Implements JWT-based authentication.\n\n" +
                "Fixes #123";
        CommitMessage message = CommitMessage.parse(messageStr);

        assertEquals(CommitType.FEAT, message.getType());
        assertEquals("auth", message.getScope());
        assertEquals("add user authentication", message.getSubject());
        assertEquals("Implements JWT-based authentication.", message.getBody());
        assertEquals("Fixes #123", message.getFooter());
    }

    @Test
    public void testParseEmptyMessage() {
        CommitMessage message = CommitMessage.parse("");
        assertNull(message.getType());
        assertNull(message.getSubject());
    }

    @Test
    public void testParseNullMessage() {
        CommitMessage message = CommitMessage.parse(null);
        assertNull(message.getType());
        assertNull(message.getSubject());
    }

    @Test
    public void testCommitTypeFromString() {
        assertEquals(CommitType.FEAT, CommitType.fromString("feat"));
        assertEquals(CommitType.FIX, CommitType.fromString("fix"));
        assertEquals(CommitType.DOCS, CommitType.fromString("docs"));
        assertNull(CommitType.fromString("invalid"));
    }

    @Test
    public void testCommitTypeToString() {
        assertEquals("feat", CommitType.FEAT.toString());
        assertEquals("fix", CommitType.FIX.toString());
        assertEquals("docs", CommitType.DOCS.toString());
    }
}
