package com.example.hanoconnectapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanoconnectapp.R;

public class OrgProfileFragment extends Fragment {

    public OrgProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_org_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvOrgProfileName = view.findViewById(R.id.tvOrgProfileName);
        TextView tvUpdateOrgInfo = view.findViewById(R.id.tvUpdateOrgInfo);
        TextView tvLogout = view.findViewById(R.id.tvLogout);

        // Tải dữ liệu giả
        if (tvOrgProfileName != null) {
            tvOrgProfileName.setText("Tổ chức Xanh Hà Nội");
        }

        // Thiết lập sự kiện click
        if (tvUpdateOrgInfo != null) {
            tvUpdateOrgInfo.setOnClickListener(v -> {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Chức năng Cập nhật thông tin Tổ chức", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (tvLogout != null) {
            tvLogout.setOnClickListener(v -> {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Chức năng Đăng xuất", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}