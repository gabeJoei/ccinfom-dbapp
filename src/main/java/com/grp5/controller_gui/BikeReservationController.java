package com.grp5.controller_gui;

import com.grp5.dao.bikeReservationDAO;
import com.grp5.model.bikeReservation;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ArrayList;

/**
 * Controller for Bike Reservation Management
 * Handles CRUD operations for bike reservations
 */
public class BikeReservationController {

    @FXML
    private TableView<bikeReservation> reservationTable;
    @FXML
    private TextField searchField;

    // Table columns
    @FXML
    private TableColumn<bikeReservation, Integer> colRefNum;
    @FXML
    private TableColumn<bikeReservation, Integer> colCustomer;
    @FXML
    private TableColumn<bikeReservation, Integer> colBike;
    @FXML
    private TableColumn<bikeReservation, Integer> colBranch;
    @FXML
    private TableColumn<bikeReservation, Timestamp> colStartDate;
    @FXML
    private TableColumn<bikeReservation, Timestamp> colEndDate;
    @FXML
    private TableColumn<bikeReservation, String> colStatus;

    private bikeReservationDAO reservationDAO;
    private ObservableList<bikeReservation> reservationList;

    /**
     * Initialize method - called automatically by JavaFX
     */
    @FXML
    public void initialize() {
        reservationDAO = new bikeReservationDAO();
        reservationList = FXCollections.observableArrayList();
        setupTableColumns();
        loadAllReservations();
    }

    /**
     * Setup table column bindings
     */
    private void setupTableColumns() {
        colRefNum.setCellValueFactory(new PropertyValueFactory<>("reservationReferenceNum"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerAccID"));
        colBike.setCellValueFactory(new PropertyValueFactory<>("bikeID"));
        colBranch.setCellValueFactory(new PropertyValueFactory<>("branchID"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Format status column with colors
        colStatus.setCellFactory(column -> new TableCell<bikeReservation, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status.toLowerCase()) {
                        case "pending":
                            setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                            break;
                        case "ongoing":
                            setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
                            break;
                        case "completed":
                            setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                            break;
                        case "cancelled":
                            setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
    }

    /**
     * Load all reservations into the table
     */
    private void loadAllReservations() {
        reservationList.clear();
        ArrayList<bikeReservation> reservations = reservationDAO.getAllReservations();
        reservationList.addAll(reservations);
        reservationTable.setItems(reservationList);
    }

    /**
     * Handle search button click
     */
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();

        if (searchText.isEmpty()) {
            loadAllReservations();
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
    }

    /**
     * Handle add reservation button
     */
    @FXML
    private void handleAdd() {
        Dialog<bikeReservation> dialog = new Dialog<>();
        dialog.setTitle("Add New Reservation");
        dialog.setHeaderText("Enter reservation details:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField txtCustomerID = new TextField();
        TextField txtBikeID = new TextField();
        TextField txtBranchID = new TextField();
        DatePicker dateStart = new DatePicker();
        DatePicker dateEnd = new DatePicker();
        ComboBox<String> cmbStatus = new ComboBox<>();

        txtCustomerID.setPromptText("Customer ID");
        txtBikeID.setPromptText("Bike ID");
        txtBranchID.setPromptText("Branch ID");
        dateStart.setPromptText("Start Date");
        dateEnd.setPromptText("End Date");
        cmbStatus.getItems().addAll("pending", "ongoing", "completed", "cancelled");
        cmbStatus.setValue("pending");

        grid.add(new Label("Customer ID:"), 0, 0);
        grid.add(txtCustomerID, 1, 0);
        grid.add(new Label("Bike ID:"), 0, 1);
        grid.add(txtBikeID, 1, 1);
        grid.add(new Label("Branch ID:"), 0, 2);
        grid.add(txtBranchID, 1, 2);
        grid.add(new Label("Start Date:"), 0, 3);
        grid.add(dateStart, 1, 3);
        grid.add(new Label("End Date:"), 0, 4);
        grid.add(dateEnd, 1, 4);
        grid.add(new Label("Status:"), 0, 5);
        grid.add(cmbStatus, 1, 5);

        dialog.getDialogPane().setContent(grid);
        ButtonType btnAdd = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnAdd, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnAdd) {
                try {
                    bikeReservation newReservation = new bikeReservation();
                    newReservation.setCustomerAccID(Integer.parseInt(txtCustomerID.getText()));
                    newReservation.setBikeID(Integer.parseInt(txtBikeID.getText()));
                    newReservation.setBranchID(Integer.parseInt(txtBranchID.getText()));
                    newReservation.setReservationDate(Timestamp.valueOf(LocalDateTime.now()));
                    newReservation.setStartDate(Timestamp.valueOf(dateStart.getValue().atStartOfDay()));
                    newReservation.setEndDate(Timestamp.valueOf(dateEnd.getValue().atStartOfDay()));
                    newReservation.setStatus(cmbStatus.getValue());
                    return newReservation;
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input",
                            "Please check all fields are filled correctly.");
                    return null;
                }
            }
            return null;
        });

        Optional<bikeReservation> result = dialog.showAndWait();
        result.ifPresent(reservation -> {
            if (reservationDAO.addReservation(reservation)) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Reservation added successfully!");
                loadAllReservations();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to add reservation.");
            }
        });
    }

    /**
     * Handle update reservation button
     */
    @FXML
    private void handleUpdate() {
        bikeReservation selected = reservationTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a reservation to update.");
            return;
        }

        Dialog<bikeReservation> dialog = new Dialog<>();
        dialog.setTitle("Update Reservation");
        dialog.setHeaderText("Update reservation #" + selected.getReservationReferenceNum());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<String> cmbStatus = new ComboBox<>();
        cmbStatus.getItems().addAll("pending", "ongoing", "completed", "cancelled");
        cmbStatus.setValue(selected.getStatus());

        DatePicker dateReturn = new DatePicker();
        if (selected.getDateReturned() != null) {
            dateReturn.setValue(selected.getDateReturned().toLocalDateTime().toLocalDate());
        }

        grid.add(new Label("Status:"), 0, 0);
        grid.add(cmbStatus, 1, 0);
        grid.add(new Label("Date Returned:"), 0, 1);
        grid.add(dateReturn, 1, 1);

        dialog.getDialogPane().setContent(grid);
        ButtonType btnUpdate = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnUpdate, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnUpdate) {
                selected.setStatus(cmbStatus.getValue());
                if (dateReturn.getValue() != null) {
                    selected.setDateReturned(Timestamp.valueOf(dateReturn.getValue().atStartOfDay()));
                }
                return selected;
            }
            return null;
        });

        Optional<bikeReservation> result = dialog.showAndWait();
        result.ifPresent(reservation -> {
            if (reservationDAO.updateReservation(reservation)) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Reservation updated successfully!");
                loadAllReservations();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to update reservation.");
            }
        });
    }

    /**
     * Handle delete reservation button
     */
    @FXML
    private void handleDelete() {
        bikeReservation selected = reservationTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a reservation to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Reservation #" + selected.getReservationReferenceNum());
        confirmAlert.setContentText("Are you sure you want to delete this reservation?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (reservationDAO.deleteReservation(selected.getReservationReferenceNum())) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Reservation deleted successfully!");
                loadAllReservations();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to delete reservation.");
            }
        }
    }

    /**
     * Handle view details button
     */
    @FXML
    private void handleViewDetails() {
        bikeReservation selected = reservationTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a reservation to view details.");
            return;
        }

        Alert detailsAlert = new Alert(Alert.AlertType.INFORMATION);
        detailsAlert.setTitle("Reservation Details");
        detailsAlert.setHeaderText("Reservation #" + selected.getReservationReferenceNum());

        String details = String.format(
                "Customer ID: %d%nBike ID: %d%nBranch ID: %d%nReservation Date: %s%nStart Date: %s%nEnd Date: %s%nDate Returned: %s%nStatus: %s",
                selected.getCustomerAccID(),
                selected.getBikeID(),
                selected.getBranchID(),
                selected.getReservationDate(),
                selected.getStartDate(),
                selected.getEndDate(),
                selected.getDateReturned() != null ? selected.getDateReturned() : "Not yet returned",
                selected.getStatus());

        detailsAlert.setContentText(details);
        detailsAlert.showAndWait();
    }

    /**
     * Handle back button - return to main menu
     */
    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) reservationTable.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/grp5/view/userMainPage.fxml"));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to return to main menu.");
        }
    }

    /**
     * Utility method to show alerts
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
