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
        Connection connect=databaseConnection.getConnection();
        PreparedStatement prepState = connect.prepareStatement(
 "INSERT INTO payment (paymentReferenceNum,customerID,reservationReferenceNum,bikeID,branchID,paymentDate,paymentAmount) VALUES (?,?,?,?,?,?,?)"
        );

        prepState.setInt(1, transaction.getPaymentReferenceNumber());
        prepState.setInt(2, transaction.getCustomerAccountID());
        prepState.setInt(3, transaction.getReservationReferenceNumber());
        prepState.setInt(4, transaction.getBikeID());
        prepState.setInt(5, transaction.getBrachID());
        prepState.setTimestamp(6, transaction.getpaymentDate());
        prepState.setBigDecimal(7, transaction.getPaymentAmount());

        prepState.executeUpdate();

        prepState.close();
        connect.close();

        return 1;
    }catch (SQLException e) {
        System.out.println("SQL Error: " + e.getMessage());
        return 0;
    }
}

    public transactionRecordModel getTransactionRecordData(int paymentReferenceNumber){
        try{
            Connection connect=databaseConnection.getConnection();
            PreparedStatement prepState=connect.prepareStatement("SELECT * FROM payment WHERE paymentReferenceNum=?");
            prepState.setInt(1,paymentReferenceNumber);
            
            ResultSet result=prepState.executeQuery();
            if (result.next()){
                return extractFromTable(result);
            }
            result.close();
            prepState.close();
            connect.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<transactionRecordModel> getAllPayment(){
        ArrayList<transactionRecordModel> transaction=new ArrayList<>();
        try{
            Connection connect=databaseConnection.getConnection();
            PreparedStatement prepState=connect.prepareStatement("SELECT * FROM payment");
            ResultSet result=prepState.executeQuery();
            while(result.next()){
                transaction.add(extractFromTable(result));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return transaction;
    }

    private transactionRecordModel extractFromTable(ResultSet rs)throws SQLException {
        transactionRecordModel transaction=new transactionRecordModel();
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
