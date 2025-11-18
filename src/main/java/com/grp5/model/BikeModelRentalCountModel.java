package com.grp5.model;

/**
 * Model for bike model rental count.
 * 
 * This model stores key information related to bike model rental count,
 * including the bike model, rental count.
 * It provides getters and setters for all fields,
 * @author [group 5]
 * @version 1.0
 */
public class BikeModelRentalCountModel {
    //variable declaration
    private final String bikeModel;
    private final int rentalCount;

    /**
     * Full constructor, used for initialization of new 
     * and complete BikeModelRentalCountModel
     * @param model
     * @param count
     */
    public BikeModelRentalCountModel(String model, int count) {
        this.bikeModel = model;
        this.rentalCount = count;
    }

    /**
     * gets the bike Model
     * @return bikeModel
     */
    public String getBikeModel() {
        return this.bikeModel;
    }

    /**
     * gets the rental count
     * @return rentalCount
     */
    public int getRentalCount() {
        return this.rentalCount;
    }

}
