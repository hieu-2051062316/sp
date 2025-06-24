package com.example.hanoconnectapp.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class VolunteerProfileResponse {

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("skills")
    private List<String> skills;

    @SerializedName("causes")
    private List<String> causes;

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getSkills() {
        return skills;
    }

    public List<String> getCauses() {
        return causes;
    }
}
