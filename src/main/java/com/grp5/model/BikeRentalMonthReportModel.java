package com.grp5.model;

import java.util.List;

/*
 * Model for bike rental month report
 * 
 * This model stores key information related to monthly bike rental report,
 * including the year, month, daily volumes, most rented model. 
 * It provides getters and setters for all fields.
 * 
 */
public class BikeRentalMonthReportModel {
    //variable declaration
    private final int year;
    private final int month;
    private final List<DailyRentalVolumeModel> dailyVolumes;
    private final BikeModelRentalCountModel mostRentedModel;

    //constructor
    public BikeRentalMonthReportModel(int year, int month, List<DailyRentalVolumeModel> dailyVolumes,
            BikeModelRentalCountModel mostRentedModel) {
        this.year = year;
        this.month = month;
        this.dailyVolumes = dailyVolumes;
        this.mostRentedModel = mostRentedModel;
    }

    //getters
    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public List<DailyRentalVolumeModel> getDailyVolumes() {
        return this.dailyVolumes;
    }

    public BikeModelRentalCountModel getMostRentedBike() {
        return this.mostRentedModel;
    }
    
}
