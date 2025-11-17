package com.grp5.controller_gui;

import com.grp5.dao.adminDAO;
import com.grp5.dao.customerRecordDAO;
import com.grp5.model.adminModel;
import com.grp5.model.customerRecordModel;
import com.grp5.session.AccountSession;
import com.grp5.utils.sessionManager;

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

public class Profile_updateInfoController {

    @FXML
    private TextField newEmailField;
    @FXML
    private TextField newContctField;
    @FXML
    PasswordField passwordField;
    @FXML
    private Button confirmBtn;
    @FXML
    private Button backBtn;

    @FXML
    private void handleConfirmBtn() {
        System.out.println("Confirm button clicked!");
        String newEmail = null;
        String newContactNo = null;

        if (newEmailField != null) {
            newEmail = newEmailField.getText().trim();
            newEmail = newEmail.isEmpty() ? null : newEmail;
        }

        if (newContctField != null) {
            newContactNo = newContctField.getText().trim();
            newContactNo = newContactNo.isEmpty() ? null : newContactNo;
        }

        int flag = -1;

        if (newEmail == null && newContactNo == null) {
            showError("Error!", "Fields are blank!");
        }

        if (newEmail != null && newContactNo != null) { // both are available
            flag = 3;
        } else if (newEmail != null && newContactNo == null) { // only email is available
            flag = 2;
        } else if (newEmail == null && newContactNo != null) { // only contact is available
            flag = 1;
        }

        if (AccountSession.isAdmin()) {
            if (isAdminPasswordCorrect()) {
                adminDAO dao = new adminDAO();
                adminModel admin = sessionManager.getLoggedInAdmin();
                if (newEmail != null) {
                    admin.setAdminEmail(newEmail);
                    dao.updateAdmin(admin);
                    if (newEmailField != null)
                        newEmailField.clear();
                    if (passwordField != null)
                        passwordField.clear();
                    showInfo("Success", "Email successfully updated!");
                } else {
                    passwordField.clear();
                    showError("Error!", "Incorrect Password!");
                }
            } else {
                passwordField.clear();
                showError("Incorrect Password", "Input correct password!");
            }
        } else if (AccountSession.isUser()) {
            if (isUserPasswordCorrect()) {
                customerRecordDAO dao = new customerRecordDAO();
                customerRecordModel user = sessionManager.getLoggedInCustomer();
                switch (flag) {
                    case 1: // Only new contact num is available
                        user.setPhoneNum(newContactNo);
                        dao.modifyCustomerRecordData(user);
                        if (newContctField != null)
                            newContctField.clear();
                        if (passwordField != null)
                            passwordField.clear();
                        showInfo("Success", "Phone number successfully updated!");
                        break;
                    case 2: // Only new email is avail.able
                        user.setEmail(newEmail);
                        dao.modifyCustomerRecordData(user);
                        if (newEmailField != null)
                            newEmailField.clear();
                        if (passwordField != null)
                            passwordField.clear();
                        showInfo("Success", "Email successfully updated!");
                        break;
                    case 3: // Both are available
                        user.setPhoneNum(newContactNo);
                        user.setEmail(newEmail);
                        dao.modifyCustomerRecordData(user);
                        if (newContctField != null)
                            newContctField.clear();
                        if (newEmailField != null)
                            newEmailField.clear();
                        if (passwordField != null)
                            passwordField.clear();
                        showInfo("Success", "Email and phone number successfully updated!");
                    default:
                        break;
                }
            } else {
                passwordField.clear();
                showError("Incorrect Password", "Input correct password!");
            }
        } else {
            System.out.println("There is an error around here :/");
        }
    }

    private boolean isAdminPasswordCorrect() {
        String password = passwordField.getText();
        return sessionManager.getLoggedInAdmin().getAdminPassword().trim().equals(password.trim());
    }

    private boolean isUserPasswordCorrect() {
        String password = passwordField.getText();
        return sessionManager.getLoggedInCustomer().getCustomerPass().trim().equals(password.trim());
    }

    @FXML
    private void handleBackBtn() {
        System.out.println("Back button clicked!");
        if (AccountSession.isAdmin()) {
            loadNextScene("/com/grp5/view/Admin_dashBoard.fxml", "Dashboard", backBtn);
        } else if (AccountSession.isUser()) {
            loadNextScene("/com/grp5/view/User_userMenu.fxml", "Dashboard", backBtn);
        } else {
            System.out.println("There is an error around here :/");
        }
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

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String header, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Success");
        a.setHeaderText(header);
        a.setContentText(content);
        a.showAndWait();
    }
}
