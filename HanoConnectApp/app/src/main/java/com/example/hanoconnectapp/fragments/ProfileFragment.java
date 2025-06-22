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

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvProfileName = view.findViewById(R.id.tvProfileName);
        TextView tvUpdateInfo = view.findViewById(R.id.tvUpdateInfo);
        TextView tvLogout = view.findViewById(R.id.tvLogout);

        if (tvProfileName != null) {
            tvProfileName.setText("DLuong Volunteer");
        }

        if (tvUpdateInfo != null) {
            tvUpdateInfo.setOnClickListener(v -> {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Chức năng Cập nhật thông tin", Toast.LENGTH_SHORT).show();
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
