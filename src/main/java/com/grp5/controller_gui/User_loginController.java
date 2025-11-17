package com.grp5.controller_gui;

import com.grp5.dao.customerRecordDAO;
import com.grp5.model.customerRecordModel;
import com.grp5.session.AccountSession;
import com.grp5.session.ProfileSnapshot;
import com.grp5.session.AccountSession.AccountType;
import com.grp5.utils.sessionManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class User_loginController {

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

    @FXML
    private Button btnBack;

    private customerRecordDAO customerDAO;

    @FXML
    public void initialize() {
        customerDAO = new customerRecordDAO();
        System.out.println("User loginController initialized");
        lblMessage.setVisible(false);

        // Clear message on focus
        txtUsername.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal)
                lblMessage.setText("");
        });
        txtPassword.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal)
                lblMessage.setText("");
        });

        // Enter key to login
        txtPassword.setOnAction(e -> handleLogin());

        // Add button action handler
        btnLogin.setOnAction(e -> handleLogin());

        System.out.println("Connection initialization complete.");
    }

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        System.out.println("Login attempt with username: " + username);

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter both username and password", "error");
            return;
        }

        // Attempt login
        if (authenticateUser(username, password)) {
            showMessage("Login successful!", "success");

            // Small delay for user to see success message
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    // Update UI on the JavaFX Application Thread
                    javafx.application.Platform.runLater(this::navigateToDashboard);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }).start();

        } else {
            showMessage("Invalid username or password", "error");
            txtPassword.clear();
        }
    }

    private boolean authenticateUser(String username, String password) {
        // Clear previous session data
        sessionManager.clearSession();

        // Authenticate customer only
        customerRecordModel customer = customerDAO.authenticateCustomer(username, password);

        if (customer != null) {

            if (customer.getCustomerPass() == null) {
                customer.setCustomerPass(password);
            }

            System.out.println("Logged in as Customer: " + customer.getFirstName() + " " + customer.getLastName());
            System.out.println("Customer ID: " + customer.getCustomerAccID());
            System.out.println("Customer Acc ID: " + customer.getCustomerAccID());
            sessionManager.setLoggedInCustomer(customer);
            ProfileSnapshot snap = new ProfileSnapshot(customer.getFirstName(), customer.getLastName(),
                    customer.getEmail());
            AccountSession.setAccount(AccountType.USER, customer.getCustomerAccID(), snap);
            return true;
        }

        System.out.println("Authentication failed for username: " + username);
        return false;
    }

    private void navigateToDashboard() {
        try {
            Stage stage = (Stage) btnLogin.getScene().getWindow();

            // Load User Dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grp5/view/User_userMenu.fxml"));
            Parent root = loader.load();

            // Get customer info from session
            customerRecordModel customer = sessionManager.getLoggedInCustomer();

            // Set user info in dashboard
            User_dashBoardController userController = loader.getController();
            if (userController != null && customer != null) {
                String fullName = customer.getFirstName() + " " + customer.getLastName();
                String userId = String.valueOf(customer.getCustomerAccID());

                userController.setUserName(fullName);
                userController.setUserId(userId);

                System.out.println("User dashboard loaded for: " + fullName + " (ID: " + userId + ")");
            } else {
                System.err.println("Controller or customer is null!");
                if (userController == null) {
                    System.err.println("User controller is null");
                }
                if (customer == null) {
                    System.err.println("Customer is null");
                }
            }

            Scene scene = new Scene(root);
            stage.setTitle("User Dashboard - CIGE Bike Rentals");
            stage.setScene(scene);

            // Set minimum window size for dashboard
            stage.setMinWidth(1065);
            stage.setMinHeight(600);
            stage.setResizable(true);

            // Center on screen
            stage.centerOnScreen();

            stage.show();

            System.out.println("Dashboard displayed successfully");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading dashboard: " + e.getMessage());
            showMessage("Error loading dashboard: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void handleSignUp() {
        try {
            Stage stage = (Stage) btnSignUp.getScene().getWindow();

            // Load sign-up page
            Parent root = FXMLLoader.load(getClass().getResource("/com/grp5/view/User_signUp.fxml"));

            Scene scene = new Scene(root);
            stage.setTitle("Sign Up - CIGE Bike Rentals");
            stage.setScene(scene);

            // Set window size for sign-up
            stage.setMinWidth(1065);
            stage.setMinHeight(600);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Error loading sign-up page", "error");
        }
    }

    @FXML
    public void handleBackBtnClick(ActionEvent click) {
        System.out.println("Back button clicked!");
        try {
            Parent nextSceneRoot = FXMLLoader.load(getClass().getResource("/com/grp5/view/AdminOrUser.fxml"));
            Scene nextScene = new Scene(nextSceneRoot);
            Stage currentStage = (Stage) btnBack.getScene().getWindow();

            currentStage.setScene(nextScene);
            currentStage.setTitle("CIGE Bike Rentals - Login Selection");

            // Reset window size for login selection
            currentStage.setMinWidth(600);
            currentStage.setMinHeight(400);
            currentStage.setResizable(false);
            currentStage.centerOnScreen();

            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading AdminOrUser page: " + e.getMessage());
        }
    }

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