package com.grp5.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.grp5.model.bikeReservation;
import com.grp5.utils.databaseConnection;

public class bikeReservationDAO {

    // CREATE - Add new reservation
    public boolean addReservation(bikeReservation reservation) {
        String sql = "INSERT INTO reservation (customerAccID, bikeID, reservationDate, startDate, endDate, reservationStatus, branchID) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reservation.getCustomerAccID());
            pstmt.setInt(2, reservation.getBikeID());
            pstmt.setTimestamp(3, reservation.getReservationDate());
            pstmt.setTimestamp(4, reservation.getStartDate());
            pstmt.setTimestamp(5, reservation.getEndDate());
            pstmt.setString(6, reservation.getStatus());
            pstmt.setInt(7, reservation.getBranchID());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
            pstmt.setInt(8, reservation.getBranchID());
            pstmt.setInt(9, reservation.getReservationReferenceNum());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteReservation(int reservationRefNumber) {
        String sql = "DELETE FROM reservation WHERE reservationReferenceNum = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reservationRefNumber);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
