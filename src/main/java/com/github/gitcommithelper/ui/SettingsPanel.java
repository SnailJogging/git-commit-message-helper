package com.github.gitcommithelper.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Settings panel UI for the plugin configuration
 */
public class SettingsPanel {
    private JPanel mainPanel;
    private JCheckBox enableValidationCheckBox;
    private JCheckBox enableAutoGenerationCheckBox;
    private JCheckBox showWarningsCheckBox;
    private JSpinner maxSubjectLengthSpinner;
    private JTextArea customTypesTextArea;

    private boolean modified = false;

    public SettingsPanel() {
        createUI();
        loadSettings();
    }

    private void createUI() {
        mainPanel = new JPanel(new BorderLayout());
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Title
        JLabel titleLabel = new JLabel("Git Commit Message Helper Settings");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Validation settings
        JLabel validationLabel = new JLabel("Validation Settings");
        validationLabel.setFont(validationLabel.getFont().deriveFont(Font.BOLD));
        contentPanel.add(validationLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        enableValidationCheckBox = new JCheckBox("Enable commit message validation");
        enableValidationCheckBox.addActionListener(e -> modified = true);
        contentPanel.add(enableValidationCheckBox);

        showWarningsCheckBox = new JCheckBox("Show warnings for commit messages");
        showWarningsCheckBox.addActionListener(e -> modified = true);
        contentPanel.add(showWarningsCheckBox);
        contentPanel.add(Box.createVerticalStrut(20));

        // Generation settings
        JLabel generationLabel = new JLabel("Generation Settings");
        generationLabel.setFont(generationLabel.getFont().deriveFont(Font.BOLD));
        contentPanel.add(generationLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        enableAutoGenerationCheckBox = new JCheckBox("Enable automatic message generation");
        enableAutoGenerationCheckBox.addActionListener(e -> modified = true);
        contentPanel.add(enableAutoGenerationCheckBox);
        contentPanel.add(Box.createVerticalStrut(10));

        // Max subject length
        JPanel lengthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lengthPanel.add(new JLabel("Maximum subject length:"));
        maxSubjectLengthSpinner = new JSpinner(new SpinnerNumberModel(72, 20, 200, 1));
        maxSubjectLengthSpinner.addChangeListener(e -> modified = true);
        lengthPanel.add(maxSubjectLengthSpinner);
        contentPanel.add(lengthPanel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Custom commit types
        JLabel customTypesLabel = new JLabel("Custom Commit Types (one per line):");
        customTypesLabel.setFont(customTypesLabel.getFont().deriveFont(Font.BOLD));
        contentPanel.add(customTypesLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        customTypesTextArea = new JTextArea(5, 40);
        customTypesTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        customTypesTextArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { modified = true; }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { modified = true; }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { modified = true; }
        });
        JScrollPane scrollPane = new JScrollPane(customTypesTextArea);
        contentPanel.add(scrollPane);

        mainPanel.add(contentPanel, BorderLayout.NORTH);
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public boolean isModified() {
        return modified;
    }

    public void apply() {
        // In a real implementation, save settings to persistent storage
        // For now, we'll just reset the modified flag
        modified = false;

        // Here you would typically save to:
        // - PropertiesComponent for simple key-value pairs
        // - PersistentStateComponent for complex settings
    }

    public void reset() {
        loadSettings();
        modified = false;
    }

    private void loadSettings() {
        // Load default settings
        // In a real implementation, load from persistent storage
        enableValidationCheckBox.setSelected(true);
        enableAutoGenerationCheckBox.setSelected(true);
        showWarningsCheckBox.setSelected(true);
        maxSubjectLengthSpinner.setValue(72);
        customTypesTextArea.setText("");
    }
}
