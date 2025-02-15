package com.anrikot.services.tatoeba;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TatoebaService {
    // %s - format string
    private static final String TATOEBA_URL = "https://tatoeba.org/ja/api_v0/search?from=jpn&query=%s&to=eng"; 
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static List<Sentence> searchSentences(String query) {

        try {
            String encodedQuery = URLEncoder.encode("\"" + query + "\"", StandardCharsets.UTF_8);
            String request = String.format(TATOEBA_URL, encodedQuery);

            // Open connection
            URI uri = new URI(request);
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("Bad response: " + responseCode);
            }

            // Get response
            JsonNode results = OBJECT_MAPPER.readTree(conn.getInputStream())
                    .get("results");
            conn.disconnect();

            if (results.isEmpty()) {
                return null;
            } else {

                List<Sentence> sentences = getSentences(results);
                return sentences;
            }

        } catch (IOException e) {
            throw new RuntimeException("Connection Error: " + e.getMessage(), e);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI/URL: " + e.getMessage(), e);
        }
    }

    private static List<Sentence> getSentences(JsonNode results) {
        String japaneseSentence;
        String translation;
        String transcription;

        JsonNode result;
        List<Sentence> sentences = new ArrayList<>();

        // Handle JSON
        for (int i = 0; i < results.size(); i++) {
            result = results.get(i);

            if (result.isEmpty()) {
                continue;
            }

            try {
                japaneseSentence = result.get("text")
                        .asText();
                translation = result.get("translations")
                        .get(0)
                        .get(0)
                        .get("text").asText();
                transcription = result.get("transcriptions")
                        .get(0)
                        .get("text")
                        .asText();

                sentences.add(new Sentence(japaneseSentence, translation, transcription));

            } catch (NullPointerException e) {
                continue;
            }
        }
        return sentences;
    }
}