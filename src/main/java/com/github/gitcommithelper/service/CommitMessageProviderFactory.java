package com.github.gitcommithelper.service;

import com.github.gitcommithelper.validator.CommitMessageValidator;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory;
import com.intellij.openapi.vcs.ui.RefreshableOnComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for creating commit handlers that validate commit messages
 */
public class CommitMessageProviderFactory extends CheckinHandlerFactory {

    @NotNull
    @Override
    public CheckinHandler createHandler(@NotNull CheckinProjectPanel panel, @NotNull CommitContext commitContext) {
        return new CommitMessageValidationHandler(panel);
    }

    /**
     * Handler that validates commit messages before commit
     */
    private static class CommitMessageValidationHandler extends CheckinHandler {
        private final CheckinProjectPanel panel;
        private final CommitMessageValidator validator;

        public CommitMessageValidationHandler(CheckinProjectPanel panel) {
            this.panel = panel;
            this.validator = new CommitMessageValidator();
        }

        @Override
        public ReturnResult beforeCheckin() {
            String commitMessage = panel.getCommitMessage();

            // Validate the commit message
            CommitMessageValidator.ValidationResult result = validator.validate(commitMessage);

            if (!result.isValid()) {
                // Show error dialog
                int answer = com.intellij.openapi.ui.Messages.showYesNoDialog(
                        panel.getProject(),
                        "Commit message validation failed:\n\n" + result.getErrorMessage() +
                                "\n\nDo you want to commit anyway?",
                        "Invalid Commit Message",
                        com.intellij.openapi.ui.Messages.getWarningIcon()
                );

                if (answer == com.intellij.openapi.ui.Messages.NO) {
                    return ReturnResult.CANCEL;
                }
            } else if (result.hasWarnings()) {
                // Show warnings but allow commit
                int answer = com.intellij.openapi.ui.Messages.showYesNoDialog(
                        panel.getProject(),
                        "Commit message has warnings:\n\n" + result.getWarningMessage() +
                                "\n\nDo you want to continue?",
                        "Commit Message Warnings",
                        com.intellij.openapi.ui.Messages.getWarningIcon()
                );

                if (answer == com.intellij.openapi.ui.Messages.NO) {
                    return ReturnResult.CANCEL;
                }
            }

            return ReturnResult.COMMIT;
        }
    }
}
