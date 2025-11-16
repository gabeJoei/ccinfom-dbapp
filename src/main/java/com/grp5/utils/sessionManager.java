package com.grp5.utils;

import com.grp5.model.adminModel;
import com.grp5.model.customerRecordModel;

/**
 * A simple utility class to manage the logged-in user/admin session data.
 */
public class sessionManager {
    private static customerRecordModel loggedInCustomer = null;
    private static adminModel loggedInAdmin = null;

    public static void setLoggedInCustomer(customerRecordModel customer) {
        loggedInCustomer = customer;
        loggedInAdmin = null;
    }

    public static customerRecordModel getLoggedInCustomer() {
        return loggedInCustomer;
    }

    public static void setLoggedInAdmin(adminModel admin) {
        loggedInAdmin = admin;
        loggedInCustomer = null;
    }

    public static adminModel getLoggedInAdmin() {
        return loggedInAdmin;
    }

    public static boolean isAdminLoggedIn() {
        return loggedInAdmin != null;
    }
    
    public static boolean isCustomerLoggedIn() {
        return loggedInCustomer != null;
    }

    public static void clearSession() {
        loggedInCustomer = null;
        loggedInAdmin = null;
    }
}