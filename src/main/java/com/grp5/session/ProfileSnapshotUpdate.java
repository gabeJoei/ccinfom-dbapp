package com.grp5.session;

/*
 * A snapshot of updated profile information for a user session.
 * This immutable class is used to carry modified profile fields when updating
 * a user's stored session profile.
 */
public final class ProfileSnapshotUpdate {
    public String firstName;
    public String lastName;
    public String email;
}