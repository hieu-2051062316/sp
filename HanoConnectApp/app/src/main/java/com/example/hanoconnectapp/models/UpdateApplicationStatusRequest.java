package com.example.hanoconnectapp.models;

import com.google.gson.annotations.SerializedName;

// Model để gửi đi khi cập nhật trạng thái
public class UpdateApplicationStatusRequest {

    @SerializedName("status")
    private String status;

    public UpdateApplicationStatusRequest(String status) {
        this.status = status;
    }
}
