package com.grp5.session;

import com.grp5.model.adminModel;
import com.grp5.model.customerRecordModel;

public class User_session {
    private static customerRecordModel currentUser;

    private User_session() {

    }

    public static void setcurrentUser(customerRecordModel user) {
        currentUser = user;
    }

    public static customerRecordModel getcurrentUser() {
        return currentUser;
    }

    public static Integer getcurrentUserId() {
        return currentUser == null ? null : currentUser.getCustomerAccID();
    }

    public static void clearcurrentUser() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
