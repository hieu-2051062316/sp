package com.example.hanoconnectapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Model để gửi dữ liệu cập nhật profile lên server
public class VolunteerProfileUpdateRequest {

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("district")
    private String district;

    @SerializedName("skillIds")
    private List<Integer> skillIds;

    @SerializedName("causeIds")
    private List<Integer> causeIds;

    public VolunteerProfileUpdateRequest(String fullName, String phoneNumber, String district, List<Integer> skillIds, List<Integer> causeIds) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.district = district;
        this.skillIds = skillIds;
        this.causeIds = causeIds;
    }
}
