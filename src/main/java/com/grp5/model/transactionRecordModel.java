package com.grp5.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import com.grp5.utils.generalUtilities;

/**
 * Model for transaction record in the Bike Rental System.
 * 
 * This model stores key information related to payment transaction ,
 * including the customer account ID, branch ID, bike ID, paymenet date, 
 * payment amount. It provides getters and setters for all fields, and a toString method.
 * 
 * @author [Group 5] 
 * @version 1.0
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
    
    /* Default constructor, empty arguments. 
     * Used for initialization of new and empty 
     * transactionRecordModel Object
    */
    public transactionRecordModel() {}

     /** Full constructor, used for initialization of new 
     * and complete transaction record. 
     * @param customerAccountID, the unique identifier of a specific customer. 
     * @param reservationReferenceNumber, the unique identifier of a reservation made by the customer.
     * @param branchID, the unique identifier of the branch to which the transaction occurred.
     * @param bikeID, the unique identifier of the bike rented. 
     * @param startDate, the start date of the reservation
     * @param endDate, the end date of the reservation. 
     * @param hourlyRate, the hourly rate of a bike.
     * @param dailyRate, the daily rate of a bike. 
     * @param paymentDate, the date to which the transaction occurred. 
     * @param paymentAmount, the total bill of the customer.
     * -startDate, endDate, dailyRate, hourlyRate will be used to calculate the payement Amount
    */
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

    /** 
     * This method calculates paymentAmount using the {@code computePayment} found in {@code generalUtilities}.
     * It also, generates paymentReferenceNumber with a random 7 digit number using 
     * a method in {@code generalUtilities} called {@code generateRandIntNum}. It also checks for any 
     * duplicates using {@code primaryNumChecker}.
     * 
     * @param customerAccountID, the unique identifier of a specific customer. 
     * @param reservationReferenceNumber, the unique identifier of a reservation made by the customer.
     * @param branchID, the unique identifier of the branch to which the transaction occurred.
     * @param bikeID, the unique identifier of the bike rented. 
     * @param startDate, the start date of the reservation
     * @param endDate, the end date of the reservation. 
     * @param hourlyRate, the hourly rate of a bike.
     * @param dailyRate, the daily rate of a bike. 
     * @param paymentDate, the date to which the transaction occurred. 
     * @param paymentAmount, the total bill of the customer.
     * @return transactionRecordModel object with calculated payment.
    */
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

    /**
     * gets the unique paymentReferenceNumber
     * @return payment reference number
     */
    public int getPaymentReferenceNumber() { return paymentReferenceNumber; }

    /**
     * gets the customerAccountID of a customer
     * @return customer account ID
     */
    public int getCustomerAccountID() { return customerAccountID; }

    /**
     * gets the reservation referenece number associated with the payment transaction
     * @return reservation reference number
     */
    public int getReservationReferenceNumber() { return reservationReferenceNumber; }

     /**
     * gets the branch id to which the transaction took place
     * @return branch Id
     */
    public int getBranchID() { return branchID; }
    
     /**
     * gets the bike Id of the bike rented
     * @return branch Id
     */
    public int getBikeID() { return bikeID; }
    
     /**
     * gets the payment amount of the specific payment transaction
     * @return payment amount
     */
    public BigDecimal getPaymentAmount() { return paymentAmount; }
    
     /**
     * gets the hourly rate of the rented bike
     * @return hourly rate
     */
    public BigDecimal getHourlyRate() { return hourlyRate; }

     /**
     * gets the daily rate of the rented bike
     * @return daily rate
     */
    public BigDecimal getDailyRate() { return dailyRate; }

     /**
     * gets the payment date to which the date the transaction took place
     * @return payment date
     */
    public Timestamp getPaymentDate() { return paymentDate; }

     /**
     * gets the start date of a specific reservation associated with a transcation. 
     * @return start date
     */
    public Timestamp getStartDate() { return startDate; }

     /**
     * gets the end date of a specific reservation associated with a transcation. 
     * @return end date
     */
    public Timestamp getEndDate() { return endDate; }

    /**
     * sets the payment reference number.
     * @param paymentReferenceNum, new payment reference number
     */
    public void setPaymentReferenceNumber(int paymentReferenceNum) {
        paymentReferenceNumber = paymentReferenceNum;
    }

     /**
     * sets the customer account id.
     * @param customerAcccID, new customer account id
     */
    public void setCustomerAccountID(int customerAccID) {
        customerAccountID = customerAccID;
    }

     /**
     * sets the reservation reference number
     * @param reserveNum, new reservation reference number
     */
    public void setReservationReferenceNum(int reserveNum) {
        reservationReferenceNumber = reserveNum;
    }

     /**
     * sets the branch id.
     * @param branchId, new branch id
     */
    public void setBranchID(int branchId) {
        branchID = branchId;
    }

     /**
     * sets the bike id.
     * @param bikeId, new bike Id
     */
    public void setBikeID(int bikeId) {
        bikeID = bikeId;
    }

    /**
     * sets the payment date associated with a transcation
     * @param date, the new payment date associated with the transaction
     */
    public void setPaymentDate(Timestamp date) {
        paymentDate = date;
    }

    /**
     * sets the start date associated with a reservation of a specific bike 
     * @param date, the new start date associated with a reservation
     */
    public void setStartDate(Timestamp date) {
        startDate = date;
    }

     /**
     * sets the end date associated with a reservation of a specific bike 
     * @param date, the new end date associated with a reservation
     */
    public void setEndDate(Timestamp date) {
        endDate = date;
    }

     /**
     * sets the payment amount associated with a transaction.
     * @param payment, the new payment amount
     */
    public void setPaymentAmount(BigDecimal payment) {
        paymentAmount = payment;
    }

     /**
     * sets the hourly rate associated with a specific bike 
     * @param rate, the new hourly rate
     */
    public void setHourlyRate(BigDecimal rate) {
        hourlyRate = rate;
    }

    /**
     * sets the daily rate associated with a specific bike 
     * @param rate, the new daily rate
     */
    public void setDailyRate(BigDecimal rate) {
        dailyRate = rate;
    }

    /**
     * Computes the payment amount of a bike reservation. It uses the start date and end date of the reservation
     * finding it difference in terms of day and time. Then calculates the sum of the hourly and daily rate,
     * it sum becomes the payment amount. 
     * 
     * @param startDate, the start date of the bike reservation 
     * @param endDate, the end date of the bike reservation
     * @param hourlyRate, the hourly rate of the reserved bike
     * @param dailyRate, the daily rate of the reserved bike
     * @return the calculated payment amount. 
     */
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

    /**
     * The string representation of the transaction record model.
     * @return a string containing the paymenet reference number, customer id,
     * reservation reference number, branch id, bike id, payment date, and payment amount. 
     */
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