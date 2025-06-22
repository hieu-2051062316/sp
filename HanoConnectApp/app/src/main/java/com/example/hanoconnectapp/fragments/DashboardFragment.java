package com.example.hanoconnectapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.hanoconnectapp.R;

public class DashboardFragment extends Fragment {

    private TextView tvNewApplications, tvActiveCampaigns;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNewApplications = view.findViewById(R.id.tvNewApplications);
        tvActiveCampaigns = view.findViewById(R.id.tvActiveCampaigns);

        loadDummyData();
    }

    private void loadDummyData() {
        // Dữ liệu giả cho các thẻ thống kê
        if (tvNewApplications != null) {
            tvNewApplications.setText("123");
        }
        if (tvActiveCampaigns != null) {
            tvActiveCampaigns.setText("5");
        }
    }
}
