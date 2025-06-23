package com.example.hanoconnectapp.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("userId")
    private int userId;

    @SerializedName("email")
    private String email;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("role")
    private String role;

    // --- BẮT ĐẦU THÊM MỚI ---
    @SerializedName("organizationId")
    private Integer organizationId; // Sử dụng Integer để có thể nhận giá trị null
    // --- KẾT THÚC THÊM MỚI ---

    public int getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }

    // --- BẮT ĐẦU THÊM MỚI ---
    public Integer getOrganizationId() { return organizationId; }
    // --- KẾT THÚC THÊM MỚI ---
}
