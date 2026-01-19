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
 * DeepSeek provider for commit message generation
 * Uses OpenAI-compatible API
 */
public class DeepSeekProvider extends BaseAIProvider {

    private static final Logger LOG = Logger.getInstance(DeepSeekProvider.class);

    public DeepSeekProvider(PluginSettings settings) {
        super(settings);
    }

    @NotNull
    @Override
    public String getName() {
        return "DeepSeek";
    }

    @Nullable
    @Override
    public String testConnection() {
        try {
            URL url = new URL(getEndpoint() + "/models");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            configureSSL(conn); // Configure SSL certificate handling
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + getApiKey());
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

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

            URL url = new URL(getEndpoint() + "/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            configureSSL(conn); // Configure SSL certificate handling
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + getApiKey());
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
                throw new AIProviderException("DeepSeek API error (" + responseCode + "): " + response.toString());
            }

        } catch (AIProviderException e) {
            throw e;
        } catch (Exception e) {
            LOG.warn("DeepSeek provider error", e);
            throw new AIProviderException("Failed to generate commit message: " + e.getMessage(), e);
        }
    }

    private String buildRequestBody(String prompt) {
        return String.format(
                "{\"model\":\"%s\",\"messages\":[{\"role\":\"user\",\"content\":%s}],\"temperature\":%.1f,\"max_tokens\":%d}",
                getModel(),
                escapeJson(prompt),
                getTemperature(),
                getMaxTokens()
        );
    }

    private String parseResponse(String jsonResponse) throws AIProviderException {
        try {
            // Simple JSON parsing - extract content from response
            int contentStart = jsonResponse.indexOf("\"content\":\"");
            if (contentStart == -1) {
                throw new AIProviderException("Invalid response format");
            }
            contentStart += 11; // Length of "content":"

            int contentEnd = jsonResponse.indexOf("\"", contentStart);
            if (contentEnd == -1) {
                throw new AIProviderException("Invalid response format");
            }

            return unescapeJson(jsonResponse.substring(contentStart, contentEnd));
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
