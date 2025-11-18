package com.grp5.model;

import java.math.BigDecimal;

/*
 * Model for bike record in the Bike Rental System.
 * 
 * This model stores key information related to bike record,
 * including the branch id, bike availability, bike model
 * hourly rate, and daily rate. It provides getters and setters for 
 * all fields.
 * 
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
    public bikeRecordModel(int bikeID){
        this.bikeID=bikeID;
    }


    public bikeRecordModel(int bikeID, int branchIDNum, boolean bikeAvailability,
            String bikeModel, BigDecimal hourlyRate, BigDecimal dailyRate) {
        this.bikeID = bikeID;
        this.branchIDNum = branchIDNum;
        this.bikeAvailability = bikeAvailability;
        this.bikeModel = bikeModel;
        this.hourlyRate = hourlyRate;
        this.dailyRate = dailyRate;
    }

    public bikeRecordModel(int bikeID, String bikeModel) {
        this.bikeID = bikeID;
        this.bikeModel = bikeModel;
    }

    // ============ Setters ============
    public void setBranchIDNum(int branchIDNum) {
        this.branchIDNum = branchIDNum;
    }

    public void setBikeAvailability(boolean availability) {
        this.bikeAvailability = availability;
    }

    public void setbikeAvailabilityFalse() {
        this.bikeAvailability = false;
    }

    public void setbikeAvailabilityTrue() {
        this.bikeAvailability = true;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    // ============ Getters ============
    public int getBikeID() {
        return this.bikeID;
    }

    public int getBranchIDNum() {
        return this.branchIDNum;
    }

    public boolean getBikeAvailability() {
        return this.bikeAvailability;
    }

    public String getBikeModel() {
        return this.bikeModel;
    }

    public BigDecimal getHourlyRate() {
        return this.hourlyRate;
    }

    public BigDecimal getDailyRate() {
        return this.dailyRate;
    }

}