package com.grp5.controller_gui;

import com.grp5.session.Admin_session;
import com.grp5.dao.adminDAO;
import com.grp5.model.adminModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Profile_settingsConstroller {
    @FXML
    private Button changePassBtn;
    @FXML
    private Button updtInfoBtn;
    @FXML
    private Button deleteAccountBtn;
    @FXML
    private Button backBtn;

    @FXML
    private void handleBckBtn() {
        System.out.println("Back button clicked!");
        loadNextScene("/com/grp5/view/AdminOrUser.fxml", "Home", backBtn);
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

    // Add this helper so the earlier calls compile
    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
