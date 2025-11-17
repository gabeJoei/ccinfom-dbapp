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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import com.grp5.reports.rentalSalesReport;
import com.grp5.reports.rentalSalesReport.RentalSales;

public class Admin_RentalSalesReportController {

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private TableView<RentalSalesDisplay> reportTableView;

    @FXML
    private TableColumn<RentalSalesDisplay, Date> dateColumn;

    @FXML
    private TableColumn<RentalSalesDisplay, String> bikeModelColumn;

    @FXML
    private TableColumn<RentalSalesDisplay, Long> unitSalesColumn;

    @FXML
    private TableColumn<RentalSalesDisplay, Double> revenueColumn;

    private rentalSalesReport reportService;
    private ObservableList<RentalSalesDisplay> reportData;

    @FXML
    public void initialize() {
        reportService = new rentalSalesReport();
        reportData = FXCollections.observableArrayList();

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        bikeModelColumn.setCellValueFactory(new PropertyValueFactory<>("bikeModel"));
        unitSalesColumn.setCellValueFactory(new PropertyValueFactory<>("unitSales"));
        revenueColumn.setCellValueFactory(new PropertyValueFactory<>("revenue"));

        revenueColumn.setCellFactory(col -> new javafx.scene.control.TableCell<RentalSalesDisplay, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
            }
        });

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
            List<RentalSales> salesData = reportService.generateRentalSalesDataByDateRange(fromDate, toDate);

            for (RentalSales sale : salesData) {
                reportData.add(new RentalSalesDisplay(
                    sale.getDate(),
                    sale.getBikeModel(),
                    sale.getUnitSales(),
                    sale.getRevenue()
                ));
            }

            if (reportData.isEmpty()) {
                showAlert("No Data", "No rental sales data found for the selected date range.", AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showAlert("Database Error", "Failed to retrieve rental sales data: " + e.getMessage(), AlertType.ERROR);
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

    // Inner class for JavaFX property binding (remains the same)
    public static class RentalSalesDisplay {
        private final SimpleObjectProperty<Date> date;
        private final SimpleStringProperty bikeModel;
        private final SimpleLongProperty unitSales;
        private final SimpleDoubleProperty revenue;

        public RentalSalesDisplay(Date date, String bikeModel, long unitSales, double revenue) {
            this.date = new SimpleObjectProperty<>(date);
            this.bikeModel = new SimpleStringProperty(bikeModel);
            this.unitSales = new SimpleLongProperty(unitSales);
            this.revenue = new SimpleDoubleProperty(revenue);
        }

        public Date getDate() {
            return date.get();
        }

        public SimpleObjectProperty<Date> dateProperty() {
            return date;
        }

        public String getBikeModel() {
            return bikeModel.get();
        }

        public SimpleStringProperty bikeModelProperty() {
            return bikeModel;
        }

        public long getUnitSales() {
            return unitSales.get();
        }

        public SimpleLongProperty unitSalesProperty() {
            return unitSales;
        }

        public double getRevenue() {
            return revenue.get();
        }

        public SimpleDoubleProperty revenueProperty() {
            return revenue;
        }
    }
}