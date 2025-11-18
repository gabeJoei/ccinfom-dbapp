package com.grp5.model;

/*
 * Model for admin in the Bike Rental System.
 * 
 * This model stores key information related to admin,
 * including the admin username, admin password, 
 * admin email address, admin first name, admin last name.  
 * It provides getters and setters for all fields,and a toString method.
 * 
 */
public class adminModel {
    // variable declaration
    private int adminID;
    private String adminUsername;
    private String adminPassword;
    private String adminEmail;
    private String adminFirstName;
    private String adminLastName;

    // Constructors
    public adminModel() {
    }

    public adminModel(int adminID, String adminUsername, String adminEmail,
            String adminFirstName, String adminLastName) {
        this.adminID = adminID;
        this.adminUsername = adminUsername;
        this.adminEmail = adminEmail;
        this.adminFirstName = adminFirstName;
        this.adminLastName = adminLastName;
    }

    // Getters and Setters
    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminFirstName() {
        return adminFirstName;
    }

    public void setAdminFirstName(String adminFirstName) {
        this.adminFirstName = adminFirstName;
    }

    public String getAdminLastName() {
        return adminLastName;
    }

    public void setAdminLastName(String adminLastName) {
        this.adminLastName = adminLastName;
    }

    public String getFullName() {
        return adminFirstName + " " + adminLastName;
    }

    @Override
    public String toString() {
        return String.format("admin{id=%d, username='%s', name='%s'}",
                adminID, adminUsername, getFullName());
    }
}