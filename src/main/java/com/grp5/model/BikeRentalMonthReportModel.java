package com.grp5.model;

import java.util.List;

/**
 * Model for bike rental month report
 * 
 * This model stores key information related to monthly bike rental report,
 * including the year, month, daily volumes, most rented model. 
 * It provides getters and setters for all fields.
 * @author [group 5]
 * @version 1.0
 */
public class BikeRentalMonthReportModel {
    //variable declaration
    private final int year;
    private final int month;
    private final List<DailyRentalVolumeModel> dailyVolumes;
    private final BikeModelRentalCountModel mostRentedModel;

    /**
     * Full constructor, used for initialization of new 
     * and complete BikeRentalMonthReportModel. 
     * @param year
     * @param month
     * @param dailyVolumes
     * @param mostRentedModel
     */
    public BikeRentalMonthReportModel(int year, int month, List<DailyRentalVolumeModel> dailyVolumes,
            BikeModelRentalCountModel mostRentedModel) {
        this.year = year;
        this.month = month;
        this.dailyVolumes = dailyVolumes;
        this.mostRentedModel = mostRentedModel;
    }

    /**
     * gets the year
     * @return year
     */
    public int getYear() {
        return this.year;
    }

    /**
     * gets the month
     * @return month
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * gets the daily volumes 
     * @return dailyVolumes
     */
    public List<DailyRentalVolumeModel> getDailyVolumes() {
        return this.dailyVolumes;
    }

    /**
     * gets the most rented bike
     * @return most rented model
     */
    public BikeModelRentalCountModel getMostRentedBike() {
        return this.mostRentedModel;
    }
    
}
