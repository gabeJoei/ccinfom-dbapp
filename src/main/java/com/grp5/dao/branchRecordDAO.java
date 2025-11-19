package com.grp5.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grp5.model.branchRecordModel;
import com.grp5.utils.databaseConnection;

/**
 * Data Access Object (DAO) for managing branch records.
 * 
 * This class handles all CRUD operations for the branch database table,
 * including inserting, retrieving, updating, deleting customer records.
 *
 */
public class branchRecordDAO {

    // INSERTS A NEW BRANCH RECORD IN THE BRANCH DATA BASE
    public int addBranchRecordData(branchRecordModel branch) {
        try {
            Connection connect = databaseConnection.getConnection();
            PreparedStatement prepState = connect
                    .prepareStatement("INSERT INTO branch (branchName,branchAddress) VALUES (?,?) ");
            prepState.setString(1, branch.getBranchName());
            prepState.setString(2, branch.getBranchAddress());
            prepState.executeUpdate();
            prepState.close();
            connect.close();
            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    // UPDATES A RECORD IN THE BRANCH DATABASE
    public boolean updateBranchRecord(branchRecordModel branch) {
        try {
            Connection connect = databaseConnection.getConnection();
            PreparedStatement prepState = connect.prepareStatement(
                    "UPDATE branch SET branchName=?,branchAddress=? WHERE branchID=?");
            prepState.setString(1, branch.getBranchName());
            prepState.setString(2, branch.getBranchAddress());
            prepState.setInt(3, branch.getBranchID()); // FIXED: Changed from 4 to 3

            int rowsAffected = prepState.executeUpdate();

            prepState.close();
            connect.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // RETRIEVES A SPECIFIC BRANCH RECORD IN THE DATABASE
    public branchRecordModel getBranchRecordData(int branchID) {
        try {
            Connection connect = databaseConnection.getConnection();
            PreparedStatement prepState = connect.prepareStatement("SELECT * FROM branch WHERE branchID=?");
            prepState.setInt(1, branchID);
            ResultSet result = prepState.executeQuery();
            if (result.next()) {
                return extractFromBranchTable(result);
            }
            result.close();
            prepState.close();
            connect.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // RETRIEVES ALL BRANCH RECORD IN THE DATABASE
    public ArrayList<branchRecordModel> getAllBranch() {
        ArrayList<branchRecordModel> branch = new ArrayList<>();
        try {
            Connection connect = databaseConnection.getConnection();
            PreparedStatement prepState = connect.prepareStatement("SELECT * FROM branch");
            ResultSet result = prepState.executeQuery();
            while (result.next()) {
                branch.add(extractFromBranchTable(result));
            }
            result.close();
            prepState.close();
            connect.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return branch;
    }

    /**
     * Helper method to clean up all dependent records (Payments, Reservations, then
     * Bikes)
     * associated with a specific branch ID within a single transaction.
     * 
     * @param branchID The ID of the branch whose dependent records need to be
     *                 deleted.
     * @return 1 on success, 0 on failure.
     */
    private int deleteDependentRecords(int branchID) {
        Connection connect = null;
        try {
            connect = databaseConnection.getConnection();
            connect.setAutoCommit(false); // Start transaction for atomicity

            // Delete payments for reservations linked to bikes at this branch
            String deletePaymentsViaBikeSQL = "DELETE FROM payment WHERE reservationReferenceNum IN (SELECT reservationReferenceNum FROM reservation WHERE bikeID IN (SELECT bikeID FROM bike WHERE branchIDNum = ?))";
            try (PreparedStatement prepState = connect.prepareStatement(deletePaymentsViaBikeSQL)) {
                prepState.setInt(1, branchID);
                prepState.executeUpdate();
            } 

            // Delete payments for reservations directly linked to this branch
            
             
            String deletePaymentsViaBranchSQL = "DELETE FROM payment WHERE reservationReferenceNum IN (SELECT reservationReferenceNum FROM reservation WHERE branchID = ?)";
            try (PreparedStatement prepState = connect.prepareStatement(deletePaymentsViaBranchSQL)) {
                prepState.setInt(1, branchID);
                prepState.executeUpdate();
            } 

            // Delete reservations linked to bikes at this branch
            String deleteReservationsViaBikeSQL = "DELETE FROM reservation WHERE bikeID IN (SELECT bikeID FROM bike WHERE branchIDNum = ?)";
            try (PreparedStatement prepState = connect.prepareStatement(deleteReservationsViaBikeSQL)) {
                prepState.setInt(1, branchID);
                prepState.executeUpdate();
            }

            // Delete reservations directly linked to this branch
            String deleteReservationsViaBranchSQL = "DELETE FROM reservation WHERE branchID = ?";
            try (PreparedStatement prepState = connect.prepareStatement(deleteReservationsViaBranchSQL)) {
                prepState.setInt(1, branchID);
                prepState.executeUpdate();
            }

            // Delete bikes at this branch
            String deleteBikesSQL = "DELETE FROM bike WHERE branchIDNum = ?";
            try (PreparedStatement prepState = connect.prepareStatement(deleteBikesSQL)) {
                prepState.setInt(1, branchID);
                prepState.executeUpdate();
            }

            connect.commit();
            return 1;
        } catch (SQLException e) {
            try {
                if (connect != null) {
                    connect.rollback();
                }
            } catch (SQLException rollbackE) {
                System.out.println("Rollback failed: " + rollbackE.getMessage());
            }
            System.out.println("Error deleting dependent records: " + e.getMessage());
            return 0;
        } finally {
            try {
                if (connect != null) {
                    connect.setAutoCommit(true);
                    connect.close();
                }
            } catch (SQLException closeE) {
                System.out.println("Error closing connection: " + closeE.getMessage());
            }
        }
    }

    /**
     * Deletes a branch record. It first deletes all associated dependent records
     * (Payments, Reservations, then Bikes) to satisfy the foreign key constraints.
     * 
     * @param branchID The ID of the branch to delete.
     * @return 1 on success, 0 on failure.
     */
    public int delBranchRecordData(int branchID) {

        /* 
        if (deleteDependentRecords(branchID) == 0) {
            System.out.println("Deletion aborted due to failure in deleting dependent records.");
            return 0;
        }*/

        try (Connection connect = databaseConnection.getConnection();
                PreparedStatement prepState = connect.prepareStatement(
                        "DELETE FROM branch WHERE branchID=?")) {

            prepState.setInt(1, branchID);
            prepState.executeUpdate();

            return 1;
        } catch (SQLException e) {
            System.out.println("Error deleting branch record: " + e.getMessage());
            return 0;
        }
    }

    // HELPER TO EXTRACT FROM RESULT SET
    private branchRecordModel extractFromBranchTable(ResultSet rs) throws SQLException {
        branchRecordModel branch = new branchRecordModel();
        branch.setBranchID(rs.getInt("branchID"));
        branch.setBranchName(rs.getString("branchName"));
        branch.setBranchAddress(rs.getString("branchAddress"));
        return branch;
    }

    // RETRIEVES ALL BRANCH RECORD FROM THE BRANCH DATABASE
    public List<branchRecordModel> getAllBranches() {
        List<branchRecordModel> branches = new ArrayList<>();
        String query = "SELECT * FROM branch";

        try (Connection connect = databaseConnection.getConnection();
                PreparedStatement prepState = connect.prepareStatement(query);
                ResultSet result = prepState.executeQuery()) {

            while (result.next()) {
                branchRecordModel branch = new branchRecordModel();
                branch.setBranchID(result.getInt("branchID"));
                branch.setBranchName(result.getString("branchName"));
                branch.setBranchAddress(result.getString("branchAddress"));
                branches.add(branch);
            }
        } catch (SQLException e) {
            System.out.println("Error loading branches: " + e.getMessage());
        }
        return branches;
    }

    // RETRIEVES A SPECIFIC BRANCH RECORD, BASED ON THE BRANCH NAME PASSED IN THE
    // PARAMETER
    public branchRecordModel getBranch(String branchName) {
        try (Connection connect = databaseConnection.getConnection();
                PreparedStatement prepState = connect.prepareStatement(
                        "SELECT * FROM branch WHERE branchName = ?")) {

            prepState.setString(1, branchName);
            ResultSet result = prepState.executeQuery();

            if (result.next()) {
                return extractFromBranchTable(result);
            }

        } catch (SQLException e) {
            System.out.println("Error getting branch: " + e.getMessage());
        }
        return null;
    }
}