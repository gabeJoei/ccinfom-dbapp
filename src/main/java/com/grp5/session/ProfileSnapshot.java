package com.grp5.session;

/**
 * An immutable snapshot of a user's profile information.
 * This class represents the stored profile details for a user during an
 * active session. It is immutable, ensuring that profile data cannot be
 * modified once the snapshot is created. Any updates should be performed
 * using a separate update object and applied through the session management 
 * logic.
 * 
 */
public class ProfileSnapshot {
    private final String firstName;
    private final String lastName;
    private final String email;

    //constructor
    public ProfileSnapshot(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    //getters
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
