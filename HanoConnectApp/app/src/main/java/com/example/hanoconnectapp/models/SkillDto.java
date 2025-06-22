package com.example.hanoconnectapp.models;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class SkillDto implements Serializable {
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