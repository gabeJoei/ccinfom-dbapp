package com.grp5.reports;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.grp5.utils.databaseConnection;

public class rentalSalesReport {
    
    public class RentalSales {

        private Date date;
        private String bikeModel;
        private long unitSales;
        private double revenue;

        public RentalSales(Date date, String bikeModel, long unitSales, double revenue) {
            this.date = date;
            this.bikeModel = bikeModel;
            this.unitSales = unitSales;
            this.revenue = revenue;
        }

        // Getters
        public Date getDate() { return this.date; }
        public String getBikeModel() { return this.bikeModel; }
        public long getUnitSales() { return this.unitSales; }
        public double getRevenue() { return this.revenue; }
    }

    public ArrayList<RentalSales> generateRentalSalesData(int year, int month) { 
        ArrayList<RentalSales> rentalSales = new ArrayList<>();

        String query = """
                       SELECT DATE(p.paymentDate) AS date, b.bikeModel, COUNT(b.bikeModel) AS unitSales, SUM(p.paymentAmount) AS revenue
                       FROM payment p
                       JOIN reservation r ON p.reservationReferenceNum = r.reservationReferenceNum
                       JOIN bike b ON r.bikeID = b.bikeID
                       WHERE YEAR(p.paymentDate) = ? AND MONTH(p.paymentDate) = ?
                       AND r.reservationStatus = 'completed'
                       GROUP BY b.bikeModel
                       ORDER BY date DESC
                       """;

        try (Connection conn = databaseConnection.getConnection(); 
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, year);
            pstmt.setInt(2, month);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                RentalSales rentalSalesData = new RentalSales(
                rs.getDate("date"),
                rs.getString("bikeModel"),
                rs.getLong("unitSales"),
                rs.getDouble("revenue")
                );
                rentalSales.add(rentalSalesData);
            }
        }
        catch (SQLException e) {
            System.out.println("Error generating rental sales report: " + e.getMessage());
            e.printStackTrace();
        }

        return rentalSales;
    }

    public String generateRentalSalesReport(int year, int month) {
        ArrayList<RentalSales> data = generateRentalSalesData(year, month);

        if (data.isEmpty()) {
            return "No data available for the specified period.";
        }

        long totalRentals = 0;
        long unitSales = 0;
        double totalRevenue = 0; 

        RentalSales topRental = data.get(0);
        
        for (RentalSales row : data) {
            totalRentals++; // This calculates the number of rows (unique bike models in the result), not total rentals.
            unitSales += row.getUnitSales();
            totalRevenue += row.getRevenue();
        }

        StringBuilder generatedSummary = new StringBuilder();
        generatedSummary.append(String.format("RENTAL SALES REPORT - %02d/%d\n", month, year));
        generatedSummary.append(String.format("Total Bike Models in Report: %d%n\n", totalRentals)); // Changed label
        generatedSummary.append(String.format("Total Unit Sales: %d%n\n", unitSales));
        generatedSummary.append(String.format("Total Revenue: $%.2f\n", totalRevenue));
        generatedSummary.append("\nTop Performing Rental:\n");
        generatedSummary.append(String.format("  %s \n", topRental.getBikeModel()));
        generatedSummary.append(String.format("  Revenue: $%.2f\n", topRental.getRevenue()));


        return generatedSummary.toString();
    }
    

    public List<RentalSales> generateRentalSalesDataByDateRange(LocalDate fromDate, LocalDate toDate) {
        List<RentalSales> sales = new ArrayList<>();

        String sql = "SELECT DATE(r.reservationDate) AS report_date, " +
                    "       b.bikeModel AS bike_model, " + 
                    "       COUNT(r.reservationReferenceNum) AS unit_sales, " +
                    "       SUM(p.paymentAmount) AS total_revenue " +
                    "FROM reservation r " +
                    "JOIN bike b ON r.bikeID = b.bikeID " +
                    "JOIN payment p ON r.reservationReferenceNum = p.reservationReferenceNum " +
                    "WHERE DATE(r.reservationDate) BETWEEN ? AND ? " +
                    "  AND r.reservationStatus IN ('completed') " + 
                    "GROUP BY report_date, b.bikeModel " +
                    "ORDER BY report_date ASC, b.bikeModel ASC";

        try (Connection conn = databaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(fromDate));
            pstmt.setDate(2, Date.valueOf(toDate));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Date date = rs.getDate("report_date");
                    String model = rs.getString("bike_model");
                    long units = rs.getLong("unit_sales");
                    double revenue = rs.getDouble("total_revenue");

                    sales.add(new RentalSales(date, model, units, revenue));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error generating rental sales report by date range: " + e.getMessage());
            e.printStackTrace();
        }

        return sales;
    }
}