package com.grp5.model;

import java.time.LocalDate;

public class DailyRentalVolumeModel {
    private final LocalDate date;
    private final int rentalCount;

    public DailyRentalVolumeModel(LocalDate date, int count) {
        this.date = date;
        this.rentalCount = count;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public int getRentalCount() {
        return this.rentalCount;
    }
}
