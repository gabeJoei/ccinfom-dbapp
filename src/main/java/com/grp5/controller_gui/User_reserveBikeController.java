package com.grp5.controller_gui;

import static java.lang.Integer.parseInt;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

import org.w3c.dom.events.MouseEvent;

import com.grp5.dao.customerRecordDAO;
import com.grp5.model.bikeRecordModel;
import com.grp5.model.customerRecordModel;
import com.grp5.utils.generalUtilities;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class User_reserveBikeController {
    
    @FXML
    private Text bikeModelText;
    @FXML
    private Text bikeCostText;
    
    @FXML
    private DatePicker startDateSelection;
    @FXML
    private DatePicker endDateSelection;

    @FXML
    private Button reserveButton;

    private customerRecordDAO customerDAO;

    private bikeRecordModel selectedBike;
    private customerRecordModel user;
    private Date currentDate;

    @FXML
    public void initializePage() {
        customerDAO = new customerRecordDAO();
    }  

    public void initData(String userID, bikeRecordModel bike) {
        this.selectedBike = bike;

        // Display bike details
        this.bikeModelText.setText(selectedBike.getBikeModel());
        this.bikeCostText.setText(String.format("₱%.2f / day", selectedBike.getDailyRate()));

        // Get User Details
        user = customerDAO.getCustomer(parseInt(userID));
    }

    @FXML
    protected void onReservationClick() {
        Timestamp startDate;
        Timestamp endDate;
        try {
            startDate = Timestamp.valueOf(startDateSelection.getValue().atStartOfDay());
            endDate = Timestamp.valueOf(endDateSelection.getValue().atStartOfDay());

            // Warn user for any reservation errors
            validateReservation(startDate, endDate);
        } catch (Exception e) {
            generalUtilities.showAlert(Alert.AlertType.WARNING, "Input Error", "No startDate or endDate selected.");
        }
    
        // Reservation Process
        // idk if nagawa na ni enzo sa rentingTransactionController.java so ill make this empty for now..

        try {
            goToPayment();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void validateReservation(Timestamp startDate, Timestamp endDate) {

        currentDate = new Date(System.currentTimeMillis());

        if (selectedBike == null) {
            generalUtilities.showAlert(Alert.AlertType.ERROR, "Error", "No bike selected.");
            return;
        }
        if (startDate == null || endDate == null) {
            generalUtilities.showAlert(Alert.AlertType.WARNING, "Input Error", "Please select both a start and end date.");
            return;
        }
        if (startDate.before(currentDate)) {
            generalUtilities.showAlert(Alert.AlertType.WARNING, "Input Error", "Start date cannot be in the past.");
            return;
        }
        if (endDate.before(startDate)) {
            generalUtilities.showAlert(Alert.AlertType.WARNING, "Input Error", "End date must be after the start date.");
        }
    }

    private void goToPayment() throws IOException {
        AnchorPane dashboardContentArea = (AnchorPane) reserveButton.getScene().lookup("#contentArea");
        if (dashboardContentArea != null) {
            // Load rentBike.fxml
            AnchorPane newPane = FXMLLoader.load(getClass().getResource("/com/grp5/view/User_paymentSummary.fxml"));
            dashboardContentArea.getChildren().setAll(newPane);
        }
    }
}
