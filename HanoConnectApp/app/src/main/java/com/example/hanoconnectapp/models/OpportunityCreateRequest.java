package com.example.hanoconnectapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Class này dùng để đóng gói dữ liệu khi một tổ chức tạo cơ hội mới.
public class OpportunityCreateRequest {

    @SerializedName("organizationId")
    private int organizationId;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("causeId")
    private int causeId; // Tạm thời sẽ gán cứng một giá trị để test

    @SerializedName("location")
    private String location;

    // Các trường thời gian và số lượng là optional, có thể bỏ qua nếu không nhập
    @SerializedName("requiredVolunteers")
    private Integer requiredVolunteers;

    @SerializedName("skillIds")
    private List<Integer> skillIds; // Danh sách ID của các kỹ năng

    // Constructor để dễ dàng tạo đối tượng
    public OpportunityCreateRequest(int organizationId, String title, String description, String location, Integer requiredVolunteers, int causeId, List<Integer> skillIds) {
        this.organizationId = organizationId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.requiredVolunteers = requiredVolunteers;
        this.causeId = causeId;
        this.skillIds = skillIds;
    }
}
