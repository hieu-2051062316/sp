package com.example.hanoconnectapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OpportunityResponseDto {

    @SerializedName("opportunityId")
    private int opportunityId;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("location")
    private String location;

    @SerializedName("organizationName")
    private String organizationName;

    @SerializedName("skills")
    private List<SkillDto> skills;

    // Getters
    public int getOpportunityId() { return opportunityId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getOrganizationName() { return organizationName; }
    public List<SkillDto> getSkills() { return skills; }

    // Constructor để tạo dữ liệu giả
    public OpportunityResponseDto(String title, String organizationName, String description) {
        this.title = title;
        this.organizationName = organizationName;
        this.description = description;
        // Các trường khác sẽ có giá trị mặc định (null hoặc 0)
    }
}
