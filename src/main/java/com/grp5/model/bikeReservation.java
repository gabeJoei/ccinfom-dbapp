package com.grp5.model;

import java.sql.Timestamp;

/**
 * Model for bike reservation record in the Bike Rental System.
 * 
 * This model stores key information related to bike reservation ,
 * including the customer account ID, branch ID, bike ID, reservation date, 
 * start date, end date, date returned, and status. It provides getters and
 * setters for all fields, and a toString method.
 * @author [group 5]
 * @version 1.0
 */
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
    
    /**
     * Default constructor, empty arguments. 
     * Used for initialization of new and empty 
     * bikeReservation Object
     * 
     */
    public bikeReservation() {}
    
    /**
     * Full constructor, used for initialization of new 
     * and complete bike Reservation record. 
     * @param reservationReferenceNum
     * @param customerAccID
     * @param bikeID
     * @param branchID
     * @param reservationDate
     * @param startDate
     * @param endDate
     * @param dateReturned
     */
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
    
    /**
     * gets the reservation reference number
     * @return reservationReferenceNum 
     */
    public int getReservationReferenceNum() { return reservationReferenceNum; }

     /**
     * gets the customer account id
     * @return customerAccID 
     */
    public int getCustomerAccID() { return customerAccID; }

    /**
     * gets the bike ID
     * @return bikeID
     */
    public int getBikeID() { return bikeID; }

     /**
     * gets the branch ID
     * @return branchID
     */
    public int getBranchID() { return branchID; }

     /**
     * gets the reservation date
     * @return reservationDate
     */
    public Timestamp getReservationDate() { return reservationDate; }
    
     /**
     * gets the start date
     * @return startDate
     */
    public Timestamp getStartDate() { return startDate; }

     /**
     * gets the end date
     * @return endDate
     */
    public Timestamp getEndDate() { return endDate; }

     /**
     * gets the date Returned
     * @return dateReturned
     */
    public Timestamp getDateReturned() { return dateReturned; }
    
    /**
     * gets the status
     * @return status
     */
    public String getStatus(){return status;}
    
    /**
     * sets the reservation reference number
     * @param reservationReferenceNum
     */
    public void setReservationReferenceNum(int reservationReferenceNum) {
        this.reservationReferenceNum = reservationReferenceNum;
    }

    /**
     * sets the customer account id
     * @param customerAccID
     */
    public void setCustomerAccID(int customerAccID) {
        this.customerAccID = customerAccID;
    }

    /**
     * sets the bike id
     * @param bikeID
     */
    public void setBikeID(int bikeID) {
        this.bikeID = bikeID;
    }

    /**
     * sets the branch id
     * @param branchID
     */
    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    /**
     * sets the reservation date
     * @param reservationDate
     */
    public void setReservationDate(Timestamp reservationDate) {
        this.reservationDate = reservationDate;
    }

    /**
     * sets the start date
     * @param startDate
     */
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    /**
     * sets the end date
     * @param endDate
     */
    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    /**
     * sets the date returned
     * @param dateReturned
     */
    public void setDateReturned(Timestamp dateReturned) {
        this.dateReturned = dateReturned;
    }

    /**
     * sets the status
     * @param status
     */
    public void setStatus(String status){
        this.status = status;
    }
    
    /**
    * The string representation of the branch record model.
    * @return a string containing the reservationReferenceNum,
    * customerAccID, bikeID, branchID, reservationDate, startDate, 
    * endDate, dateReturned.
     */
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




