package com.example.hanoconnectapp.models;

public class LoginResponse {
    private int userId;
    private String email;
    private String fullName;
    private String role;

    public int getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
}