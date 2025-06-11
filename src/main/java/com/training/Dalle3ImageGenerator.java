package com.training;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Dalle3ImageGenerator {

    private static String loadApiKey() throws IOException {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("application.properties")) {
            props.load(input);
        }
        System.out.println(props.getProperty("openai.api.key"));
        return props.getProperty("openai.api.key");
    }

    public static void main(String[] args) throws Exception {
        String prompt = "A futuristic cityscape at sunset with flying cars and tall glass towers";

        String apiKey = loadApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("API key not found in application.properties");
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "dall-e-2");
        requestBody.put("prompt", prompt);

        String requestJson = mapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/images/generations"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonNode root = mapper.readTree(response.body());
            String imageUrl = root.get("data").get(0).get("url").asText();

            System.out.println("Image URL: " + imageUrl);

            HttpRequest imageRequest = HttpRequest.newBuilder()
                    .uri(URI.create(imageUrl))
                    .build();

            HttpResponse<InputStream> imageResponse = client.send(imageRequest, HttpResponse.BodyHandlers.ofInputStream());

            try (InputStream inputStream = imageResponse.body();
                 FileOutputStream outputStream = new FileOutputStream("dalle3_image.png")) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("Image saved as dalle3_image.png");
        } else {
            System.out.println("Failed to generate image. HTTP Status: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        }
    }
}
