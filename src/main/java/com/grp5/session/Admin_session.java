package com.grp5.session;

import com.grp5.model.adminModel;

public class Admin_session {
    private static adminModel currentAdmin;

    private Admin_session() {
    }

    public static void setCurrentAdmin(adminModel admin) {
        currentAdmin = admin;
    }

    public static adminModel getCurrentAdmin() {
        return currentAdmin;
    }

    public static Integer getCurrentAdminId() {
        return currentAdmin == null ? null : currentAdmin.getAdminID();
    }

    public static void clearCurrentAdmin() {
        currentAdmin = null;
    }

    public static boolean isLoggedIn() {
        return currentAdmin != null;
    }
}
