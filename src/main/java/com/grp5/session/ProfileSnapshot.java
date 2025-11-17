package com.grp5.session;

public class ProfileSnapshot {
    private final String firstName;
    private final String lastName;
    private final String email;

    public ProfileSnapshot(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }
}
