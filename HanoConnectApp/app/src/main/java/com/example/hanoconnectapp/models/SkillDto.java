package com.example.hanoconnectapp.models;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable; // Thêm import

public class SkillDto implements Serializable { // Thêm implements
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
