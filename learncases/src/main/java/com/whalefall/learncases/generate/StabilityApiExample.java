package com.whalefall.learncases.generate;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * @author Halcyon
 * @since 1.0.0
 */
public class StabilityApiExample {

    public static void main(String[] args) throws IOException {
        String engineId = "stable-diffusion-v1-6";
        String apiHost = "https://api.stability.ai";
        String apiKey = System.getenv("STABILITY_API_KEY");

        if (apiKey == null) {
            throw new ImageException("Missing Stability API key.");
        }
        HttpResponse response;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()){
            HttpPost httpPost = getHttpPost(apiHost, engineId, apiKey);

            // Execute HTTP request
            response = httpClient.execute(httpPost);
        } catch (Exception e) {
            throw new ImageException(e);
        }


        if (response.getStatusLine().getStatusCode() != 200) {
            throw new ImageException("Non-200 response: " + response.getStatusLine().getReasonPhrase());
        }

        // Process response
        List<String> artifacts = getArtifacts(response.getEntity());

        for (String base64Image : artifacts) {
            LocalDateTime now = LocalDateTime.now();
            String randomUuidStr = now.toString().replace(":", "");

            // Decode and save the image
            byte[] imageBytes = Base64.decodeBase64(base64Image);
            // Specify the output directory
            String outputDirectoryPath = "../out/";

            // Create the output directory if it doesn't exist
            Files.createDirectories(Paths.get(outputDirectoryPath));

            Path imagePath = Paths.get("../out/v1_txt2img_" + randomUuidStr + ".png");
            try (FileOutputStream fos = new FileOutputStream(imagePath.toFile())) {
                fos.write(imageBytes);
                fos.flush();
            }
        }
    }

    private static HttpPost getHttpPost(String apiHost, String engineId, String apiKey) throws UnsupportedEncodingException {
        String apiUrl = apiHost + "/v1/generation/" + engineId + "/text-to-image";
        HttpPost httpPost = new HttpPost(apiUrl);

        // Set headers
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + apiKey);

        // Set request body
        String requestBody = """
                {
                    "text_prompts": [
                        {
                            "text": "beautiful girls"
                        }
                    ],
                    "cfg_scale": 7,
                    "height": 1024,
                    "width": 1024,
                    "sampler": "K_DPM_2_ANCESTRAL",
                    "samples": 1,
                    "steps": 30
                }
                """;
        httpPost.setEntity(new StringEntity(requestBody));
        return httpPost;
    }

    // Method to handle JSON parsing and extract the 'artifacts' array
    private static List<String> getArtifacts(HttpEntity httpEntity) throws IOException {
        String content = EntityUtils.toString(httpEntity, "UTF-8");

        // Check if the content is gzip-compressed
        if (httpEntity.getContentEncoding() != null && "gzip".equalsIgnoreCase(httpEntity.getContentEncoding().getValue())) {
            try (InputStream is = new GZIPInputStream(httpEntity.getContent())) {
                return parseJsonResponse(is);
            }
        } else {
            return parseJsonResponse(content);
        }
    }

    private static List<String> parseJsonResponse(Object obj) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        if (obj instanceof String str) {
            rootNode = objectMapper.readTree(str);
        } else if (obj instanceof InputStream stream) {
            rootNode = objectMapper.readTree(stream);
        } else {
            throw new IllegalArgumentException();
        }


        List<String> artifacts = new ArrayList<>();
        for (JsonNode artifactNode : rootNode.get("artifacts")) {
            String base64Image = artifactNode.get("base64").asText();
            artifacts.add(base64Image);
        }

        return artifacts;
    }
}
