package com.example.hanoconnectapp.models;

import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

// Model để chứa dữ liệu Lĩnh vực (Cause) từ API
public class Cause {

    @SerializedName("causeId")
    private int causeId;

    @SerializedName("causeName")
    private String causeName;

    public int getCauseId() {
        return causeId;
    }

    public String getCauseName() {
        return causeName;
    }

    // Ghi đè phương thức toString() để ArrayAdapter hiển thị tên trong Spinner.
    @NonNull
    @Override
    public String toString() {
        return causeName;
    }
}
