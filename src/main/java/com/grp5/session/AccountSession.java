package com.grp5.session;

/**
 * Manages the active user session for the application.
 * This class stores information about the currently logged-in account,
 * including account type, account ID, and a containing the user’s personal 
 * profile details.
 * */
public class AccountSession {

    public enum AccountType {
        ADMIN, USER
    };

    private static volatile AccountType accountType;
    private static volatile int accountID = -1;
    private static volatile ProfileSnapshot accountSnapshot;

    //constructor
    public AccountSession() {

    }

    //Initializes or updates the active session.
    public static void setAccount(AccountType type, int id, ProfileSnapshot snapshot) {
        accountType = type;
        accountID = id;
        accountSnapshot = snapshot;
    }

    //Clears all session data and logs out the current account.
    public static void cleanSession() {
        accountType = null;
        accountID = -1;
        accountSnapshot = null;
    }

    // Checks whether a valid account session is active.
    public static boolean isLoggedIn() {
        return accountType != null && accountID != -1;
    }

    //getters
    public static AccountType getAccountType() {
        return accountType;
    }

    public static int getAccountID() {
        return accountID;
    }

    public static ProfileSnapshot getSnapshot() {
        return accountSnapshot;
    }

    //Updates the stored profile snapshot for the current session.
    public static void updateSnapshot(ProfileSnapshot snapshot) {
        accountSnapshot = snapshot;
    }

    //Determines if the active session belongs to an administrator account.
    public static boolean isAdmin() {
        return accountType == AccountType.ADMIN;
    }

    //Determines if the active session belongs to a regular user account.
    public static boolean isUser() {
        return accountType == AccountType.USER;
    }

    //Returns the full name of the logged-in user using the profile snapshot.
    public static String getAccountFullName() {
        String fullName = accountSnapshot.getFirstName() + " " + accountSnapshot.getLastName().trim();
        return fullName;
    }
}
