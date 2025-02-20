package com.anrikot.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class DeckSelectController {

    @FXML private ChoiceBox<String> choiceBox;
    @FXML private Button okButton;
    @FXML private Button cancelButton;

    // OK button action
    @FXML
    private void handleOk() {
        String selectedOption = choiceBox.getValue();
        System.out.println("Selected option: " + selectedOption);
        closeWindow();
    }

    // Cancel button action
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    // Close the window
    private void closeWindow() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
    
}
