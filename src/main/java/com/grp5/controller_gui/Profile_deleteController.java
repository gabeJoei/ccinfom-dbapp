package com.grp5.controller_gui;

import com.grp5.dao.adminDAO;
import com.grp5.dao.customerRecordDAO;
import com.grp5.session.AccountSession;
import com.grp5.utils.sessionManager;

import java.util.concurrent.ThreadLocalRandom;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class Profile_deleteController {

    @FXML
    Button backBtn;
    @FXML
    Button confirmBtn;
    @FXML
    PasswordField passwordField;

    @FXML
    void handleConfirmBtn() {
        System.out.println("Confirm Button Clicked!");
        if (isCorrect()) {
            if (AccountSession.isAdmin()) {
                adminDAO dao = new adminDAO();
                int accountId = sessionManager.getLoggedInAdmin().getAdminID();
                dao.deleteAdmin(accountId);
                dao.updateAdminPassword(accountId, generateSimpleRandomString());
                showInfo("Goodbye :(", "Account successfully deleted!");
                AccountSession.cleanSession();
                sessionManager.clearSession();
                loadNextScene("/com/grp5/view/AdminOrUser.fxml", "Pick?", confirmBtn);
            } else if (AccountSession.isUser()) {
                customerRecordDAO dao = new customerRecordDAO();
                int accountId = sessionManager.getLoggedInCustomer().getCustomerAccID();
                dao.delCustomerRecordData(accountId);
                dao.updateCustomerPassword(accountId, generateSimpleRandomString());
                showInfo("Goodbye :(", "Account successfully deleted!");
                AccountSession.cleanSession();
                sessionManager.clearSession();
                loadNextScene("/com/grp5/view/AdminOrUser.fxml", "Pick?", confirmBtn);
            } else {
                System.out.println("Something is wrong here.");
            }
        } else {
            showError("Error", "Incorrect Password!");
        }
    }

    private boolean isCorrect() {
        String inputPass = passwordField.getText();
        String accntPass;
        if (AccountSession.isAdmin()) {
            accntPass = sessionManager.getLoggedInAdmin().getAdminPassword();
            return inputPass.trim().equals(accntPass.trim());
        } else if (AccountSession.isUser()) {
            accntPass = sessionManager.getLoggedInCustomer().getCustomerPass();
            return inputPass.trim().equals(accntPass.trim());
        } else {
            System.out.println("Something is wrong here :/");
        }
        return false;
    }

    @FXML
    private void handleBackBtn() {
        System.out.println("Back button clicked!");
        if (AccountSession.isAdmin()) {
            loadNextScene("/com/grp5/view/Admin_dashBoard.fxml", "Dashboard", backBtn);
        } else if (AccountSession.isUser()) {
            loadNextScene("/com/grp5/view/User_userMenu.fxml", "Dashboard", backBtn);
        } else {
            System.out.println("There is an error around here :P");
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

    private void showInfo(String header, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Success");
        a.setHeaderText(header);
        a.setContentText(content);
        a.showAndWait();
    }

    private String generateSimpleRandomString() {
        int length = 20;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = ThreadLocalRandom.current().nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }
}
