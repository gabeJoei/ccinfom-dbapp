/*
 * Model for bike reservation record in the Bike Rental System.
 * 
 * This model stores key information related to bike reservation ,
 * including the customer account ID, branch ID, bike ID, reservation date, 
 * start date, end date, date returned, and status. It provides getters and
 * setters for all fields, and a toString method.
 * 
 */
package com.grp5.model;

import java.sql.Timestamp;

public class bikeReservation {
    private int reservationReferenceNum;
    private int customerAccID;
    private int bikeID;
    private int branchID;
    private Timestamp reservationDate;
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp dateReturned;
    private String status; 
    
    // Empty constructor
    public bikeReservation() {}
    
    // Full constructor
    public bikeReservation(int reservationReferenceNum, int customerAccID, 
                          int bikeID, int branchID, Timestamp reservationDate, 
                          Timestamp startDate, Timestamp endDate, Timestamp dateReturned) {
        this.reservationReferenceNum = reservationReferenceNum;
        this.customerAccID = customerAccID;
        this.bikeID = bikeID;
        this.branchID = branchID;
        this.reservationDate = reservationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateReturned = dateReturned;
  }
    
    // Getters
    public int getReservationReferenceNum() { return reservationReferenceNum; }
    public int getCustomerAccID() { return customerAccID; }
    public int getBikeID() { return bikeID; }
    public int getBranchID() { return branchID; }
    public Timestamp getReservationDate() { return reservationDate; }
    public Timestamp getStartDate() { return startDate; }
    public Timestamp getEndDate() { return endDate; }
    public Timestamp getDateReturned() { return dateReturned; }
    public String getStatus(){return status;}
    
    // Setters
    public void setReservationReferenceNum(int reservationReferenceNum) {
        this.reservationReferenceNum = reservationReferenceNum;
    }
    public void setCustomerAccID(int customerAccID) {
        this.customerAccID = customerAccID;
    }
    public void setBikeID(int bikeID) {
        this.bikeID = bikeID;
    }
    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }
    public void setReservationDate(Timestamp reservationDate) {
        this.reservationDate = reservationDate;
    }
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
    public void setDateReturned(Timestamp dateReturned) {
        this.dateReturned = dateReturned;
    }
    public void setStatus(String status){
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "BikeReservation{" +
                "reservationReferenceNum=" + reservationReferenceNum +
                ", customerAccID=" + customerAccID +
                ", bikeID=" + bikeID +
                ", branchID=" + branchID +
                ", reservationDate=" + reservationDate +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", dateReturned='" + dateReturned + '\'' +
                '}';
    }
}




