
package com.grp5.model;

/**
 * Model for branch record in the Bike Rental System.
 * 
 * This model stores key information related to a branch record,
 * including the branch name, branch address.
 * It provides getters and setters for all fields.
 * 
 * @author [group 5]
 * @verion 1.0
 */

public class branchRecordModel {
/*Variable declaration */
private int branchID;
private String branchName;
private String branchAddress;
private int locationID;

/* Default constructor, empty arguments. 
* Used for initialization of new and empty 
* branchRecordModel Object
*/
public branchRecordModel(){}

/**
 * Full constructor, used for initialization of new 
 * and complete branch record. 
 * 
 * @param branchID, the unique identifier of a branch
 * @param branchName, the name of the branch
 * @param branchAddress, the address of the branc
 * @param locationID, the unique identifier of a location
 */
public branchRecordModel( int branchID,String branchName, String branchAddress, int locationID){
    this.branchID=branchID;
    this.locationID=locationID;
    this.branchName=branchName;
    this.branchAddress=branchAddress;
}

/**
* sets the branch Id
* @param branID, new branch ID
*/
public void setBranchID(int branID){
    branchID=branID;
}

/**
* sets the location Id
* @param locID, new location 
*/
public void setLocationID(int locID){
    locationID=locID;
}

/**
* sets the branch name
* @param bName, new branch Name
*/
public void setBranchName(String bName){
    branchName=bName;
}

/**
* sets the branch address
* @param add, new branch address
*/
public void setBranchAddress(String add){
    branchAddress=add;
}


/**
* gets the branch id
* @return branch id
*/     
public int getBranchID(){return branchID;}

/**
* gets the location id
* @return location id
*/
public int getLocationID(){return locationID;}

/**
* gets the branch name
* @return branch name
*/
public String getBranchName(){return branchName;}

/**
* gets the branch address
* @return branch address
*/
public String getBranchAddress(){return branchAddress;}

/**
* The string representation of the branch record model.
* @return a string containing the branchID, branchName, branchAddress
* locationID.
*/
public String toString(){
        return "{branch"+
                "branchID"+branchID
                +",branchName="+branchName
                +",branchAddress="+branchAddress
                +",locationID="+locationID
                +"\n}";
        
    }
    
}

