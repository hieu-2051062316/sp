package com.example.hanoconnectapp.models;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("role")
    private String role;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("district")
    private String district;
    @SerializedName("organizationName")
    private String organizationName;
    @SerializedName("address")
    private String address;
    @SerializedName("website")
    private String website;
    @SerializedName("description")
    private String description;

    public RegisterRequest(String email, String password, String role, String fullName, String phoneNumber, String district, String organizationName, String address, String website, String description) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.district = district;
        this.organizationName = organizationName;
        this.address = address;
        this.website = website;
        this.description = description;
    }
}
