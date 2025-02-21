package com.anrikot.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anrikot.services.ankiconnect.AnkiConnectService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AnkiCardController {

    MainController mainController;

    @FXML
    TextField kanjiField;
    @FXML
    TextField readingsField;
    @FXML
    TextArea meaningsField;
    @FXML
    TextArea examplesField;
    @FXML
    Button saveBtn;
    @FXML
    Button cancel;

    @FXML
    public void saveNote(ActionEvent event) {
        Map<String, String> fields = new HashMap<>();
        fields.put("Kanji", kanjiField.getText());
        fields.put("Reading", readingsField.getText());
        fields.put("Meaning", meaningsField.getText());
        fields.put("Examples", examplesField.getText().replace("\n", "<br>"));

        try {
            AnkiConnectService.createNote("KanjiCon",
                    "KanjiTest",
                    fields, List.of(""));

            mainController.updateTextArea("Note created successfully");
            close(event);

        } catch (Exception e) {
            mainController.updateTextArea("Somethings went wrong: " + e.getMessage());
        }

    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainController.ankiCardController = null;
        stage.close();
    }

    public void setData(String kanji, String readings, String meanings, String examples, MainController mainController) {
        kanjiField.setText(kanji);
        readingsField.setText(readings);
        meaningsField.setText(meanings);
        examplesField.setText(examples);

        this.mainController = mainController;
    }

}
