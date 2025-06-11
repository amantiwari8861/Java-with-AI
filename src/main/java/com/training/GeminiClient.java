package com.training;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class GeminiClient {

    public static void main(String[] args) throws Exception{
        String apiKey = ""; // replace with your Gemini API key
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a prompt:");
        String prompt = sc.nextLine();
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

//        System.out.println("Request body:\n" + requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//        System.out.println("Response:\n" + response.body());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        // Extract the text from the nested structure
        String text = root.path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText();

        System.out.println("Extracted text:\n" + text);

    }
}
