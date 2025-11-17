package com.grp5.controller_gui;

import static java.lang.Integer.parseInt;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.events.MouseEvent;

import com.grp5.dao.bikeRecordDAO;
import com.grp5.dao.bikeReservationDAO;
import com.grp5.dao.customerRecordDAO;
import com.grp5.model.bikeRecordModel;
import com.grp5.model.bikeReservation;
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
    private bikeReservationDAO reservationDAO;

    private bikeRecordModel selectedBike;
    private customerRecordModel user;
    private bikeReservation reservation;
    private BigDecimal priceToPay;

    public void initData(String userID, bikeRecordModel bike) {
        this.selectedBike = bike;
        
        // get DAO
        customerDAO = new customerRecordDAO();
        reservationDAO = new bikeReservationDAO();

        // Display bike details
        this.bikeModelText.setText(selectedBike.getBikeModel());
        this.bikeCostText.setText(String.format("₱%.2f / day", selectedBike.getDailyRate()));

        // Get User Details
        user = customerDAO.getCustomer(parseInt(userID));
    }

    @FXML
    protected void onReservationClick() {
        try {
            LocalDate startDate = startDateSelection.getValue();
            LocalDate endDate = endDateSelection.getValue();

            // Warn user for any reservation errors. If fails, stop
            if (!validateReservation(startDate, endDate)) {
                return; 
            }

            // Convert localDate to Timestamp
            Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
            Timestamp endTimestamp = Timestamp.valueOf(endDate.atStartOfDay());

            // Check if reservation already exists
            if (reservationDAO.hasOverlappingReservation(selectedBike.getBikeID(), startTimestamp, endTimestamp)) {
                generalUtilities.showAlert(Alert.AlertType.WARNING, "Bike Not Available!", 
                    "This bike is already reserved. Please choose different dates.");
                return; // Stop the process if reservation overlaps
            }

            // Create NEW Reservation
            reservation = new bikeReservation();
            reservation.setCustomerAccID(user.getCustomerAccID());
            reservation.setBikeID(selectedBike.getBikeID());
            reservation.setBranchID(selectedBike.getBranchIDNum());
            reservation.setReservationDate(Timestamp.valueOf(LocalDateTime.now()));
            reservation.setStartDate(startTimestamp);
            reservation.setEndDate(endTimestamp);
            reservation.setStatus("ongoing");

            // Calculate price
            priceToPay = calculatePrice(reservation);

            // Load Payment
            goToPayment(reservation, priceToPay);

        } catch (Exception e) {
            generalUtilities.showAlert(Alert.AlertType.WARNING, "Input Error", "No startDate or endDate selected.");
        }
    }

    private BigDecimal calculatePrice(bikeReservation reservation) {

        // Get number of days in rental
        long days = java.time.temporal.ChronoUnit.DAYS.between(
            reservation.getStartDate().toInstant(), 
            reservation.getEndDate().toInstant()
        );

        if (days == 0) {days = 1; } // Minimum 1 day
        BigDecimal amount = selectedBike.getDailyRate().multiply(new BigDecimal(days));

        return amount;
    }

    private boolean validateReservation(LocalDate startDate, LocalDate endDate) {

        LocalDate today = LocalDate.now(); // Get today date

        if (selectedBike == null) {
            generalUtilities.showAlert(
                Alert.AlertType.ERROR, "Error", "No bike selected.");
            return false;
        }
        if (startDate == null || endDate == null) {
            generalUtilities.showAlert(
                Alert.AlertType.WARNING, "Input Error", "Please select both a start and end date.");
            return false;
        }
        if (startDate.isBefore(today)) {
            generalUtilities.showAlert(
                Alert.AlertType.WARNING, "Input Error", "Start date cannot be in the past.");
            return false;
        }
        if (endDate.isBefore(startDate)) {
            generalUtilities.showAlert(
                Alert.AlertType.WARNING, "Input Error", "End date must be after the start date.");
            return false;
        }
        // No errors
        return true;
    }

    private void goToPayment(bikeReservation reservation, BigDecimal priceToPay) {
        try {
            AnchorPane dashboardContentArea = (AnchorPane) reserveButton.getScene().lookup("#contentArea");
            if (dashboardContentArea != null) {
                // Load User_paymentSummary.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grp5/view/User_paymentSummary.fxml"));
                AnchorPane newPane = loader.load();
                User_paymentSummaryController paymentController = loader.getController();

                // Carry data to paymentSummary.fxml
                paymentController.init(reservation, priceToPay);

                dashboardContentArea.getChildren().setAll(newPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
            generalUtilities.showAlert(Alert.AlertType.ERROR, "Error", "Error in loading payment transaction.");
        }
    }
}
