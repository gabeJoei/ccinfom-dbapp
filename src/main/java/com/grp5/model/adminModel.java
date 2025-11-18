package com.grp5.model;

/**
 * Model for admin in the Bike Rental System.
 * 
 * This model stores key information related to admin,
 * including the admin username, admin password, 
 * admin email address, admin first name, admin last name.  
 * It provides getters and setters for all fields,and a toString method.
 * @author [group 5]
 * @version 1.0
 */
public class adminModel {
    // variable declaration
    private int adminID;
    private String adminUsername;
    private String adminPassword;
    private String adminEmail;
    private String adminFirstName;
    private String adminLastName;

    /* Default constructor, empty arguments. 
     * Used for initialization of new and empty 
     * adminModel.
    */
    public adminModel() {
    }

    /**
     * Full constructor, used for initialization of new 
     * and complete admin Record
     * 
     * @param adminID
     * @param adminUsername
     * @param adminEmail
     * @param adminFirstName
     * @param adminLastName
     */
    public adminModel(int adminID, String adminUsername, String adminEmail,
            String adminFirstName, String adminLastName) {
        this.adminID = adminID;
        this.adminUsername = adminUsername;
        this.adminEmail = adminEmail;
        this.adminFirstName = adminFirstName;
        this.adminLastName = adminLastName;
    }

    /**
     * gets the admin id
     * @return adminID
     */
    public int getAdminID() {
        return adminID;
    }

    /**
     * sets the adminID
     * @param adminID
     */
    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    /**
     * gets the adminUsername
     * @return adminUserName
     */
    public String getAdminUsername() {
        return adminUsername;
    }

    /**
     * sets the admin Username
     * @param adminUsername
     */
    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    /**
     * gets the adminPassword
     * @return adminPassword
     */
    public String getAdminPassword() {
        return adminPassword;
    }

    /**
     * sets the adminPassword
     * @param adminPassword
     */
    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    /**
     * gets the adminEmail
     * @return adminEmail
     */
    public String getAdminEmail() {
        return adminEmail;
    }

    /**
     * sets the adminEmail
     * @param adminEmail
     */
    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    /**
     * gets the admin first name
     * @return adminFirstName
     */
    public String getAdminFirstName() {
        return adminFirstName;
    }

    /**
     * sets the adminFirstName
     * @param adminFirstName
     */
    public void setAdminFirstName(String adminFirstName) {
        this.adminFirstName = adminFirstName;
    }

    /**
     * gets the adminLastName
     * @return adminLastName
     */
    public String getAdminLastName() {
        return adminLastName;
    }

    /**
     * sets the admin last name
     * @param adminLastName
     */
    public void setAdminLastName(String adminLastName) {
        this.adminLastName = adminLastName;
    }

    /**
     * gets the admin's full name
     * @return adminFullName
     */
    public String getFullName() {
        return adminFirstName + " " + adminLastName;
    }

     /**
     * The string representation of the admin Model.
     * @return a string containing the adminID, adminUsername, fullName
     */
    @Override
    public String toString() {
        return String.format("admin{id=%d, username='%s', name='%s'}",
                adminID, adminUsername, getFullName());
    }
}