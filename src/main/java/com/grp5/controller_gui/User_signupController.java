package com.grp5.controller_gui;

import com.grp5.dao.customerRecordDAO;
import com.grp5.model.customerRecordModel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Pattern;

public class User_signupController {

    @FXML
    private TextField txtUsername;  // Email

    @FXML
    private TextField txtName;  // Full Name

    @FXML
    private TextField txtPhoneNumber;  // Phone Number

    @FXML
    private PasswordField txtPassword;  // Password

    @FXML
    private PasswordField txtConfirmPassword;  // Re-enter Password

    @FXML
    private Button btnSignUp;

    @FXML
    private Button btnBack;

    @FXML
    private Label lblMessage;

    private customerRecordDAO customerDAO;


    @FXML
    public void initialize() {
        customerDAO = new customerRecordDAO();
        System.out.println("SignUp Controller initialized");

        if (lblMessage != null) {
            lblMessage.setVisible(false);
        }

        // Set up button handlers
        if (btnSignUp != null) {
            btnSignUp.setOnAction(e -> handleSignUp());
        }

        if (btnBack != null) {
            btnBack.setOnAction(e -> handleBack());
        }

        // Clear message on focus
        if (txtUsername != null) {
            txtUsername.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal && lblMessage != null) lblMessage.setVisible(false);
            });
        }
    }

    @FXML
    private void handleSignUp() {
        System.out.println("Sign Up button clicked");

        // Get input values
        String email = txtUsername.getText().trim();
        String name = txtName.getText().trim();
        String phoneNumber = txtPhoneNumber.getText().trim();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        // Validate inputs
        if (!validateInputs(email, name, phoneNumber, password, confirmPassword)) {
            return;
        }

        // Check if email already exists
        if (customerDAO.emailExists(email)) {
            showMessage("Email address already registered. Please use a different email.", "error");
            return;
        }

        // Split name into first and last name
        String[] nameParts = name.trim().split("\\s+", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        // Create new customer
        customerRecordModel newCustomer = new customerRecordModel();
        newCustomer.setFirstName(firstName);
        newCustomer.setLastName(lastName);
        newCustomer.setEmail(email);
        newCustomer.setPhoneNum(phoneNumber);
        newCustomer.setCustomerPass(password);

        // Save to database
        if (customerDAO.createCustomer(newCustomer)) {
            showMessage("Account created successfully! Redirecting to login...", "success");
            
            // Redirect to login after short delay
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    javafx.application.Platform.runLater(this::navigateToLogin);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }).start();
        } else {
            showMessage("Failed to create account. Please try again.", "error");
        }
    }

    private boolean validateInputs(String email, String name, String phoneNumber, 
                                   String password, String confirmPassword) {
        // Check if any field is empty
        if (email.isEmpty() || name.isEmpty() || phoneNumber.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("Please fill in all fields", "error");
            return false;
        }

        // Validate name (at least 2 characters)
        if (name.length() < 2) {
            showMessage("Please enter your full name", "error");
            return false;
        }



        // Validate password length
        if (password.length() < 6) {
            showMessage("Password must be at least 6 characters long", "error");
            return false;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match", "error");
            return false;
        }

        return true;
    }

    @FXML
    private void handleBack() {
        System.out.println("Back button clicked");
        navigateToLogin();
    }

    private void navigateToLogin() {
        try {
            Stage stage = (Stage) btnSignUp.getScene().getWindow();
            
            Parent root = FXMLLoader.load(getClass().getResource("/com/grp5/view/User_login.fxml"));
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.setTitle("User Login - CIGE Bike Rentals");
            
            // Set window size
            stage.setMinWidth(1065);
            stage.setMinHeight(600);
            stage.centerOnScreen();
            
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading login page: " + e.getMessage());
            showMessage("Error loading login page", "error");
        }
    }

    private void showMessage(String message, String type) {
        if (lblMessage != null) {
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
}