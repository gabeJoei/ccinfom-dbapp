package com.grp5.controller_gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * Controller for Main Dashboard Sidebar (included via fx:include)
 */

public class MainDashBoardUserController {

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
    private AnchorPane contentArea;

    @FXML
    public void initialize() {
        loadUI("/com/grp5/view/RentBike.fxml");
        setupSidebarButtons();
    }

    private void setupSidebarButtons() {

        btnProfile.setOnAction(e -> handleProfile());
        btnReservations.setOnAction(e -> handleRentBike());
        btnRentalHistory.setOnAction(e -> handleReservations());
        btnLogout.setOnAction(e -> handleLogout());
    }

    // Loads any FXML into the content area
    private void loadUI(String fxmlPath) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleProfile() {
        System.out.println("Profile clicked");
         loadUI("/com/grp5/view/Settings.fxml");
    }

    private void handleReservations() {
        System.out.println("Reservations clicked");
        loadUI("/com/grp5/view/reserveBike.fxml");
    }

    private void handleRentBike() {
        System.out.println("RentBike clicked");
        loadUI("/com/grp5/view/RentBike.fxml");
    }

    private void handleLogout() {
        System.out.println("Log Out clicked");
        // Load login screen here
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
