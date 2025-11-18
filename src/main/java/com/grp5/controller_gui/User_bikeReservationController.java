package com.grp5.controller_gui;

import com.grp5.dao.bikeReservationDAO;
import com.grp5.model.bikeReservation;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ArrayList;

/**
 * Controller for User's Bike Reservation Management
 * Shows only the logged-in user's reservations
 */
public class User_bikeReservationController {

    @FXML
    private TableView<bikeReservation> reservationTable;
    @FXML
    private TextField searchField;

    // Table columns
    @FXML
    private TableColumn<bikeReservation, Integer> colRefNum;
    @FXML
    private TableColumn<bikeReservation, Integer> colBike;
    @FXML
    private TableColumn<bikeReservation, Integer> colBranch;
    @FXML
    private TableColumn<bikeReservation, Timestamp> colReservationDate;
    @FXML
    private TableColumn<bikeReservation, Timestamp> colStartDate;
    @FXML
    private TableColumn<bikeReservation, Timestamp> colEndDate;
    @FXML
    private TableColumn<bikeReservation, Timestamp> colReturnDate;
    @FXML
    private TableColumn<bikeReservation, String> colStatus;

    private bikeReservationDAO reservationDAO;
    private ObservableList<bikeReservation> reservationList;
    private String userID;

    /**
     * Initialize method - called automatically by JavaFX
     */
    @FXML
    public void initialize() {
        reservationDAO = new bikeReservationDAO();
        reservationList = FXCollections.observableArrayList();
        setupTableColumns();
        
        // Get user ID from the dashboard
        getUserIDFromDashboard();
        
        // Load only this user's reservations
        loadUserReservations();
    }

    /**
     * Get the logged-in user's ID from the dashboard
     */
    private void getUserIDFromDashboard() {
        
        try {
            Text userIdText = (Text) reservationTable.getScene().lookup("#txtUserID");
            if (userIdText != null) {
                userID = userIdText.getText();
                System.out.println(userID);
            } else {
                userID = "0"; // Default fallback
                showAlert(Alert.AlertType.WARNING, "Warning", "Could not retrieve user ID.");
            }
        } catch (Exception e) {
            userID = "0";
            System.err.println("Error getting user ID: " + e.getMessage());
        }
    }

    /**
     * Setup table column bindings
     */
    private void setupTableColumns() {
        colRefNum.setCellValueFactory(new PropertyValueFactory<>("reservationReferenceNum"));
        colBike.setCellValueFactory(new PropertyValueFactory<>("bikeID"));
        colBranch.setCellValueFactory(new PropertyValueFactory<>("branchID"));
        colReservationDate.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("dateReturned"));
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
     * Load only the logged-in user's reservations
     */
    private void loadUserReservations() {
        reservationList.clear();
        
        if (userID == null || userID.equals("0")) {
            showAlert(Alert.AlertType.WARNING, "Warning", "User ID not found. Cannot load reservations.");
            return;
        }

        try {
            int customerID = Integer.parseInt(userID);
            ArrayList<bikeReservation> allReservations = reservationDAO.getAllReservations();
            
            // Filter to show only this user's reservations
            for (bikeReservation reservation : allReservations) {
                if (reservation.getCustomerAccID() == customerID) {
                    reservationList.add(reservation);
                }
            }
            
            reservationTable.setItems(reservationList);
            
            if (reservationList.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Reservations", 
                    "You don't have any reservations yet.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid user ID format.");
        }
    }

    /**
     * Handle search button click
     */
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();

        if (searchText.isEmpty()) {
            loadUserReservations();
            return;
        }

        try {
            int refNum = Integer.parseInt(searchText);
            bikeReservation reservation = reservationDAO.getReservation(refNum);

            reservationList.clear();
            
            // Only show if the reservation belongs to this user
            if (reservation != null && String.valueOf(reservation.getCustomerAccID()).equals(userID)) {
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
        searchField.clear();
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
                "Bike ID: %d\nBranch ID: %d\nReservation Date: %s\nStart Date: %s\nEnd Date: %s\nDate Returned: %s\nStatus: %s",
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

        

   
    @FXML
    private void handleUpdateReservation() {
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
                loadUserReservations();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to update reservation.");
            }
        });
    }

    /**
     * Handle cancel reservation button
     * Users can only cancel pending reservations
     */
    @FXML
    private void handleCancelReservation() {
        bikeReservation selected = reservationTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a reservation to cancel.");
            return;
        }

        // Only allow cancelling pending or ongoing reservations
        if (!selected.getStatus().equalsIgnoreCase("pending") && 
            !selected.getStatus().equalsIgnoreCase("ongoing")) {
            showAlert(Alert.AlertType.WARNING, "Cannot Cancel",
                    "Only pending or ongoing reservations can be cancelled.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Cancellation");
        confirmAlert.setHeaderText("Cancel Reservation #" + selected.getReservationReferenceNum());
        confirmAlert.setContentText("Are you sure you want to cancel this reservation?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            selected.setStatus("cancelled");
            if (reservationDAO.updateReservation(selected)) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Reservation cancelled successfully!");
                loadUserReservations();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to cancel reservation.");
            }
        }
    }

    /**
     * Handle back button - return to rent bike page
     */
    @FXML
    private void handleBack() {
        try {
            AnchorPane dashboardContentArea = (AnchorPane) reservationTable.getScene().lookup("#contentArea");
            
            if (dashboardContentArea != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grp5/view/User_rentBike.fxml"));
                AnchorPane newPane = loader.load();
                dashboardContentArea.getChildren().setAll(newPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to return to rent bike page.");
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