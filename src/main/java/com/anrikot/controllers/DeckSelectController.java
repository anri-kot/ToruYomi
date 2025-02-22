package com.anrikot.controllers;

import java.util.ArrayList;
import java.util.List;

import com.anrikot.services.ankiconnect.AnkiConnectService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class DeckSelectController {

    private static String selectedOption = "ToruYomi";
    private static List<String> decks; 
    
    @FXML private ChoiceBox<String> choiceBox;
    @FXML private Button reconnectButton;
    @FXML private Button okButton;
    @FXML private Button cancelButton;

    @FXML
    public void initialize() {
        populateChoiceBox();
    }

    // OK button action
    @FXML
    private void handleOk() {
        selectedOption = choiceBox.getValue();
        if (!decks.contains(selectedOption)) {
            AnkiConnectService.createDefaultDeck();
        }
        closeWindow();
    }

    // Cancel button action
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    @FXML
    private void handleReconnect() {
        populateChoiceBox();
    }

    // Close the window
    private void closeWindow() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    private void populateChoiceBox() {
        List<String> options = new ArrayList<>();
        try {
            decks = AnkiConnectService.getDeckNames();

            if (!decks.contains("ToruYomi")) {
                options.add("ToruYomi");
            }

            options.addAll(decks);    
        } catch (Exception e) {}

        if (!options.contains("ToruYomi")) {
            options.add("ToruYomi");
        }

        choiceBox.setItems(FXCollections.observableArrayList(options));
        choiceBox.setValue(getSelectedDeck());
    }

    public static String getSelectedDeck() {
        return selectedOption;
    }
    
}
