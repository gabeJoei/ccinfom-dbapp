package com.grp5.controller_gui;
import com.grp5.dao.adminDAO;
import com.grp5.model.adminModel;
import com.grp5.session.AccountSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Profile_settingsController {
    @FXML
    private Button changePassBtn;
    @FXML
    private Button updtInfoBtn;
    @FXML
    private Button deleteAccountBtn;
    @FXML
    private Text nameText;
    @FXML
    private Text userIdText;
    
    @FXML
    public void initialize() {
        // Load user data when the scene is initialized
        loadUserData();
    }
    
    private void loadUserData() {
        try {
            if (AccountSession.isLoggedIn()) {
                // Get full name from AccountSession
                String fullName = AccountSession.getAccountFullName();
                nameText.setText(fullName);
                
                // Get account ID from AccountSession
                int accountId = AccountSession.getAccountID();
                userIdText.setText(String.valueOf(accountId));
            } else {
                nameText.setText("N/A");
                userIdText.setText("N/A");
            }
        } catch (Exception e) {
            e.printStackTrace();
            nameText.setText("N/A");
            userIdText.setText("N/A");
            showError("Data Load Error", "Could not load user information.");
        }
    }
    
    @FXML
    void handleChangePassBtn() {
        System.out.println("Change password button clicked!");
        loadNextScene("/com/grp5/view/Profile_changePassword.fxml", "Change Password", changePassBtn);
    }
    
    @FXML
    void handleUpdtInfoBtn() {
        System.out.println("Update Info button clicked!");
        if (AccountSession.isAdmin()) {
            loadNextScene("/com/grp5/view/Profile_updateAdminInfo.fxml", "Update Information", updtInfoBtn);
        } else if (AccountSession.isUser()) {
            loadNextScene("/com/grp5/view/Profile_updateInfo.fxml", "Update Information", updtInfoBtn);
        }
    }
    
    @FXML
    void handleDeleteAccountBtn() {
        System.out.print("Delete account button clicked!");
        loadNextScene("/com/grp5/view/Profile_delete.fxml", "Account Deletion", deleteAccountBtn);
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