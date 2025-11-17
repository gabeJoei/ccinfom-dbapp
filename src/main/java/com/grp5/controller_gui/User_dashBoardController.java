package com.grp5.controller_gui;

import com.grp5.utils.sessionManager;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

/**
 * Controller for Main Dashboard Sidebar (included via fx:include)
 */
public class User_dashBoardController {

    @FXML
    private Text txtUserName;

    @FXML
    private Text txtUserID;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnReservations;

    @FXML
    private Button btnRentalHistory;

    @FXML
    private Button btnLogout;

    @FXML
    private Pane contentArea;

    @FXML
    public void initialize() {
        loadUI("/com/grp5/view/User_rentBike.fxml");
        setupSidebarButtons();
    }

    private void setupSidebarButtons() {
        btnProfile.setOnAction(e -> handleProfile());
        btnReservations.setOnAction(e -> handleRentBike());
        btnRentalHistory.setOnAction(e -> handleReservations());
        btnLogout.setOnAction(e -> handleLogout());
    }

    // Loads any FXML into the content area
    public void loadUI(String fxmlPath) {
        try {
            Parent pane = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading FXML: " + fxmlPath);
        }
    }

    private void handleProfile() {
        System.out.println("Profile clicked");
        loadUI("/com/grp5/view/Profile_settings.fxml");
    }

    private void handleReservations() {
        System.out.println("Reservations clicked");
        loadUI("/com/grp5/view/User_bikeReservationView.fxml");
    }

    private void handleRentBike() {
        System.out.println("RentBike clicked");
        loadUI("/com/grp5/view/User_rentBike.fxml");
    }

    private void handleLogout() {
        System.out.println("Log Out clicked");

        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        sessionManager session=new sessionManager();
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("You will be returned to the login selection screen.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                session.performLogout(btnLogout);
            }
        });
    }

   
    public void setUserName(String userName) {
        if (txtUserName != null) {
            txtUserName.setText(userName);
        }
    }

    public String getUserName() {
        return txtUserName != null ? txtUserName.getText() : null;
    }

    public void setUserId(String userId) {
        if (txtUserID != null) {
            txtUserID.setText(userId);
        }
    }

    public String getUserId() {
        return txtUserID != null ? txtUserID.getText() : null;
    }

    public Button getBtnProfile() {
        return btnProfile;
    }

    public Button getBtnReservations() {
        return btnReservations;
    }

    public Button getBtnRentalHistory() {
        return btnRentalHistory;
    }

    public Button getBtnLogout() {
        return btnLogout;
    }
}