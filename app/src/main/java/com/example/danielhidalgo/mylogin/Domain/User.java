package com.example.danielhidalgo.mylogin.Domain;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Daniel Hidalgo on 14/07/2017.
 */

@IgnoreExtraProperties
public class User {

    private String username;
    private String email;
    private String authId;

    public User(){
    }

    public User(String username, String email, String authId){
        this.username = username;
        this.email = email;
        this.authId = authId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }
}
