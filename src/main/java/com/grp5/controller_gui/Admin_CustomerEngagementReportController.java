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
import java.util.ArrayList;

import com.grp5.reports.customerEngagementReport;

public class Admin_CustomerEngagementReportController {

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private TableView<customerEngagementReport> reportTableView;

    @FXML
    private TableColumn<customerEngagementReport, Integer> engagementColumn;

    @FXML
    private TableColumn<customerEngagementReport, Integer> customerIDColumn;

    @FXML
    private TableColumn<customerEngagementReport, String> customerNameColumn;

    @FXML
    private TableColumn<customerEngagementReport, String> branchNameColumn;

    private customerEngagementReport reportService;
    private ObservableList<customerEngagementReport> reportData;

    @FXML
    public void initialize() {
        reportService = new customerEngagementReport();
        reportData = FXCollections.observableArrayList();

        // Configure table columns using the getter method names
        engagementColumn.setCellValueFactory(new PropertyValueFactory<>("customerEngagement"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        // Note: Using the corrected getter name
        branchNameColumn.setCellValueFactory(new PropertyValueFactory<>("branchName")); 

        reportTableView.setItems(reportData);

        fromDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> generateReport());
        toDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> generateReport());

        LocalDate now = LocalDate.now();
        fromDatePicker.setValue(now.withDayOfMonth(1));
        toDatePicker.setValue(now);
        
        generateReport();
    }

    private void generateReport() {
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        reportData.clear();
        
        if (fromDate == null || toDate == null) {
            return;
        }

        if (fromDate.isAfter(toDate)) {
            showAlert("Invalid Date Range", "The 'From' date cannot be after the 'To' date.", AlertType.WARNING);
            return;
        }

        try {
            String startDate = fromDate.toString();
            String endDate = toDate.toString();

            ArrayList<customerEngagementReport> aggregatedReport = 
                reportService.customerEngageReport(startDate, endDate);

            reportData.addAll(aggregatedReport);

            if (reportData.isEmpty()) {
                showAlert("No Data", "No customer engagement found for the selected date range.", AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to retrieve report data: " + e.getMessage(), AlertType.ERROR);
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
    }
}