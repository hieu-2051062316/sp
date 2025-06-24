package com.example.hanoconnectadmin.models;

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

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("isFlexibleTime")
    private boolean isFlexibleTime;

    @SerializedName("requiredVolunteers")
    private Integer requiredVolunteers; // Dùng Integer để chấp nhận giá trị null

    @SerializedName("benefits")
    private String benefits;

    @SerializedName("contactInfo")
    private String contactInfo;

    @SerializedName("applicationDeadline")
    private String applicationDeadline;

    @SerializedName("status")
    private String status;

    @SerializedName("isApprovedByAdmin")
    private boolean isApprovedByAdmin;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("organizationId")
    private int organizationId;

    @SerializedName("organizationName")
    private String organizationName;

    @SerializedName("organizationContactPerson")
    private String organizationContactPerson;

    @SerializedName("causeId")
    private int causeId;

    @SerializedName("causeName")
    private String causeName;

    @SerializedName("skills")
    private List<SkillDto> skills;

    // Getters cho các trường quan trọng để hiển thị
    public int getOpportunityId() { return opportunityId; }
    public String getTitle() { return title; }
    public String getOrganizationName() { return organizationName; }
    public String getLocation() { return location; }
}