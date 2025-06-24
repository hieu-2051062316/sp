package com.example.hanoconnectadmin.models;

import com.google.gson.annotations.SerializedName;

// Dùng để gửi dữ liệu đăng ký lên server
public class RegisterRequest {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("role")
    private String role;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("organizationName")
    private String organizationName;

    public RegisterRequest(String email, String password, String role, String fullName, String organizationName) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.organizationName = organizationName;
    }
}
