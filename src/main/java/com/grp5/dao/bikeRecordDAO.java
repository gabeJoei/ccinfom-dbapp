package com.grp5.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.grp5.model.bikeRecordModel;
import com.grp5.utils.databaseConnection;

public class bikeRecordDAO {

    // Create
    public int addBikeRecord(bikeRecordModel bikeRecord) {
        try {
            Connection connect = databaseConnection.getConnection();

            PreparedStatement prepState = connect.prepareStatement(
                    "INSERT INTO bike (bikeID,branchIDNum,bikeAvailability,bikeModel,hourlyRate,dailyRate) VALUES (?,?,?,?,?,?)");
            prepState.setInt(1, bikeRecord.getBikeID());
            prepState.setInt(2, bikeRecord.getBranchIDNum());
            prepState.setBoolean(3, bikeRecord.getBikeAvailability());
            prepState.setString(4, bikeRecord.getBikeModel());
            prepState.setBigDecimal(5, bikeRecord.getHourlyRate());
            prepState.setBigDecimal(6, bikeRecord.getDailyRate());

            int rowsInserted = prepState.executeUpdate();
            prepState.close();
            connect.close();
            return rowsInserted;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    // Read
    public bikeRecordModel getBikeRecord(int bikeID) {
        try {
            Connection connect = databaseConnection.getConnection();
            PreparedStatement prepState = connect.prepareStatement("SELECT * FROM bike WHERE ?");
            prepState.setInt(1, bikeID);
            ResultSet result = prepState.executeQuery();

            while (result.next()) {

            }

            connect.close();
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private bikeRecordModel extractBikeFromResultSet(ResultSet result) throws SQLException {
        bikeRecordModel bikeRecord = new bikeRecordModel(result.getInt("bikeID"), result.getString("bikeModel"));
        bikeRecord.setBranchIDNum(result.getInt("branchIDNum"));
        // TO-DO : Modify SQL file to only store True or False
        return bikeRecord;
    }

    // Update

    // Delete
    public int deleteBikeRecord(int bikeID) {
        try {
            Connection connect = databaseConnection.getConnection();

            PreparedStatement prepState = connect.prepareStatement("DELETE from bike WHERE bikeID=?");
            prepState.setInt(1, bikeID);
            int rowsDeleted = prepState.executeUpdate();

            connect.close();
            return rowsDeleted;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
}
