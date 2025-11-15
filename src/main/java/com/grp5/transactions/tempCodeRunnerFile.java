package com.grp5.transactions;

import com.grp5.dao.*;
import com.grp5.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;


public class returningTransactionController {
    
    @FXML private TextField txtReservationRef;
    @FXML private Label lblCustomerName;
    @FXML private Label lblBikeModel;
    @FXML private Label lblStartDate;
    @FXML private Label lblEndDate;
    @FXML private Label lblStatus;
    @FXML private DatePicker dateReturn;
    @FXML private TextArea txtNotes;
    @FXML private Label lblLateFee;
    @FXML private Label lblMessage;
    @FXML private Button btnProcessReturn;
    @FXML private Button btnCancel;
    
    private bikeReservationDAO reservationDAO;
    private bikeRecordDAO bikeDAO;
    private customerRecordDAO customerDAO;
    private transactionRecordDAO paymentDAO;
    
    private bikeReservation currentReservation;
    private bikeRecordModel currentBike;
    private customerRecordModel currentCustomer;
    
    @FXML
    public void initialize() {
        reservationDAO = new bikeReservationDAO();
        bikeDAO = new bikeRecordDAO();
        customerDAO = new customerRecordDAO();
        paymentDAO = new transactionRecordDAO();
        
        dateReturn.setValue(LocalDate.now());
        clearForm();
    }
    
    /**
     * Handle load reservation button
     */
    @FXML
    private void handleLoadReservation() {
        String refText = txtReservationRef.getText().trim();
        
        if (refText.isEmpty()) {
            showMessage("Please enter a reservation reference number", "error");
            return;
        }
        
        try {
            int refNum = Integer.parseInt(refText);
            
            // Step a: Check reservation record
            currentReservation = reservationDAO.getReservation(refNum);
            
            if (currentReservation == null) {
                showMessage("Reservation not found!", "error");
                clearForm();
                return;
            }
            
            // Check status
            if (!currentReservation.getStatus().equalsIgnoreCase("ongoing")) {
                showMessage("Only ongoing reservations can be returned. Status: " + 
                           currentReservation.getStatus(), "error");
                clearForm();
                return;
            }
            
            // Step b: Get customer and bike info
            currentCustomer = customerDAO.getCustomer(currentReservation.getCustomerAccID());
            currentBike = bikeDAO.getBikeRecord(currentReservation.getBikeID());
            
            // Load details
            loadReservationDetails();
            calculateLateFee();
            showMessage("Reservation loaded successfully!", "success");
            
        } catch (NumberFormatException e) {
            showMessage("Invalid reservation reference number!", "error");
        }
    }
    
    /**
     * Load reservation details
     */
    private void loadReservationDetails() {
        if (currentCustomer != null) {
            lblCustomerName.setText(currentCustomer.getFirstName() + " " + 
                                   currentCustomer.getLastName());
        }
        
        if (currentBike != null) {
            lblBikeModel.setText(currentBike.getBikeModel());
        }
        
        lblStartDate.setText(currentReservation.getStartDate().toString());
        lblEndDate.setText(currentReservation.getEndDate().toString());
        lblStatus.setText(currentReservation.getStatus());
        lblStatus.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
    }
    
    /**
     * Calculate late fee if applicable
     */
    private void calculateLateFee() {
        if (currentReservation == null) return;
        
        LocalDate endDate = currentReservation.getEndDate().toLocalDateTime().toLocalDate();
        LocalDate returnDate = dateReturn.getValue();
        
        long daysLate = ChronoUnit.DAYS.between(endDate, returnDate);
        
        if (daysLate > 0) {
            // Late fee: 50 pesos per day
            BigDecimal lateFee = new BigDecimal(daysLate * 50);
            lblLateFee.setText(String.format("₱%.2f (%d days late)", lateFee, daysLate));
            lblLateFee.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        } else {
            lblLateFee.setText("No late fee");
            lblLateFee.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
        }
    }
    
    /**
     * Process bike return
     */
    @FXML
    private void handleProcessReturn() {
        if (currentReservation == null) {
            showMessage("Please load a reservation first!", "error");
            return;
        }
        
        // Confirm return
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Return");
        confirmAlert.setHeaderText("Process Bike Return");
        confirmAlert.setContentText(
            "Customer: " + lblCustomerName.getText() + "\n" +
            "Bike: " + lblBikeModel.getText() + "\n" +
            "Return Date: " + dateReturn.getValue() + "\n" +
            "Late Fee: " + lblLateFee.getText() + "\n\n" +
            "Confirm return?"
        );
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                processReturn();
            }
        });
    }
    
    /**
     * Execute return transaction
     */
    private void processReturn() {
        try {
            // Step c: Update bike availability to "Yes"
            currentBike.setBikeAvailability("Yes");
            if (!bikeDAO.updateBike(currentBike)) {
                showMessage("Failed to update bike availability!", "error");
                return;
            }
            
            // Step d: Record return date
            currentReservation.setDateReturned(Timestamp.valueOf(dateReturn.getValue().atStartOfDay()));
            
            // Step e: Update reservation status to "completed"
            currentReservation.setStatus("completed");
            
            if (!reservationDAO.updateReservation(currentReservation)) {
                // Rollback bike availability
                currentBike.setBikeAvailability(false);
                bikeDAO.updateBikeRecord(currentBike);
                showMessage("Failed to update reservation!", "error");
                return;
            }
            
            // If late, record late fee payment
            if (lblLateFee.getText().startsWith("₱")) {
                recordLateFee();
            }
            
            // Success!
            showReturnSummary();
            clearForm();
            currentReservation = null;
            currentBike = null;
            currentCustomer = null;
            txtReservationRef.clear();
            
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error processing return: " + e.getMessage(), "error");
        }
    }
    
    /**
     * Record late fee payment
     */
    private void recordLateFee() {
        try {
            String feeText = lblLateFee.getText();
            String amountStr = feeText.substring(1, feeText.indexOf("(")).trim();
            BigDecimal lateFee = new BigDecimal(amountStr);
            
            transactionRecordModel payment = new transactionRecordModel();
            payment.setCustomerAccountID(currentReservation.getCustomerAccID());
            payment.setReservationReferenceNum(currentReservation.getReservationReferenceNum());
            payment.setBikeID(currentReservation.getBikeID());
            payment.setBranchID(currentReservation.getBranchID());
            payment.setPaymentDate(Timestamp.valueOf(dateReturn.getValue().atStartOfDay()));
            payment.setPaymentAmount(lateFee);
            
            paymentDAO.addTransactionRecordData(payment);
            
        } catch (Exception e) {
            System.err.println("Failed to record late fee: " + e.getMessage());
        }
    }
    
    /**
     * Show return summary
     */
    private void showReturnSummary() {
        Alert summaryAlert = new Alert(Alert.AlertType.INFORMATION);
        summaryAlert.setTitle("Return Summary");
        summaryAlert.setHeaderText("Bike Return Completed");
        
        String summary = String.format(
            "Reservation #: %d\n" +
            "Customer: %s\n" +
            "Bike: %s\n" +
            "Return Date: %s\n" +
            "Late Fee: %s\n" +
            "Status: Completed\n\n" +
            "Notes: %s",
            currentReservation.getReservationReferenceNum(),
            lblCustomerName.getText(),
            lblBikeModel.getText(),
            dateReturn.getValue(),
            lblLateFee.getText(),
            txtNotes.getText().isEmpty() ? "None" : txtNotes.getText()
        );
        
        summaryAlert.setContentText(summary);
        summaryAlert.showAndWait();
        
        showMessage("✓ Bike returned successfully!", "success");
    }
    
    /**
     * Handle cancel button
     */
    @FXML
    private void handleCancel() {
        try {
            Stage stage = (Stage) txtReservationRef.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/grp5/view/userMainPage.fxml"));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Clear form
     */
    private void clearForm() {
        lblCustomerName.setText("-");
        lblBikeModel.setText("-");
        lblStartDate.setText("-");
        lblEndDate.setText("-");
        lblStatus.setText("-");
        lblStatus.setStyle("");
        lblLateFee.setText("-");
        txtNotes.clear();
        dateReturn.setValue(LocalDate.now());
        lblMessage.setText("");
    }
    
    /**
     * Show message
     */
    private void showMessage(String message, String type) {
        lblMessage.setText(message);
        
        if (type.equals("success")) {
            lblMessage.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
        } else if (type.equals("error")) {
            lblMessage.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        }
    }
}