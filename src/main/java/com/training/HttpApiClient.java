package com.training;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpApiClient {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/posts";

    public static void main(String[] args) throws Exception {
        performGET();
        performPOST();
        performPUT();
        performDELETE();
    }

    public static void performGET() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Post post = JsonUtils.fromJson(response.body(), Post.class);
        System.out.println("GET:\n" + post);
    }

    public static void performPOST() throws Exception {
        Post newPost = new Post();
        newPost.title = "New Post Title";
        newPost.body = "Post body from Java";
        newPost.userId = 1;

        String json = JsonUtils.toJson(newPost);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Post created = JsonUtils.fromJson(response.body(), Post.class);
        System.out.println("POST:\n" + created);
    }

    public static void performPUT() throws Exception {
        Post updatePost = new Post();
        updatePost.id = 1;
        updatePost.title = "Updated Title";
        updatePost.body = "Updated body";
        updatePost.userId = 1;

        String json = JsonUtils.toJson(updatePost);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/1"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Post updated = JsonUtils.fromJson(response.body(), Post.class);
        System.out.println("PUT:\n" + updated);
    }

    public static void performDELETE() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("DELETE status code: " + response.statusCode());
    }
}