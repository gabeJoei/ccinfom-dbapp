package com.grp5.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.grp5.model.DailyRentalVolumeModel;
import com.grp5.model.BikeModelRentalCountModel;
import com.grp5.model.BikeRentalMonthReportModel;
import com.grp5.utils.databaseConnection;

//Provides reporting utilities for analyzing bike rental activity.
public class bikeRentalReports {

    //Generates a complete rental report for a given month and year.
    public BikeRentalMonthReportModel generateReport(int year, int month) {

        List<DailyRentalVolumeModel> dailyVolumes = getDailyRentalVolume(year, month);
        List<BikeModelRentalCountModel> topModels = getTopBikeModels(year, month, 1);
        BikeModelRentalCountModel mostRented = topModels.isEmpty() ? null : topModels.get(0);

        return new BikeRentalMonthReportModel(year, month, dailyVolumes, mostRented);
    }

    //Retrieves the number of rentals for each day in the specified month.
    private List<DailyRentalVolumeModel> getDailyRentalVolume(int year, int month) {
        List<DailyRentalVolumeModel> volumes = new ArrayList<>();

        String sql = "SELECT DATE(reservationDate) AS rental_date, COUNT(*) AS rental_count " +
                "FROM reservation " +
                "WHERE YEAR(reservationDate) = ? " +
                "  AND MONTH(reservationDate) = ? " +
                "  AND reservationStatus IN ('ongoing', 'completed') " +
                "GROUP BY DATE(reservationDate) " +
                "ORDER BY rental_date";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, year);
            pstmt.setInt(2, month);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate date = rs.getDate("rental_date").toLocalDate();
                    int count = rs.getInt("rental_count");
                    volumes.add(new DailyRentalVolumeModel(date, count));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getDailyRentalVolume: " + e.getMessage());
        }

        return volumes;
    }

    // Retrieves the top rented bike models for a given month and year.
    private List<BikeModelRentalCountModel> getTopBikeModels(int year, int month, int limit) {
        List<BikeModelRentalCountModel> models = new ArrayList<>();

        String sql = "SELECT b.bikeModel AS bike_model, COUNT(*) AS rental_count " +
                "FROM reservation r " +
                "JOIN bike b ON r.bikeID = b.bikeID " +
                "WHERE YEAR(r.reservationDate) = ? " +
                "  AND MONTH(r.reservationDate) = ? " +
                "  AND r.reservationStatus IN ('ongoing', 'completed') " +
                "GROUP BY b.bikeModel " +
                "ORDER BY rental_count DESC " +
                "LIMIT ?";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            pstmt.setInt(3, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String model = rs.getString("bike_model");
                    int count = rs.getInt("rental_count");
                    models.add(new BikeModelRentalCountModel(model, count));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getTopBikeModels: " + e.getMessage());
        }

        return models;
    }

    //Retrieves the total rental count per bike model for a given date range.
    public List<BikeModelRentalCountModel> getBikeModelTotalsByDateRange(LocalDate fromDate, LocalDate toDate) {
        List<BikeModelRentalCountModel> models = new ArrayList<>();

        String sql = "SELECT b.bikeModel AS bike_model, COUNT(*) AS rental_count " +
                    "FROM reservation r " +
                    "JOIN bike b ON r.bikeID = b.bikeID " +
                    "WHERE DATE(r.reservationDate) BETWEEN ? AND ? " +
                    "  AND r.reservationStatus IN ('ongoing', 'completed') " +
                    "GROUP BY b.bikeModel " +
                    "ORDER BY rental_count DESC";

        try (Connection conn = databaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the date parameters
            pstmt.setDate(1, java.sql.Date.valueOf(fromDate));
            pstmt.setDate(2, java.sql.Date.valueOf(toDate));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String model = rs.getString("bike_model");
                    int count = rs.getInt("rental_count");
                    models.add(new BikeModelRentalCountModel(model, count));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getBikeModelTotalsByDateRange: " + e.getMessage());
        }

        return models;
    }

}
