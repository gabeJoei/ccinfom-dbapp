package com.grp5.utils;
import java.sql.*;
import java.util.concurrent.ThreadLocalRandom;

import com.grp5.model.customerRecordModel;

import javafx.scene.control.Alert;

//General Utilities used as support
public class generalUtilities{

    //constructor
    private generalUtilities(){}

    /*Generates long numbers with 11 characters. */
    public static int generateRandIntNum(){
        int randomNum;
        int min=1000000;
        int max=9999999;
        randomNum=ThreadLocalRandom.current().nextInt(min,max);
        return randomNum;
    }
    /* Checks if there are duplicates, if none assigns the generated number to PK*/
    public static int primaryNumChecker(int primaryNumber, String column, String entity){
        try{
        int count=0;
        Connection connect=databaseConnection.getConnection();
        PreparedStatement prepState=connect.prepareStatement(
            "SELECT COUNT("+column+") FROM "+entity+" WHERE "+column+"=?");
        prepState.setLong(1,primaryNumber);
        ResultSet result=prepState.executeQuery();
        result.next();
        count=result.getInt(1);
        if(count>0){
        primaryNumChecker(generateRandIntNum(),column,entity);
        }
        return primaryNumber;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return 0;
        }
     

    }
    /*checks if the customer email address already exists in the database.
    prevents duplicates.
    */
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

    /**
     * Helper method to show a simple alert dialog.
     */
    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
