package com.grp5.model;
public class customerRecordModel {
    /*Declare Variables: Make it String */
    private int customerAccID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNum;

    // empty constructor
    public customerRecordModel() {}

    // full constructor
    public customerRecordModel(int customerAccID, String firstName, String lastName, 
                                String email, String phoneNum){
        this.customerAccID = customerAccID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    // ============ Setters ============
    public void setCustomerAccID(int customerAccID) {
        this.customerAccID = customerAccID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    // ============ Getters ============
    public int getCustomerAccID() { return this.customerAccID; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public String getEmail() { return this.email; }
    public String getPhoneNum() { return this.phoneNum; }
    
    public static void main(String[] args){

    }
}
