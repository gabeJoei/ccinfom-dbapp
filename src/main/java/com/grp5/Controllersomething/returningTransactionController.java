package com.grp5.Controllersomething;

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

/**
 * Returning Transaction Controller (
 */
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
    @FXML private Button btnLoad;
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
        
        btnLoad.setOnAction(e -> handleLoadReservation());
        btnProcessReturn.setOnAction(e -> handleProcessReturn());
        btnCancel.setOnAction(e -> handleCancel());
        dateReturn.setOnAction(e -> calculateLateFee());
    }
    
    @FXML
    private void handleLoadReservation() {
        String refText = txtReservationRef.getText().trim();
        
        if (refText.isEmpty()) {
            showMessage("Please enter a reservation reference number", "error");
            return;
        }
        
        try {
            int refNum = Integer.parseInt(refText);
            
            // Check reservation record
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
            
            //  Get customer and bike info
            currentCustomer = customerDAO.getCustomer(currentReservation.getCustomerAccID());
            currentBike = bikeDAO.getBikeRecord(currentReservation.getBikeID());
            
            loadReservationDetails();
            calculateLateFee();
            showMessage("Reservation loaded successfully!", "success");
            
        } catch (NumberFormatException e) {
            showMessage("Invalid reservation reference number!", "error");
        }
    }
    
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
    
    private void calculateLateFee() {
        if (currentReservation == null) return;
        
        LocalDate endDate = currentReservation.getEndDate().toLocalDateTime().toLocalDate();
        LocalDate returnDate = dateReturn.getValue();
        
        long daysLate = ChronoUnit.DAYS.between(endDate, returnDate);
        
        if (daysLate > 0) {
            BigDecimal lateFee = new BigDecimal(daysLate * 50);
            lblLateFee.setText(String.format("₱%.2f (%d days late)", lateFee, daysLate));
            lblLateFee.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        } else {
            lblLateFee.setText("No late fee");
            lblLateFee.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
        }
    }
    
    @FXML
    private void handleProcessReturn() {
        if (currentReservation == null) {
            showMessage("Please load a reservation first!", "error");
            return;
        }
        
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
    
    private void processReturn() {
        try {
            //  Update bike availability to true
            int result = bikeDAO.updateBikeAvailability(currentBike, true);
            if (result == 0) {
                showMessage("Failed to update bike availability!", "error");
                return;
            }
            
            // Record return date
            currentReservation.setDateReturned(Timestamp.valueOf(dateReturn.getValue().atStartOfDay()));
            
            // Update reservation status to "completed"
            currentReservation.setStatus("completed");
            
            if (!reservationDAO.updateReservation(currentReservation)) {
                // Rollback bike availability
                bikeDAO.updateBikeAvailability(currentBike, false);
                showMessage("Failed to update reservation!", "error");
                return;
            }
            
            // If late, record late fee payment
            if (lblLateFee.getText().startsWith("₱")) {
                recordLateFee();
            }
            
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
        
        showMessage(" Bike returned successfully!", "success");
    }
    
    @FXML
    private void handleCancel() {
        try {
            Stage stage = (Stage) txtReservationRef.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/grp5/view/userMainPage.fxml")); //to be changed later
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
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
    
    private void showMessage(String message, String type) {
        lblMessage.setText(message);
        
        if (type.equals("success")) {
            lblMessage.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
        } else if (type.equals("error")) {
            lblMessage.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        }
    }
}