package com.grp5.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.grp5.model.transactionRecordModel;
import com.grp5.utils.databaseConnection;

public class transactionRecordDAO {

    public int addTransactionRecordData(transactionRecordModel transaction) {
        try {
            Connection connect = databaseConnection.getConnection();
            PreparedStatement prepState = connect.prepareStatement(
                "INSERT INTO payment (customerID, reservationReferenceNum, bikeID, branchID, paymentDate, paymentAmount) VALUES (?,?,?,?,?,?)"
            );

            prepState.setInt(1, transaction.getCustomerAccountID());
            prepState.setInt(2, transaction.getReservationReferenceNumber());
            prepState.setInt(3, transaction.getBikeID());
            prepState.setInt(4, transaction.getBranchID());
            prepState.setTimestamp(5, transaction.getPaymentDate());
            prepState.setBigDecimal(6, transaction.getPaymentAmount());

            prepState.executeUpdate();

            prepState.close();
            connect.close();

            return 1;
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return 0;
        }
    }

    public transactionRecordModel getTransactionRecordData(int paymentReferenceNumber) {
        try {
            Connection connect = databaseConnection.getConnection();
            PreparedStatement prepState = connect.prepareStatement("SELECT * FROM payment WHERE paymentReferenceNum=?");
            prepState.setInt(1, paymentReferenceNumber);
            
            ResultSet result = prepState.executeQuery();
            if (result.next()) {
                return extractFromPaymentTable(result);
            }
            result.close();
            prepState.close();
            connect.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public int delTransactionRecordData(int paymentReferenceNum) {
        try {
            Connection connect = databaseConnection.getConnection();
            PreparedStatement prepState = connect.prepareStatement("DELETE FROM payment WHERE paymentReferenceNum=?");
            prepState.setInt(1, paymentReferenceNum);
            prepState.executeUpdate();
            prepState.close();
            connect.close();
            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public int modifyTransactionRecordData(transactionRecordModel transaction) {
        String query = "UPDATE payment SET customerID=?, reservationReferenceNum=?, bikeID=?, branchID=?, paymentDate=?, paymentAmount=? WHERE paymentReferenceNum=?";
        try {
            Connection connect = databaseConnection.getConnection();
            PreparedStatement prepState = connect.prepareStatement(query); 
            prepState.setInt(1, transaction.getCustomerAccountID());
            prepState.setInt(2, transaction.getReservationReferenceNumber());
            prepState.setInt(3, transaction.getBikeID());
            prepState.setInt(4, transaction.getBranchID());
            prepState.setTimestamp(5, transaction.getPaymentDate());
            prepState.setBigDecimal(6, transaction.getPaymentAmount());
            prepState.setInt(7, transaction.getPaymentReferenceNumber());
            prepState.executeUpdate();
            prepState.close();
            connect.close();
            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public void paymentJoinCustomer(String columnName, int columnIVal, String columnSVal) {
        try {
            Connection connect = databaseConnection.getConnection();
            String query = "SELECT p.paymentReferenceNum as 'Reference Number', concat(c.firstName,' ',c.lastName) as Name, r.startDate as 'Reservation Date', b.branchName as 'Branch Name' " +
                "FROM payment p " +
                "JOIN customer c ON p.customerID=c.customerAccID " +
                "JOIN reservation r ON p.reservationReferenceNum=r.reservationReferenceNum " +
                "JOIN branch b ON p.branchID=b.branchID " +
                "WHERE p." + columnName + "=? " +
                "ORDER BY p.paymentReferenceNum";
            PreparedStatement prepState = connect.prepareStatement(query);
            if (columnIVal > -1) {
                prepState.setInt(1, columnIVal);
            } else {
                prepState.setString(1, columnSVal);
            }
            
            ResultSet result = prepState.executeQuery();
            while (result.next()) {
                System.out.print(result.getInt("Reference Number") + " ");
                System.out.print(result.getString("Name") + " ");
                System.out.print(result.getTimestamp("Reservation Date") + " ");
                System.out.print(result.getString("Branch Name") + " \n");
            }
            
            result.close();
            prepState.close();
            connect.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<transactionRecordModel> getAllPayment() {
        ArrayList<transactionRecordModel> transaction = new ArrayList<>();
        try {
            Connection connect = databaseConnection.getConnection();
            PreparedStatement prepState = connect.prepareStatement("SELECT * FROM payment");
            ResultSet result = prepState.executeQuery();
            while (result.next()) {
                transaction.add(extractFromPaymentTable(result));
            }
            result.close();
            prepState.close();
            connect.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return transaction;
    }

    private transactionRecordModel extractFromPaymentTable(ResultSet rs) throws SQLException {
        transactionRecordModel transaction = new transactionRecordModel();
        transaction.setPaymentReferenceNumber(rs.getInt("paymentReferenceNum"));
        transaction.setCustomerAccountID(rs.getInt("customerID"));
        transaction.setReservationReferenceNum(rs.getInt("reservationReferenceNum"));
        transaction.setBikeID(rs.getInt("bikeID"));
        transaction.setBranchID(rs.getInt("branchID"));
        transaction.setPaymentDate(rs.getTimestamp("paymentDate"));
        transaction.setPaymentAmount(rs.getBigDecimal("paymentAmount"));
        return transaction;
    }
}