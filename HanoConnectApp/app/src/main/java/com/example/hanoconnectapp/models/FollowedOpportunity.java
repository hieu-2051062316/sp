package com.example.hanoconnectapp.models;

public class FollowedOpportunity {
    private String campaignName;
    private String status;
    private int logoResId; // Dùng ID của drawable để hiển thị logo giả

    public FollowedOpportunity(String campaignName, String status, int logoResId) {
        this.campaignName = campaignName;
        this.status = status;
        this.logoResId = logoResId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public String getStatus() {
        return status;
    }

    public int getLogoResId() {
        return logoResId;
    }
}
