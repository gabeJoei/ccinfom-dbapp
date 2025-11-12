package com.grp5.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.grp5.model.bikeRecordModel;
import com.grp5.utils.databaseConnection;

public class bikeRecordDAO {

    // Create
    public int addBikeRecord(bikeRecordModel bikeRecord) {
        String query = "INSERT INTO bike (bikeID,branchIDNum,bikeAvailability,bikeModel,hourlyRate,dailyRate) VALUES (?,?,?,?,?,?)";
        try (Connection connect = databaseConnection.getConnection();
                PreparedStatement prepState = connect.prepareStatement(query);) {
            prepState.setInt(1, bikeRecord.getBikeID());
            prepState.setInt(2, bikeRecord.getBranchIDNum());
            prepState.setBoolean(3, bikeRecord.getBikeAvailability());
            prepState.setString(4, bikeRecord.getBikeModel());
            prepState.setBigDecimal(5, bikeRecord.getHourlyRate());
            prepState.setBigDecimal(6, bikeRecord.getDailyRate());

            int rowsInserted = prepState.executeUpdate();
            return rowsInserted;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    // Read
    public bikeRecordModel getBikeRecord(int bikeID) {
        String query = "SELECT bikeID,branchIDNum,bikeAvailability,bikeModel,hourlyRate,dailyRate FROM bike WHERE bikeID=?";
        try (Connection connect = databaseConnection.getConnection();
                PreparedStatement prepState = connect.prepareStatement(query);) {

            prepState.setInt(1, bikeID);
            try (ResultSet result = prepState.executeQuery();) {
                if (result.next()) {
                    return extractBikeFromResultSet(result);
                }
            }
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private bikeRecordModel extractBikeFromResultSet(ResultSet result) throws SQLException {
        bikeRecordModel bikeRecord = new bikeRecordModel(result.getInt("bikeID"), result.getString("bikeModel"));
        bikeRecord.setBranchIDNum(result.getInt("branchIDNum"));
        bikeRecord.setBikeAvailability(result.getBoolean("bikeAvailability"));
        bikeRecord.setHourlyRate(result.getBigDecimal("hourlyRate"));
        bikeRecord.setDailyRate(result.getBigDecimal("dailyRate"));
        return bikeRecord;
    }

    // Update
    public int updateBikeRecord(bikeRecordModel bikeRecord) {
        String query = "UPDATE bike SET branchIDNum = ?, bikeAvailability = ?, bikeModel = ?, hourlyRate = ?, dailyRate = ? WHERE bikeID = ?";
        try (Connection connect = databaseConnection.getConnection();
                PreparedStatement prepState = connect.prepareStatement(query)) {

            prepState.setInt(1, bikeRecord.getBranchIDNum());
            prepState.setBoolean(2, bikeRecord.getBikeAvailability());
            prepState.setString(3, bikeRecord.getBikeModel());
            prepState.setBigDecimal(4, bikeRecord.getHourlyRate());
            prepState.setBigDecimal(5, bikeRecord.getDailyRate());
            prepState.setInt(6, bikeRecord.getBikeID());

            int rowsAffected = prepState.executeUpdate();
            return rowsAffected;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    // Delete
    public int deleteBikeRecord(int bikeID) {
        String query = "DELETE FROM bike WHERE bikeID=?";
        try (Connection connect = databaseConnection.getConnection();
                PreparedStatement prepState = connect.prepareStatement(query);) {

            prepState.setInt(1, bikeID);
            int rowsDeleted = prepState.executeUpdate();

            return rowsDeleted;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    // TO-DO: Create "nice-to-have" methods
    // updateRates()
    // updateBikeAvailability
    // updateBikeModel
    // updateBranch
}
