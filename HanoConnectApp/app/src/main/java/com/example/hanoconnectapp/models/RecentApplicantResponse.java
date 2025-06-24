package com.example.hanoconnectapp.models;

import com.google.gson.annotations.SerializedName;

// Model cho các ứng viên gần đây trên Dashboard
public class RecentApplicantResponse {
    @SerializedName("volunteerName")
    private String volunteerName;
    @SerializedName("opportunityTitle")
    private String opportunityTitle;
    @SerializedName("applicationTime")
    private String applicationTime;

    public String getVolunteerName() { return volunteerName; }
    public String getOpportunityTitle() { return opportunityTitle; }
    public String getApplicationTime() { return applicationTime; }
}
