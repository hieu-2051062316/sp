package com.example.hanoconnectapp.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

// Model để nhận dữ liệu của một ứng viên từ API
public class ApplicantResponse implements Serializable {

    @SerializedName("applicationId")
    private int applicationId;

    @SerializedName("volunteerUserId")
    private int volunteerUserId;

    @SerializedName("volunteerName")
    private String volunteerName;

    @SerializedName("volunteerEmail")
    private String volunteerEmail;

    @SerializedName("applicationTime")
    private String applicationTime; // Nhận dưới dạng chuỗi ngày tháng

    @SerializedName("cvUrl")
    private String cvUrl;

    @SerializedName("status")
    private String status;

    // Getters
    public int getApplicationId() { return applicationId; }
    public int getVolunteerUserId() { return volunteerUserId; }
    public String getVolunteerName() { return volunteerName; }
    public String getVolunteerEmail() { return volunteerEmail; }
    public String getApplicationTime() { return applicationTime; }
    public String getCvUrl() { return cvUrl; }
    public String getStatus() { return status; }
}
