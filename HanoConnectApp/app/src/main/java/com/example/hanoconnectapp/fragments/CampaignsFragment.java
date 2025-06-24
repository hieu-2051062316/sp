package com.example.hanoconnectapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.adapters.OrgCampaignAdapter;
import com.example.hanoconnectapp.models.OpportunityResponseDto;
import com.example.hanoconnectapp.networking.ApiService;
import com.example.hanoconnectapp.networking.RetrofitClient;
import com.example.hanoconnectapp.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampaignsFragment extends Fragment {

    private RecyclerView rvCampaigns;
    private OrgCampaignAdapter orgCampaignAdapter;
    private List<OpportunityResponseDto> campaignList = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView tvNoResults;
    private SessionManager sessionManager;
    private ApiService apiService;

    public CampaignsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_campaigns, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCampaigns = view.findViewById(R.id.rvCampaigns);
        progressBar = view.findViewById(R.id.progressBar);
        tvNoResults = view.findViewById(R.id.tvNoResults); // Ánh xạ view
        sessionManager = new SessionManager(getContext());
        apiService = RetrofitClient.getApiService();

        tvNoResults.setText("Chưa có chiến dịch nào được đăng tải"); // Tùy chỉnh text
        setupRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchCampaigns();
    }

    private void setupRecyclerView() {
        orgCampaignAdapter = new OrgCampaignAdapter(getContext(), campaignList);
        rvCampaigns.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCampaigns.setAdapter(orgCampaignAdapter);
    }

    private void fetchCampaigns() {
        progressBar.setVisibility(View.VISIBLE);
        rvCampaigns.setVisibility(View.GONE);
        tvNoResults.setVisibility(View.GONE);

        int organizationId = sessionManager.getOrganizationId();

        if (organizationId == -1) {
            Toast.makeText(getContext(), "Lỗi xác thực tổ chức.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        apiService.getOpportunitiesByOrganization(organizationId).enqueue(new Callback<List<OpportunityResponseDto>>() {
            @Override
            public void onResponse(Call<List<OpportunityResponseDto>> call, Response<List<OpportunityResponseDto>> response) {
                progressBar.setVisibility(View.GONE);
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    campaignList.clear();
                    campaignList.addAll(response.body());
                    orgCampaignAdapter.notifyDataSetChanged();

                    // Hiển thị thông báo nếu danh sách trống
                    if (campaignList.isEmpty()) {
                        tvNoResults.setVisibility(View.VISIBLE);
                    } else {
                        rvCampaigns.setVisibility(View.VISIBLE);
                    }
                } else {
                    if(isAdded()) Toast.makeText(getContext(), "Không thể tải danh sách chiến dịch.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OpportunityResponseDto>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                if(isAdded()) {
                    Toast.makeText(getContext(), "Lỗi kết nối.", Toast.LENGTH_SHORT).show();
                    Log.e("CampaignsFragment", "API call failed: ", t);
                }
            }
        });
    }
}
