package com.lambda.watermyplants.models;

/**
 * A model used to create a new user. The minimum information needed to create a user.
 * Note the role will default to USER.
 */
public class UserMinimum {
    private String username;
    private String password;
    private String phonenumber;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
