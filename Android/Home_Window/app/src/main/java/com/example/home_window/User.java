package com.example.home_window;

public class User {
    private String userId;
    private String name;
    private String phone;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String userId, String name, String phone) {
        this.userId = userId;
        this.name = name;
        this.phone = "+91" +phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = "+91" +phone;
    }
}
