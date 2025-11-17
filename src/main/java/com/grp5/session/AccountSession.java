package com.grp5.session;

import com.grp5.model.adminModel;
import com.grp5.model.customerRecordModel;

public class AccountSession {

    public enum AccountType {
        ADMIN, USER
    };

    private static volatile AccountType accountType;
    private static volatile int accountID;
    private static volatile ProfileSnapshot accountSnapshot;

    public AccountSession() {

    }

    public void setAccount(AccountType type, int id, ProfileSnapshot snapshot) {
        accountType = type;
        accountID = id;
        accountSnapshot = snapshot;
    }

    public void cleanSession() {
        accountType = null;
        accountID = -1;
        accountSnapshot = null;
    }

    public boolean isLoggedIn() {
        return accountType != null && accountID != -1;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public int getAccountID() {
        return accountID;
    }

    public ProfileSnapshot getSnapshot() {
        return accountSnapshot;
    }

    public boolean isAdmin() {
        return accountType == AccountType.ADMIN;
    }

    public boolean isUser() {
        return accountType == AccountType.USER;
    }
}
