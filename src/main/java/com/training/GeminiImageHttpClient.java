package com.training;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeminiImageHttpClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String API_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:generateContent?key=";
    private static String GEMINI_API_KEY;

    public void readPropertyFile() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("application.properties")) {
            properties.load(fis);
            GEMINI_API_KEY = properties.getProperty("gemini.api.key");
        } catch (IOException e) {
            System.err.println("Failed to read properties file: " + e.getMessage());
        }
    }

    public void generateAndSaveImage(String prompt, String outputFilePath)
            throws IOException, InterruptedException, ExecutionException {

        String requestBody = """
                {
                  "contents": [
                    {
                      "parts": [
                        {
                          "text": "%s"
                        }
                      ]
                    }
                  ]
                }
                """.formatted(prompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT + GEMINI_API_KEY))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = httpClient.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = responseFuture.get();

        if (response.statusCode() == 200) {
            String responseBody = response.body();
            String imageData = extractImageData(responseBody);

            if (imageData != null) {
                try {
                    byte[] imageBytes = Base64.getDecoder().decode(imageData);
                    try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                        fos.write(imageBytes);
                        System.out.println("Image saved to: " + outputFilePath);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Error decoding base64 image data: " + e.getMessage());
                } catch (IOException e) {
                    System.err.println("Error saving image: " + e.getMessage());
                }
            } else {
                System.err.println("Image data not found in the response.");
            }
        } else {
            System.err.println("Failed API call. HTTP code: " + response.statusCode());
            System.err.println("Response: " + response.body());
        }
    }

    private String extractImageData(String jsonResponse) {
        Pattern pattern = Pattern.compile("\"imageData\":\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(jsonResponse);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static void main(String[] args) {
        GeminiImageHttpClient client = new GeminiImageHttpClient();
        client.readPropertyFile();
        String prompt = "A vibrant watercolor painting of a mountain landscape.";
        String outputPath = "watercolor_mountains.png";
        try {
            client.generateAndSaveImage(prompt, outputPath);
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
