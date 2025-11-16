package com.grp5.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.grp5.model.bikeRecordModel;
import com.grp5.model.branchRecordModel;
import com.grp5.utils.databaseConnection;


public class branchRecordDAO {
    public int addBranchRecordData(branchRecordModel branch){
        try{
            Connection connect=databaseConnection.getConnection();
            PreparedStatement prepState=connect.prepareStatement("INSERT INTO branch (branchName,branchAddress,locationID) VALUES (?,?,?) ");
            prepState.setString(1,branch.getBranchName());
            prepState.setString(2,branch.getBranchAddress());
            prepState.setInt(3,branch.getLocationID());
            prepState.executeUpdate();
            prepState.close();
            connect.close();
            return 1;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public int delBranchRecordData(int branchID){
        try{
            Connection connect=databaseConnection.getConnection();
            PreparedStatement prepState=connect.prepareStatement(
                "DELETE FROM branch WHERE branchID=?");
            prepState.setInt(1,branchID);
            prepState.executeUpdate();
            prepState.close();
            connect.close();
            return 1;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return 0;
        }
       
    }
    public int updateBranchRecordData(branchRecordModel branch){
        try{
            Connection connect=databaseConnection.getConnection();
            PreparedStatement prepState=connect.prepareStatement(
                "UPDATE branch SET branchName=?,branchAddress=?,locationID=? WHERE branchID=?"); 
            prepState.setString(1,branch.getBranchName());
            prepState.setString(2,branch.getBranchAddress());
            prepState.setInt(3,branch.getLocationID());
            prepState.setInt(4,branch.getBranchID());
            prepState.executeUpdate();
            prepState.close();
            connect.close();
            return 1;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return 0;
        }
    }
    
    public branchRecordModel getBranchRecordData(int branchID){
        try{
            Connection connect=databaseConnection.getConnection();
            PreparedStatement prepState=connect.prepareStatement("SELECT * FROM branch WHERE branchID=?");
            prepState.setInt(1,branchID);
            ResultSet result=prepState.executeQuery();
            if (result.next()){
                
            }
            result.close();
            prepState.close();
            connect.close();
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<branchRecordModel> getAllBranch(){
        ArrayList<branchRecordModel> branch=new ArrayList<>();
        try{
            Connection connect=databaseConnection.getConnection();
            PreparedStatement prepState=connect.prepareStatement("SELECT * FROM branch");
            ResultSet result=prepState.executeQuery();
            while(result.next()){
                branch.add(extractFromBranchTable(result));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return branch;
    }

     private branchRecordModel extractFromBranchTable(ResultSet rs)throws SQLException {
        branchRecordModel branch=new branchRecordModel();
        branch.setBranchID(rs.getInt("branchID"));
        branch.setBranchName(rs.getString("branchName"));
        branch.setBranchAddress(rs.getString("branchAddress"));
        branch.setLocationID(rs.getInt("locationID"));
        return branch;
    }
    public List<branchRecordModel> getAllBranches() {
    List<branchRecordModel> branches = new ArrayList<>();
    String query = "SELECT * FROM branch";

    try (Connection connect = databaseConnection.getConnection();
         PreparedStatement prepState = connect.prepareStatement(query);
         ResultSet result = prepState.executeQuery()) {

        while (result.next()) {
            branchRecordModel branch = new branchRecordModel();
            branch.setBranchID(result.getInt("branchID"));
            branch.setBranchName(result.getString("branchName"));
            // Add other setters if needed
            branches.add(branch);
        }
    } catch (SQLException e) {
        System.out.println("Error loading branches: " + e.getMessage());
    }
    return branches;
}
public branchRecordModel getBranch(String branchName) {
    try (Connection connect = databaseConnection.getConnection();
         PreparedStatement prepState = connect.prepareStatement(
            "SELECT * FROM branch WHERE branchName = ?")) {

        prepState.setString(1, branchName);
        ResultSet result = prepState.executeQuery();

        if (result.next()) {
            return extractFromBranchTable(result);
        }

    } catch (SQLException e) {
        System.out.println("Error getting branch: " + e.getMessage());
    }
    return null;
}



}
