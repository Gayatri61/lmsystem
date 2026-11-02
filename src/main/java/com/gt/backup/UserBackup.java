package com.gt.backup;

import com.gt.enums.UserType;

/**
 * UserBackup
 */

public class UserBackup {

    private int id;
    private String name;
    private String email;
    private UserType userType;

    public UserBackup() {
    }

    public UserBackup(
            int id,
            String name,
            String email,
            UserType userType) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.userType = userType;
       
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserType getUserType() {
        return userType;
    }


}
