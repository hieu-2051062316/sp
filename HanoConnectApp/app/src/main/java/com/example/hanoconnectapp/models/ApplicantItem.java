// File này dùng để định nghĩa dữ liệu cho một ứng viên trong danh sách.
package com.example.hanoconnectapp.models;

import java.io.Serializable;

// Cần triển khai Serializable để có thể gửi đối tượng này qua Intent.
public class ApplicantItem implements Serializable {
    private String name;
    private String applyDate;
    private String cvUrl;
    private int avatarResId;

    public ApplicantItem(String name, String applyDate, String cvUrl, int avatarResId) {
        this.name = name;
        this.applyDate = applyDate;
        this.cvUrl = cvUrl;
        this.avatarResId = avatarResId;
    }

    public String getName() {
        return name;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public int getAvatarResId() {
        return avatarResId;
    }
}
