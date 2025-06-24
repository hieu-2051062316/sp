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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanoconnectapp.CreateCampaignActivity;
import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.adapters.RecentApplicantAdapter;
import com.example.hanoconnectapp.models.OrganizationProfileResponse;
import com.example.hanoconnectapp.models.RecentApplicantResponse;
import com.example.hanoconnectapp.networking.ApiService;
import com.example.hanoconnectapp.networking.RetrofitClient;
import com.example.hanoconnectapp.util.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private TextView tvNewApplications, tvActiveCampaigns;
    private RecyclerView rvRecentApplicants;
    private RecentApplicantAdapter recentApplicantAdapter;
    private List<RecentApplicantResponse> recentApplicantList = new ArrayList<>();
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

        sessionManager = new SessionManager(getContext());
        apiService = RetrofitClient.getApiService();

        tvNewApplications = view.findViewById(R.id.tvNewApplications);
        tvActiveCampaigns = view.findViewById(R.id.tvActiveCampaigns);
        rvRecentApplicants = view.findViewById(R.id.rvRecentApplicants);
        FloatingActionButton fabCreatePost = view.findViewById(R.id.fabCreatePost);

        if (fabCreatePost != null) {
            fabCreatePost.setOnClickListener(v -> {
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), CreateCampaignActivity.class);
                    startActivity(intent);
                }
            });
        }

        setupRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchDashboardData();
        fetchRecentApplicants();
    }

    private void setupRecyclerView() {
        recentApplicantAdapter = new RecentApplicantAdapter(recentApplicantList);
        rvRecentApplicants.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecentApplicants.setAdapter(recentApplicantAdapter);
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
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    OrganizationProfileResponse profile = response.body();
                    tvNewApplications.setText(String.valueOf(profile.getTotalApplications()));
                    tvActiveCampaigns.setText(String.valueOf(profile.getTotalOpportunities()));
                }
            }

            @Override
            public void onFailure(Call<OrganizationProfileResponse> call, Throwable t) {
                if(isAdded()) {
                    Toast.makeText(getContext(), "Lỗi tải dữ liệu Dashboard", Toast.LENGTH_SHORT).show();
                    Log.e("DashboardFragment", "Stats API call failed: ", t);
                }
            }
        });
    }

    private void fetchRecentApplicants() {
        int orgId = sessionManager.getOrganizationId();
        if (orgId == -1) return;

        apiService.getRecentApplicants(orgId).enqueue(new Callback<List<RecentApplicantResponse>>() {
            @Override
            public void onResponse(Call<List<RecentApplicantResponse>> call, Response<List<RecentApplicantResponse>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    recentApplicantList.clear();
                    recentApplicantList.addAll(response.body());
                    recentApplicantAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<RecentApplicantResponse>> call, Throwable t) {
                if(isAdded()) {
                    Log.e("DashboardFragment", "Recent Applicants API call failed: ", t);
                }
            }
        });
    }
}
