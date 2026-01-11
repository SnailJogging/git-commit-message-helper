package com.github.gitcommithelper.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Settings page for Git Commit Message Helper plugin
 */
public class SettingsConfigurable implements Configurable {

    private SettingsPanel settingsPanel;

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Git Commit Message Helper";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (settingsPanel == null) {
            settingsPanel = new SettingsPanel();
        }
        return settingsPanel.getPanel();
    }

    @Override
    public boolean isModified() {
        return settingsPanel != null && settingsPanel.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        if (settingsPanel != null) {
            settingsPanel.apply();
        }
    }

    @Override
    public void reset() {
        if (settingsPanel != null) {
            settingsPanel.reset();
        }
    }

    @Override
    public void disposeUIResources() {
        settingsPanel = null;
    }
}
