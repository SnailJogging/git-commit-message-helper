package com.github.gitcommithelper.ai;

import com.github.gitcommithelper.model.FileChangeInfo;
import com.github.gitcommithelper.settings.PluginSettings;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Claude (Anthropic) provider for commit message generation
 */
public class ClaudeProvider extends BaseAIProvider {

    private static final Logger LOG = Logger.getInstance(ClaudeProvider.class);
    private static final String API_VERSION = "2023-06-01";

    public ClaudeProvider(PluginSettings settings) {
        super(settings);
    }

    @NotNull
    @Override
    public String getName() {
        return "Claude (Anthropic)";
    }

    @Nullable
    @Override
    public String testConnection() {
        // For Claude, we'll do a simple connectivity test
        try {
            URL url = new URL(getEndpoint() + "/v1/messages");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            configureSSL(conn); // Configure SSL certificate handling
            conn.setRequestMethod("POST");
            conn.setRequestProperty("x-api-key", getApiKey());
            conn.setRequestProperty("anthropic-version", API_VERSION);
            conn.setRequestProperty("content-type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // Send minimal test request
            String testBody = String.format(
                    "{\"model\":\"%s\",\"max_tokens\":10,\"messages\":[{\"role\":\"user\",\"content\":\"test\"}]}",
                    getModel()
            );

            try (OutputStream os = conn.getOutputStream()) {
                os.write(testBody.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                return null; // Success
            } else {
                return "Connection failed with status code: " + responseCode;
            }
        } catch (Exception e) {
            return "Connection error: " + e.getMessage();
        }
    }

    @Nullable
    @Override
    public String generateCommitMessage(@NotNull String diffContent, @NotNull List<FileChangeInfo> fileChanges)
            throws AIProviderException {
        try {
            String prompt = buildPrompt(diffContent, fileChanges);
            String requestBody = buildRequestBody(prompt);

            URL url = new URL(getEndpoint() + "/v1/messages");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            configureSSL(conn); // Configure SSL certificate handling
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("x-api-key", getApiKey());
            conn.setRequestProperty("anthropic-version", API_VERSION);
            conn.setDoOutput(true);
            conn.setConnectTimeout(getTimeout());
            conn.setReadTimeout(getTimeout());

            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Read response
            int responseCode = conn.getResponseCode();
            StringBuilder response = new StringBuilder();

            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                }

                String content = parseResponse(response.toString());
                return extractCommitMessage(content);
            } else {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                }
                throw new AIProviderException("Claude API error (" + responseCode + "): " + response.toString());
            }

        } catch (AIProviderException e) {
            throw e;
        } catch (Exception e) {
            LOG.warn("Claude provider error", e);
            throw new AIProviderException("Failed to generate commit message: " + e.getMessage(), e);
        }
    }

    private String buildRequestBody(String prompt) {
        return String.format(
                "{\"model\":\"%s\",\"max_tokens\":%d,\"messages\":[{\"role\":\"user\",\"content\":%s}]}",
                getModel(),
                getMaxTokens(),
                escapeJson(prompt)
        );
    }

    private String parseResponse(String jsonResponse) throws AIProviderException {
        try {
            // Extract text from Claude's response format
            int textStart = jsonResponse.indexOf("\"text\":\"");
            if (textStart == -1) {
                throw new AIProviderException("Invalid response format");
            }
            textStart += 8; // Length of "text":"

            int textEnd = jsonResponse.indexOf("\"", textStart);
            if (textEnd == -1) {
                throw new AIProviderException("Invalid response format");
            }

            return unescapeJson(jsonResponse.substring(textStart, textEnd));
        } catch (Exception e) {
            throw new AIProviderException("Failed to parse response: " + e.getMessage(), e);
        }
    }

    private String escapeJson(String str) {
        return "\"" + str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t") + "\"";
    }

    private String unescapeJson(String str) {
        return str.replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }
}
