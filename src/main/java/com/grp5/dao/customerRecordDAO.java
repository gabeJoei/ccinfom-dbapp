package com.grp5.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grp5.model.customerRecordModel;
import com.grp5.utils.databaseConnection;

/**
 * Data Access Object (DAO) for managing customer records.
 * 
 * This class handles all CRUD operations for the customer database table,
 * including inserting, retrieving, updating, deleting customer records
 * and other supporting methods.
 *
 */
public class customerRecordDAO {

    // CREATE || inserts a new record to the customer record
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

    // READ || it reads a record from the customer database that has the same customer ID. 
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

    // READ - CUSTOMER TRANSACTION || It retrieves all the payement transaction made by a customer
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

    // UPDATE || it updates a record from the customer data base
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

    // DELETE || it deletes a record from the customer database
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

    // Updates a customer's password 
    public boolean updateCustomerPassword(int customerAccID, String newPass) {
        String sql = "UPDATE customer SET customerPass=? WHERE customerAccID=?";
        try (Connection connect = databaseConnection.getConnection();
                PreparedStatement prepState = connect.prepareStatement(sql)) {

            prepState.setString(1, newPass);
            prepState.setInt(2, customerAccID);

            int updated = prepState.executeUpdate();
            return updated == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Add to customerRecordDAO.java

    /**
     * Authenticate customer by email and password
     * 
     * @param email    Customer email
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

    //retrieves all the records in the customer data base
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

    //gets a specific customer record in the database that matches the customerAccID passed through the parameter
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

    //checks if the email exists in the customer database. 
    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM customer WHERE customerEmail = ?";
        try (Connection connect = databaseConnection.getConnection();
                PreparedStatement prepState = connect.prepareStatement(query)) {

            prepState.setString(1, email);
            ResultSet result = prepState.executeQuery();

            if (result.next()) {
                return result.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error checking email existence: " + e.getMessage());
        }
        return false;
    }

    //retrieves the next available customerAccID. 
    public int getNextCustomerAccID() {
        String query = "SELECT MAX(customerAccID) as maxID FROM customer";
        try (Connection connect = databaseConnection.getConnection();
                PreparedStatement prepState = connect.prepareStatement(query)) {

            ResultSet result = prepState.executeQuery();

            if (result.next()) {
                int maxId = result.getInt("maxID");
                return maxId > 0 ? maxId + 1 : 100000; // Start from 100000 if no records
            }

        } catch (SQLException e) {
            System.out.println("Error getting next customer ID: " + e.getMessage());
        }
        return 100000; // Default starting ID
    }

    //
    public boolean createCustomer(customerRecordModel customer) {
        return addCustomerRecordData(customer);
    }
}
