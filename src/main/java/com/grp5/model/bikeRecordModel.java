package com.grp5.model;

import java.math.BigDecimal;

/**
 * Model for bike record in the Bike Rental System.
 * 
 * This model stores key information related to bike record,
 * including the branch id, bike availability, bike model
 * hourly rate, and daily rate. It provides getters and setters for 
 * all fields.
 * @author [group 5]
 * @version 1.0
 */
public class bikeRecordModel {

    //variable declaration
    private final int bikeID;
    private int branchIDNum;
    private boolean bikeAvailability = false;
    private String bikeModel;

    private BigDecimal hourlyRate;
    private BigDecimal dailyRate;

    // ============ Constructors ============
    /**
     * Partial constructor, with one parameter. 
     * Used for initialization of new
     * bikeRecordModel Object with bikeID
     * @param bikeID
     */
    public bikeRecordModel(int bikeID){
        this.bikeID=bikeID;
    }

    /**
     * Full constructor, used for initialization of new 
     * and complete bikeRecordModel
     * @param bikeID
     * @param branchIDNum
     * @param bikeAvailability
     * @param bikeModel
     * @param hourlyRate
     * @param dailyRate
     */
    public bikeRecordModel(int bikeID, int branchIDNum, boolean bikeAvailability,
            String bikeModel, BigDecimal hourlyRate, BigDecimal dailyRate) {
        this.bikeID = bikeID;
        this.branchIDNum = branchIDNum;
        this.bikeAvailability = bikeAvailability;
        this.bikeModel = bikeModel;
        this.hourlyRate = hourlyRate;
        this.dailyRate = dailyRate;
    }

    /**
     * Partial constructor, with one parameter. 
     * Used for initialization of new
     * bikeRecordModel Object with bikeID and bikeModel
     * 
     * @param bikeID
     * @param bikeModel
     */
    public bikeRecordModel(int bikeID, String bikeModel) {
        this.bikeID = bikeID;
        this.bikeModel = bikeModel;
    }

    // ============ Setters ============

    /**
     * sets the branch id number
     * @param branchIDNum
     */
    public void setBranchIDNum(int branchIDNum) {
        this.branchIDNum = branchIDNum;
    }

    /**
     * sets the bike availability
     * @param availability
     */
    public void setBikeAvailability(boolean availability) {
        this.bikeAvailability = availability;
    }

    /**
     * sets the bike availability to false
     */
    public void setbikeAvailabilityFalse() {
        this.bikeAvailability = false;
    }

    /**
     * sets the bike availability to true
     */
    public void setbikeAvailabilityTrue() {
        this.bikeAvailability = true;
    }

    /**
     * sets the hourly rate
     * @param hourlyRate
     */
    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    /**
     * sets the daily rate
     * @param dailyRate
     */
    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    // ============ Getters ============

    /**
     * gets the bike id
     * @return bikeID
     */
    public int getBikeID() {
        return this.bikeID;
    }

    /**
     * gets the branch ID number
     * @return branchIDNum
     */
    public int getBranchIDNum() {
        return this.branchIDNum;
    }

    /**
     * gets the bike availability
     * @return bikeAvailability
     */
    public boolean getBikeAvailability() {
        return this.bikeAvailability;
    }

    /**
     * gets the bike model
     * @return bikeModel
     */
    public String getBikeModel() {
        return this.bikeModel;
    }

    /**
     * gets the hourly Raye
     * @return hourlyRate
     */
    public BigDecimal getHourlyRate() {
        return this.hourlyRate;
    }

    /**
     * gets the daily Rate
     * @return dailyRate
     */
    public BigDecimal getDailyRate() {
        return this.dailyRate;
    }

}