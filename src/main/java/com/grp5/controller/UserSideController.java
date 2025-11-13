package com.grp5.controller;
import com.grp5.model.customerRecordModel;
import com.grp5.utils.databaseConnection;
import java.sql.*;

public class UserSideController {
    //access sql, to check if may existing acc under that email
    public boolean duplicateAccounts(String inputedEmail){
        String query="SELECT COUNT(customerEmail) FROM customer WHERE customerEmail=? LIMIT 1";
        try(Connection connect=databaseConnection.getConnection();
        PreparedStatement prepState=connect.prepareStatement(query)){
            prepState.setString(1, inputedEmail);
            try( ResultSet result=prepState.executeQuery()){
            if(result.next())return result.getInt(1)>0;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    //checks if there is an email and password already in the database matching the input.
    public customerRecordModel loginChecker(String inputedEmail, String inputedPass){
        String query="SELECT * FROM customer WHERE customerEmail=? AND customerPass=?";
        customerRecordModel user=new customerRecordModel();
        try(Connection connect=databaseConnection.getConnection();
        PreparedStatement prepState=connect.prepareStatement(query)){
            prepState.setString(1, inputedEmail.trim());
            prepState.setString(2, inputedPass.trim());
           ResultSet result=prepState.executeQuery();
           if(result.next()){
                user=new customerRecordModel(
                    result.getInt("customerAccID"),
                    result.getString("firstName"),
                    result.getString("lastName"),
                    result.getString("customerEmail"),
                    result.getString("phoneNumber"),
                    result.getString("customerPass")

                );
                return user;
            
            
           }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }


}
