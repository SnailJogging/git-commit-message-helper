package com.github.gitcommithelper.action;

import com.github.gitcommithelper.model.CommitMessage;
import com.github.gitcommithelper.service.CommitMessageGenerator;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * Action to generate commit message from the Git commit dialog
 */
public class GenerateCommitMessageAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        try {
            // Generate commit message template
            CommitMessageGenerator generator = new CommitMessageGenerator();
            com.github.gitcommithelper.model.CommitMessage template = generator.generateTemplate(project);

            if (template == null) {
                Messages.showInfoMessage(
                        project,
                        "No changes detected. Please stage some files first.",
                        "Generate Commit Message"
                );
                return;
            }

            // Format the message
            String formattedMessage = template.format();

            // Try to set the message in the commit dialog
            // Note: This is a simplified version. In a real implementation,
            // you would need to access the actual commit message editor
            showGeneratedMessage(project, formattedMessage, template);

        } catch (Exception ex) {
            Messages.showErrorDialog(
                    project,
                    "Failed to generate commit message: " + ex.getMessage(),
                    "Error"
            );
        }
    }

    private void showGeneratedMessage(Project project, String message,
                                     com.github.gitcommithelper.model.CommitMessage template) {
        // Show dialog with generated message
        String[] options = {"Copy to Clipboard", "Cancel"};
        int result = Messages.showDialog(
                project,
                "Generated commit message:\n\n" + message +
                        "\n\nType: " + template.getType() +
                        (template.getScope() != null ? "\nScope: " + template.getScope() : ""),
                "Generated Commit Message",
                options,
                0,
                Messages.getInformationIcon()
        );

        if (result == 0) {
            // Copy to clipboard
            java.awt.datatransfer.StringSelection selection =
                new java.awt.datatransfer.StringSelection(message);
            java.awt.Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(selection, selection);

            Messages.showInfoMessage(
                project,
                "Commit message copied to clipboard!",
                "Success"
            );
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        // Enable action only when project is available
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }
}
