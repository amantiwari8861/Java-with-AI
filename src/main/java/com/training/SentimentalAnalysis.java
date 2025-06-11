package com.training;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class SentimentalAnalysis {
    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        String apiKey = ""; // replace with your Gemini API key
        String prompt="";
        HttpClient client = HttpClient.newHttpClient();


        while (!prompt.equalsIgnoreCase("quit"))
        {
//            System.out.print("Enter Prompt:");
            System.out.print("Me :");
            prompt=sc.nextLine();
            if(prompt.equalsIgnoreCase("quit"))
                break;
            String requestBody = """
        {
          "contents": [
            {
              "parts": [
                {
                  "text": "is this statement is positive,negative or neutral '%s' "
                }
              ]
            }
          ]
        }
        """.formatted(prompt);
//            System.out.println("Me : "+prompt);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

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

            System.out.println("Bot:" + text);
        }
        System.out.println("Thank you for using our chat bot.");
        sc.close();
    }
}
