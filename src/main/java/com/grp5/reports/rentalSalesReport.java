package com.grp5.reports;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

        // Get the query (Query not checked yet cuz no data avail)
        String query = """
                    SELECT p.paymentDate AS date, r.bikeModel, COUNT(r.bikeModel) AS unitSales, SUM(p.paymentAmount) AS revenue
                    FROM payment p
                    JOIN reservation r ON p.reservationReferenceNumpr.reservationReferenceNum
                    WHERE YEAR(p.paymentDate) = ? AND MONTH(p.paymentDate) = ?
                    GROUP BY r.bikeModel
                    ORDER BY date DESC""";

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
        double totalRevenue = 0; // Already calculated by SUM() in query

        RentalSales topRental = data.get(0);
        
        for (RentalSales row : data) {
            totalRentals++;
            unitSales += row.getUnitSales();
            totalRevenue += row.getRevenue();
        }

        StringBuilder generatedSummary = new StringBuilder();
        generatedSummary.append("\n========================================\n");
        generatedSummary.append(String.format("RENTAL SALES REPORT - %02d/%d\n", month, year));
        generatedSummary.append("========================================\n");
        generatedSummary.append(String.format("Total Rentals: %d%n\n", totalRentals));
        generatedSummary.append(String.format("Unit Sales: %d%n\n", unitSales));
        generatedSummary.append(String.format("Total Revenue: $%.2f\n", totalRevenue));
        generatedSummary.append("\nTop Performing Rental:\n");
        generatedSummary.append(String.format("  %s \n", topRental.getBikeModel()));
        generatedSummary.append(String.format("  Revenue: $%.2f\n", topRental.getRevenue()));
        generatedSummary.append("========================================\n");

        return generatedSummary.toString();
    }
}
