package com.grp5.controller_gui;

import com.grp5.model.bikeReservation;
import com.grp5.model.customerRecordModel;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ArrayList;

public class Admin_transactionController {
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
     @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            loadAllPayments();
            return;
        }

        try {
            int transNum = Integer.parseInt(searchText);
            transactionRecordModel customer = transactionD.getTransactionRecordData(transNum);

            paymentLists.clear();
            if (customer != null) {
                paymentLists.add(customer);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found",
                        "No reservation found with ID: " + transNum);
            }
            paymentsTable.setItems(paymentLists);

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input",
                    "Please enter a valid reservation ID number.");
        }
    }

     @FXML
    private void handleUpdate() {
        transactionRecordModel selected = paymentsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a reservation to update.");
            return;
        }

        Dialog<transactionRecordModel> dialog = new Dialog<>();
        dialog.setTitle("Update Customer");
        dialog.setHeaderText("Update payment #" + selected.getPaymentReferenceNumber());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        DecimalFormat format=new DecimalFormat("#,##0.00");
    
        TextField txtPaymentAmt = new TextField();
        txtPaymentAmt.setText(format.format(selected.getPaymentAmount()));
        
        DatePicker paymentDate = new DatePicker();
        if (selected.getpaymentDate() != null) {
            paymentDate.setValue(selected.getpaymentDate().toLocalDateTime().toLocalDate());
        }

        grid.add(new Label("Payment Date:"), 0, 0);
        grid.add(paymentDate, 1, 0);
        grid.add(new Label("Payment Amount:"), 0, 1);
        grid.add(txtPaymentAmt, 1, 1);
        dialog.getDialogPane().setContent(grid);
        ButtonType btnUpdate = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnUpdate, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnUpdate) {
                selected.setPaymentAmount(new BigDecimal(txtPaymentAmt.getText()));
                if (paymentDate.getValue() != null) {
                    selected.setPaymentDate(Timestamp.valueOf(paymentDate.getValue().atStartOfDay()));
                }
                return selected;
            }
            return null;
        });

        Optional<transactionRecordModel> result = dialog.showAndWait();
        result.ifPresent(transaction -> {
            if (transactionD.modifyTransactionRecordData(transaction)==1) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Reservation updated successfully!");
                loadAllPayments();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to update reservation.");
            }
        });
    }

@FXML
    private void handleDelete() {
        transactionRecordModel selected = paymentsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a reservation to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Payment #" + selected.getPaymentReferenceNumber());
        confirmAlert.setContentText("Are you sure you want to delete this reservation?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (transactionD.delTransactionRecordData(selected.getPaymentReferenceNumber())==1) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Reservation deleted successfully!");
                loadAllPayments();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to delete reservation.");
            }
        }
    }
     @FXML
    private void handleViewDetails() {
        transactionRecordModel selected = paymentsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a reservation to view details.");
            return;
        }

        Alert detailsAlert = new Alert(Alert.AlertType.INFORMATION);
        detailsAlert.setTitle("Payment Details");
         detailsAlert.setHeaderText("Payment #" + selected.getPaymentReferenceNumber());

        String details = String.format(
                "Customer ID: %d%nBike ID: %d%nBranch ID: %d%nPayment Date: %s%nPayment Amount: %s%n",
                selected.getCustomerAccountID(),
                selected.getBikeID(),
                selected.getBrachID(),
                selected.getpaymentDate(),
                selected.getPaymentAmount());

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
