package com.example.hanoconnectapp.models;

import com.google.gson.annotations.SerializedName;

// Class này dùng để đóng gói dữ liệu gửi đi khi ứng tuyển
public class ApplyRequest {

    @SerializedName("opportunityId")
    private int opportunityId;

    @SerializedName("volunteerUserId")
    private int volunteerUserId;

    @SerializedName("cvUrl")
    private String cvUrl;

    // motivationLetter là không bắt buộc nên chúng ta có thể bỏ qua trong constructor
    @SerializedName("motivationLetter")
    private String motivationLetter;

    public ApplyRequest(int opportunityId, int volunteerUserId, String cvUrl) {
        this.opportunityId = opportunityId;
        this.volunteerUserId = volunteerUserId;
        this.cvUrl = cvUrl;
        this.motivationLetter = ""; // Gửi một chuỗi rỗng nếu không có
    }
}
