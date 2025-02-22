package com.anrikot.services.ankiconnect;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AnkiConnectService {

    private static final String ANKI_CONNECT_URL = "http://127.0.0.1:8765";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static boolean isActive() {
        boolean isActive = false;
        try {
            URI uri = new URI(ANKI_CONNECT_URL);
            URL url = uri.toURL();

            // Open connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            
            conn.connect();

            isActive = true;

        } catch (Exception e) {}

        return isActive;

    }

    private static AnkiResponse sendRequest(String action, Map<String, Object> params) {
        try {
            // Convert request to JSON
            String jsonRequest = OBJECT_MAPPER.writeValueAsString(Map.of(
                "action", action,
                "params", params,
                "version", 6
            ));

            URI uri = new URI(ANKI_CONNECT_URL);
            URL url = uri.toURL();

            // Open connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonRequest.getBytes());
            }

            // Read response
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("Bad response: " + responseCode);
            }

            AnkiResponse response = OBJECT_MAPPER.readValue(conn.getInputStream(), AnkiResponse.class);
            conn.disconnect();
            return response;

        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON Processing Error: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("Connection Error: " + e.getMessage(), e);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI/URL: " + e.getMessage(), e);
        }
    }

    // Anki actions

    public static List<String> getDeckNames() {
        AnkiResponse response = sendRequest("deckNames", Map.of());
        return response.getResultAsList(String.class);
    }

    public static Map<String, Integer> getDeckNamesAndIds() {
        AnkiResponse response = sendRequest("deckNamesAndIds", Map.of());
        return response.getResultAsMap(String.class, Integer.class);
    }

    public static void createDeck(String deckName) {
        // Validate deck name
        String current = getDeckNames().toString().toLowerCase();
        if (current.contains(deckName.toLowerCase())) {
            throw new RuntimeException("Deck already exists.");
        }

        AnkiResponse response = sendRequest("createDeck", Map.of("deck", deckName));
        if (response.getError() != null) {
            throw new RuntimeException("Could not create deck: " + response.getError());
        } else {
            System.out.println("Deck '" + deckName + "' successfully created.");
        }
    }

    public static AnkiResponse getModelsByName(String modelName) {
        AnkiResponse response = sendRequest("findModelsByName", Map.of("modelNames", List.of(modelName)));
        return response;
    }

    public static void createModel(String modelName, List<String> inOrderFields, List<Object> cardTemplates) {
        Map<String, Object> cardModel = new HashMap<>();
        cardModel.put("modelName", modelName);
        cardModel.put("inOrderFields", inOrderFields);
        cardModel.put("cardTemplates", cardTemplates);

        AnkiResponse response = sendRequest("createModel", cardModel);
        if (response.getError() != null) {
            throw new RuntimeException("Could not create model: " + response.getError());
        } else {
            System.out.println("Model sucessfully created.");
        }
    }

    public static void createNote(String deckName, String modelName, Map<String,String> fields, List<String> tags) {

        Map<String, Object> note = new HashMap<>();
        note.put("deckName", deckName);
        note.put("modelName", modelName);
        note.put("fields", fields);
        note.put("options", Map.of("allowDuplicate", false));
        note.put("tags", tags);

        AnkiResponse response = sendRequest("addNote", Map.of("note", note));
        if (response.getError() != null) {
            throw new RuntimeException("Could not create note: " + response.getError());
        } else {
            System.out.println("Note successfully created!");
        }

    }

    // Default deck
    public static void createDefaultDeck() {
        String deckName = "ToruYomi";
        String modelName = "ToruYomi";

        List<String> fields = new ArrayList<>();

        fields.add("Word");
        fields.add("Readings");
        fields.add("Meanings");
        fields.add("Examples");

        String backHTML = """
                <div id='word'>{{Word}}</id>
                <hr>
                <div id='readings'>{{Readings}}</div>
                <hr>
                <div id='meanings'>{{Meanings}}</div>
                <hr>
                <details style="text-align:left">
                <summary> Examples: </summary>
                {{Examples}}
                """;

        Map<String, String> cardTemplate = new HashMap<>();
        cardTemplate.put("Front", "<div id='word'>{{Word}}</id>");
        cardTemplate.put("Back", backHTML);

        CompletableFuture.runAsync(() -> 
            createModel(modelName, fields, List.of(cardTemplate)))
            .thenRun(() -> createDeck(deckName))
            .exceptionally(e -> {
                throw new RuntimeException("Could not create ToruYomi deck: " + e.getMessage());
            });
    }

    public static void main(String[] args) {
        createDefaultDeck();
    }
}