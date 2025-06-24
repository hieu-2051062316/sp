package com.example.hanoconnectapp.models;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Objects;

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

    // Override lại phương thức toString để Dialog hiển thị đúng tên
    @NonNull
    @Override
    public String toString() {
        return skillName;
    }

    // Override equals và hashCode để xử lý List.contains() chính xác
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillDto skillDto = (SkillDto) o;
        return skillId == skillDto.skillId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(skillId);
    }
}
