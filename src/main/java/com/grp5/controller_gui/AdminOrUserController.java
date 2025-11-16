package com.grp5.controller_gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class AdminOrUserController {

    @FXML
    private Button btnAdmin2;
    @FXML
    private Button btnUser2;

    @FXML
    private void handleAdminClick(ActionEvent e) {
        System.out.println("Admin Clicked");
        loadNextScene("/com/grp5/view/Admin_login.fxml", "Admin Log In", btnAdmin2);
    }

    @FXML
    private void handleUserClick(ActionEvent e) {
        System.out.println("User Clicked");
        loadNextScene("/com/grp5/view/User_logIn.fxml", "User Log In", btnUser2);
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
            System.err.println(e);
        }
    }

}
