package com.anrikot.services.jisho;

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

public class JishoService {
    private static String JISHO_URL = "https://jisho.org/api/v1/search/words?keyword=%s"; // %s - format string
    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static JishoWord searchWord(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String request = String.format(JISHO_URL, encodedQuery);
            System.out.println(request);

            // Open connection
            URI uri = new URI(request);
            URL url = uri.toURL();

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new IOException("Bad response: " + responseCode);
            }

            // Get response
            JsonNode data = OBJECT_MAPPER.readTree(conn.getInputStream()).get("data");
            conn.disconnect();

            if (data.isEmpty()) {
                return null;
            } else {

                // Get readings
                List<String> readings = getReadings(data, query);
                // Get meanings
                List<String> meanings = getMeanings(data, query);

                return new JishoWord(query, readings, meanings);
            }

        } catch (IOException e) {
            throw new RuntimeException("Connection Error: " + e.getMessage(), e);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI/URL: " + e.getMessage(), e);
        }
    }

    // The slug check is not necessary if your query has quotation marks. Ex: keyword="上"

    private static List<String> getReadings(JsonNode data, String word) {
        List<String> readings = new ArrayList<>();
        JsonNode result;
        String slug;

        // iterates through every object in the 'data' node
        for (int j = 0; j < data.size(); j ++) {
            JsonNode japaneseNode = data.get(j).get("japanese");
            slug = data.get(j).get("slug").asText();

            // checks if the query word is the same as the result (won't add compound words)
            if (slug.startsWith(word) || slug.startsWith(word + "-")) {
                // iterate through 'japanese' node
                for (int i = 0; i < japaneseNode.size(); i++) {
                    result = japaneseNode.get(i);
                    if (result.isEmpty()) {
                        continue;
                    }

                    // !result.has("word") is here because some kanji don't have the 'word' field for some reason, but the reading SHOULD match the query
                    if (result.get("word").asText().equals(word) || !result.has("word")) {
                        readings.add(result.get("reading").asText());
                    }
                }

            } else {
                break;
            }
        }
        return readings;
    }

    private static List<String> getMeanings(JsonNode data, String word) {
        List<String> meanings = new ArrayList<>();
        
        // iterates through every object in the 'data' node
        for (int j = 0; j < data.size(); j++) {
            String slug;
            slug = data.get(j).get("slug").asText();

            // checks if the query word is the same as the result (won't add compound words)
            if (slug.startsWith(word) || slug.startsWith(word + "-")) {
                JsonNode englishDefinitionsNode = data.get(j).get("senses").get(0).get("english_definitions");
                
                for (int i = 0; i < englishDefinitionsNode.size(); i++) {
                    meanings.add(englishDefinitionsNode.get(i).asText());
                }
            } else {
                break;
            }

        }
        return meanings;
    }

    public static void main(String[] args) {
        System.out.println(searchWord("相"));
    }

}
