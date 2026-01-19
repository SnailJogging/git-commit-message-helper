package com.github.gitcommithelper.ui;

import com.github.gitcommithelper.ai.AIProvider;
import com.github.gitcommithelper.ai.AIProviderFactory;
import com.github.gitcommithelper.ai.AIProviderType;
import com.github.gitcommithelper.settings.PluginSettings;
import com.intellij.openapi.ui.Messages;

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

    // AI settings
    private JCheckBox enableAICheckBox;
    private JComboBox<AIProviderType> aiProviderComboBox;
    private JPasswordField apiKeyField;
    private JTextField apiEndpointField;
    private JTextField modelField;
    private JSpinner maxTokensSpinner;
    private JSpinner temperatureSpinner;
    private JSpinner timeoutSpinner;
    private JCheckBox enableCachingCheckBox;
    private JCheckBox fallbackToBasicCheckBox;
    private JCheckBox trustAllCertificatesCheckBox;
    private JButton testConnectionButton;
    private JTextArea promptTemplateArea;
    private JButton resetPromptButton;
    private JComboBox<String> languageComboBox;

    private JPanel aiSettingsPanel;

    private final PluginSettings settings;
    private boolean modified = false;

    public SettingsPanel() {
        this.settings = PluginSettings.getInstance();
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
        addValidationSettings(contentPanel);

        // Generation settings
        addGenerationSettings(contentPanel);

        // AI settings
        addAISettings(contentPanel);

        // Custom commit types
        addCustomTypesSettings(contentPanel);

        // Add bottom padding to ensure all content is visible
        contentPanel.add(Box.createVerticalStrut(20));

        JScrollPane mainScrollPane = new JScrollPane(contentPanel);
        mainScrollPane.setBorder(null);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(mainScrollPane, BorderLayout.CENTER);
    }

    private void addValidationSettings(JPanel contentPanel) {
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
    }

    private void addGenerationSettings(JPanel contentPanel) {
        JLabel generationLabel = new JLabel("Generation Settings");
        generationLabel.setFont(generationLabel.getFont().deriveFont(Font.BOLD));
        contentPanel.add(generationLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        enableAutoGenerationCheckBox = new JCheckBox("Enable automatic message generation");
        enableAutoGenerationCheckBox.addActionListener(e -> modified = true);
        contentPanel.add(enableAutoGenerationCheckBox);
        contentPanel.add(Box.createVerticalStrut(10));

        JPanel lengthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lengthPanel.add(new JLabel("Maximum subject length:"));
        maxSubjectLengthSpinner = new JSpinner(new SpinnerNumberModel(72, 20, 200, 1));
        maxSubjectLengthSpinner.addChangeListener(e -> modified = true);
        lengthPanel.add(maxSubjectLengthSpinner);
        lengthPanel.add(new JLabel("characters"));
        contentPanel.add(lengthPanel);
        contentPanel.add(Box.createVerticalStrut(20));
    }

    private void addAISettings(JPanel contentPanel) {
        JLabel aiLabel = new JLabel("AI Enhancement Settings");
        aiLabel.setFont(aiLabel.getFont().deriveFont(Font.BOLD));
        contentPanel.add(aiLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        enableAICheckBox = new JCheckBox("Enable AI-enhanced generation (slower, more accurate)");
        enableAICheckBox.addActionListener(e -> {
            modified = true;
            updateAISettingsVisibility();
        });
        contentPanel.add(enableAICheckBox);
        contentPanel.add(Box.createVerticalStrut(10));

        // AI settings panel (shown only when AI is enabled)
        aiSettingsPanel = new JPanel();
        aiSettingsPanel.setLayout(new BoxLayout(aiSettingsPanel, BoxLayout.Y_AXIS));
        aiSettingsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Provider selection
        JPanel providerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        providerPanel.add(new JLabel("AI Provider:"));
        aiProviderComboBox = new JComboBox<>(AIProviderType.values());
        aiProviderComboBox.addActionListener(e -> {
            modified = true;
            onProviderChanged();
        });
        providerPanel.add(aiProviderComboBox);
        aiSettingsPanel.add(providerPanel);
        aiSettingsPanel.add(Box.createVerticalStrut(10));

        // API Key with show/hide toggle
        JPanel apiKeyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        apiKeyPanel.add(new JLabel("API Key:"));
        apiKeyField = new JPasswordField(30);
        apiKeyField.getDocument().addDocumentListener(createDocumentListener());
        apiKeyPanel.add(apiKeyField);

        // Add show/hide password button
        JButton togglePasswordButton = new JButton("👁");
        togglePasswordButton.setToolTipText("显示/隐藏 API Key");
        togglePasswordButton.setFocusable(false);
        togglePasswordButton.addActionListener(e -> {
            if (apiKeyField.getEchoChar() == 0) {
                // Currently visible, hide it
                apiKeyField.setEchoChar('•');
                togglePasswordButton.setText("👁");
                togglePasswordButton.setToolTipText("显示 API Key");
            } else {
                // Currently hidden, show it
                apiKeyField.setEchoChar((char) 0);
                togglePasswordButton.setText("🙈");
                togglePasswordButton.setToolTipText("隐藏 API Key");
            }
        });
        apiKeyPanel.add(togglePasswordButton);

        aiSettingsPanel.add(apiKeyPanel);
        aiSettingsPanel.add(Box.createVerticalStrut(10));

        // Endpoint
        JPanel endpointPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        endpointPanel.add(new JLabel("API Endpoint:"));
        apiEndpointField = new JTextField(40);
        apiEndpointField.getDocument().addDocumentListener(createDocumentListener());
        endpointPanel.add(apiEndpointField);
        aiSettingsPanel.add(endpointPanel);
        aiSettingsPanel.add(Box.createVerticalStrut(10));

        // Model
        JPanel modelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        modelPanel.add(new JLabel("Model:"));
        modelField = new JTextField(20);
        modelField.getDocument().addDocumentListener(createDocumentListener());
        modelPanel.add(modelField);
        aiSettingsPanel.add(modelPanel);
        aiSettingsPanel.add(Box.createVerticalStrut(10));

        // Max tokens
        JPanel tokensPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tokensPanel.add(new JLabel("Max Tokens:"));
        maxTokensSpinner = new JSpinner(new SpinnerNumberModel(500, 50, 2000, 50));
        maxTokensSpinner.addChangeListener(e -> modified = true);
        tokensPanel.add(maxTokensSpinner);
        aiSettingsPanel.add(tokensPanel);
        aiSettingsPanel.add(Box.createVerticalStrut(10));

        // Temperature
        JPanel tempPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tempPanel.add(new JLabel("Temperature:"));
        temperatureSpinner = new JSpinner(new SpinnerNumberModel(0.3, 0.0, 1.0, 0.1));
        temperatureSpinner.addChangeListener(e -> modified = true);
        tempPanel.add(temperatureSpinner);
        aiSettingsPanel.add(tempPanel);
        aiSettingsPanel.add(Box.createVerticalStrut(10));

        // Timeout
        JPanel timeoutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timeoutPanel.add(new JLabel("Timeout:"));
        timeoutSpinner = new JSpinner(new SpinnerNumberModel(30, 5, 120, 5));
        timeoutSpinner.addChangeListener(e -> modified = true);
        timeoutPanel.add(timeoutSpinner);
        timeoutPanel.add(new JLabel("seconds"));
        aiSettingsPanel.add(timeoutPanel);
        aiSettingsPanel.add(Box.createVerticalStrut(10));

        // Advanced options
        enableCachingCheckBox = new JCheckBox("Enable caching");
        enableCachingCheckBox.addActionListener(e -> modified = true);
        aiSettingsPanel.add(enableCachingCheckBox);

        fallbackToBasicCheckBox = new JCheckBox("Fallback to basic mode if AI fails");
        fallbackToBasicCheckBox.addActionListener(e -> modified = true);
        aiSettingsPanel.add(fallbackToBasicCheckBox);

        trustAllCertificatesCheckBox = new JCheckBox("Trust all SSL certificates (for corporate proxy)");
        trustAllCertificatesCheckBox.setToolTipText("WARNING: Disables SSL certificate verification. Only enable in corporate proxy environments.");
        trustAllCertificatesCheckBox.addActionListener(e -> modified = true);
        aiSettingsPanel.add(trustAllCertificatesCheckBox);
        aiSettingsPanel.add(Box.createVerticalStrut(10));

        // Message language
        JPanel languagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        languagePanel.add(new JLabel("Message Language:"));
        languageComboBox = new JComboBox<>(new String[]{"中文 (Chinese)", "English"});
        languageComboBox.addActionListener(e -> modified = true);
        languagePanel.add(languageComboBox);
        aiSettingsPanel.add(languagePanel);
        aiSettingsPanel.add(Box.createVerticalStrut(10));

        // Test connection button
        testConnectionButton = new JButton("Test Connection");
        testConnectionButton.addActionListener(e -> testConnection());
        JPanel testPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        testPanel.add(testConnectionButton);
        aiSettingsPanel.add(testPanel);
        aiSettingsPanel.add(Box.createVerticalStrut(10));

        // Prompt template
        JLabel promptLabel = new JLabel("Custom Prompt Template (optional):");
        aiSettingsPanel.add(promptLabel);
        aiSettingsPanel.add(Box.createVerticalStrut(5));

        promptTemplateArea = new JTextArea(4, 40);
        promptTemplateArea.setLineWrap(true);
        promptTemplateArea.setWrapStyleWord(true);
        promptTemplateArea.getDocument().addDocumentListener(createDocumentListener());
        JScrollPane promptScrollPane = new JScrollPane(promptTemplateArea);
        promptScrollPane.setPreferredSize(new Dimension(500, 100));
        promptScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        aiSettingsPanel.add(promptScrollPane);
        aiSettingsPanel.add(Box.createVerticalStrut(5));

        resetPromptButton = new JButton("Reset to Default");
        resetPromptButton.addActionListener(e -> {
            promptTemplateArea.setText("");
            modified = true;
        });
        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        resetPanel.add(resetPromptButton);
        aiSettingsPanel.add(resetPanel);

        contentPanel.add(aiSettingsPanel);
        contentPanel.add(Box.createVerticalStrut(20));
    }

    private void addCustomTypesSettings(JPanel contentPanel) {
        JLabel customTypesLabel = new JLabel("Custom Commit Types (one per line):");
        customTypesLabel.setFont(customTypesLabel.getFont().deriveFont(Font.BOLD));
        contentPanel.add(customTypesLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        customTypesTextArea = new JTextArea(5, 40);
        customTypesTextArea.setLineWrap(false);
        customTypesTextArea.getDocument().addDocumentListener(createDocumentListener());
        JScrollPane scrollPane = new JScrollPane(customTypesTextArea);
        scrollPane.setPreferredSize(new Dimension(500, 100));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        contentPanel.add(scrollPane);
    }

    private javax.swing.event.DocumentListener createDocumentListener() {
        return new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { modified = true; }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { modified = true; }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { modified = true; }
        };
    }

    private void updateAISettingsVisibility() {
        aiSettingsPanel.setVisible(enableAICheckBox.isSelected());
    }

    /**
     * Called when AI provider selection changes
     * Saves current API key and loads the API key for the newly selected provider
     */
    private void onProviderChanged() {
        // First, save the current API key to the previous provider
        AIProviderType previousType = settings.getAiProviderType();
        if (previousType != AIProviderType.NONE) {
            String currentApiKey = apiKeyField.getText();
            settings.setApiKeyForProvider(previousType, currentApiKey);
        }

        // Then, load settings for the newly selected provider
        AIProviderType selectedType = (AIProviderType) aiProviderComboBox.getSelectedItem();
        if (selectedType == null) {
            return;
        }

        // Load API key for the new provider
        String providerApiKey = settings.getApiKeyForProvider(selectedType);
        apiKeyField.setText(providerApiKey);

        // Update endpoint
        if (!selectedType.getDefaultEndpoint().isEmpty()) {
            apiEndpointField.setText(selectedType.getDefaultEndpoint());
        }

        // Update default model based on provider
        switch (selectedType) {
            case OPENAI:
                modelField.setText("gpt-4-turbo");
                break;
            case CLAUDE:
                modelField.setText("claude-3-5-sonnet-20241022");
                break;
            case DEEPSEEK:
                modelField.setText("deepseek-chat");
                break;
            case ZHIPU:
                modelField.setText("glm-4");
                break;
            case QWEN:
                modelField.setText("qwen-turbo");
                break;
            case OLLAMA:
                modelField.setText("llama2");
                break;
        }
    }

    private void testConnection() {
        // Save current values temporarily
        PluginSettings tempSettings = new PluginSettings();
        saveToSettings(tempSettings);

        AIProvider provider = AIProviderFactory.createProvider(tempSettings);
        if (provider == null) {
            Messages.showErrorDialog("Please select an AI provider", "Test Connection");
            return;
        }

        testConnectionButton.setEnabled(false);
        testConnectionButton.setText("Testing...");

        // Run test in background
        new Thread(() -> {
            String result = provider.testConnection();
            SwingUtilities.invokeLater(() -> {
                testConnectionButton.setEnabled(true);
                testConnectionButton.setText("Test Connection");

                if (result == null) {
                    Messages.showInfoMessage("Connection successful!", "Test Connection");
                } else {
                    Messages.showErrorDialog("Connection failed:\n" + result, "Test Connection");
                }
            });
        }).start();
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public boolean isModified() {
        return modified;
    }

    public void apply() {
        saveToSettings(settings);
        modified = false;
    }

    private void saveToSettings(PluginSettings targetSettings) {
        targetSettings.setEnableValidation(enableValidationCheckBox.isSelected());
        targetSettings.setShowWarnings(showWarningsCheckBox.isSelected());
        targetSettings.setEnableAutoGeneration(enableAutoGenerationCheckBox.isSelected());
        targetSettings.setMaxSubjectLength((Integer) maxSubjectLengthSpinner.getValue());
        targetSettings.setCustomTypes(customTypesTextArea.getText());

        // AI settings
        targetSettings.setEnableAI(enableAICheckBox.isSelected());

        // Save current API key for current provider before saving provider type
        AIProviderType currentType = (AIProviderType) aiProviderComboBox.getSelectedItem();
        if (currentType != null && currentType != AIProviderType.NONE) {
            targetSettings.setApiKeyForProvider(currentType, apiKeyField.getText());
        }

        targetSettings.setAiProviderType(currentType);
        targetSettings.setApiKey(apiKeyField.getText()); // Also update legacy field
        targetSettings.setApiEndpoint(apiEndpointField.getText());
        targetSettings.setModel(modelField.getText());
        targetSettings.setMaxTokens((Integer) maxTokensSpinner.getValue());
        targetSettings.setTemperature((Double) temperatureSpinner.getValue());
        targetSettings.setTimeoutSeconds((Integer) timeoutSpinner.getValue());
        targetSettings.setEnableCaching(enableCachingCheckBox.isSelected());
        targetSettings.setFallbackToBasic(fallbackToBasicCheckBox.isSelected());
        targetSettings.setTrustAllCertificates(trustAllCertificatesCheckBox.isSelected());
        targetSettings.setPromptTemplate(promptTemplateArea.getText());

        // Save language setting
        String selectedLanguage = (String) languageComboBox.getSelectedItem();
        if (selectedLanguage.startsWith("中文")) {
            targetSettings.setMessageLanguage("zh-CN");
        } else {
            targetSettings.setMessageLanguage("en");
        }
    }

    public void reset() {
        loadSettings();
        modified = false;
    }

    private void loadSettings() {
        enableValidationCheckBox.setSelected(settings.isEnableValidation());
        enableAutoGenerationCheckBox.setSelected(settings.isEnableAutoGeneration());
        showWarningsCheckBox.setSelected(settings.isShowWarnings());
        maxSubjectLengthSpinner.setValue(settings.getMaxSubjectLength());
        customTypesTextArea.setText(settings.getCustomTypes());

        // AI settings
        enableAICheckBox.setSelected(settings.isEnableAI());
        aiProviderComboBox.setSelectedItem(settings.getAiProviderType());
        apiKeyField.setText(settings.getApiKey());
        apiEndpointField.setText(settings.getApiEndpoint());
        modelField.setText(settings.getModel());
        maxTokensSpinner.setValue(settings.getMaxTokens());
        temperatureSpinner.setValue(settings.getTemperature());
        timeoutSpinner.setValue(settings.getTimeoutSeconds());
        enableCachingCheckBox.setSelected(settings.isEnableCaching());
        fallbackToBasicCheckBox.setSelected(settings.isFallbackToBasic());
        trustAllCertificatesCheckBox.setSelected(settings.isTrustAllCertificates());
        promptTemplateArea.setText(settings.getPromptTemplate());

        // Load language setting
        String language = settings.getMessageLanguage();
        if ("zh-CN".equals(language) || "zh".equals(language)) {
            languageComboBox.setSelectedIndex(0); // 中文
        } else {
            languageComboBox.setSelectedIndex(1); // English
        }

        updateAISettingsVisibility();
    }
}
