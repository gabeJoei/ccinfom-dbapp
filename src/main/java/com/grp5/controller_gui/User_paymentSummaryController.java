package com.grp5.controller_gui;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import com.grp5.dao.bikeRecordDAO;
import com.grp5.dao.bikeReservationDAO;
import com.grp5.dao.branchRecordDAO;
import com.grp5.dao.customerRecordDAO;
import com.grp5.dao.transactionRecordDAO;
import com.grp5.model.bikeRecordModel;
import com.grp5.model.bikeReservation;
import com.grp5.model.branchRecordModel;
import com.grp5.model.customerRecordModel;
import com.grp5.model.transactionRecordModel;
import com.grp5.utils.generalUtilities;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class User_paymentSummaryController {
    
    @FXML
    private Text customerNameTxt;
    @FXML
    private Text bikeModelTxt;
    @FXML
    private Text branchTxt;
    @FXML
    private Text phoneNumTxt;
    @FXML
    private Text startDateTxt;
    @FXML
    private Text endDateTxt;
    @FXML
    private Text totalPriceTxt;

    private customerRecordDAO customerDAO;
    private bikeRecordDAO bikeDAO;
    private branchRecordDAO branchDAO;
    private transactionRecordDAO transactionDAO;
    private bikeReservationDAO reservationDAO;

    private bikeReservation reservation;
    private BigDecimal priceToPay;

    private customerRecordModel user;
    private bikeRecordModel bike;
    private branchRecordModel branch;

    public void init(bikeReservation reservation, BigDecimal priceToPay) {
        this.reservation = reservation;
        this.priceToPay = priceToPay;

        // load DAOs 
        customerDAO = new customerRecordDAO();
        bikeDAO = new bikeRecordDAO();
        branchDAO = new branchRecordDAO();
        transactionDAO = new transactionRecordDAO();
        reservationDAO = new bikeReservationDAO();

        // get user, bike, and branch information
        user = customerDAO.getCustomer(reservation.getCustomerAccID());
        bike = bikeDAO.getBikeRecord(reservation.getBikeID());
        branch = branchDAO.getBranchRecordData(bike.getBranchIDNum());

        // display text information
        customerNameTxt.setText(user.getFirstName() + " " + user.getLastName());
        bikeModelTxt.setText(bike.getBikeModel());
        branchTxt.setText(branch.getBranchAddress());
        phoneNumTxt.setText(user.getPhoneNum());
        startDateTxt.setText(reservation.getStartDate().toLocalDateTime().toLocalDate().toString());
        endDateTxt.setText(reservation.getEndDate().toLocalDateTime().toLocalDate().toString());
        totalPriceTxt.setText(String.format("₱%.2f", priceToPay));
    }

    @FXML
    protected void onPayClick() {

        if (confirmPayment()) {

            // ADD reservation to database
            int reservationID = reservationDAO.addReservation(reservation);

            // Check if adding was successful 
            if (reservationID == 0) {
                generalUtilities.showAlert(Alert.AlertType.ERROR, "Error", "Failed to create reservation!");
                return;
            }

            // update object reservationID
            System.out.println("Reservation Added");
            reservation.setReservationReferenceNum(reservationID);

            // Create payment transaction record
            transactionRecordModel transaction = new transactionRecordModel(
                user.getCustomerAccID(), 
                reservation.getReservationReferenceNum(), 
                branch.getBranchID(), 
                bike.getBikeID(), 
                reservation.getStartDate(), 
                reservation.getEndDate(), 
                Timestamp.valueOf(LocalDateTime.now()), 
                priceToPay, // change this pa once hourly/daily rate is implemented
                priceToPay, // change this pa once hourly/daily rate is implemented
                priceToPay
            );
            transactionDAO.addTransactionRecordData(transaction);

            goToRentPage();
        }
    }

    private boolean confirmPayment() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Payment Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Press OK to confirm your payment.");

        // --- 3. Show the alert and WAIT for the user's response ---
        Optional<ButtonType> result = alert.showAndWait();

        return (result.isPresent() && result.get() == ButtonType.OK);
    }

    private void goToRentPage() {
        try {
            AnchorPane dashboardContentArea = (AnchorPane) totalPriceTxt.getScene().lookup("#contentArea");
            
            if (dashboardContentArea != null) {
                // Load the User_rentBike.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grp5/view/User_rentBike.fxml"));
                AnchorPane newPane = loader.load();

                // Set the dashboard's content to the rent page
                dashboardContentArea.getChildren().setAll(newPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
            generalUtilities.showAlert(Alert.AlertType.ERROR, "Error", "Could not load bike rental page.");
        }
    }
}
