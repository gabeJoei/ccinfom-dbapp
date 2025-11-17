/*
 * Model for customer record in the Bike Rental System.
 * 
 * This model stores key information related to a customer record,
 * including the first name, last name, 
 * email address, phone number, and customer password.
 * It provides getters and setters for all fields.
 * 
 */

package com.grp5.model;

public class customerRecordModel {
    /* Variable Declarations*/
    private int customerAccID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNum;
    private String customerPass;

    // empty constructor
    public customerRecordModel() {
    }

    public customerRecordModel(int customerAccID) {
        this.customerAccID = customerAccID;
    }

    // full constructor
    public customerRecordModel(int customerAccID, String firstName, String lastName,
            String email, String phoneNum, String customerPass) {
        this.customerAccID = customerAccID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.customerPass = customerPass;
    }

    // Setters
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

    public void setCustomerPass(String customerPass) {
        this.customerPass = customerPass;
    }

    // Getters
    public int getCustomerAccID() {
        return this.customerAccID;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    public String getCustomerPass() {
        return this.customerPass;
    }

    public static void main(String[] args) {
    }

}
