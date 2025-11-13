package com.grp5.reports;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.grp5.utils.databaseConnection;



public class customerEngagementReport {
    //in this section you just need to display 
    private int customerEngagement;
    private int customerID;
    private String customerName;
    private String branchName;

    public customerEngagementReport(){}
    public customerEngagementReport(int eCount, int id, String cName, String bName){
        customerEngagement=eCount;
        customerID=id;
        customerName=cName;
        branchName=bName;
    }

    public int getCustomerEngagement(){return customerEngagement;}
    public int getCustomerID(){return customerID;}
    public String getCustomerName(){return customerName;}
    public String branchName(){return branchName;}

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

    public ArrayList<customerEngagementReport> customerEngageReport(String startDate, String endDate){

        if(startDate==""||startDate.isEmpty()){
            startDate="0000-00-00";
        }
        if(endDate==""||endDate.isEmpty()){
            endDate="9999-12-31";
        }
        
        String query="SELECT COUNT(p.customerID) as Engagement,p.customerID as 'Customer ID',concat(c.firstName,' ',c.lastName) as Name, b.branchName as 'Branch Name' "+
            "FROM payment p "+
            "JOIN customer c ON p.customerID=c.customerAccID "+
            "JOIN branch b ON p.branchID=b.branchID "+
            "WHERE p.paymentDate BETWEEN ? AND ? "+
            "GROUP BY p.customerID, b.branchName "+
            "ORDER BY Engagement ";
             ArrayList<customerEngagementReport> cEReport=new ArrayList<>();
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
            System.out.println(e.getMessage());
            }
    return cEReport;
    }
}
