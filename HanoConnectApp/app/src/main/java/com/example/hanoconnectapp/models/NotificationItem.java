package com.example.hanoconnectapp.models;

public class NotificationItem {
    private String campaignName;
    private String message;
    private int logoResId;
    private boolean isNew; // Để xác định thông báo mới hay đã đọc

    public NotificationItem(String campaignName, String message, int logoResId, boolean isNew) {
        this.campaignName = campaignName;
        this.message = message;
        this.logoResId = logoResId;
        this.isNew = isNew;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public String getMessage() {
        return message;
    }

    public int getLogoResId() {
        return logoResId;
    }

    public boolean isNew() {
        return isNew;
    }
}
