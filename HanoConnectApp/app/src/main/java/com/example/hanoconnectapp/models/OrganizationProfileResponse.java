package com.example.hanoconnectapp.models;

import com.google.gson.annotations.SerializedName;

public class OrganizationProfileResponse {

    @SerializedName("organizationName")
    private String organizationName;

    @SerializedName("description")
    private String description;

    @SerializedName("totalOpportunities")
    private int totalOpportunities;

    @SerializedName("totalApplications")
    private int totalApplications;


    public String getOrganizationName() {
        return organizationName;
    }

    public String getDescription() {
        return description;
    }

    public int getTotalOpportunities() {
        return totalOpportunities;
    }

    public int getTotalApplications() {
        return totalApplications;
    }
}
