package com.grp5.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.grp5.utils.databaseConnection;


/**
 * Generates customer engagement reports based on payment activity within a specified date range.
 * This report calculates how many payments (engagements) each customer made, 
 * including the customer's name and associated branch. 
 * Results are sorted by highest engagement first.
 **/
public class customerEngagementReport {
    
    // Fields
    private int customerEngagement;
    private int customerID;
    private String customerName;
    private String branchName;

    // Constructors
    public customerEngagementReport(){}
    
    public customerEngagementReport(int eCount, int id, String cName, String bName){
        customerEngagement=eCount;
        customerID=id;
        customerName=cName;
        branchName=bName;
    }

    // Getters 
    public int getCustomerEngagement(){return customerEngagement;}
    public int getCustomerID(){return customerID;}
    public String getCustomerName(){return customerName;}
    public String getBranchName(){return branchName;} // Corrected method name to standard JavaFX convention

    // Setters
    public void setCustomerEngagement(int count){
        customerEngagement=count;
    }
    public void setCustomerID(int ID){
        customerID=ID;
    }
    public void setCustomerName(String name){
        customerName=name;
    }
    public void setBranchName(String bName){
        branchName=bName;
    }

    public String toString(){
        return String.format("Customer Engagement Count: %d , Customer ID: %d , Customer Name: %s",
        customerEngagement,customerID,customerName);
    }

    /*Retrieves customer engagement data based on payments between two dates. 
    It counts the total engagement a customer has with the bike rental*/
    public ArrayList<customerEngagementReport> customerEngageReport(String startDate, String endDate){

        if(startDate == null || startDate.isEmpty()){
            startDate="0000-00-00";
        }
        if(endDate == null || endDate.isEmpty()){
            endDate="9999-12-31";
        }
        

        String query="SELECT COUNT(p.customerID) as Engagement, p.customerID as 'Customer ID', concat(c.firstName,' ',c.lastName) as Name, b.branchName as 'Branch Name' "+
            "FROM payment p "+
            "JOIN customer c ON p.customerID=c.customerAccID "+
            "JOIN branch b ON p.branchID=b.branchID "+
            "WHERE DATE(p.paymentDate) BETWEEN ? AND ? "+ 
            "GROUP BY p.customerID, b.branchName, c.firstName, c.lastName "+
            "ORDER BY Engagement DESC"; 
            
            ArrayList<customerEngagementReport> cEReport = new ArrayList<>();
        try(Connection connect=databaseConnection.getConnection();
            PreparedStatement prepState=connect.prepareStatement(query);)
            {
            prepState.setString(1,startDate);
            prepState.setString(2,endDate);
            
            try(ResultSet result=prepState.executeQuery()){
                while(result.next()){
                cEReport.add(
                    new customerEngagementReport(
                        result.getInt("Engagement"),
                        result.getInt("Customer ID"),
                        result.getString("Name"),
                        result.getString("Branch Name")
                            )
                        );
                }
            }
            if(cEReport.isEmpty()){
                System.out.println("No Customer Engagment during that Date Range");
                }
            }catch(SQLException e){
            System.err.println("SQL Error in customerEngageReport: " + e.getMessage());
            e.printStackTrace();
            }
        return cEReport;
    }
}