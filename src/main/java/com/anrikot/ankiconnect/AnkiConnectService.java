package com.anrikot.ankiconnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AnkiConnectService {
    
    private static AnkiResponse getResponse(String action, String params) {
        int responseCode = 0;
        AnkiResponse response = null;

        try {
            URL url = new URL("http://127.0.0.1:8765");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Headers
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("accept", "application/json");

            // Body
            String json = "{\"action\": \"" + action + "\", \"params\": {" + params + "}, \"version\": 6}";
            byte[] responseBody = json.getBytes();

            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(responseBody, 0, responseBody.length);
            os.flush();
            os.close();
            
            responseCode = conn.getResponseCode();
            
            if (responseCode != 200) {
                throw new RuntimeException("Http response code: " + responseCode);
            } else {
                response = getResponseBody(conn);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            System.err.println("Invalid URL format. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Couldn't open the connection. " + e.getMessage());
        }

        return response;
    }

    private static AnkiResponse getResponseBody(HttpURLConnection conn) {
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            // Parse JSON response to AnkiResponse object
            ObjectMapper objectMapper = new ObjectMapper();
            AnkiResponse responseBody = objectMapper.readValue(response.toString(), AnkiResponse.class);

            return responseBody;

        } catch (IOException e) {
            System.err.println("Error reading response: " + e.getMessage());
        }

        return null;
    }

    private static List<String> getDeckNames() {
        AnkiResponse response = getResponse("deckNames", "");
        List<String> deckNames = (List<String>) response.getResult();

        return deckNames;
    }

    private static Map<String, Integer> getDeckNamesAndIds() {
        AnkiResponse response = getResponse("deckNamesAndIds", "");
        Map<String, Integer> deckNamesAndIds = (HashMap) response.getResult();
        
        return deckNamesAndIds;
    }

    private static void createDeck(String deckName) {
        String currentDecks = getDeckNames().toString().toLowerCase();

        if (currentDecks.contains(deckName.toLowerCase())) {
            System.err.println("Deck already exists.");
        } else {
            String params = "\"deck\": " + "\"" + deckName + "\"";

            AnkiResponse response = getResponse("createDeck", params);
            if (response.getError() != null) {
                System.err.println("Something went wrong:" + response.getError());
            } else {
                System.out.println("Deck '" + deckName + "' sucessfully created.");
            }
        }

    }

    public static void main(String[] args) {
        System.out.println(getDeckNamesAndIds());
    }
}
