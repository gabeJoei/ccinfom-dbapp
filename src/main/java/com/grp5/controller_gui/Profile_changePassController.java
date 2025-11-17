package com.grp5.controller_gui;

import com.grp5.session.AccountSession;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Profile_changePassController {

    @FXML
    private PasswordField newPassField;
    @FXML
    private PasswordField confirmPassField;
    @FXML
    private Button confirmBtn;
    @FXML
    private Button backBtn;

    @FXML
    private void handleBackBtn() {
        System.out.println("Back button pressed!");
        if (AccountSession.isAdmin()) {
            loadNextScene("/com/grp5/view/Admin_settings.fxml", "Dashboard", backBtn);
        }

        if (AccountSession.isUser()) {
            loadNextScene("/com/grp5/view/Profile_settings.fxml", "Dashboard", backBtn);
        }
    }

    private void loadNextScene(String fxmlFile, String title, Button button) {
        try {

            Parent nextSceneRoot = FXMLLoader.load(getClass().getResource(fxmlFile));
            Scene nextScene = new Scene(nextSceneRoot);
            Stage currentStage = (Stage) button.getScene().getWindow();

            currentStage.setScene(nextScene);
            currentStage.setTitle(title);
            currentStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Navigation error", "Could not load the next screen.");
        }
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
