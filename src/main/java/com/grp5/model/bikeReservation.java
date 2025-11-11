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
    private String dateReturned; // ENUM: 'inUse' or 'returned
    private String status; 
    
    // Empty constructor
    public bikeReservation() {}
    
    // Full constructor
    public bikeReservation(int reservationReferenceNum, int customerAccID, 
                          int bikeID, int branchID, Timestamp reservationDate, 
                          Timestamp startDate, Timestamp endDate, String dateReturned) {
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
    public String getDateReturned() { return dateReturned; }
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
    public void setDateReturned(String dateReturned) {
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