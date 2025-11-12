package com.grp5.model;

import java.math.BigDecimal;
//package
import java.sql.*;
import java.time.*;

import com.grp5.utils.generalUtilities;

public class transactionRecordModel {

private int paymentReferenceNumber;
private int customerAccountID;
private int reservationReferenceNumber;
private int branchID;
private int bikeID;
private Timestamp paymentDate;//i made it string muna
private Timestamp startDate;//will be use for calculation of payement amount
private Timestamp endDate;//will be use for calculation of payment amount
private BigDecimal paymentAmount;
private BigDecimal hourlyRate;
private BigDecimal dailyRate;
    
    public transactionRecordModel(){}

    public transactionRecordModel(int paymentReferenceNumber,int customerAccountID,
    int reservationReferenceNumber, int branchID, int bikeID,Timestamp startDate, 
    Timestamp endDate,Timestamp paymentDate, BigDecimal hourlyRate, BigDecimal dailyRate,BigDecimal paymentAmount){
        this.paymentReferenceNumber=paymentReferenceNumber;
        this.customerAccountID=customerAccountID;
        this.reservationReferenceNumber=reservationReferenceNumber;
        this.branchID=branchID;
        this.bikeID=bikeID;
        this.startDate=startDate;
        this.endDate=endDate;
        this.hourlyRate=hourlyRate;
        this.dailyRate=dailyRate;
        this.paymentDate=paymentDate;
        this.paymentAmount=paymentAmount;
    }

    public transactionRecordModel create(int customerAccountID,
    int reservationReferenceNumber, int branchID,int bikeID, Timestamp startDate, 
    Timestamp endDate,Timestamp paymentDate, BigDecimal hourlyRate, BigDecimal dailyRate){
    paymentReferenceNumber=generalUtilities.primaryNumChecker(
        generalUtilities.generateRandIntNum(),
        "paymentReferenceNum","payment");
    paymentAmount=computePayement(startDate, endDate, hourlyRate, dailyRate);

    return new transactionRecordModel(paymentReferenceNumber,customerAccountID,
    reservationReferenceNumber, branchID,bikeID,startDate, 
    endDate,paymentDate, hourlyRate, dailyRate,paymentAmount);

}

public int getPaymentReferenceNumber(){return paymentReferenceNumber;}
public int getCustomerAccountID(){return customerAccountID;}
public int getReservationReferenceNumber(){return reservationReferenceNumber;}
public int getBrachID(){return branchID;}
public int getBikeID(){return bikeID;}
public BigDecimal getPaymentAmount(){return paymentAmount;}
public BigDecimal getHourlyRate(){return hourlyRate;}
public BigDecimal getDailyRate(){return dailyRate;}
public Timestamp getpaymentDate(){return paymentDate;}
public Timestamp getStartDate(){return startDate;}
public Timestamp getEndDate(){return endDate;}

public void setPaymentReferenceNumber(int paymentReferenceNum){
    paymentReferenceNumber=paymentReferenceNum;
}

public void setCustomerAccountID(int customerAccID){
    customerAccountID=customerAccID;
}
public void setReservationReferenceNum(int reserveNum){
    reservationReferenceNumber=reserveNum;
}
public void setBranchID(int branchId){
    branchID=branchId;
}
public void setBikeID(int bikeId){
    bikeID=bikeId;
}
public void setPaymentDate(Timestamp date){
    paymentDate=date;
}
public void setStartDate(Timestamp date){
    startDate=date;
}
public void setEnd(Timestamp date){
    endDate=date;
}
public void setPaymentAmount(BigDecimal payment){
    paymentAmount=payment;
}

private BigDecimal computePayement(Timestamp startDate, Timestamp endDate, BigDecimal hourlyRate, BigDecimal dailyRate){

    BigDecimal payment;
Duration duration=Duration.between(startDate.toInstant(), endDate.toInstant());

if(duration.toHours()>24){
    long days=duration.toDays();
    payment=dailyRate.multiply(BigDecimal.valueOf(days));
}else{
    long mins=duration.toMinutes();
    mins/=30;
    if(mins%30!=0){
        mins++;
    }

    
    BigDecimal halfRate=hourlyRate.divide(BigDecimal.valueOf(2));
    payment=halfRate.multiply(BigDecimal.valueOf(mins));
}
return payment;
}
    public String toString(){
        return  "paymentReferenceNumber="+ paymentReferenceNumber
                +",customerID="+customerAccountID
                +",reservationReferenceNumber="+reservationReferenceNumber
                +",branchID="+branchID
                +",bikeID="+bikeID
                +",paymentDate="+paymentDate
                +",paymentAmount="+paymentAmount
                +"\n";
        
    }
}

