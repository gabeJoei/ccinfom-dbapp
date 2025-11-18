package com.grp5.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.grp5.model.bikeReservation;
import com.grp5.utils.databaseConnection;

public class bikeReservationDAO {

    // CREATE - Add new reservation
    public int addReservation(bikeReservation reservation) {
        String sql = "INSERT INTO reservation (customerAccID, bikeID, reservationDate, startDate, endDate, reservationStatus, branchID) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, reservation.getCustomerAccID());
            pstmt.setInt(2, reservation.getBikeID());
            pstmt.setTimestamp(3, reservation.getReservationDate());
            pstmt.setTimestamp(4, reservation.getStartDate());
            pstmt.setTimestamp(5, reservation.getEndDate());
            pstmt.setString(6, reservation.getStatus());
            pstmt.setInt(7, reservation.getBranchID());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) { return 0; }  // Insert failed
            
            // Get the generated ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return auto-incremented ID
                } else {
                    return 0; // Failed to get ID
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // READ - Get reservation by reference number
    public bikeReservation getReservation(int reservationRefNumber) {
        String sql = "SELECT * FROM reservation WHERE reservationReferenceNum = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reservationRefNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractReservationFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // UPDATE
    public boolean updateReservation(bikeReservation reservation) {
        String sql = "UPDATE reservation SET customerAccID = ?, bikeID = ?, reservationDate = ?, startDate = ?, endDate = ?, " +
                     "dateReturned = ?, reservationStatus = ?, branchID = ? WHERE reservationReferenceNum = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reservation.getCustomerAccID());
            pstmt.setInt(2, reservation.getBikeID());
            pstmt.setTimestamp(3, reservation.getReservationDate());
            pstmt.setTimestamp(4, reservation.getStartDate());
            pstmt.setTimestamp(5, reservation.getEndDate());
            pstmt.setTimestamp(6, reservation.getDateReturned());
            pstmt.setString(7, reservation.getStatus());  // FIXED: Added missing status parameter
            pstmt.setInt(8, reservation.getBranchID());
            pstmt.setInt(9, reservation.getReservationReferenceNum());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE - Now handles foreign key constraint by deleting payments first
    public boolean deleteReservation(int reservationRefNumber) {
        Connection conn = null;
        try {
            conn = databaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // First, delete all payments associated with this reservation
            String deletePaymentsSQL = "DELETE FROM payment WHERE reservationReferenceNum = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deletePaymentsSQL)) {
                pstmt.setInt(1, reservationRefNumber);
                pstmt.executeUpdate();
            }

            // Then delete the reservation
            String deleteReservationSQL = "DELETE FROM reservation WHERE reservationReferenceNum = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteReservationSQL)) {
                pstmt.setInt(1, reservationRefNumber);
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackE) {
                System.out.println("Rollback failed: " + rollbackE.getMessage());
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException closeE) {
                System.out.println("Error closing connection: " + closeE.getMessage());
            }
        }
    }

    // LIST ALL
    public ArrayList<bikeReservation> getAllReservations() {
        ArrayList<bikeReservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation ORDER BY reservationDate DESC";
        try (Connection conn = databaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reservations.add(extractReservationFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // CHECK FOR OVERLAPPING
    public boolean hasOverlappingReservation(int bikeID, Timestamp newStart, Timestamp newEnd) {
        String sql = "SELECT COUNT(*) FROM reservation " +
                    "WHERE bikeID = ? " +
                    "AND reservationStatus = 'ongoing' " +
                    "AND startDate <= ? " +  // Check for overlapping startDate
                    "AND endDate >= ?";     // Check for overlapping endDate
        try (Connection conn = databaseConnection.getConnection(); 
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bikeID);
            pstmt.setTimestamp(2, newEnd);   
            pstmt.setTimestamp(3, newStart); 
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // overlap exists if count != 0
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; 
    }

    // Helper to extract from ResultSet
    private bikeReservation extractReservationFromResultSet(ResultSet rs) throws SQLException {
        bikeReservation reservation = new bikeReservation();
        reservation.setReservationReferenceNum(rs.getInt("reservationReferenceNum"));
        reservation.setCustomerAccID(rs.getInt("customerAccID"));
        reservation.setBikeID(rs.getInt("bikeID"));
        reservation.setBranchID(rs.getInt("branchID"));
        reservation.setReservationDate(rs.getTimestamp("reservationDate"));
        reservation.setStartDate(rs.getTimestamp("startDate"));
        reservation.setEndDate(rs.getTimestamp("endDate"));
        reservation.setDateReturned(rs.getTimestamp("dateReturned"));
        reservation.setStatus(rs.getString("reservationStatus"));
        return reservation;
    }
}