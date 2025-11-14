package com.grp5.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for Admin or User Selection Screen
 * Allows user to choose between Admin and User interfaces
 */
public class adminOrUserController {
    
    @FXML
    private Button btnAdmin;
    
    @FXML
    private Button btnUser;
    
    /**
     * Initialize method - called automatically by JavaFX
     */
    @FXML
    public void initialize() {
        System.out.println("adminOrUserController initialized");
    }
    
    /**
     * Handle Admin button click
     * Navigates to Admin login/dashboard
     */
    @FXML
    private void handleAdminClick() {
        System.out.println("Admin button clicked");
        
        try {
            // Load admin login or admin dashboard
            Stage stage = (Stage) btnAdmin.getScene().getWindow();
            
            // Try to load login.fxml or AdminSideController
            Parent root = FXMLLoader.load(getClass().getResource("/com/grp5/view/login.fxml"));
            
            Scene scene = new Scene(root);
            stage.setTitle("Admin Login");
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                     "Failed to load Admin interface: " + e.getMessage());
        }
    }
    
    /**
     * Handle User button click
     * Navigates to User login/dashboard
     */
    @FXML
    private void handleUserClick() {
        System.out.println("User button clicked");
        
        try {
            // Load user main page or user login
            Stage stage = (Stage) btnUser.getScene().getWindow();
            
            // Try to load userMainPage.fxml or login
            Parent root = FXMLLoader.load(getClass().getResource("/com/grp5/view/userMainPage.fxml"));
            
            Scene scene = new Scene(root);
            stage.setTitle("User Dashboard");
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                     "Failed to load User interface: " + e.getMessage());
        }
    }
    
    /**
     * Show alert dialog
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}