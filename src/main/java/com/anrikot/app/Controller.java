package com.anrikot.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anrikot.services.ankiconnect.AnkiConnectService;
import com.anrikot.services.jisho.JishoService;
import com.anrikot.services.jisho.JishoWord;
import com.anrikot.services.tatoeba.Sentence;
import com.anrikot.services.tatoeba.TatoebaService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
    JishoWord kanjiDef = null;
    List<Sentence> sentences = null;

    @FXML
    private Label titleLabel;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private Button addButton;
    @FXML
    private TextArea outputArea;

    @FXML
    private void onSearch() {
        outputArea.setText("");
        String kanji = searchField.getText().trim();
        if (!kanji.isEmpty()) {
            kanjiDef = JishoService.searchWord(kanji);
            sentences = TatoebaService.searchSentences(kanji);

            outputArea.appendText("Searching for: " + kanji + "\n");
            outputArea.appendText("Readings: ");
            outputArea.appendText(kanjiDef.getReadings().toString() + "\n");
            outputArea.appendText("Meanings: \n");
            outputArea.appendText(kanjiDef.getMeanings().toString() + "\n");
            for (Sentence sentence : sentences) {
                outputArea.appendText("Ex: " + sentence.getTranscription() + "\n");
                outputArea.appendText("Tl: " + sentence.getTranslation() + "\n");
            }
        }
    }

    @FXML
    private void onAdd() {
        String kanji = searchField.getText().trim();
        if (!kanji.isEmpty()) {
            if (kanjiDef == null && sentences == null) {
                outputArea.setText("Search for a kanji first.");
            } else {
                outputArea.setText("Adding: " + kanji + " to Anki\n");
                outputArea.appendText(createNote(kanji));

            }
        }
    }

    private String createNote(String kanji) {
        Map<String, String> fields = new HashMap<>();
        fields.put("Kanji", kanji);
        fields.put("Reading", kanjiDef.getReadings().toString());
        fields.put("Meaning", kanjiDef.getMeanings().toString());

        List<String> theSentences = new ArrayList<>();
        theSentences.add(sentences.get(0).getTranscription() + "\n");
        theSentences.add(sentences.get(0).getTranslation() + "\n");
        theSentences.add(sentences.get(1).getTranscription() + "\n");
        theSentences.add(sentences.get(1).getTranslation() + "\n");
        fields.put("Examples", theSentences.toString());

        try {
            AnkiConnectService.createNote("KanjiCon",
            "KanjiTest",
            fields, List.of(""));

            return "Note created successfully!";

        } catch (Exception e) {
            return "Something went wrong. " + e.getMessage();
        }

        
    }

}
