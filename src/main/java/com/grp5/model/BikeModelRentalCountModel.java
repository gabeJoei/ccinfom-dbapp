/*
 * Model for bike model rental count.
 * 
 * This model stores key information related to bike model rental count,
 * including the bike model, rental count.
 * It provides getters and setters for all fields,
 * 
 */
package com.grp5.model;

public class BikeModelRentalCountModel {
    //variable declaration
    private final String bikeModel;
    private final int rentalCount;

    //constructor
    public BikeModelRentalCountModel(String model, int count) {
        this.bikeModel = model;
        this.rentalCount = count;
    }

    //getters
    public String getBikeModel() {
        return this.bikeModel;
    }
    public int getRentalCount() {
        return this.rentalCount;
    }

}
