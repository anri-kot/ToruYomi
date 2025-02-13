package com.anrikot.controllers;

import com.anrikot.services.AnkiConnectService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PrimaryController extends Stage {

    @FXML
    private Text decks;

    @FXML
    private TextField txtField;

    @FXML
    private Button getDecksBtn;

    @FXML
    private Button createDeckBtn;

    @FXML
    public void initialize() {
        getDecksBtn.setOnAction(event -> decks.setText(AnkiConnectService.getDeckNames().toString()));

        createDeckBtn.setOnAction(event -> {
            String deckName = txtField.getText();
            AnkiConnectService.createDeck(deckName);
            decks.setText(deckName + " created successfully.");
        });
    }

}
