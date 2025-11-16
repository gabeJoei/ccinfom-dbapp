package com.grp5.controller_gui;

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

public class Admin_logInController {

    @FXML
    private Button logInBtn;
    @FXML
    private Button bckBtn;

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private final adminDAO dao = new adminDAO();

    private void handleAdminLogIn() {
        String username = emailField.getText() != null ? emailField.getText().trim() : "";
        String password = passwordField.getText() != null ? passwordField.getText() : "";

        // check if either of the fields are empty
        if (username.isEmpty() || password.isEmpty()) {
            showError("Missing info", "Please enter both email and password.");
            return;
        }

        adminModel admin = dao.authenticateAdmin(username, password);
        if (admin != null) {
            loadNextScene("/com/grp5/view/Admin_dashBoard.fxml", "Admin Dashboard", logInBtn);
        } else {
            showError("Login failed", "Invalid username or password.");
        }

    }

    private void handleBckBtn() {
        System.out.println("Back button clicked!");
        loadNextScene("/com/grp5/view/AdminOrUser.fxml", "Home", bckBtn);
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
            e.getStackTrace();
            showError("Navigation error", "Could not load the next screen.");
        }
    }

}
