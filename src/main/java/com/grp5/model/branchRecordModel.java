package com.grp5.model;

public class branchRecordModel {
/*Variable declaration */
private int branchID;
private String branchName;
private String branchAddress;
private int locationID;
public void setBranchID(int branID){
    branchID=branID;
}
public void setLocationID(int locID){
    locationID=locID;
}

public void setBranchName(String bName){
    branchName=bName;
}
public void setBranchAddress(String add){
    branchAddress=add;
}

public int getBranchID(){return branchID;}
public int getLocationID(){return locationID;}
public String getBranchName(){return branchName;}
public String getBranchAddress(){return branchAddress;}

public branchRecordModel(){}

public branchRecordModel( int branchID,String branchName, String branchAddress, int locationID){
    this.branchID=branchID;
    this.locationID=locationID;
    this.branchName=branchName;
    this.branchAddress=branchAddress;
}

public String toString(){
        return "{branch"+
                "branchID"+branchID
                +",branchName="+branchName
                +",branchAddress="+branchAddress
                +",locationID="+locationID
                +"\n}";
        
    }
    
}

