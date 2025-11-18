package com.grp5.controller_gui;

import static java.lang.Integer.parseInt;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class User_reserveBikeController {
    
    @FXML
    private Text bikeModelText;
    @FXML
    private Text bikeCostText;
    
    @FXML
    private ImageView img;

    @FXML
    private DatePicker startDateSelection;
    @FXML
    private DatePicker endDateSelection;

    @FXML
    private Spinner<Integer> startHour;
    @FXML
    private Spinner<Integer> startMinute;
    @FXML
    private Spinner<Integer> endHour;
    @FXML
    private Spinner<Integer> endMinute;

    @FXML
    private Button reserveButton;

    private customerRecordDAO customerDAO;
    private bikeReservationDAO reservationDAO;

    private bikeRecordModel selectedBike;
    private customerRecordModel user;
    private bikeReservation reservation;
    private BigDecimal priceToPay;
    private String userID;

    public void initData(String userID, bikeRecordModel bike) {
        this.selectedBike = bike;
        this.userID = userID; 
        
        // get DAO
        customerDAO = new customerRecordDAO();
        reservationDAO = new bikeReservationDAO();

        // Display bike details
        this.bikeModelText.setText(selectedBike.getBikeModel());
        this.bikeCostText.setText(String.format("₱%.2f / day", selectedBike.getDailyRate()));

        // Initialize Spinners
        SpinnerValueFactory<Integer> startHourSpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(
            0, 23, 12 // (min, max, default)
        ); 
        startHour.setValueFactory(startHourSpinner);

        SpinnerValueFactory<Integer> endHourSpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(
            0, 23, 12
        );
        endHour.setValueFactory(endHourSpinner);

        // Configure minute spinners (0-59, in 15-min steps)
        SpinnerValueFactory<Integer> startMinuteSpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(
            0, 59, 0, 15 // (min, max, default, step)
        ); 
        startMinute.setValueFactory(startMinuteSpinner);

        SpinnerValueFactory<Integer> endMinuteSpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(
            0, 59, 0, 15
        );
        endMinute.setValueFactory(endMinuteSpinner);

        // Get User Details
        user = customerDAO.getCustomer(parseInt(userID));

        changeImage();
    }

    private void changeImage() {
        switch (selectedBike.getBikeModel()) {
            case "Mountain Bike" -> img.setImage(new Image(
                getClass().getResource("/com/grp5/view/images/MountainBike-.png").toExternalForm()));
            case "Road Bike" -> img.setImage(new Image(
                getClass().getResource("/com/grp5/view/images/RoadBike-.png").toExternalForm()));
            case "Bike with E-assist" -> img.setImage(new Image(
                getClass().getResource("/com/grp5/view/images/E-Bike-.png").toExternalForm()));
            case "Tandem Bike" -> img.setImage(new Image(
                getClass().getResource("/com/grp5/view/images/TandemBike-.png").toExternalForm()));
            case "E-Bike" -> img.setImage(new Image(
                getClass().getResource("/com/grp5/view/images/Electric_Bike-.png").toExternalForm()));
            case "BMX bike" -> img.setImage(new Image(
                getClass().getResource("/com/grp5/view/images/BMX bike.png").toExternalForm()));
            default -> System.err.println("Unknown bike: " + selectedBike.getBikeModel());
        }
    }

    @FXML
    protected void onReservationClick() {
        try {
            LocalDate startDate = startDateSelection.getValue();
            LocalDate endDate = endDateSelection.getValue();

            // Get time values 
            int sHour = startHour.getValue();
            int sMin = startMinute.getValue();
            int eHour = endHour.getValue();
            int eMin = endMinute.getValue();

            LocalDateTime startDateTime = startDate.atTime(sHour, sMin);
            LocalDateTime endDateTime = endDate.atTime(eHour, eMin);

            // Warn user for any reservation errors. If fails, stop
            if (!validateReservation(startDateTime, endDateTime)) {
                return; 
            }

            // Convert localDate to Timestamp
            Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
            Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

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

        // Get the rates from bike model
        BigDecimal hourlyRate = selectedBike.getHourlyRate();
        BigDecimal dailyRate = selectedBike.getDailyRate();

        // Get start and end times
        LocalDateTime start = reservation.getStartDate().toLocalDateTime();
        LocalDateTime end = reservation.getEndDate().toLocalDateTime();
        
        // Calculate total hours (rounded up)
        long totalMinutes = java.time.temporal.ChronoUnit.MINUTES.between(start, end);
        if (totalMinutes <= 0) { totalMinutes = 60; } // Min 1 hour
        
        // Add any minutes totaling to an hour
        long totalHours = (totalMinutes / 60);
        if (totalMinutes % 60 > 0) {
            totalHours++; 
        }

        // Add any hours totaling to days
        long totalDays = (totalHours / 24);
        if (totalHours % 24 > 0) {
            totalDays++; 
        }
        
        // Calculate hourly and daily price
        BigDecimal priceByHour = hourlyRate.multiply(new BigDecimal(totalHours));
        BigDecimal priceByDay = dailyRate.multiply(new BigDecimal(totalDays));

        // Compare prices and return the cheaper one        
        if (priceByDay.compareTo(priceByHour) < 0) {
            return priceByDay; 
        } else {
            return priceByHour;
        }
    }

    private boolean validateReservation(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        LocalDateTime now = LocalDateTime.now(); 

        if (selectedBike == null) {
            generalUtilities.showAlert(
                Alert.AlertType.ERROR, "Error", "No Bike Selected.");
            return false;
        }
        if (startDateTime == null || endDateTime == null) {
            generalUtilities.showAlert(
                Alert.AlertType.WARNING, "Input Error", "Please select a start and end time.");
            return false;
        }
        if (startDateTime.isBefore(now)) {
            generalUtilities.showAlert(
                Alert.AlertType.WARNING, "Input Error", "Start date and time cannot be in the past.");
            return false;
        }
        if (endDateTime.isBefore(startDateTime) || endDateTime.isEqual(startDateTime)) {
            generalUtilities.showAlert(
                Alert.AlertType.WARNING, "Input Error", "End time must be after the start time.");
            return false;
        }
        return true;
    }

    private void goToPayment(bikeReservation reservation, BigDecimal priceToPay) {
        try {
            AnchorPane dashboardContentArea = (AnchorPane) reserveButton.getScene().lookup("#contentArea");
            if (dashboardContentArea != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grp5/view/User_paymentSummary.fxml"));
                AnchorPane newPane = loader.load();
                User_paymentSummaryController paymentController = loader.getController();

                paymentController.init(reservation, priceToPay, userID);

                dashboardContentArea.getChildren().setAll(newPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
            generalUtilities.showAlert(Alert.AlertType.ERROR, "Error", "Error in loading payment transaction.");
        }
    }
}