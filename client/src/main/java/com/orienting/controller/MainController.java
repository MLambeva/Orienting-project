package com.orienting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orienting.context.UserContext;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

abstract public class MainController {

    protected final String url;

    public MainController(String url) {
        this.url = url;
    }


    public Object makeRequest(Object object, String url, String httpMethod, Object responseType) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        try {
            String requestBody = null;
            if (httpMethod.equals("POST") || httpMethod.equals("PUT"))
                requestBody = objectMapper.writeValueAsString(object);
            HttpURLConnection connection = getHttpURLConnection(url, httpMethod, requestBody);
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                return handleSuccessfulResponse(connection, objectMapper, responseType);
            } else {
                handleErrorResponse(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object handleSuccessfulResponse(HttpURLConnection connection, ObjectMapper objectMapper, Object responseType) throws IOException {
        String responseContent = readResponseContent(connection);
        connection.disconnect();

        if (responseType instanceof String) {
            return responseContent;
        }
        return objectMapper.readValue(responseContent, responseType.getClass());
    }

    private String readResponseContent(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            return content.toString();
        }
    }

    private void handleErrorResponse(HttpURLConnection connection) throws IOException {
        try (InputStream errorStream = connection.getErrorStream()) {
            if (errorStream != null) {
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    System.out.println("Error Messages: " + errorResponse);
                }
            } else {
                System.out.println("Error Response: No error message provided by the server.");
            }
            connection.disconnect();
        }
    }

    private HttpURLConnection getHttpURLConnection(String requestUrl, String httpMethod, String requestBody) throws IOException {
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(httpMethod);

        if (!Objects.equals(UserContext.getToken(), "")) {
            connection.setRequestProperty("Authorization", "Bearer " + UserContext.getToken());
        }
        if (httpMethod.equals("POST") || httpMethod.equals("PUT")) {
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }
        }
        return connection;
    }

    public static boolean checkConnection(String host, int port) {
        String url = String.format("%s:%d/api", host, port);
        try {
            URL urlObject = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            int responseCode = connection.getResponseCode();

            if (responseCode >= 200 && responseCode < 300) {
                System.out.println(url + " is accessible. HTTP Status Code: " + responseCode);
                return true;
            } else {
                System.out.println(url + " is not accessible. HTTP Status Code: " + responseCode);
                return false;
            }
        } catch (IOException e) {
            System.out.println(url + " is not accessible. Error: " + e.getMessage());
            return false;
        }
    }
}
