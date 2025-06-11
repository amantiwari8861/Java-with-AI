package com.training;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.nio.file.*;
import java.util.Properties;
import java.util.concurrent.*;

public class OpenAIImageGenerator {

    private static final String API_ENDPOINT = "https://api.openai.com/v1/chat/completions";
    private static String OPENAI_API_KEY;

    public void readPropertyFile() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("application.properties")) {
            properties.load(fis);
            OPENAI_API_KEY = properties.getProperty("openai.api.key");
        } catch (IOException e) {
            System.err.println("Failed to read properties file: " + e.getMessage());
        }
    }

    public void generateImage(String prompt, String outputFilePath)
            throws IOException, InterruptedException, ExecutionException {

        ObjectMapper mapper = new ObjectMapper();

        // Create JSON body using Jackson
        String requestBody = mapper.createObjectNode()
                .put("prompt", prompt)
                .put("n", 1)
                .put("size", "512x512")
                .put("response_format", "url")
                .toString();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<HttpResponse<String>> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = future.get();

        if (response.statusCode() == 200) {
            JsonNode root = mapper.readTree(response.body());
            String imageUrl = root.path("data").get(0).path("url").asText();
            downloadImage(imageUrl, outputFilePath);
        } else {
            System.err.println("Image generation failed. Code: " + response.statusCode());
            System.err.println("Response: " + response.body());
        }
    }

    private void downloadImage(String imageUrl, String outputPath) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest imageRequest = HttpRequest.newBuilder()
                .uri(URI.create(imageUrl))
                .build();

        HttpResponse<byte[]> imageResponse = client.send(imageRequest, HttpResponse.BodyHandlers.ofByteArray());

        if (imageResponse.statusCode() == 200) {
            Files.write(Paths.get(outputPath), imageResponse.body());
            System.out.println("Image saved to: " + outputPath);
        } else {
            System.err.println("Failed to download image: " + imageResponse.statusCode());
        }
    }

    public static void main(String[] args) {
        OpenAIImageGenerator generator = new OpenAIImageGenerator();
        generator.readPropertyFile();
        String prompt = "A futuristic city skyline at sunset";
        String outputPath = "openai_image.png";

        try {
            generator.generateImage(prompt, outputPath);
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
