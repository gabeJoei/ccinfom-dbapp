package com.grp5.utils;
import java.sql.*;
import java.util.concurrent.ThreadLocalRandom;

/*Generates long numbers with 11 characters. */
public class generalUtilities{
    private generalUtilities(){}
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
}
