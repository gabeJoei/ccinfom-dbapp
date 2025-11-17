package com.grp5.controller_gui;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.List;
import com.grp5.reports.bikeRentalReports;
import com.grp5.model.BikeModelRentalCountModel;

public class Admin_RentalBikeTrendReportController {

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private TableView<BikeModelRentalCountModel> reportTableView;

    @FXML
    private TableColumn<BikeModelRentalCountModel, Integer> rentalCountColumn;

    @FXML
    private TableColumn<BikeModelRentalCountModel, String> bikeModelColumn;

    private bikeRentalReports reportService;
    private ObservableList<BikeModelRentalCountModel> reportData;

    @FXML
    public void initialize() {
        reportService = new bikeRentalReports();
        reportData = FXCollections.observableArrayList();

        rentalCountColumn.setCellValueFactory(new PropertyValueFactory<>("rentalCount"));
        bikeModelColumn.setCellValueFactory(new PropertyValueFactory<>("bikeModel"));

        reportTableView.setItems(reportData);

        fromDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> generateReport());
        toDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> generateReport());

        // Set default dates (current month)
        LocalDate now = LocalDate.now();
        fromDatePicker.setValue(now.withDayOfMonth(1));
        toDatePicker.setValue(now);
        
        generateReport(); 
    }

    private void generateReport() {
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        if (fromDate == null || toDate == null) {
            reportData.clear();
            return;
        }

        if (fromDate.isAfter(toDate)) {
            showAlert("Invalid Date Range", "The 'From' date cannot be after the 'To' date.", AlertType.WARNING);
            reportData.clear();
            return;
        }

        reportData.clear();

        try {
            List<BikeModelRentalCountModel> aggregatedReport = 
                reportService.getBikeModelTotalsByDateRange(fromDate, toDate);

            reportData.addAll(aggregatedReport);

            if (reportData.isEmpty()) {
                showAlert("No Data", "No rental data found for the selected date range.", AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showAlert("Database Error", "Failed to retrieve rental data: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleRefresh() {

        generateReport();
    }

    @FXML
    private void handleClear() {
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
        reportData.clear();
    }
}