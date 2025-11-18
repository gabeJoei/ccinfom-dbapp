package com.grp5.controller_gui;

import com.grp5.model.transactionRecordModel;
import com.grp5.dao.transactionRecordDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.*;
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Optional;
import java.util.ArrayList;

public class Admin_transactionController {
    @FXML
    private TableView<transactionRecordModel> paymentsTable;
    @FXML
    private TextField searchField;
    @FXML
    private Button btnAdd, btnUpdate, btnView, btnEnter, btnDelete;
    @FXML
    private TableColumn<transactionRecordModel, Integer> colRefNum;
    @FXML
    private TableColumn<transactionRecordModel, Integer> colCustomer;
    @FXML
    private TableColumn<transactionRecordModel, Integer> colReservation;
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
        transactionD = new transactionRecordDAO();
        paymentLists = FXCollections.observableArrayList();
        setupTableColumns();
        loadAllPayments();
    }

    private void setupTableColumns() {
        colRefNum.setCellValueFactory(new PropertyValueFactory<>("paymentReferenceNumber"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerAccountID"));
        colReservation.setCellValueFactory(new PropertyValueFactory<>("reservationReferenceNumber"));
        colBike.setCellValueFactory(new PropertyValueFactory<>("bikeID"));
        colBranch.setCellValueFactory(new PropertyValueFactory<>("branchID"));
        colPaymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colPaymentAmount.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
    }

    private void loadAllPayments() {
        paymentLists.clear();
        ArrayList<transactionRecordModel> transaction = transactionD.getAllPayment();
        paymentLists.addAll(transaction);
        paymentsTable.setItems(paymentLists);
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            loadAllPayments();
            return;
        }

        try {
            int paymentRefNum = Integer.parseInt(searchText);
            transactionRecordModel payment = transactionD.getTransactionRecordData(paymentRefNum);

            paymentLists.clear();
            if (payment != null) {
                paymentLists.add(payment);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found",
                        "No payment found with Reference Number: " + paymentRefNum);
            }
            paymentsTable.setItems(paymentLists);

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input",
                    "Please enter a valid payment reference number.");
        }
        searchField.clear();
    }

    @FXML
    private void handleUpdate() {
        transactionRecordModel selected = paymentsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a payment to update.");
            return;
        }

        Dialog<transactionRecordModel> dialog = new Dialog<>();
        dialog.setTitle("Update Payment");
        dialog.setHeaderText("Update Payment Reference #" + selected.getPaymentReferenceNumber());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField txtCustomerID = new TextField(String.valueOf(selected.getCustomerAccountID()));
        TextField txtReservationRef = new TextField(String.valueOf(selected.getReservationReferenceNumber()));
        TextField txtBikeID = new TextField(String.valueOf(selected.getBikeID()));
        TextField txtBranchID = new TextField(String.valueOf(selected.getBranchID()));
        
        DatePicker paymentDatePicker = new DatePicker();
        if (selected.getPaymentDate() != null) {
            paymentDatePicker.setValue(selected.getPaymentDate().toLocalDateTime().toLocalDate());
        }

        DecimalFormat format = new DecimalFormat("#,##0.00");
        TextField txtPaymentAmt = new TextField(format.format(selected.getPaymentAmount()));

        grid.add(new Label("Customer ID:"), 0, 0);
        grid.add(txtCustomerID, 1, 0);
        grid.add(new Label("Reservation Ref:"), 0, 1);
        grid.add(txtReservationRef, 1, 1);
        grid.add(new Label("Bike ID:"), 0, 2);
        grid.add(txtBikeID, 1, 2);
        grid.add(new Label("Branch ID:"), 0, 3);
        grid.add(txtBranchID, 1, 3);
        grid.add(new Label("Payment Date:"), 0, 4);
        grid.add(paymentDatePicker, 1, 4);
        grid.add(new Label("Payment Amount:"), 0, 5);
        grid.add(txtPaymentAmt, 1, 5);

        dialog.getDialogPane().setContent(grid);
        ButtonType btnUpdate = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnUpdate, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnUpdate) {
                try {
                    selected.setCustomerAccountID(Integer.parseInt(txtCustomerID.getText().trim()));
                    selected.setReservationReferenceNum(Integer.parseInt(txtReservationRef.getText().trim()));
                    selected.setBikeID(Integer.parseInt(txtBikeID.getText().trim()));
                    selected.setBranchID(Integer.parseInt(txtBranchID.getText().trim()));
                    
                    if (paymentDatePicker.getValue() != null) {
                        selected.setPaymentDate(Timestamp.valueOf(paymentDatePicker.getValue().atStartOfDay()));
                    }
                    
                    // Parse amount, removing commas
                    String amountStr = txtPaymentAmt.getText().trim().replace(",", "");
                    selected.setPaymentAmount(new BigDecimal(amountStr));
                    
                    return selected;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numeric values.");
                    return null;
                }
            }
            return null;
        });

        Optional<transactionRecordModel> result = dialog.showAndWait();
        result.ifPresent(transaction -> {
            if (transactionD.modifyTransactionRecordData(transaction) == 1) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Payment updated successfully!");
                loadAllPayments();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to update payment.");
            }
        });
    }

    @FXML
    private void handleDelete() {
        transactionRecordModel selected = paymentsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a payment to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Payment Reference #" + selected.getPaymentReferenceNumber());
        confirmAlert.setContentText("Are you sure you want to delete this payment?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (transactionD.delTransactionRecordData(selected.getPaymentReferenceNumber()) == 1) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Payment deleted successfully!");
                loadAllPayments();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to delete payment.");
            }
        }
    }

    @FXML
    private void handleViewDetails() {
        transactionRecordModel selected = paymentsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a payment to view details.");
            return;
        }

        Alert detailsAlert = new Alert(Alert.AlertType.INFORMATION);
        detailsAlert.setTitle("Payment Details");
        detailsAlert.setHeaderText("Payment Reference #" + selected.getPaymentReferenceNumber());

        DecimalFormat format = new DecimalFormat("#,##0.00");
        String details = String.format(
                "Customer ID: %d\nReservation Reference: %d\nBike ID: %d\nBranch ID: %d\nPayment Date: %s\nPayment Amount: ₱%s",
                selected.getCustomerAccountID(),
                selected.getReservationReferenceNumber(),
                selected.getBikeID(),
                selected.getBranchID(),
                selected.getPaymentDate(),
                format.format(selected.getPaymentAmount()));

        detailsAlert.setContentText(details);
        detailsAlert.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}