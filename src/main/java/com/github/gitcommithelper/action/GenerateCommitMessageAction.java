package com.github.gitcommithelper.action;

import com.github.gitcommithelper.model.CommitMessage;
import com.github.gitcommithelper.service.SmartCommitMessageGenerator;
import com.github.gitcommithelper.settings.PluginSettings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.CommitMessageI;
import com.intellij.openapi.vcs.VcsDataKeys;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.ui.Refreshable;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Action to generate commit message from the Git commit dialog
 */
public class GenerateCommitMessageAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(GenerateCommitMessageAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        // Try to get the commit panel to access current message and set new message
        Refreshable refreshable = e.getData(Refreshable.PANEL_KEY);
        CommitMessageI commitMessageI = e.getData(VcsDataKeys.COMMIT_MESSAGE_CONTROL);

        // Get selected changes from the data context
        // Note: If no specific selection is available, we'll analyze all changes
        Collection<Change> selectedChanges = null;

        // Try to get changes from refreshable panel
        if (refreshable instanceof CheckinProjectPanel) {
            CheckinProjectPanel checkinPanel = (CheckinProjectPanel) refreshable;
            Collection<Change> panelChanges = checkinPanel.getSelectedChanges();

            // Only use selected changes if at least one file is selected
            // If the collection is empty (no files checked), treat it as null to analyze all changes
            if (panelChanges != null && !panelChanges.isEmpty()) {
                selectedChanges = panelChanges;
                LOG.info("Found " + selectedChanges.size() + " selected changes from checkin panel");
            } else {
                LOG.info("No files selected, will prompt user");
                // Show warning that no files are selected
                Messages.showWarningDialog(
                    project,
                    "请至少选择一个要提交的文件。\nPlease select at least one file to commit.",
                    "No Files Selected"
                );
                return;
            }
        }

        final Collection<Change> finalSelectedChanges = selectedChanges;

        PluginSettings settings = PluginSettings.getInstance();
        AtomicReference<CommitMessage> templateRef = new AtomicReference<>();
        AtomicReference<Exception> errorRef = new AtomicReference<>();

        // Run generation in background with progress indicator
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Generating Commit Message", false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                try {
                    indicator.setText("Analyzing file changes...");
                    indicator.setFraction(0.2);

                    SmartCommitMessageGenerator generator = new SmartCommitMessageGenerator();

                    if (settings.isEnableAI()) {
                        indicator.setText("Generating with AI (" + settings.getAiProviderType().getDisplayName() + ")...");
                        indicator.setFraction(0.5);
                    } else {
                        indicator.setText("Generating commit message...");
                        indicator.setFraction(0.5);
                    }

                    // Pass selected changes to generator
                    CommitMessage template = generator.generateCommitMessage(project, finalSelectedChanges);
                    templateRef.set(template);

                    indicator.setFraction(1.0);

                } catch (Exception ex) {
                    errorRef.set(ex);
                }
            }

            @Override
            public void onSuccess() {
                Exception error = errorRef.get();
                if (error != null) {
                    Messages.showErrorDialog(
                            project,
                            "Failed to generate commit message: " + error.getMessage(),
                            "Error"
                    );
                    return;
                }

                CommitMessage template = templateRef.get();
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

                // Try to set message directly in commit panel
                if (commitMessageI != null) {
                    // Direct fill: Set message in commit panel
                    commitMessageI.setCommitMessage(formattedMessage);

                    // Show notification
                    showNotification(project, formattedMessage, template, settings.isEnableAI());
                } else {
                    // Fallback: Show dialog with copy option
                    showGeneratedMessage(project, formattedMessage, template, settings.isEnableAI());
                }
            }

            @Override
            public void onThrowable(@NotNull Throwable error) {
                Messages.showErrorDialog(
                        project,
                        "Failed to generate commit message: " + error.getMessage(),
                        "Error"
                );
            }
        });
    }

    private void showNotification(Project project, String message,
                                  CommitMessage template, boolean isAIGenerated) {
        // Build notification content
        StringBuilder content = new StringBuilder();
        content.append("Type: ").append(template.getType());

        if (template.getScope() != null && !template.getScope().isEmpty()) {
            content.append(" | Scope: ").append(template.getScope());
        }

        if (isAIGenerated) {
            content.append(" | Generated by: ").append(
                    PluginSettings.getInstance().getAiProviderType().getDisplayName()
            );
        } else {
            content.append(" | Generated by: Rule-based analysis");
        }

        // Show balloon notification
        Notification notification = new Notification(
                "Git Commit Message Helper",
                "Commit Message Generated",
                content.toString(),
                NotificationType.INFORMATION
        );

        Notifications.Bus.notify(notification, project);
    }

    private void showGeneratedMessage(Project project, String message,
                                      CommitMessage template, boolean isAIGenerated) {
        // Build dialog message
        StringBuilder dialogMessage = new StringBuilder();
        dialogMessage.append("Generated commit message:\n\n");
        dialogMessage.append(message);
        dialogMessage.append("\n\n");
        dialogMessage.append("Type: ").append(template.getType());

        if (template.getScope() != null && !template.getScope().isEmpty()) {
            dialogMessage.append("\nScope: ").append(template.getScope());
        }

        if (isAIGenerated) {
            dialogMessage.append("\n\nGenerated by: ").append(
                    PluginSettings.getInstance().getAiProviderType().getDisplayName()
            );
        }

        // Show dialog with options
        String[] options = {"Copy to Clipboard", "Cancel"};
        int result = Messages.showDialog(
                project,
                dialogMessage.toString(),
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
