package com.grp5.session;

public class AccountSession {

    public enum AccountType {
        ADMIN, USER
    };

    private static volatile AccountType accountType;
    private static volatile int accountID = -1;
    private static volatile ProfileSnapshot accountSnapshot;

    public AccountSession() {

    }

    public static void setAccount(AccountType type, int id, ProfileSnapshot snapshot) {
        accountType = type;
        accountID = id;
        accountSnapshot = snapshot;
    }

    public static void cleanSession() {
        accountType = null;
        accountID = -1;
        accountSnapshot = null;
    }

    public static boolean isLoggedIn() {
        return accountType != null && accountID != -1;
    }

    public static AccountType getAccountType() {
        return accountType;
    }

    public static int getAccountID() {
        return accountID;
    }

    public static ProfileSnapshot getSnapshot() {
        return accountSnapshot;
    }

    public static void updateSnapshot(ProfileSnapshot snapshot) {
        accountSnapshot = snapshot;
    }

    public static boolean isAdmin() {
        return accountType == AccountType.ADMIN;
    }

    public static boolean isUser() {
        return accountType == AccountType.USER;
    }

    public static String getAccountFullName() {
        String fullName = accountSnapshot.getFirstName() + " " + accountSnapshot.getLastName().trim();
        return fullName;
    }
}
