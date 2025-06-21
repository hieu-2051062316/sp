package com.example.hanoconnectapp.models;

import com.google.gson.annotations.SerializedName;

public class SkillDto {
    @SerializedName("skillId")
    private int skillId;

    @SerializedName("skillName")
    private String skillName;

    public int getSkillId() {
        return skillId;
    }

    public String getSkillName() {
        return skillName;
    }
}
