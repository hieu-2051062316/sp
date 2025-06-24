package com.example.hanoconnectapp.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class OpportunityResponseDto implements Serializable {

    @SerializedName("opportunityId")
    private int opportunityId;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("location")
    private String location;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("status")
    private String status;

    @SerializedName("organizationName")
    private String organizationName;

    // Thêm thuộc tính mới để lấy URL logo
    @SerializedName("organizationLogoUrl")
    private String organizationLogoUrl;

    @SerializedName("skills")
    private List<SkillDto> skills;

    public OpportunityResponseDto() {
    }

    public OpportunityResponseDto(String title, String organizationName, String description) {
        this.title = title;
        this.organizationName = organizationName;
        this.description = description;
        this.organizationLogoUrl = null; // Logo sẽ không hiển thị cho dữ liệu giả
    }

    public int getOpportunityId() {
        return opportunityId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStatus() {
        return status;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getOrganizationLogoUrl() {
        return organizationLogoUrl;
    }

    public List<SkillDto> getSkills() {
        return skills;
    }
}
