package com.grp5.model;

import java.time.LocalDate;

/**
 * Model for daily rent volume.
 * 
 * This model stores key information related to daily rent volume,
 * including the date, and rental count.
 * It provides getters and setters for all fields,
 * @author [group 5]
 * @version 1.0
 */
public class DailyRentalVolumeModel {
    /*Variable Declaration */
    private final LocalDate date;
    private final int rentalCount;

    /**
     * The full constructor of DailyRentVolumeModel used for 
     * initialization of new and complete DailyRentVolumeModel
     *
     * @param date The specific date for which the rental volume is being recorded.
     * @param count The total number of rentals recorded on the specified date.
     */
    public DailyRentalVolumeModel(LocalDate date, int count) {
        this.date = date;
        this.rentalCount = count;
    }

    /**
     * gets the specific date used to record the rental volume
     * @return the specific date
     */
    public LocalDate getDate() {
        return this.date;
    }

     /**
     * gets the rental count associated with the specific date
     * @return the rental count.
     */
    public int getRentalCount() {
        return this.rentalCount;
    }
}
