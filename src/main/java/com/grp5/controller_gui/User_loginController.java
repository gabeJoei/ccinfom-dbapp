package com.grp5.controller_gui;

import com.grp5.dao.adminDAO;
import com.grp5.dao.customerRecordDAO;
import com.grp5.model.adminModel;
import com.grp5.model.customerRecordModel;
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
    private adminDAO adminDAO;
    private String userType = "user";

    @FXML
    public void initialize() {
        customerDAO = new customerRecordDAO();
        adminDAO = new adminDAO();
        System.out.println("loginController initialized");
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
        System.out.println("Connection initialization complete.");
    }

    public void setUserType(String userType) {
        if (userType != null && (userType.equals("admin") || userType.equals("user"))) {
            this.userType = userType;
            System.out.println("User type set to: " + userType);
        }
    }

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

        if (userType.equals("admin")) {
            adminModel admin = adminDAO.authenticateAdmin(username, password);
            if (admin != null) {
                System.out.println("Logged in as Admin: " + admin.getAdminUsername());
                sessionManager.setLoggedInAdmin(admin); // Store admin in session
                return true;
            }
        } else {
            customerRecordModel customer = customerDAO.authenticateCustomer(username, password);

            if (customer != null) {
                System.out.println("Logged in as Customer: " + customer.getFirstName() + " " + customer.getLastName());
                sessionManager.setLoggedInCustomer(customer); // Store customer in session
                return true;
            }
        }

        return false;
    }

    private void navigateToDashboard() {
        try {
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            Parent root;
            String title;
            String fxmlPath = "/com/grp5/view/RentBike.fxml"; // Example FXML path

            if (userType.equals("admin")) {
                loader.setLocation(getClass().getResource(fxmlPath));
                root = loader.load();
                // If you had a dedicated AdminController, you would access it here:
                // AdminController adminController = loader.getController();
                title = "Admin Dashboard";
            } else {
                loader.setLocation(getClass().getResource(fxmlPath));
                root = loader.load();
                // If you had a dedicated UserController, you would access it here:
                // UserController userController = loader.getController();
                title = "User Dashboard";
            }

            Scene scene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Error loading dashboard: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void handleSignUp() {
        try {
            Stage stage = (Stage) btnSignUp.getScene().getWindow();

            // Load sign-up page ( FXML as a placeholder for the next screen)
            Parent root = FXMLLoader.load(getClass().getResource("/com/grp5/view/UserSignUp.fxml"));

            Scene scene = new Scene(root);
            stage.setTitle("Sign Up");
            stage.setScene(scene);
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

            Parent nextSceneRoot = FXMLLoader
                    .load(getClass().getResource("/com/grp5/view/AdminOrUser.fxml"));
            Scene nextScene = new Scene(nextSceneRoot); //
            Stage currentStage = (Stage) btnBack.getScene().getWindow();

            currentStage.setScene(nextScene);
            currentStage.setTitle("Home");
            currentStage.show();

        } catch (IOException e) {
            e.getStackTrace();
            System.err.println("Tite");
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