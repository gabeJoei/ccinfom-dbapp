package com.grp5.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.grp5.model.customerRecordModel;
import com.grp5.utils.databaseConnection;

public class customerRecordDAO {

    // CREATE 
    public boolean addCustomerRecordData(customerRecordModel customer){
        String query = "INSERT INTO customer (customerAccID,lastName,firstName,customerEmail,phoneNumber,customerPass) VALUES (?,?,?,?,?,?)";
        try (Connection connect=databaseConnection.getConnection();  
            PreparedStatement prepState=connect.prepareStatement(query)) {
            
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
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    // READ
    public customerRecordModel getCustomerRecordData(customerRecordModel customer){
        String query = "SELECT * FROM customer WHERE customerAccID=?";
        try (Connection connect=databaseConnection.getConnection(); 
            PreparedStatement prepState=connect.prepareStatement(query)) {

            prepState.setInt(1, customer.getCustomerAccID());

            ResultSet result=prepState.executeQuery(); 
            while (result.next()){
                return extractCustomerFromResultSet(result);
            }
            
            result.close();
            prepState.close();
            connect.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // READ - CUSTOMER TRANSACTION
    public customerRecordModel getCustomerTransaction(customerRecordModel customer) {
        String query = "SELECT * FROM customer C JOIN payment P ON C.customerAccID=P.customerAccID WHERE C.customerAccID=?";
        try (Connection connect=databaseConnection.getConnection(); 
            PreparedStatement prepState=connect.prepareStatement(query)) {
            prepState.setInt(1, customer.getCustomerAccID());

            ResultSet result = prepState.executeQuery();
            while(result.next()) {
                return extractCustomerFromResultSet(result);
            }

            result.close();
            prepState.close();
            connect.close();
        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // UPDATE 
    public boolean modifyCustomerRecordData(customerRecordModel customer){
        String query = "UPDATE Customer SET lastName=?, firstName=?, email=?, phoneNum=?, customerPass=?, WHERE customerAccID=?";
        try (Connection connect=databaseConnection.getConnection(); 
            PreparedStatement prepState=connect.prepareStatement(query)){
            
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
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    // DELETE 
    public boolean delCustomerRecordData(int customerAccID){
        String query = "DELETE FROM Customer WHERE customerAccID=?";
        try (Connection connect=databaseConnection.getConnection(); 
            PreparedStatement prepState=connect.prepareStatement(query)){

            prepState.setInt(1,customerAccID);

            prepState.executeUpdate();
            prepState.close();
            connect.close();
            return true;
        }catch(SQLException e){
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
        customerModel.setEmail(result.getString("email"));
        customerModel.setPhoneNum(result.getString("phoneNum"));
        return customerModel;
    }
}

