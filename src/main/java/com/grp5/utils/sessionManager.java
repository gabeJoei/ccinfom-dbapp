package com.grp5.utils;

import java.io.IOException;

import com.grp5.model.adminModel;
import com.grp5.model.customerRecordModel;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javafx.scene.control.Button;

/**
 * A simple utility class to manage the logged-in user/admin session data.
 */
public class sessionManager {
    private static customerRecordModel loggedInCustomer = null;
    private static adminModel loggedInAdmin = null;

    public static void setLoggedInCustomer(customerRecordModel customer) {
        loggedInCustomer = customer;
        loggedInAdmin = null;
    }

    public static customerRecordModel getLoggedInCustomer() {
        return loggedInCustomer;
    }

    public static void setLoggedInAdmin(adminModel admin) {
        loggedInAdmin = admin;
        loggedInCustomer = null;
    }

    public static adminModel getLoggedInAdmin() {
        return loggedInAdmin;
    }

    public static boolean isAdminLoggedIn() {
        return loggedInAdmin != null;
    }
    
    public static boolean isCustomerLoggedIn() {
        return loggedInCustomer != null;
    }

    public static void clearSession() {
        loggedInCustomer = null;
        loggedInAdmin = null;
    }

    public void performLogout(Button btnLogOut) {
        try {
            // Clear session data
            sessionManager.clearSession();
            System.out.println("Session cleared");

            // Get current stage
            Stage stage = (Stage) btnLogOut.getScene().getWindow();

            // Load AdminOrUser selection screen
            Parent root = FXMLLoader.load(getClass().getResource("/com/grp5/view/AdminOrUser.fxml"));
            Scene scene = new Scene(root);

            // Update stage
            stage.setScene(scene);
            stage.setTitle("CIGE Bike Rentals - Login Selection");

            // Reset window size for login selection
            stage.setMinWidth(600);
            stage.setMinHeight(400);
            stage.setResizable(false);
            stage.centerOnScreen();

            stage.show();

            System.out.println("Logged out successfully");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error during logout: " + e.getMessage());

            // Show error dialog
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Logout Error");
            errorAlert.setHeaderText("Failed to logout");
            errorAlert.setContentText("An error occurred while logging out. Please try again.");
            errorAlert.showAndWait();
        }
    }

}