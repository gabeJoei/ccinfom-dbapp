package com.grp5.reports;

import com.grp5.utils.databaseConnection;
import java.sql.*;
import java.util.ArrayList;


/*Generates performance reports for all branches within a selected year and month.
 * 
 */
public class branchPerformanceReport {
    
    public class branchPerformance {
        private int branchId;
        private String branchName;
        private int numberOfRentals;
        private int numberOfReservations;
        private int customerEngagement;
        private double totalRevenue;
        
        public branchPerformance(int branchId, String branchName, int numberOfRentals, 
                                int numberOfReservations, int customerEngagement, 
                                double totalRevenue) {
            this.branchId = branchId;
            this.branchName = branchName;
            this.numberOfRentals = numberOfRentals;
            this.numberOfReservations = numberOfReservations;
            this.customerEngagement = customerEngagement;
            this.totalRevenue = totalRevenue;
        }
        
        // Getters
        public int getBranchId() { return branchId; }
        public String getBranchName() { return branchName; }
        public int getNumberOfRentals() { return numberOfRentals; }
        public int getNumberOfReservations() { return numberOfReservations; }
        public int getCustomerEngagement() { return customerEngagement; }
        public double getTotalRevenue() { return totalRevenue; }
        
        @Override
        public String toString() {
            return String.format(
                "Branch ID: %d | Name: %s | Rentals: %d | Reservations: %d | " +
                "Customers: %d | Revenue: $%.2f",
                branchId, branchName, numberOfRentals, numberOfReservations,
                customerEngagement, totalRevenue
            );
        }
    }
    
    /**
     * Generate branch performance report for a given year and month
     * @param year The year to generate report for
     * @param month The month (1-12) to generate report for
     * @return List of branchPerformance objects
     */
    public ArrayList<branchPerformance> generateReport(int year, int month) {
        ArrayList<branchPerformance> performances = new ArrayList<>();

        // SQL query to generate branch performance report
        String sql = 
            "SELECT " +
            "    b.branchID, " +
            "    b.branchName, " +
            "    COUNT(DISTINCT rt.paymentReferenceNum) AS number_of_rentals, " +
            "    COUNT(DISTINCT br.reservationReferenceNum) AS number_of_reservations, " +
            "    COUNT(DISTINCT rt.customerID) AS customer_engagement, " +
            "    COALESCE(SUM(rt.paymentAmount), 0) AS total_revenue " +
            "FROM branch b " +
            "LEFT JOIN payment rt ON b.branchID = rt.branchID " +
            "    AND YEAR(rt.paymentDate) = ? " +
            "    AND MONTH(rt.paymentDate) = ? " +
            "LEFT JOIN reservation br ON b.branchID = br.branchID " +
            "    AND YEAR(br.reservationDate) = ? " +
            "    AND MONTH(br.reservationDate) = ? " +
            "GROUP BY b.branchID, b.branchName " +
            "ORDER BY total_revenue DESC";

        try (Connection conn = databaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            pstmt.setInt(4, month);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                branchPerformance performance = new branchPerformance(
                    rs.getInt("branchID"),
                    rs.getString("branchName"),
                    rs.getInt("number_of_rentals"),
                    rs.getInt("number_of_reservations"),
                    rs.getInt("customer_engagement"),
                    rs.getDouble("total_revenue")
                );
                performances.add(performance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return performances;
    }

    /**
     * Generate summary statistics for all branches
     * @param year The year
     * @param month The month
     * @return Summary string
     */
    public String generateSummary(int year, int month) {
        ArrayList<branchPerformance> performances = generateReport(year, month);
        
        if (performances.isEmpty()) {
            return "No data available for the specified period.";
        }
        
        int totalRentals = 0;
        int totalReservations = 0;
        int totalCustomers = 0;
        double totalRevenue = 0.0;
        
        branchPerformance topBranch = performances.get(0); // Already sorted by revenue DESC
        
        for (branchPerformance bp : performances) {
            totalRentals += bp.getNumberOfRentals();
            totalReservations += bp.getNumberOfReservations();
            totalCustomers += bp.getCustomerEngagement();
            totalRevenue += bp.getTotalRevenue();
        }
        
        double avgRevenue = totalRevenue / performances.size();
        StringBuilder summary = new StringBuilder();
        summary.append(String.format("BRANCH PERFORMANCE SUMMARY - %02d/%d\n", month, year));
        summary.append(String.format("Total Branches: %d\n", performances.size()));
        summary.append(String.format("Total Rentals: %d\n", totalRentals));
        summary.append(String.format("Total Reservations: %d\n", totalReservations));
        summary.append(String.format("Total Unique Customers: %d\n", totalCustomers));
        summary.append(String.format("Total Revenue: $%.2f\n", totalRevenue));
        summary.append(String.format("Average Revenue per Branch: $%.2f\n", avgRevenue));
        summary.append("\nTop Performing Branch:\n");
        summary.append(String.format("  %s (Branch ID: %d)\n", 
                                     topBranch.getBranchName(), 
                                     topBranch.getBranchId()));
        summary.append(String.format("  Revenue: $%.2f\n", topBranch.getTotalRevenue()));

        
        return summary.toString();
    }
    
    /**
     * Identify underperforming branches (below average revenue)
     * @param year The year
     * @param month The month
     * @return List of underperforming branches
     */
    public ArrayList<branchPerformance> getUnderperformingBranches(int year, int month) {
        ArrayList<branchPerformance> allBranches = generateReport(year, month);
        ArrayList<branchPerformance> underperforming = new ArrayList<>();
        
        if (allBranches.isEmpty()) {
            return underperforming;
        }
        
        // Calculate average revenue
        double totalRevenue = 0.0;
        for (branchPerformance bp : allBranches) {
            totalRevenue += bp.getTotalRevenue();
        }
        double avgRevenue = totalRevenue / allBranches.size();
        
        // Filter branches below average
        for (branchPerformance bp : allBranches) {
            if (bp.getTotalRevenue() < avgRevenue) {
                underperforming.add(bp);
            }
        }
        
        return underperforming;
    }
    
}