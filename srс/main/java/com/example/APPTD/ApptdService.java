package com.example.APPTD;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class ApptdService {

    private static final String BASE_URL = "http://193.19.100.32:7000/api";

    private CloseableHttpClient createHttpClient() {
        return HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
    }

    public String getRole() throws IOException {
        try (CloseableHttpClient httpClient = createHttpClient();
             CloseableHttpResponse response = httpClient.execute(new HttpGet(BASE_URL + "/get-roles"))) {
            return EntityUtils.toString(response.getEntity());
        }
    }

    public void registerUser(String lastName, String firstName, String email, String role) throws IOException {
        try (CloseableHttpClient httpClient = createHttpClient()) {
            HttpPost request = new HttpPost(BASE_URL + "/sign-up");
            String json = String.format("{ \"last_name\": \"%s\", \"first_name\": \"%s\", \"email\": \"%s\", \"role\": \"%s\" }", lastName, firstName, email, role);
            request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
            request.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                System.out.println(EntityUtils.toString(response.getEntity()));
            }
        }
    }

    public String codeEmail(String email) throws IOException {
        String urlCode = BASE_URL + "/get-code?email=" + email;
        try (CloseableHttpClient httpClient = createHttpClient();
             CloseableHttpResponse response = httpClient.execute(new HttpGet(urlCode))) {
            String responseBody = EntityUtils.toString(response.getEntity());
            return responseBody.replaceAll("\"", "");
        }
    }

    public String encodeToBase64(String email, String code) {
        if (email == null || code == null || email.isEmpty() || code.isEmpty()) {
            return "";
        }

        String text = email + ":" + code;
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    public String setStatus(String token) throws IOException {
        try (CloseableHttpClient httpClient = createHttpClient()) {
            HttpPost request = new HttpPost(BASE_URL + "/set-status");
            String json = String.format("{ \"token\": \"%s\", \"status\": \"increased\" }", token);
            request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
            request.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }
}
