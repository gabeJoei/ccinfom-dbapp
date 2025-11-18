package com.grp5.model;

import java.time.LocalDate;

/*
 * Model for daily rent volume.
 * 
 * This model stores key information related to daily rent volume,
 * including the date, and rental count.
 * It provides getters and setters for all fields,
 * 
 * 
 */
public class DailyRentalVolumeModel {
    //variable declaration
    private final LocalDate date;
    private final int rentalCount;

    //constructor
    public DailyRentalVolumeModel(LocalDate date, int count) {
        this.date = date;
        this.rentalCount = count;
    }

    //getters
    public LocalDate getDate() {
        return this.date;
    }

    public int getRentalCount() {
        return this.rentalCount;
    }
}
