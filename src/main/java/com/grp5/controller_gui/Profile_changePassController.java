package com.grp5.controller_gui;

import com.grp5.dao.adminDAO;
import com.grp5.dao.customerRecordDAO;
import com.grp5.session.AccountSession;
import com.grp5.utils.sessionManager;
import com.grp5.model.adminModel;
import com.grp5.model.customerRecordModel;

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
    private PasswordField oldPassField;
    @FXML
    private PasswordField newPassField;
    @FXML
    private PasswordField confirmPassField;
    @FXML
    private Button confirmBtn;
    @FXML
    private Button backBtn;

    @FXML
    private void handleConfirmBtn() {
        if (AccountSession.isAdmin()) {
            adminModel admin = sessionManager.getLoggedInAdmin();
            String oldPass = oldPassField.getText();
            if (oldPass.trim().equals(admin.getAdminPassword().trim())) {
                if (passwordsMatch()) {
                    adminDAO dao = new adminDAO();
                    admin.setAdminPassword(newPassField.getText());
                    dao.updateAdminPassword(admin.getAdminID(), newPassField.getText());
                } else {
                    showError("Error", "Passwords doesn't match!");
                }
            } else {
                showError("Error", "Incorrect current password!");
            }
        } else if (AccountSession.isUser()) {
            customerRecordModel customer = sessionManager.getLoggedInCustomer();
            String oldPass = oldPassField.getText();
            if (oldPass.trim().equals(customer.getCustomerPass().trim())) {
                if (passwordsMatch()) {
                    customerRecordDAO dao = new customerRecordDAO();
                    customer.setCustomerPass(newPassField.getText());
                    dao.updateCustomerPassword(customer.getCustomerAccID(), newPassField.getText());
                } else {
                    showError("Error", "Passwords doesn't match!");
                }
            } else {
                showError("Error", "Incorrect current password!");
            }
        } else {
            System.out.printf("Something is wrong here :/");
        }
    }

    @FXML
    private void handleBackBtn() {
        System.out.println("Back button pressed!");
        if (AccountSession.isAdmin()) {
            loadNextScene("/com/grp5/view/Admin_dashBoard.fxml", "Dashboard", backBtn);
        }

        if (AccountSession.isUser()) {
            loadNextScene("/com/grp5/view/User_userMenu.fxml", "Dashboard", backBtn);
        }
    }

    private boolean passwordsMatch() {
        String newPass = newPassField.getText();
        String confirmPass = confirmPassField.getText();
        return newPass != null && confirmPass != null && newPass.trim().equals(confirmPass.trim());
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
