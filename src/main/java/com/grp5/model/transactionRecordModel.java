package com.grp5.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import com.grp5.utils.generalUtilities;

/*
 * Model for transaction record in the Bike Rental System.
 * 
 * This model stores key information related to payment transaction ,
 * including the customer account ID, branch ID, bike ID, paymenet date, 
 * payment amount. It provides getters and setters for all fields, and a toString method.
 * 
 */
public class transactionRecordModel {

    /* Variable Declarations */
    private int paymentReferenceNumber;
    private int customerAccountID;
    private int reservationReferenceNumber;
    private int branchID;
    private int bikeID;
    private Timestamp paymentDate;
    private Timestamp startDate;
    private Timestamp endDate;
    private BigDecimal paymentAmount;
    private BigDecimal hourlyRate;
    private BigDecimal dailyRate;
    
    // Empty constructor
    public transactionRecordModel() {}

    // Full constructor
    public transactionRecordModel(int customerAccountID,
        int reservationReferenceNumber, int branchID, int bikeID, Timestamp startDate, 
        Timestamp endDate, Timestamp paymentDate, BigDecimal hourlyRate, BigDecimal dailyRate, BigDecimal paymentAmount) {
        this.customerAccountID = customerAccountID;
        this.reservationReferenceNumber = reservationReferenceNumber;
        this.branchID = branchID;
        this.bikeID = bikeID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hourlyRate = hourlyRate;
        this.dailyRate = dailyRate;
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
    }

    public transactionRecordModel create(int customerAccountID,
        int reservationReferenceNumber, int branchID, int bikeID, Timestamp startDate, 
        Timestamp endDate, Timestamp paymentDate, BigDecimal hourlyRate, BigDecimal dailyRate) {
        paymentReferenceNumber = generalUtilities.primaryNumChecker(
            generalUtilities.generateRandIntNum(),
            "paymentReferenceNum", "payment");
        paymentAmount = computePayment(startDate, endDate, hourlyRate, dailyRate);

        return new transactionRecordModel(customerAccountID,
            reservationReferenceNumber, branchID, bikeID, startDate, 
            endDate, paymentDate, hourlyRate, dailyRate, paymentAmount);
    }

    // Getters
    public int getPaymentReferenceNumber() { return paymentReferenceNumber; }
    public int getCustomerAccountID() { return customerAccountID; }
    public int getReservationReferenceNumber() { return reservationReferenceNumber; }
    public int getBranchID() { return branchID; }
    public int getBikeID() { return bikeID; }
    public BigDecimal getPaymentAmount() { return paymentAmount; }
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public BigDecimal getDailyRate() { return dailyRate; }
    public Timestamp getPaymentDate() { return paymentDate; }
    public Timestamp getStartDate() { return startDate; }
    public Timestamp getEndDate() { return endDate; }

    // Setters
    public void setPaymentReferenceNumber(int paymentReferenceNum) {
        paymentReferenceNumber = paymentReferenceNum;
    }

    public void setCustomerAccountID(int customerAccID) {
        customerAccountID = customerAccID;
    }

    public void setReservationReferenceNum(int reserveNum) {
        reservationReferenceNumber = reserveNum;
    }

    public void setBranchID(int branchId) {
        branchID = branchId;
    }

    public void setBikeID(int bikeId) {
        bikeID = bikeId;
    }

    public void setPaymentDate(Timestamp date) {
        paymentDate = date;
    }

    public void setStartDate(Timestamp date) {
        startDate = date;
    }

    public void setEndDate(Timestamp date) {
        endDate = date;
    }

    public void setPaymentAmount(BigDecimal payment) {
        paymentAmount = payment;
    }

    public void setHourlyRate(BigDecimal rate) {
        hourlyRate = rate;
    }

    public void setDailyRate(BigDecimal rate) {
        dailyRate = rate;
    }

    // A method used to calculate the payment of the customer
    private BigDecimal computePayment(Timestamp startDate, Timestamp endDate, BigDecimal hourlyRate, BigDecimal dailyRate) {
        BigDecimal payment;
        Duration duration = Duration.between(startDate.toInstant(), endDate.toInstant());

        if (duration.toHours() > 24) {
            long days = duration.toDays();
            payment = dailyRate.multiply(BigDecimal.valueOf(days));
        } else {
            long mins = duration.toMinutes();
            mins /= 30;
            if (mins % 30 != 0) {
                mins++;
            }

            BigDecimal halfRate = hourlyRate.divide(BigDecimal.valueOf(2));
            payment = halfRate.multiply(BigDecimal.valueOf(mins));
        }
        return payment;
    }

    @Override
    public String toString() {
        return "paymentReferenceNumber=" + paymentReferenceNumber
                + ", customerID=" + customerAccountID
                + ", reservationReferenceNumber=" + reservationReferenceNumber
                + ", branchID=" + branchID
                + ", bikeID=" + bikeID
                + ", paymentDate=" + paymentDate
                + ", paymentAmount=" + paymentAmount
                + "\n";
    }
}