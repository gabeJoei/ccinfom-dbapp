package com.grp5.controller_gui;

import com.grp5.dao.bikeReservationDAO;
import com.grp5.model.bikeReservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class ReservationController {

    @FXML
    private TextField reservationIDTextField;

    @FXML
    private Button enterButton;

    @FXML
    private TableView<bikeReservation> reservationTableView;

    @FXML
    private TableColumn<bikeReservation, Integer> refNumColumn;

    @FXML
    private TableColumn<bikeReservation, Integer> customerIDColumn;

    @FXML
    private TableColumn<bikeReservation, Integer> bikeIDColumn;

    @FXML
    private TableColumn<bikeReservation, Integer> branchIDColumn;

    @FXML
    private TableColumn<bikeReservation, Timestamp> reservationDateColumn;

    @FXML
    private TableColumn<bikeReservation, Timestamp> startDateColumn;

    @FXML
    private TableColumn<bikeReservation, Timestamp> endDateColumn;

    @FXML
    private TableColumn<bikeReservation, Timestamp> returnDateColumn;

    @FXML
    private TableColumn<bikeReservation, String> statusColumn;

    @FXML
    private Button updateButton;

    @FXML
    private Button viewDetailsButton;

    @FXML
    private Button deleteButton;

    private bikeReservationDAO reservationDAO = new bikeReservationDAO();
    private ObservableList<bikeReservation> reservationList = FXCollections.observableArrayList();

   
    @FXML
    public void initialize() {
        refNumColumn.setCellValueFactory(new PropertyValueFactory<>("reservationReferenceNum"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerAccID"));
        bikeIDColumn.setCellValueFactory(new PropertyValueFactory<>("bikeID"));
        branchIDColumn.setCellValueFactory(new PropertyValueFactory<>("branchID"));
        reservationDateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateReturned"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadReservationData();
    }

    
    private void loadReservationData() {
        ArrayList<bikeReservation> allReservations = reservationDAO.getAllReservations();

        reservationList.clear();
        reservationList.addAll(allReservations);

        reservationTableView.setItems(reservationList);
    }

    @FXML
    void handleEnterButtonAction(ActionEvent event) {
        try {
            String idText = reservationIDTextField.getText().trim();
            if (idText.isEmpty()) {
                loadReservationData(); 
                return;
            }

            int reservationRefNum = Integer.parseInt(idText);
            bikeReservation reservation = reservationDAO.getReservation(reservationRefNum);

            reservationList.clear();
            if (reservation != null) {
                reservationList.add(reservation);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Search Result",
                            "No reservation found with Reference Number: " + reservationRefNum);
                loadReservationData(); 
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid numeric Reservation Reference Number.");
        }
        reservationIDTextField.clear();
    }

    @FXML
    void handleUpdateButtonAction(ActionEvent event) {
        bikeReservation selectedReservation = reservationTableView.getSelectionModel().getSelectedItem();

        if (selectedReservation == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a reservation record to update.");
            return;
        }

        Dialog<bikeReservation> dialog = new Dialog<>();
        dialog.setTitle("Update Reservation Record");
        dialog.setHeaderText("Update Reservation Ref: " + selectedReservation.getReservationReferenceNum());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField txtCustomerID = new TextField(String.valueOf(selectedReservation.getCustomerAccID()));
        TextField txtBikeID = new TextField(String.valueOf(selectedReservation.getBikeID()));
        TextField txtBranchID = new TextField(String.valueOf(selectedReservation.getBranchID()));
        
        // Format timestamps for display
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TextField txtStartDate = new TextField(selectedReservation.getStartDate() != null ? 
            selectedReservation.getStartDate().toLocalDateTime().format(formatter) : "");
        TextField txtEndDate = new TextField(selectedReservation.getEndDate() != null ? 
            selectedReservation.getEndDate().toLocalDateTime().format(formatter) : "");
        TextField txtReturnDate = new TextField(selectedReservation.getDateReturned() != null ? 
            selectedReservation.getDateReturned().toLocalDateTime().format(formatter) : "");
        
        ComboBox<String> cmbStatus = new ComboBox<>();
        cmbStatus.getItems().addAll("ongoing", "completed", "cancelled");
        cmbStatus.setValue(selectedReservation.getStatus());

        grid.add(new Label("Customer ID:"), 0, 0);
        grid.add(txtCustomerID, 1, 0);
        grid.add(new Label("Bike ID:"), 0, 1);
        grid.add(txtBikeID, 1, 1);
        grid.add(new Label("Branch ID:"), 0, 2);
        grid.add(txtBranchID, 1, 2);
        grid.add(new Label("Start Date:"), 0, 3);
        grid.add(txtStartDate, 1, 3);
        grid.add(new Label("End Date:"), 0, 4);
        grid.add(txtEndDate, 1, 4);
        grid.add(new Label("Return Date:"), 0, 5);
        grid.add(txtReturnDate, 1, 5);
        grid.add(new Label("Status:"), 0, 6);
        grid.add(cmbStatus, 1, 6);

        // Add format hint
        grid.add(new Label("(Format: yyyy-MM-dd HH:mm:ss)"), 1, 7);

        dialog.getDialogPane().setContent(grid);

        ButtonType btnUpdate = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnUpdate, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnUpdate) {
                try {
                    int newCustomerID = Integer.parseInt(txtCustomerID.getText().trim());
                    int newBikeID = Integer.parseInt(txtBikeID.getText().trim());
                    int newBranchID = Integer.parseInt(txtBranchID.getText().trim());
                    
                    Timestamp newStartDate = Timestamp.valueOf(LocalDateTime.parse(txtStartDate.getText().trim(), formatter));
                    Timestamp newEndDate = Timestamp.valueOf(LocalDateTime.parse(txtEndDate.getText().trim(), formatter));
                    Timestamp newReturnDate = null;
                    if (!txtReturnDate.getText().trim().isEmpty()) {
                        newReturnDate = Timestamp.valueOf(LocalDateTime.parse(txtReturnDate.getText().trim(), formatter));
                    }

                    selectedReservation.setCustomerAccID(newCustomerID);
                    selectedReservation.setBikeID(newBikeID);
                    selectedReservation.setBranchID(newBranchID);
                    selectedReservation.setStartDate(newStartDate);
                    selectedReservation.setEndDate(newEndDate);
                    selectedReservation.setDateReturned(newReturnDate);
                    selectedReservation.setStatus(cmbStatus.getValue());

                    return selectedReservation;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "IDs must be valid numbers.");
                    return null; 
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", 
                        "Invalid date format. Please use: yyyy-MM-dd HH:mm:ss\nExample: 2024-12-31 14:30:00");
                    return null;
                }
            }
            return null;
        });

        Optional<bikeReservation> result = dialog.showAndWait();
        result.ifPresent(reservation -> {
            if (reservationDAO.updateReservation(reservation)) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Reservation record updated successfully!");
                loadReservationData();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to update reservation record. Please check all fields.");
            }
        });
    }

    @FXML
    void handleViewDetailsButtonAction(ActionEvent event) {
        bikeReservation selectedReservation = reservationTableView.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            String details = String.format(
                "Reference Number: %d\n" +
                "Customer ID: %d\n" +
                "Bike ID: %d\n" +
                "Branch ID: %d\n" +
                "Reservation Date: %s\n" +
                "Start Date: %s\n" +
                "End Date: %s\n" +
                "Return Date: %s\n" +
                "Status: %s",
                selectedReservation.getReservationReferenceNum(),
                selectedReservation.getCustomerAccID(),
                selectedReservation.getBikeID(),
                selectedReservation.getBranchID(),
                selectedReservation.getReservationDate(),
                selectedReservation.getStartDate(),
                selectedReservation.getEndDate(),
                selectedReservation.getDateReturned() != null ? selectedReservation.getDateReturned() : "Not returned",
                selectedReservation.getStatus()
            );
            showAlert(Alert.AlertType.INFORMATION, "Reservation Details", details);
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a reservation record to view details.");
        }
    }

    @FXML
    void handleDeleteButtonAction(ActionEvent event) {
        bikeReservation selectedReservation = reservationTableView.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Deletion");
            confirmAlert.setHeaderText("Delete Reservation Record");
            confirmAlert.setContentText("Are you sure you want to delete reservation #" + 
                selectedReservation.getReservationReferenceNum() + "?");
            
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean deleted = reservationDAO.deleteReservation(selectedReservation.getReservationReferenceNum());
                if (deleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Reservation record deleted successfully.");
                    loadReservationData();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failure", "Failed to delete reservation record.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a reservation record to delete.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}