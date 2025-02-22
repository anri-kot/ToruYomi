package com.anrikot.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anrikot.services.ankiconnect.AnkiConnectService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AnkiCardController {

    private MainController mainController;
    private Stage stage;
    private boolean isDeckAvailable = false;

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
    void initialize() {
        isDeckAvailable = isDeckAvailable();
        
        if (!isDeckAvailable) {
            openSettings(stage);
        }
    }

    @FXML
    public void saveNote(ActionEvent event) {
        Map<String, String> fields = new HashMap<>();
        fields.put("Word", kanjiField.getText());
        fields.put("Readings", readingsField.getText());
        fields.put("Meanings", meaningsField.getText());
        fields.put("Examples", examplesField.getText().replace("\n", "<br>"));

        try {
            String currentDeck = getCurrentDeck();
            AnkiConnectService.createNote(currentDeck, currentDeck, fields, List.of(""));

            mainController.updateTextArea("Note created successfully");
            close(event);

        } catch (Exception e) {
            mainController.updateTextArea("Somethings went wrong: " + e.getMessage());
        }
    }

    @FXML
    private void close(ActionEvent event) {
        mainController.ankiCardController = null;
        stage.close();
    }

    public void setData(String kanji, String readings, String meanings, String examples, MainController mainController) {
        kanjiField.setText(kanji);
        readingsField.setText(readings);
        meaningsField.setText(meanings);
        examplesField.setText(examples);
    }

    public void setOwner(MainController mainController) {
        this.mainController = mainController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private static boolean isDeckAvailable() {
        if (!AnkiConnectService.getDeckNames().contains(getCurrentDeck())) {
            return false;
        } else {
            return true;
        }
    }

    private void openSettings(Stage ownerStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/deck_select_window.fxml"));
            Scene popupScene = new Scene(loader.load());

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(ownerStage);
            popupStage.setTitle("Settings");

            popupStage.setScene(popupScene);
            popupStage.showAndWait();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
        
    private static String getCurrentDeck() {
        return DeckSelectController.getSelectedDeck();
    }

}
