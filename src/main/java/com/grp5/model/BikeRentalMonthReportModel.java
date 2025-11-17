package com.grp5.model;

import java.util.List;

public class BikeRentalMonthReportModel {
    private final int year;
    private final int month;
    private final List<DailyRentalVolumeModel> dailyVolumes;
    private final BikeModelRentalCountModel mostRentedModel;

    public BikeRentalMonthReportModel(int year, int month, List<DailyRentalVolumeModel> dailyVolumes,
            BikeModelRentalCountModel mostRentedModel) {
        this.year = year;
        this.month = month;
        this.dailyVolumes = dailyVolumes;
        this.mostRentedModel = mostRentedModel;
    }

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
