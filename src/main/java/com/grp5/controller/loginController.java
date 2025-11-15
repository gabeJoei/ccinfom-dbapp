package com.grp5.controller;

import com.grp5.dao.customerRecordDAO;
import com.grp5.model.customerRecordModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for Login Screen
 * Handles user authentication and navigation
 */
public class loginController {
    
    @FXML
    private TextField txtUsername;
    
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private Label lblMessage;
    
    @FXML
    private Button btnLogin;
    
    @FXML
    private Button btnSignUp;
    
    private customerRecordDAO customerDAO;
    private String userType = "user"; // Default to user, can be set from previous screen
    
    /**
     * Initialize method
     */
    
    @FXML
    public void initialize() {
        customerDAO = new customerRecordDAO();
        System.out.println("loginController initialized");
        lblMessage.setVisible(false);
        // Clear message on focus
        txtUsername.setOnMouseClicked(e -> lblMessage.setText(""));
        System.out.println("Connected");
        txtPassword.setOnMouseClicked(e -> lblMessage.setText(""));
        System.out.println("Connected");
        
        // Enter key to login
        txtPassword.setOnAction(e -> handleLogin());
    }
    
    /**
     * Set user type (admin or user) from previous screen
     */
    public void setUserType(String userType) {
        this.userType = userType;
        System.out.println("User type set to: " + userType);
    }
    
    /**
     * Handle login button click
     */
    @FXML
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        
        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter both username and password", "error");
            return;
        }
        
        // Attempt login
        if (authenticateUser(username, password)) {
            showMessage("Login successful!", "success");
            
            // Wait a moment then navigate
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    javafx.application.Platform.runLater(this::navigateToDashboard);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            
        } else {
            showMessage("Invalid username or password", "error");
            txtPassword.clear();
        }
    }
    
    /**
     * Authenticate user credentials
     */
    private boolean authenticateUser(String username, String password) {
        if (userType.equals("admin")) {
            return username.equals("admin") && password.equals("admin123");
        }
        
        // Customer authentication
        customerRecordModel customer = customerDAO.authenticateCustomer(username, password);
        
        if (customer != null) {
            // Store logged-in customer info for later use
            System.out.println("Logged in as: " + customer.getFirstName() + " " + customer.getLastName());
            return true;
        }
        
        return false;
    }
        
    /**
     * Navigate to dashboard based on user type
     */
    private void navigateToDashboard() {
        try {
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            Parent root;
            String title;
            
            if (userType.equals("admin")) {
                // Load admin dashboard
                root = FXMLLoader.load(getClass().getResource("/com/grp5/view/RentBike.fxml"));
                title = "Admin Dashboard";
            } else {
                // Load user main page
                root = FXMLLoader.load(getClass().getResource("/com/grp5/view/RentBike.fxml"));
                title = "User Dashboard";
            }
            
            Scene scene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Error loading dashboard", "error");
        }
    }
    
    /**
     * Handle sign-up button click
     */
    @FXML
    private void handleSignUp() {
        try {
            Stage stage = (Stage) btnSignUp.getScene().getWindow();
            
            // Load sign-up page
            Parent root = FXMLLoader.load(getClass().getResource("/com/grp5/view/RentBike.fxml"));
            
            Scene scene = new Scene(root);
            stage.setTitle("Sign Up");
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Error loading sign-up page", "error");
        }
    }
    
    /**
     * Show message to user
     */
    private void showMessage(String message, String type) {
        lblMessage.setVisible(true);
        lblMessage.setText(message);
        
        if (type.equals("success")) {
            lblMessage.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
        } else if (type.equals("error")) {
            lblMessage.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        } else {
            lblMessage.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
        }
    }
}