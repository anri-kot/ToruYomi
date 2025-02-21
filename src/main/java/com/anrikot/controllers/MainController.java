package com.anrikot.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anrikot.services.ankiconnect.AnkiConnectService;
import com.anrikot.services.jisho.JishoService;
import com.anrikot.services.jisho.JishoWord;
import com.anrikot.services.tatoeba.Sentence;
import com.anrikot.services.tatoeba.TatoebaService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainController {
    String kanji = "";
    JishoWord kanjiDef = null;
    List<Sentence> sentences = null;

    AnkiCardController ankiCardController;

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
        outputArea.setText("Search for a word!");

        kanji = searchField.getText().trim();
        if (!kanji.isEmpty()) {
            kanjiDef = JishoService.searchWord(kanji);
            sentences = TatoebaService.searchSentences(kanji);

            StringBuilder textLog = new StringBuilder();

            textLog.append("Searching for: " + kanji + "\n\n");
            
            if (kanjiDef != null) {

                textLog.append("Readings: \n");
                textLog.append(kanjiDef.getReadings().toString() + "\n");
                textLog.append("Meanings: \n");
                textLog.append(kanjiDef.getMeanings().toString() + "\n\n");
            } else {
                textLog.append("No definition found in Jisho for '" + kanji + "'\n");
            }

            if (sentences != null) {
                textLog.append("Example sentences:\n");
                for (Sentence sentence : sentences) {
                    textLog.append("EX: " + sentence.getTranscription() + "\n");
                    textLog.append("TL: " + sentence.getTranslation() + "\n");
                }
            } else {
                textLog.append("No example sentences found in Tatoeba for '" + kanji + "'");
            }

            updateTextArea(textLog.toString());
        }
    }

    @FXML
        private void onAdd() {
            if (!AnkiConnectService.isActive()) {
                outputArea.setText("Could not connect to AnkiConnect.");
            }

            String readings = "";
            String meanings = "";
            String examples = "";
    
            if (!kanji.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                
                readings = kanjiDef.getReadings().toString().replaceAll("(^\\[|\\]$)", "");
                meanings = kanjiDef.getMeanings().toString().replaceAll("(^\\[|\\]$)", "");;
                
                if (sentences != null) {
                    for (Sentence sentence : sentences) {
                        sb.append("EX: " + sentence.getTranscription() + "\n");
                        sb.append("TL: " + sentence.getTranslation() + "\n");
                    }
                    
                    examples = sb.toString();
                }
    
                openAnkiNoteWindow(kanji, readings, meanings, examples);
            } else {
                outputArea.setText("Search for a word to automatically fill the note fields!");
                openAnkiNoteWindow(kanji, readings, meanings, examples);
            }
        }

    public void updateTextArea(String update) {
        outputArea.setText(update);
    }

    private void openAnkiNoteWindow(String kanji, String readings, String meanings, String examples) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ankicardwindow.fxml"));
            Parent root = loader.load();

            if (ankiCardController != null) {
                ankiCardController.setData(kanji, readings, meanings, examples, this);
            } else {
                ankiCardController = loader.getController();
                ankiCardController.setData(kanji, readings, meanings, examples, this);
    
                Stage stage = new Stage();
                stage.setTitle("Add Card");
                stage.setScene(new Scene(root, 500, 650));
                stage.setX(1280);
                stage.show();

                stage.setOnCloseRequest((event) -> {
                    ankiCardController = null;
                });
            }
        } catch (IOException e) {
            outputArea.setText("Something went wrong: " + e.getMessage());
        }
    }

}
