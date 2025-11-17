package com.grp5.controller_gui;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.grp5.dao.bikeRecordDAO;
import com.grp5.model.bikeRecordModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

public class BikeRecordController {

    @FXML
    private TextField bikeIDTextField;

    @FXML
    private TableView<bikeRecordModel> bikeTableView;

    @FXML
    private TableColumn<bikeRecordModel, Integer> bikeIDColumn;

    @FXML
    private TableColumn<bikeRecordModel, Integer> branchIDColumn;

    @FXML
    private TableColumn<bikeRecordModel, String> bikeModelColumn;

    @FXML
    private TableColumn<bikeRecordModel, Boolean> bikeAvailabilityColumn;

    @FXML
    private TableColumn<bikeRecordModel, BigDecimal> hourlyRateColumn;

    @FXML
    private TableColumn<bikeRecordModel, BigDecimal> dailyRateColumn;

    @FXML
    private Button enterButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button viewDetailsButton;

    @FXML
    private Button deleteButton;

    private bikeRecordDAO bikeDAO = new bikeRecordDAO();
    private ObservableList<bikeRecordModel> bikeList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        setupTableColumns();

        loadAllBikes();
    }

    private void setupTableColumns() {
        bikeIDColumn.setCellValueFactory(new PropertyValueFactory<>("bikeID"));
        branchIDColumn.setCellValueFactory(new PropertyValueFactory<>("branchIDNum"));
        bikeModelColumn.setCellValueFactory(new PropertyValueFactory<>("bikeModel"));
        bikeAvailabilityColumn.setCellValueFactory(new PropertyValueFactory<>("bikeAvailability"));
        hourlyRateColumn.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));
        dailyRateColumn.setCellValueFactory(new PropertyValueFactory<>("dailyRate"));
    }


    private void loadAllBikes() {
        List<bikeRecordModel> bikes = bikeDAO.getAllBikes();
        bikeList.clear();
        bikeList.addAll(bikes);
        bikeTableView.setItems(bikeList);
    }

   
    @FXML
    void handleEnterButtonAction(ActionEvent event) {
        try {
            String idText = bikeIDTextField.getText().trim();
            if (idText.isEmpty()) {
                loadAllBikes(); 
                return;
            }

            int bikeID = Integer.parseInt(idText);
            bikeRecordModel bike = bikeDAO.getBikeRecord(bikeID);

            bikeList.clear();
            if (bike != null) {
                bikeList.add(bike);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Search Result",
                        "No bike found with ID: " + bikeID);
                loadAllBikes();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid numeric Bike ID.");
        }
        bikeIDTextField.clear();
    }

    /**
     * Handle Update button - update selected bike with dialog
     */
    @FXML
    void handleUpdateButtonAction(ActionEvent event) {
        bikeRecordModel selectedBike = bikeTableView.getSelectionModel().getSelectedItem();

        if (selectedBike == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a bike record to update.");
            return;
        }

        Dialog<bikeRecordModel> dialog = new Dialog<>();
        dialog.setTitle("Update Bike Record");
        dialog.setHeaderText("Update Bike ID: " + selectedBike.getBikeID());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField txtBranchID = new TextField(String.valueOf(selectedBike.getBranchIDNum()));
        TextField txtBikeModel = new TextField(selectedBike.getBikeModel());
        CheckBox chkAvailability = new CheckBox();
        chkAvailability.setSelected(selectedBike.getBikeAvailability());
        TextField txtHourlyRate = new TextField(selectedBike.getHourlyRate().toString());
        TextField txtDailyRate = new TextField(selectedBike.getDailyRate().toString());

        grid.add(new Label("Branch ID:"), 0, 0);
        grid.add(txtBranchID, 1, 0);
        grid.add(new Label("Bike Model:"), 0, 1);
        grid.add(txtBikeModel, 1, 1);
        grid.add(new Label("Availability:"), 0, 2);
        grid.add(chkAvailability, 1, 2);
        grid.add(new Label("Hourly Rate:"), 0, 3);
        grid.add(txtHourlyRate, 1, 3);
        grid.add(new Label("Daily Rate:"), 0, 4);
        grid.add(txtDailyRate, 1, 4);

        dialog.getDialogPane().setContent(grid);

        ButtonType btnUpdate = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnUpdate, ButtonType.CANCEL);

        // Convert the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnUpdate) {
                try {
                    int newBranchID = Integer.parseInt(txtBranchID.getText().trim());
                    BigDecimal newHourlyRate = new BigDecimal(txtHourlyRate.getText().trim());
                    BigDecimal newDailyRate = new BigDecimal(txtDailyRate.getText().trim());

                    selectedBike.setBranchIDNum(newBranchID);
                    selectedBike.setBikeAvailability(chkAvailability.isSelected());
                    selectedBike.setHourlyRate(newHourlyRate);
                    selectedBike.setDailyRate(newDailyRate);

                    return selectedBike;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", 
                            "Branch ID, Hourly Rate, and Daily Rate must be valid numbers.");
                    return null;
                }
            }
            return null;
        });

        Optional<bikeRecordModel> result = dialog.showAndWait();
        result.ifPresent(bike -> {
            int rowsAffected = bikeDAO.updateBikeRecord(bike);
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Bike record updated successfully!");
                loadAllBikes(); 
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to update bike record. Please ensure all fields are valid.");
            }
        });
    }

  
    @FXML
    void handleViewDetailsButtonAction(ActionEvent event) {
        bikeRecordModel selectedBike = bikeTableView.getSelectionModel().getSelectedItem();

        if (selectedBike != null) {
            String details = String.format(
                "Bike ID: %d\n" +
                "Branch ID: %d\n" +
                "Model: %s\n" +
                "Availability: %s\n" +
                "Hourly Rate: $%s\n" +
                "Daily Rate: $%s",
                selectedBike.getBikeID(),
                selectedBike.getBranchIDNum(),
                selectedBike.getBikeModel(),
                selectedBike.getBikeAvailability() ? "Available" : "Not Available",
                selectedBike.getHourlyRate(),
                selectedBike.getDailyRate()
            );
            showAlert(Alert.AlertType.INFORMATION, "Bike Details", details);
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", 
                    "Please select a bike record to view details.");
        }
    }


    @FXML
    void handleDeleteButtonAction(ActionEvent event) {
        bikeRecordModel selectedBike = bikeTableView.getSelectionModel().getSelectedItem();

        if (selectedBike == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", 
                    "Please select a bike record to delete.");
            return;
        }

        // Confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Confirmation");
        confirmAlert.setHeaderText("Are you sure you want to delete Bike ID: " + selectedBike.getBikeID() + "?");
        confirmAlert.setContentText("Model: " + selectedBike.getBikeModel() + "\nThis action cannot be undone.");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int rowsDeleted = bikeDAO.deleteBikeRecord(selectedBike.getBikeID());

            if (rowsDeleted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                        "Bike record deleted successfully.");
                loadAllBikes(); // Refresh table
            } else {
                showAlert(Alert.AlertType.ERROR, "Delete Failed", 
                        "Failed to delete bike record.\n\n" +
                        "This bike may be referenced in rental records or other tables.\n" +
                        "Please check the console for detailed error information.");
            }
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