package com.training;

import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.util.Properties;

public class OpenAiTextGenerator {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final String apiKey;

    public OpenAiTextGenerator() {
        this.apiKey = loadApiKey();
    }

    private String loadApiKey() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("application.properties")) {
            props.load(input);
            return props.getProperty("openai.api.key");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load API key from properties file", e);
        }
    }

    public void generateText(String prompt) throws IOException, InterruptedException {
        String json = """
                {
                  "model": "gpt-3.5-turbo",
                  "messages": [
                    {
                      "role": "user",
                      "content": "%s"
                    }
                  ],
                  "temperature": 0.7
                }
                """.formatted(prompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Response: " + response.body());
        } else {
            System.err.println("Failed to generate text. HTTP Status: " + response.statusCode());
            System.err.println("Response Body: " + response.body());
        }
    }

    public static void main(String[] args) {
        OpenAiTextGenerator generator = new OpenAiTextGenerator();
        try {
            generator.generateText("Explain what is polymorphism in Java.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}