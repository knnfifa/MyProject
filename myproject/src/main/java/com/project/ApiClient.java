package com.project;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static String fetchApiResponse(String urlString) throws Exception { //ดึงข้อมูลจาก API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); //ส่ง request ไปยัง API และรับ response เป็น String

        if (response.statusCode() != 200) {
            throw new RuntimeException("❌ API Request Failed! Status Code: " + response.statusCode());
        }

        return response.body();
    }
}
