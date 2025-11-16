package com.grp5.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class AdminOrUserControllerv2 {

    @FXML
    private Button btnAdmin2;
    @FXML
    private Button btnUser2;

    @FXML
    private void handleAdminClick(MouseEvent e) {
        System.out.println("Admin Clicked");
        loadNextScene("AdminLogIn", "Admin Log In", btnAdmin2);
    }

    @FXML
    private void handleUserClick(MouseEvent e) {
        System.out.println("User Clicked");
        loadNextScene("UserLogIn", "User Log In", btnUser2);
    }

    private void loadNextScene(String fxmlFile, String title, Button button) {
        try {

            Parent nextSceneRoot = FXMLLoader.load(getClass().getResource(fxmlFile)); // Get the resources :D
            Scene nextScene = new Scene(nextSceneRoot); // Set - up new scene :D
            Stage currentStage = (Stage) button.getScene().getWindow();

            currentStage.setScene(nextScene);
            currentStage.setTitle(title);
            currentStage.show();

        } catch (IOException e) {
            e.getStackTrace();
            System.err.println("Tite");
        }
    }

}
