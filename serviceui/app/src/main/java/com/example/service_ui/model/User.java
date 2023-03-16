package com.example.service_ui.model;

public class User {

    private String userId;
    private String name;
    private String externalIdentifier;
    private String userType;
    private String userName;
    private String password;

    public User(String userId, String name, String externalIdentifier, String userType, String userName, String password) {
        this.userId = userId;
        this.name = name;
        this.externalIdentifier = externalIdentifier;
        this.userType = userType;
        this.userName = userName;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getExternalIdentifier() {
        return externalIdentifier;
    }

    public String getUserType() {
        return userType;
    }
}
