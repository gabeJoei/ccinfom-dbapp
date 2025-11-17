package com.grp5.model;

public class BikeModelRentalCountModel {
    private final String bikeModel;
    private final int rentalCount;

    public BikeModelRentalCountModel(String model, int count) {
        this.bikeModel = model;
        this.rentalCount = count;
    }

    public String getBikeModel() {
        return this.bikeModel;
    }

    public int rentalCount() {
        return this.rentalCount;
    }
}
