package com.grp5.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

/**
 * Controller for Main Dashboard Sidebar (included via fx:include)
 */
public class MainDashBoardUserController {

    //  Sidebar UI Components 
    @FXML private Text txtUserName;
    @FXML private Text txtUserID;
    @FXML private Button btnProfile;
    @FXML private Button btnReservations;
    @FXML private Button btnRentalHistory;
    @FXML private Button btnLogout;
    

    //  Getters and Setters 
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
