package com.anrikot.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/main_fxml.fxml"));
        Scene scene = new Scene(root, 500, 600);
        scene.getStylesheets().add(getClass().getResource("/styles/dark-theme.css").toExternalForm());

        primaryStage.setTitle("透読 - ToruYomi");
        primaryStage.setScene(scene);

        primaryStage.show();
        
        showPopup(primaryStage);
    }

    private void showPopup(Stage ownerStage) throws Exception {
        try {
            // Load the popup FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/deck_select_window.fxml"));
            Scene popupScene = new Scene(loader.load());

            // Set up the popup window
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(ownerStage);
            popupStage.setTitle("Information");

            // Show the popup
            popupStage.setScene(popupScene);
            popupStage.showAndWait();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}