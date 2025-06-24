package com.example.hanoconnectapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hanoconnectapp.CreateCampaignActivity;
import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.models.OrganizationProfileResponse;
import com.example.hanoconnectapp.networking.ApiService;
import com.example.hanoconnectapp.networking.RetrofitClient;
import com.example.hanoconnectapp.util.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private TextView tvNewApplications, tvActiveCampaigns;
    private SessionManager sessionManager;
    private ApiService apiService;

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

        // Khởi tạo các thành phần
        sessionManager = new SessionManager(getContext());
        apiService = RetrofitClient.getApiService();

        // Ánh xạ views
        tvNewApplications = view.findViewById(R.id.tvNewApplications);
        tvActiveCampaigns = view.findViewById(R.id.tvActiveCampaigns);
        FloatingActionButton fabCreatePost = view.findViewById(R.id.fabCreatePost);

        if (fabCreatePost != null) {
            fabCreatePost.setOnClickListener(v -> {
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), CreateCampaignActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    // Dùng onResume để dữ liệu được làm mới mỗi khi người dùng quay lại tab này
    @Override
    public void onResume() {
        super.onResume();
        fetchDashboardData();
    }

    private void fetchDashboardData() {
        int orgId = sessionManager.getOrganizationId();
        if (orgId == -1) {
            Toast.makeText(getContext(), "Lỗi phiên đăng nhập của tổ chức", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.getOrganizationProfile(orgId).enqueue(new Callback<OrganizationProfileResponse>() {
            @Override
            public void onResponse(Call<OrganizationProfileResponse> call, Response<OrganizationProfileResponse> response) {
                // Kiểm tra fragment có còn được gắn vào activity không
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    OrganizationProfileResponse profile = response.body();

                    // Cập nhật giao diện với dữ liệu thật
                    // Hiện tại DTO chưa có số đơn mới, ta sẽ dùng tổng số đơn
                    tvNewApplications.setText(String.valueOf(profile.getTotalApplications()));
                    tvActiveCampaigns.setText(String.valueOf(profile.getTotalOpportunities()));
                }
            }

            @Override
            public void onFailure(Call<OrganizationProfileResponse> call, Throwable t) {
                if(isAdded()) {
                    Toast.makeText(getContext(), "Lỗi tải dữ liệu Dashboard", Toast.LENGTH_SHORT).show();
                    Log.e("DashboardFragment", "API call failed: ", t);
                }
            }
        });
    }
}
