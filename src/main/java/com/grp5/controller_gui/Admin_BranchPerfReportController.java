
package com.grp5.controller_gui;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.grp5.dao.branchRecordDAO;
import com.grp5.reports.branchPerformanceReport;
import com.grp5.reports.branchPerformanceReport.branchPerformance;
import com.grp5.utils.databaseConnection;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Admin_BranchPerfReportController implements Initializable {
    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private TableView<BranchPerformance> performanceTable;

    @FXML
    private TableColumn<BranchPerformance, Integer> branchIDColumn;

    @FXML
    private TableColumn<BranchPerformance, String> branchNameColumn;

    @FXML
    private TableColumn<BranchPerformance, Integer> rentalCountColumn;

    @FXML
    private TableColumn<BranchPerformance, Integer> reservationCountColumn;

    @FXML
    private TableColumn<BranchPerformance, Integer> customerEngagementColumn;

    @FXML
    private TableColumn<BranchPerformance, Double> totalRevenueColumn;

    private branchPerformanceReport reportGenerator;
    private ObservableList<BranchPerformance> performanceData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Initialize report generator
            reportGenerator = new branchPerformanceReport();
            performanceData = FXCollections.observableArrayList();

            // Set up table columns
            branchIDColumn.setCellValueFactory(cellData -> 
                new SimpleIntegerProperty(cellData.getValue().getBranchID()).asObject());
            
            branchNameColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getBranchName()));
            
            rentalCountColumn.setCellValueFactory(cellData -> 
                new SimpleIntegerProperty(cellData.getValue().getRentalCount()).asObject());
            
            reservationCountColumn.setCellValueFactory(cellData -> 
                new SimpleIntegerProperty(cellData.getValue().getReservationCount()).asObject());
            
            customerEngagementColumn.setCellValueFactory(cellData -> 
                new SimpleIntegerProperty(cellData.getValue().getCustomerEngagement()).asObject());
            
            totalRevenueColumn.setCellValueFactory(cellData -> 
                new SimpleDoubleProperty(cellData.getValue().getTotalRevenue()).asObject());

            // Format revenue column to show currency
            totalRevenueColumn.setCellFactory(column -> new TableCell<BranchPerformance, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("₱%.2f", item));
                    }
                }
            });

            // Set default date range (current month)
            LocalDate now = LocalDate.now();
            toDatePicker.setValue(now);
            fromDatePicker.setValue(now.withDayOfMonth(1)); // First day of current month

            // Add listeners to date pickers
            fromDatePicker.setOnAction(event -> loadPerformanceData());
            toDatePicker.setOnAction(event -> loadPerformanceData());

            // Test database connection before loading data
            testDatabaseConnection();
            
            // Load initial data
            loadPerformanceData();
            
        } catch (NoClassDefFoundError e) {
            System.err.println("ERROR: Required class not found: " + e.getMessage());
            showAlert("Initialization Error", 
                "Required classes not found. Please check if all required classes are compiled.\n\nError: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("ERROR: Failed to initialize controller: " + e.getMessage());
            e.printStackTrace();
            showAlert("Initialization Error", 
                "Failed to initialize the report view.\n\nError: " + e.getMessage());
        }
    }
    
    private void testDatabaseConnection() {
        try {
            Connection testConn = databaseConnection.getConnection();
            if (testConn != null && !testConn.isClosed()) {
                testConn.close();
                System.out.println("Database connection test: SUCCESS");
            } else {
                throw new SQLException("Unable to establish database connection");
            }
        } catch (SQLException e) {
            System.err.println("Database connection test: FAILED");
            System.err.println("Error: " + e.getMessage());
            showAlert("Database Connection Error", 
                "Unable to connect to the database. Please check:\n" +
                "- Database server is running\n" +
                "- Connection settings are correct\n" +
                "- Network connectivity\n\n" +
                "Error: " + e.getMessage());
        }
    }

    private void loadPerformanceData() {
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        // Validate date range
        if (fromDate == null || toDate == null) {
            showAlert("Invalid Date Range", "Please select both start and end dates.");
            return;
        }

        if (fromDate.isAfter(toDate)) {
            showAlert("Invalid Date Range", "Start date cannot be after end date.");
            return;
        }

        performanceData.clear();

        try {
            // Get year and month from the fromDate
            int year = fromDate.getYear();
            int month = fromDate.getMonthValue();

            // Generate report using the branchPerformanceReport class
            ArrayList<branchPerformance> reportData = reportGenerator.generateReport(year, month);

            // Convert report data to table-friendly format
            for (branchPerformance bp : reportData) {
                BranchPerformance performance = new BranchPerformance(
                    bp.getBranchId(),
                    bp.getBranchName(),
                    bp.getNumberOfRentals(),
                    bp.getNumberOfReservations(),
                    bp.getCustomerEngagement(),
                    bp.getTotalRevenue()
                );
                performanceData.add(performance);
            }

            performanceTable.setItems(performanceData);

            // Optional: Show summary in console
            String summary = reportGenerator.generateSummary(year, month);
            System.out.println("\n" + summary);

        } catch (Exception e) {
            System.err.println("Error loading performance data: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to load performance data: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner class to represent branch performance data for the TableView
    public static class BranchPerformance {
        private final int branchID;
        private final String branchName;
        private final int rentalCount;
        private final int reservationCount;
        private final int customerEngagement;
        private final double totalRevenue;

        public BranchPerformance(int branchID, String branchName, int rentalCount, 
                                int reservationCount, int customerEngagement, double totalRevenue) {
            this.branchID = branchID;
            this.branchName = branchName;
            this.rentalCount = rentalCount;
            this.reservationCount = reservationCount;
            this.customerEngagement = customerEngagement;
            this.totalRevenue = totalRevenue;
        }

        public int getBranchID() { return branchID; }
        public String getBranchName() { return branchName; }
        public int getRentalCount() { return rentalCount; }
        public int getReservationCount() { return reservationCount; }
        public int getCustomerEngagement() { return customerEngagement; }
        public double getTotalRevenue() { return totalRevenue; }
    }
}