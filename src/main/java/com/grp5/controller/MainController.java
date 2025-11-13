package com.grp5.controller;
import com.grp5.model.*;
import com.grp5.dao.*;
public class MainController {

    public static void main(String[] args){
        System.out.println("Hello WOrld");
        customerRecordDAO user=new customerRecordDAO();
        user.addCustomerRecordData(new customerRecordModel(1, "Gabe", "Bactong", "Bactong@gmail.com", "12345678", "GabbyCheeks"));
        user.addCustomerRecordData(new customerRecordModel(2, "Cath", "Gunita","Gunita@gmail.com", "12345342", "CathyCheeks"));
        
    }
}
