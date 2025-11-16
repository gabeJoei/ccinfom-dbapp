package com.grp5.controller_gui;



import com.grp5.model.bikeReservation;
import com.grp5.model.transactionRecordModel;
import com.grp5.dao.bikeReservationDAO;
import com.grp5.dao.transactionRecordDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ArrayList;

public class Admin_transaction {
    @FXML
    private TableView<transactionRecordModel> paymentsTable;
    @FXML
    private TextField searchField;
    @FXML
    private Button btnAdd,btnUpdate,btnView,btnEnter;
     @FXML
    private TableColumn<transactionRecordModel, Integer> colRefNum;
    @FXML
    private TableColumn<transactionRecordModel, Integer> colCustomer;
    @FXML
    private TableColumn<transactionRecordModel, Integer> colBike;
    @FXML
    private TableColumn<transactionRecordModel, Integer> colBranch;
    @FXML
    private TableColumn<transactionRecordModel, Timestamp> colPaymentDate;
    @FXML
    private TableColumn<transactionRecordModel, BigDecimal> colPaymentAmount;
   
    private transactionRecordDAO transactionD;
    private ObservableList<transactionRecordModel> paymentLists;

    @FXML
    public void initialize() {
        transactionD= new transactionRecordDAO();
        paymentLists = FXCollections.observableArrayList();
        setupTableColumns();
        loadAllPayments();
    }


     private void setupTableColumns() {
        colRefNum.setCellValueFactory(new PropertyValueFactory<>("paymentReferenceNum"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerAccID"));
        colBike.setCellValueFactory(new PropertyValueFactory<>("bikeID"));
        colBranch.setCellValueFactory(new PropertyValueFactory<>("branchID"));
        colPaymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colPaymentAmount.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        
    }
    private void loadAllPayments(){
        paymentLists.clear();
        ArrayList<transactionRecordModel> transaction = transactionD.getAllPayment();
        paymentLists.addAll(transaction);
        paymentsTable.setItems(paymentLists);
    }
    /* 
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();

        if (searchText.isEmpty()) {
            loadAllPayments();
            return;
        }

        try {
            int refNum = Integer.parseInt(searchText);
            bikeReservation reservation = reservationDAO.getReservation(refNum);

            reservationList.clear();
            if (reservation != null) {
                reservationList.add(reservation);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found",
                        "No reservation found with ID: " + refNum);
            }
            reservationTable.setItems(reservationList);

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input",
                    "Please enter a valid reservation ID number.");
        }
    }*/

}
