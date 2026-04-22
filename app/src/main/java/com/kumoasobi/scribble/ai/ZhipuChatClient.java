package com.kumoasobi.scribble.ai;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Minimal HTTP client for the Zhipu AI (GLM) chat API.
 * Uses only java.net (no external deps) so it compiles with the existing Gradle setup.
 *
 * Call {@link #sendAsync(List, Consumer, Consumer)} on a background thread.
 * 
 * @author Yutong Xiao
 * @version 1.0
 */
public class ZhipuChatClient {

    private static final String API_URL  = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
    private static final String API_KEY  = "4c764689a1bc45b0b4f65ceb0f7a5427.4GVmojr7rlopAqu5";
    private static final String MODEL    = "glm-4-flash";

    /**
     * Sends a chat request asynchronously.
     *
     * @param messages list of {role, content} maps  (role = "system"|"user"|"assistant")
     * @param onReply  called on the EDT with the assistant's text when done
     * @param onError  called on the EDT with an error message if something goes wrong
     */
    public static void sendAsync(List<Map<String,String>> messages,
                                 Consumer<String> onReply,
                                 Consumer<String> onError) {
        Thread t = new Thread(() -> {
            try {
                String reply = send(messages);
                javax.swing.SwingUtilities.invokeLater(() -> onReply.accept(reply));
            } catch (Exception ex) {
                javax.swing.SwingUtilities.invokeLater(() -> onError.accept(ex.getMessage()));
            }
        }, "zhipu-chat");
        t.setDaemon(true);
        t.start();
    }

    private static String send(List<Map<String,String>> messages) throws Exception {
        // Build JSON manually to avoid any library dependency
        StringBuilder jsonMessages = new StringBuilder("[");
        for (int i = 0; i < messages.size(); i++) {
            Map<String,String> m = messages.get(i);
            jsonMessages.append("{\"role\":\"").append(escape(m.get("role")))
                        .append("\",\"content\":\"").append(escape(m.get("content")))
                        .append("\"}");
            if (i < messages.size() - 1) jsonMessages.append(",");
        }
        jsonMessages.append("]");

        String body = "{\"model\":\"" + MODEL + "\","
                    + "\"messages\":" + jsonMessages + ","
                    + "\"max_tokens\":300,"
                    + "\"temperature\":0.8}";

        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setDoOutput(true);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(20000);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        int code = conn.getResponseCode();
        java.io.InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
        String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        if (code < 200 || code >= 300) {
            throw new Exception("API error " + code + ": " + response);
        }

        // Extract content from JSON: "content":"..." (simple string extraction)
        return extractContent(response);
    }

    /** Very lightweight JSON field extractor for the "content" field of GLM response. */
    private static String extractContent(String json) {
        // Response shape: ...{"role":"assistant","content":"..."}...
        String marker = "\"content\":\"";
        int start = json.indexOf(marker);
        if (start == -1) throw new RuntimeException("No content in response: " + json);
        start += marker.length();
        // Walk forward respecting escape sequences
        StringBuilder sb = new StringBuilder();
        int i = start;
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '\\' && i + 1 < json.length()) {
                char next = json.charAt(i + 1);
                switch (next) {
                    case '"'  -> sb.append('"');
                    case '\\'  -> sb.append('\\');
                    case 'n'  -> sb.append('\n');
                    case 'r'  -> sb.append('\r');
                    case 't'  -> sb.append('\t');
                    default   -> sb.append(next);
                }
                i += 2;
            } else if (c == '"') {
                break;
            } else {
                sb.append(c);
                i++;
            }
        }
        return sb.toString().trim();
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
