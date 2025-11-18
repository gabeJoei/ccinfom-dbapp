package com.grp5.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.grp5.model.transactionRecordModel;
import com.grp5.utils.databaseConnection;

/**
 * Data Access Object (DAO) for managing payment transaction records.
 * 
 * This class handles all CRUD operations for the payment database table,
 * including inserting, retrieving, updating, and deleting payment transactions.
 * It also provides utility methods for joining related customer, reservation,
 * and branch data.
 * @author [group 5]
 * @version 1.0
 */
public class transactionRecordDAO {

    /**
     * A method used to insert new data into the payment transaction record. 
     * @param transaction, transactionRecordModel which has the values for the transaction record
     * @return an interger value of 1 or 0, 1 to indicate success and 0 to indicate null/unsuccessfull.
     */
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

    /**
     * retrieves a transaction record that equates to the paymentReferenceNum
     * @param paymentReferenceNumber, the Primary key of a payment table/transaction record
     * @return transactionRecordModel
     */
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


    /**
     * deletes a transaction record that equates to the paymentReferenceNum
     * @param paymentReferenceNum, unique identifier for a specific transaction record
     * @return int, 1 for true, and 0 false
     */
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

    /**
     * Allows the modification of the transaction record, with the new values of the transaction (parameter)
     * @param transaction, new transactionRecordModel that can replace the values of a specific record. 
     * @return int, 1 for true, and 0 false
     */
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

    
    /**
     * *a method used to retrieve a payment made by a specific customer. 
     * It joins payment with customer and branch table.
     * 
     * @param columnName
     * @param columnIVal
     * @param columnSVal
     */
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

    /**
     * retrieves all payement transaction record in the transaction database.  
     * @return transaction, ArrayLists of transactionRecordModel that contains all the record found in payment database
     */
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

    /**
     * Converts the results of the query to become transactionRecordModel, then returns that transaction. 
     * @param rs
     * @return transaction
     * @throws SQLException
     */
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