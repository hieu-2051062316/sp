package com.example.hanoconnectapp.models;

import com.google.gson.annotations.SerializedName;

// Model để nhận dữ liệu thông báo từ API
public class NotificationResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("message")
    private String message;

    @SerializedName("isRead")
    private boolean isRead;

    @SerializedName("createdAt")
    private String createdAt; // Nhận dạng chuỗi

    public int getId() { return id; }
    public String getMessage() { return message; }
    public boolean isRead() { return isRead; }
    public String getCreatedAt() { return createdAt; }
}
