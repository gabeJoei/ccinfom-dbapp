package com.grp5.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grp5.model.customerRecordModel;
import com.grp5.utils.databaseConnection;

public class customerRecordDAO {

    // CREATE
    public boolean addCustomerRecordData(customerRecordModel customer) {
        String query = "INSERT INTO customer (customerAccID,lastName,firstName,customerEmail,phoneNumber,customerPass) VALUES (?,?,?,?,?,?)";
        try (Connection connect = databaseConnection.getConnection();
                PreparedStatement prepState = connect.prepareStatement(query)) {

            prepState.setInt(1, customer.getCustomerAccID());
            prepState.setString(2, customer.getLastName());
            prepState.setString(3, customer.getFirstName());
            prepState.setString(4, customer.getEmail());
            prepState.setString(5, customer.getPhoneNum());
            prepState.setString(6, customer.getCustomerPass());

            prepState.executeUpdate();

            prepState.close();
            connect.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // READ
    public customerRecordModel getCustomerRecordData(customerRecordModel customer) {
        String query = "SELECT * FROM customer WHERE customerAccID=?";
        try (Connection connect = databaseConnection.getConnection();
                PreparedStatement prepState = connect.prepareStatement(query)) {

            prepState.setInt(1, customer.getCustomerAccID());

            ResultSet result = prepState.executeQuery();
            while (result.next()) {
                return extractCustomerFromResultSet(result);
            }

            result.close();
            prepState.close();
            connect.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // READ - CUSTOMER TRANSACTION
    public customerRecordModel getCustomerTransaction(customerRecordModel customer) {
        String query = "SELECT * FROM customer C JOIN payment P ON C.customerAccID=P.customerAccID WHERE C.customerAccID=?";
        try (Connection connect = databaseConnection.getConnection();
                PreparedStatement prepState = connect.prepareStatement(query)) {
            prepState.setInt(1, customer.getCustomerAccID());

            ResultSet result = prepState.executeQuery();
            while (result.next()) {
                return extractCustomerFromResultSet(result);
            }

            result.close();
            prepState.close();
            connect.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // UPDATE
    public boolean modifyCustomerRecordData(customerRecordModel customer) {
        String query = "UPDATE Customer SET lastName=?, firstName=?, customerEmail=?, phoneNumber=?, customerPass=? WHERE customerAccID=?";
        try (Connection connect = databaseConnection.getConnection();
                PreparedStatement prepState = connect.prepareStatement(query)) {

            prepState.setString(1, customer.getLastName());
            prepState.setString(2, customer.getFirstName());
            prepState.setString(3, customer.getEmail());
            prepState.setString(4, customer.getPhoneNum());
            prepState.setString(5, customer.getCustomerPass());
            prepState.setInt(6, customer.getCustomerAccID());

            prepState.executeUpdate();
            prepState.close();
            connect.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean delCustomerRecordData(int customerAccID) {
        String query = "DELETE FROM Customer WHERE customerAccID=?";
        try (Connection connect = databaseConnection.getConnection();
                PreparedStatement prepState = connect.prepareStatement(query)) {

            prepState.setInt(1, customerAccID);

            prepState.executeUpdate();
            prepState.close();
            connect.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Helper to extract from ResultSet
    private customerRecordModel extractCustomerFromResultSet(ResultSet result) throws SQLException {
        customerRecordModel customerModel = new customerRecordModel();
        customerModel.setCustomerAccID(result.getInt("customerAccID"));
        customerModel.setLastName(result.getString("lastName"));
        customerModel.setFirstName(result.getString("firstName"));
        customerModel.setEmail(result.getString("customerEmail"));
        customerModel.setPhoneNum(result.getString("phoneNumber"));
        customerModel.setCustomerPass(result.getString("customerPass"));
        return customerModel;
    }

    // Add to customerRecordDAO.java

/**
 * Authenticate customer by email and password
 * @param email Customer email
 * @param password Customer password
 * @return customerRecordModel if authenticated, null otherwise
 */
public customerRecordModel authenticateCustomer(String email, String password) {
    String sql = "SELECT * FROM customer WHERE customerEmail = ? AND customerPass = ?";
    
    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, email);
        pstmt.setString(2, password); // In production, use hashed passwords!
        
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            customerRecordModel customer = new customerRecordModel();
            customer.setCustomerAccID(rs.getInt("customerAccID"));
            customer.setFirstName(rs.getString("firstName"));
            customer.setLastName(rs.getString("lastName"));
            customer.setEmail(rs.getString("customerEmail"));
            customer.setPhoneNum(rs.getString("phoneNumber"));
            return customer;
        }
        
    } catch (SQLException e) {
        System.err.println("Authentication error: " + e.getMessage());
        e.printStackTrace();
    }
    
    return null;
}
public List<customerRecordModel> getAllCustomers() {
    List<customerRecordModel> customers = new ArrayList<>();
    String query = "SELECT * FROM customer";

    try (Connection connect = databaseConnection.getConnection();
         PreparedStatement prepState = connect.prepareStatement(query);
         ResultSet result = prepState.executeQuery()) {

        while (result.next()) {
            customerRecordModel customer = extractCustomerFromResultSet(result);
            customers.add(customer);
        }
    } catch (SQLException e) {
        System.out.println("Error loading customers: " + e.getMessage());
    }
    return customers;
}

public customerRecordModel getCustomer(int customerAccID) {
    String query = "SELECT * FROM customer WHERE customerAccID=?";
    try (Connection connect = databaseConnection.getConnection();
         PreparedStatement prepState = connect.prepareStatement(query)) {

        prepState.setInt(1, customerAccID);

        try (ResultSet result = prepState.executeQuery()) {
            if (result.next()) {
                return extractCustomerFromResultSet(result);
            }
        }

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return null;
}
}

