package com.example.hanoconnectapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Model để nhận dữ liệu chi tiết của Volunteer từ API
public class VolunteerProfileResponse {

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("district")
    private String district;

    @SerializedName("skills")
    private List<String> skills;

    @SerializedName("causes")
    private List<String> causes;

    // Getters cho các trường
    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDistrict() {
        return district;
    }

    public List<String> getSkills() {
        return skills;
    }

    public List<String> getCauses() {
        return causes;
    }
}
