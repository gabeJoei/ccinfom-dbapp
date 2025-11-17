package com.grp5.controller_gui;

import com.grp5.dao.adminDAO;
import com.grp5.model.adminModel;
import com.grp5.session.AccountSession;
import com.grp5.session.ProfileSnapshot;
import com.grp5.session.AccountSession.AccountType;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Admin_logInController {

    @FXML
    private Button logInBtn;
    @FXML
    private Button backBtn;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private final adminDAO dao = new adminDAO();

    @FXML
    private void handleAdminLogIn() {
        String username = usernameField.getText() != null ? usernameField.getText().trim() : "";
        String password = passwordField.getText() != null ? passwordField.getText() : "";

        // check if either of the fields are empty
        if (username.isEmpty() || password.isEmpty()) {
            showError("Missing info", "Please enter both username and password.");
            return;
        }

        adminModel admin = dao.authenticateAdmin(username, password);
        // Admin_session.setCurrentAdmin(admin); // Current admin bro :D
        if (admin != null) {

            ProfileSnapshot snap = new ProfileSnapshot(admin.getAdminFirstName(), admin.getAdminLastName(),
                    admin.getAdminEmail());

            AccountSession.setAccount(AccountType.ADMIN, admin.getAdminID(), snap);

            loadNextScene("/com/grp5/view/Admin_dashBoard.fxml", "Admin Dashboard", logInBtn);
        } else {
            showError("Login failed", "Invalid username or password.");
        }

    }

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

            javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            
     

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
