package com.grp5.controller_backend;

import com.grp5.dao.*;
import com.grp5.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public class rentingTransactionController {

    @FXML
    private ComboBox<Integer> cmbCustomerID;
    @FXML
    private ComboBox<Integer> cmbBikeID;
    @FXML
    private ComboBox<Integer> cmbBranchID;
    @FXML
    private DatePicker dateStart;
    @FXML
    private DatePicker dateEnd;
    @FXML
    private ComboBox<String> cmbRentalType; // Hourly or Daily
    @FXML
    private Label lblBikeModel;
    @FXML
    private Label lblRate;
    @FXML
    private Label lblTotalAmount;
    @FXML
    private Label lblMessage;
    @FXML
    private Button btnProcessRental;
    @FXML
    private Button btnCancel;

    private customerRecordDAO customerDAO;
    private bikeRecordDAO bikeDAO;
    private branchRecordDAO branchDAO;
    private bikeReservationDAO reservationDAO;
    private transactionRecordDAO paymentDAO;

    private bikeRecordModel selectedBike;

    @FXML
    public void initialize() {
        customerDAO = new customerRecordDAO();
        bikeDAO = new bikeRecordDAO();
        branchDAO = new branchRecordDAO();
        reservationDAO = new bikeReservationDAO();
        paymentDAO = new transactionRecordDAO();

        setupComboBoxes();
        setupEventHandlers();
    }

    private void setupComboBoxes() {
        // Load rental types
        cmbRentalType.getItems().addAll("Hourly", "Daily");
        cmbRentalType.setValue("Daily");

        // Load customers
        customerDAO.getAllCustomers().forEach(c -> cmbCustomerID.getItems().add(c.getCustomerAccID()));

        // Load branches
        branchDAO.getAllBranches().forEach(b -> cmbBranchID.getItems().add(b.getBranchID()));

        // Set default dates
        dateStart.setValue(java.time.LocalDate.now());
        dateEnd.setValue(java.time.LocalDate.now().plusDays(1));
    }

    private void setupEventHandlers() {
        // When branch selected, load available bikes
        cmbBranchID.setOnAction(e -> loadAvailableBikes());

        // When bike selected, show details
        cmbBikeID.setOnAction(e -> displayBikeDetails());

        // When dates change, calculate amount
        dateStart.setOnAction(e -> calculateTotalAmount());
        dateEnd.setOnAction(e -> calculateTotalAmount());
        cmbRentalType.setOnAction(e -> calculateTotalAmount());
    }

    private void loadAvailableBikes() {
        cmbBikeID.getItems().clear();
        lblBikeModel.setText("-");
        lblRate.setText("-");
        lblTotalAmount.setText("-");

        if (cmbBranchID.getValue() == null)
            return;

        int branchID = cmbBranchID.getValue();

        // Get all bikes from this branch that are available
        bikeDAO.getAllBikes().stream()
                .filter(bike -> bike.getBranchIDNum() == branchID)
                .filter(bike -> bike.getBikeAvailability())
                .forEach(bike -> cmbBikeID.getItems().add(bike.getBikeID()));
        if (cmbBikeID.getItems().isEmpty()) {
            showMessage("No available bikes at this branch", "error");
        }
    }

    private void displayBikeDetails() {
        if (cmbBikeID.getValue() == null)
            return;

        selectedBike = bikeDAO.getBikeRecord(cmbBikeID.getValue());
        if (selectedBike != null) {
            lblBikeModel.setText(selectedBike.getBikeModel());

            if (cmbRentalType.getValue().equals("Hourly")) {
                lblRate.setText(String.format("₱%.2f/hour", selectedBike.getHourlyRate()));
            } else {
                lblRate.setText(String.format("₱%.2f/day", selectedBike.getDailyRate()));
            }

            calculateTotalAmount();
        }
    }

    private void calculateTotalAmount() {
        if (selectedBike == null || dateStart.getValue() == null || dateEnd.getValue() == null) {
            return;
        }

        long days = java.time.temporal.ChronoUnit.DAYS.between(
                dateStart.getValue(),
                dateEnd.getValue());

        if (days < 0) {
            showMessage("End date must be after start date", "error");
            lblTotalAmount.setText("-");
            return;
        }

        BigDecimal amount;

        if (cmbRentalType.getValue().equals("Hourly")) {
            long hours = days * 24;
            amount = selectedBike.getHourlyRate().multiply(new BigDecimal(hours));
        } else {
            if (days == 0)
                days = 1; // Minimum 1 day
            amount = selectedBike.getDailyRate().multiply(new BigDecimal(days));
        }

        lblTotalAmount.setText(String.format("₱%.2f", amount));
    }

    public BigDecimal calculateLateFeesByHour(Timestamp endDate, Timestamp lateDateReturn){
        BigDecimal lateFee;
        BigDecimal lateRate=new BigDecimal(15);
        Duration duration = Duration.between(endDate.toInstant(), lateDateReturn.toInstant());
        long minutes=duration.toHours();
        lateFee=lateRate.multiply(BigDecimal.valueOf(minutes));
        return lateFee;
    }
    /*
     * Process the rental transaction
     */
    @FXML
    private void handleProcessRental() {
        // Validate all fields
        if (!validateInputs()) {
            return;
        }

        // Confirm rental
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Rental");
        confirmAlert.setHeaderText("Process Bike Rental");
        confirmAlert.setContentText(
                "Customer ID: " + cmbCustomerID.getValue() + "\n" +
                        "Bike: " + selectedBike.getBikeModel() + "\n" +
                        "Period: " + dateStart.getValue() + " to " + dateEnd.getValue() + "\n" +
                        "Total: " + lblTotalAmount.getText() + "\n\n" +
                        "Confirm rental?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                processRental();
            }
        });
    }

    private void processRental() {
        try {
            customerRecordModel customer = customerDAO.getCustomerRecordData(
                    new customerRecordModel(cmbCustomerID.getValue()));
            if (customer == null) {
                showMessage("Customer not found!", "error");
                return;
            }

            bikeReservation reservation = new bikeReservation();
            reservation.setCustomerAccID(customer.getCustomerAccID());
            reservation.setBikeID(selectedBike.getBikeID());
            reservation.setBranchID(cmbBranchID.getValue());
            reservation.setReservationDate(Timestamp.valueOf(LocalDateTime.now()));
            reservation.setStartDate(Timestamp.valueOf(dateStart.getValue().atStartOfDay()));
            reservation.setEndDate(Timestamp.valueOf(dateEnd.getValue().atStartOfDay()));
            reservation.setStatus("pending");

            if (reservationDAO.addReservation(reservation) == 0) {
                showMessage("Failed to create reservation!", "error");
                return;
            }

            int reservationRefNum = getLastReservationID();
            transactionRecordModel payment = new transactionRecordModel();
            payment.setCustomerAccountID(customer.getCustomerAccID());
            payment.setReservationReferenceNum(reservationRefNum);
            payment.setBikeID(selectedBike.getBikeID());
            payment.setBranchID(cmbBranchID.getValue());
            payment.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
            payment.setPaymentAmount(new BigDecimal(lblTotalAmount.getText().replace("₱", "").trim()));

            if (paymentDAO.addTransactionRecordData(payment) == 0) {
                showMessage("Failed to record payment!", "error");
                return;
            }

            selectedBike.setBikeAvailability(false);
            if (bikeDAO.updateBikeRecord(selectedBike) == 0) {
                showMessage("Warning: Failed to update bike availability", "error");
            }

            showRentalSummary(customer, reservation, payment);
            clearForm();

        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error processing rental: " + e.getMessage(), "error");
        }
    }

    // Get last inserted reservation ID (=

    private int getLastReservationID() {
        // modify addReservation to return generated key
        ArrayList<bikeReservation> all = reservationDAO.getAllReservations();
        if (!all.isEmpty()) {
            return all.get(all.size() - 1).getReservationReferenceNum();
        }
        return 1;
    }

    private boolean validateInputs() {
        if (cmbCustomerID.getValue() == null) {
            showMessage("Please select a customer", "error");
            return false;
        }

        if (cmbBranchID.getValue() == null) {
            showMessage("Please select a branch", "error");
            return false;
        }

        if (cmbBikeID.getValue() == null) {
            showMessage("Please select a bike", "error");
            return false;
        }

        if (dateStart.getValue() == null || dateEnd.getValue() == null) {
            showMessage("Please select rental dates", "error");
            return false;
        }

        if (selectedBike == null) {
            showMessage("Bike details not loaded", "error");
            return false;
        }

        return true;
    }

    private void showRentalSummary(customerRecordModel customer,
            bikeReservation reservation,
            transactionRecordModel payment) {
        Alert summaryAlert = new Alert(Alert.AlertType.INFORMATION);
        summaryAlert.setTitle("Rental Successful");
        summaryAlert.setHeaderText("Bike Rented Successfully!");

        String summary = String.format(
                "Customer: %s %s\n" +
                        "Bike: %s\n" +
                        "Rental Period: %s to %s\n" +
                        "Amount Paid: ₱%.2f\n" +
                        "Status: Ongoing\n\n" +
                        "Please return the bike by %s",
                customer.getFirstName(),
                customer.getLastName(),
                selectedBike.getBikeModel(),
                dateStart.getValue(),
                dateEnd.getValue(),
                payment.getPaymentAmount(),
                dateEnd.getValue());

        summaryAlert.setContentText(summary);
        summaryAlert.showAndWait();
    }

    private void clearForm() {
        cmbCustomerID.setValue(null);
        cmbBranchID.setValue(null);
        cmbBikeID.getItems().clear();
        cmbBikeID.setValue(null);
        dateStart.setValue(java.time.LocalDate.now());
        dateEnd.setValue(java.time.LocalDate.now().plusDays(1));
        lblBikeModel.setText("-");
        lblRate.setText("-");
        lblTotalAmount.setText("-");
        selectedBike = null;
        lblMessage.setText("");
    }

    @FXML
    private void handleCancel() {
        try {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/grp5/view/userMainPage.fxml")); // will be
                                                                                                       // changed later
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMessage(String message, String type) {
        lblMessage.setText(message);

        if (type.equals("success")) {
            lblMessage.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
        } else if (type.equals("error")) {
            lblMessage.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        }
    }
}