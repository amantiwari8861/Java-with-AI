package com.training;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class App 
{
    private static final String BASE_URL = "http://localhost:5000/api/v1/users";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main( String[] args ) throws Exception {

        performPOST();
    }
    public static void performPOST() throws Exception {
        String user= """
                {
                    "userId": 100,
                    "name": "Aman Tiwari",
                    "mobileNo": 9891062743,
                    "email": "amantiwari8861@gmail.com",
                    "password": "12345678",
                    "isMarried": false,
                    "age": 80,
                    "dob": "1995-12-07T00:00:00.000Z",
                    "salary": 44372,
                    "skills": [
                        "Spring Boot",
                        "MEAN",
                        "Node.js",
                        "MERN"
                    ],
                    "address": {
                        "city": "Jamnagar",
                        "state": "Andhra Pradesh",
                        "pincode": 164651,
                        "street": "144, Chauhan Street"
                    }
                }
                """;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(user))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}
