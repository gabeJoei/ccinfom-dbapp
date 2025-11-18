package com.grp5.model;

/**
 * Model for customer record in the Bike Rental System.
 * 
 * This model stores key information related to a customer record,
 * including the first name, last name, 
 * email address, phone number, and customer password.
 * It provides getters and setters for all fields.
 * 
 * @author [group 5]
 * @version 1.0
 */
public class customerRecordModel {
    /* Variable Declarations*/
    private int customerAccID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNum;
    private String customerPass;

    /* Default constructor, empty arguments. 
     * Used for initialization of new and empty 
     * customerRecordModel Object
    */
    public customerRecordModel() {
    }

    /** Partial constructor, with one parameter. 
     * Used for initialization of new
     * transactionRecordModel Object with customer account ID
     * 
     * @param customerAccID, the new customer account id.
    */
    public customerRecordModel(int customerAccID) {
        this.customerAccID = customerAccID;
    }

    /**
     * Full constructor, used for initialization of new 
     * and complete customer record. 
     * @param customerAccID, the unique identifier of a specific customer. 
     * @param firstName, the first name of the customer
     * @param lastName, the last name of the customer
     * @param email, the email address of the customer
     * @param phoneNum, the phone number of the customer
     * @param customerPass, the customer password of the customer
     */
    public customerRecordModel(int customerAccID, String firstName, String lastName,
            String email, String phoneNum, String customerPass) {
        this.customerAccID = customerAccID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.customerPass = customerPass;
    }

   
     /**
     * sets the customer account id.
     * @param customerAccID, new customer account id
     */
    public void setCustomerAccID(int customerAccID) {
        this.customerAccID = customerAccID;
    }

    
     /**
     * sets the first name
     * @param firstName, new customer first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * sets the last name
     * @param lastName, new customer last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * sets the email address
     * @param email, new customer email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

     /**
     * sets the phone number
     * @param phoneNum, new customer phone number
     */

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

     /**
     * sets the customer password
     * @param customerPass, new customer password
     */
    public void setCustomerPass(String customerPass) {
        this.customerPass = customerPass;
    }

    /**
     * gets the customerAccountID of a customer
     * @return customer account ID
     */
    public int getCustomerAccID() {
        return this.customerAccID;
    }

    /**
     * gets the first name of a customer
     * @return first name of the customer
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * gets the last name of a customer
     * @return last name of the customer
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * gets the email address of a customer
     * @return email address of the customer
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * gets the phone number of a customer
     * @return phone number of the customer
     */
    public String getPhoneNum() {
        return this.phoneNum;
    }

    /**
     * gets the customer password of a customer
     * @return customer password of the customer
     */
    public String getCustomerPass() {
        return this.customerPass;
    }

}
