package com.grp5.controller_gui;

import com.grp5.model.bikeReservation;
import com.grp5.model.customerRecordModel;
import com.grp5.model.transactionRecordModel;
import com.grp5.dao.bikeReservationDAO;
import com.grp5.dao.customerRecordDAO;
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
import java.util.List;

public class Admin_customerController {
    @FXML
    private TableView<customerRecordModel> customerTable;
    @FXML
    private TextField searchField;
    @FXML
    private Button btnAdd,btnView,btnEnter;
     @FXML
    private TableColumn<customerRecordModel, Integer> colCusId;
    @FXML
    private TableColumn<customerRecordModel, String> colFirstN;
    @FXML
    private TableColumn<customerRecordModel, String> colLastN;
    @FXML
    private TableColumn<customerRecordModel, String> colCustomerEmail;
    @FXML
    private TableColumn<customerRecordModel, String> colPhoneNumber;
 
    private customerRecordDAO customerD;
    private ObservableList<customerRecordModel> customerLists;

    @FXML
    public void initialize() {
        customerD= new customerRecordDAO();
        customerLists = FXCollections.observableArrayList();
        setupTableColumns();
        loadAllCustomers();
    }


     private void setupTableColumns() {
        colCusId.setCellValueFactory(new PropertyValueFactory<>("customerAccID"));
        colFirstN.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastN.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colCustomerEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        
    }
    private void loadAllCustomers(){
        customerLists.clear();
        List<customerRecordModel> customer= customerD.getAllCustomers();
        customerLists.addAll(customer);
        customerTable.setItems(customerLists);
    }
     
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();

        if (searchText.isEmpty()) {
            loadAllCustomers();
            return;
        }

        try {
            int cusNum = Integer.parseInt(searchText);
            customerRecordModel customer = customerD.getCustomer(cusNum);

            customerLists.clear();
            if (customer != null) {
                customerLists.add(customer);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found",
                        "No reservation found with ID: " + cusNum);
            }
            customerTable.setItems(customerLists);

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input",
                    "Please enter a valid reservation ID number.");
        }
    }

     @FXML
    private void handleUpdate() {
        customerRecordModel selected = customerTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a reservation to update.");
            return;
        }

        Dialog<customerRecordModel> dialog = new Dialog<>();
        dialog.setTitle("Update Customer");
        dialog.setHeaderText("Update customer #" + selected.getCustomerAccID());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField txtFirstName = new TextField();
        txtFirstName.setText(selected.getFirstName());
        TextField txtLastName=new TextField();
        txtLastName.setText(selected.getLastName());
        TextField txtEmail=new TextField();
        txtEmail.setText(selected.getEmail());
        TextField txtPhoneNum=new TextField();
        txtPhoneNum.setText(selected.getPhoneNum());

        
        grid.add(new Label("First Name:"), 0, 0);
        grid.add(txtFirstName, 1, 0);

        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(txtLastName, 1, 1);

        grid.add(new Label("Email:"), 0, 2);
        grid.add(txtEmail, 1, 2);
        grid.add(new Label("Phone Number:"), 0, 3);
        grid.add(txtPhoneNum, 1, 3);

        dialog.getDialogPane().setContent(grid);
        ButtonType btnUpdate = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnUpdate, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnUpdate) {
                selected.setFirstName(txtFirstName.getText());
                selected.setLastName(txtLastName.getText());
                selected.setEmail(txtEmail.getText());
                selected.setPhoneNum(txtPhoneNum.getText());
                return selected;
            }
            return null;
        });

        Optional<customerRecordModel> result = dialog.showAndWait();
        result.ifPresent(customer -> {
            if (customerD.modifyCustomerRecordData(customer)) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Reservation updated successfully!");
                loadAllCustomers();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to update reservation.");
            }
        });
    }

@FXML
    private void handleDelete() {
        customerRecordModel selected = customerTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a reservation to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Customer #" + selected.getCustomerAccID());
        confirmAlert.setContentText("Are you sure you want to delete this reservation?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (customerD.delCustomerRecordData(selected.getCustomerAccID())) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Reservation deleted successfully!");
                loadAllCustomers();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to delete reservation.");
            }
        }
    }
     @FXML
    private void handleViewDetails() {
        customerRecordModel selected = customerTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a reservation to view details.");
            return;
        }

        Alert detailsAlert = new Alert(Alert.AlertType.INFORMATION);
        detailsAlert.setTitle("Customer Details");
         detailsAlert.setHeaderText("Customer #" + selected.getCustomerAccID());

        String details = String.format(
                "First Name: %s%nLast Name: %s%nEmail Address: %s%nPhone Number: %s%n",
               
                selected.getFirstName(),
                selected.getLastName(),
                selected.getEmail(),
                selected.getPhoneNum());

        detailsAlert.setContentText(details);
        detailsAlert.showAndWait();
    }

 public void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

