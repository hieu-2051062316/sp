package com.example.hanoconnectapp.models;

import com.google.gson.annotations.SerializedName;

// Model để nhận dữ liệu về các đơn đã ứng tuyển của người dùng
public class MyApplicationResponse {

    @SerializedName("opportunityTitle")
    private String opportunityTitle;

    @SerializedName("organizationName")
    private String organizationName;

    @SerializedName("status")
    private String status;

    public String getOpportunityTitle() {
        return opportunityTitle;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getStatus() {
        return status;
    }
}
