package com.example.hanoconnectapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.adapters.OrgCampaignAdapter;
import com.example.hanoconnectapp.models.OrgCampaignItem;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private TextView tvNewApplications, tvActiveCampaigns;
    private RecyclerView rvOrgCampaigns;
    private OrgCampaignAdapter orgCampaignAdapter;
    private List<OrgCampaignItem> campaignList = new ArrayList<>();

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
        rvOrgCampaigns = view.findViewById(R.id.rvOrgCampaigns);

        setupRecyclerView();
        loadDummyData();
    }

    private void setupRecyclerView() {
        orgCampaignAdapter = new OrgCampaignAdapter(campaignList);
        rvOrgCampaigns.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOrgCampaigns.setAdapter(orgCampaignAdapter);
    }

    private void loadDummyData() {
        // Dữ liệu giả cho các thẻ thống kê
        tvNewApplications.setText("123");
        tvActiveCampaigns.setText("5");

        // Dữ liệu giả cho danh sách
        campaignList.clear();
        campaignList.add(new OrgCampaignItem("Mùa Hè Xanh 2025", "Có 123 đơn ứng tuyển", R.drawable.logo_hanoconnect));
        campaignList.add(new OrgCampaignItem("Chiến dịch ví dụ", "Có 368 đơn ứng tuyển", R.drawable.logo_hanoconnect));
        campaignList.add(new OrgCampaignItem("Tên chiến dịch mẫu", "Đã đóng chiến dịch", R.drawable.logo_hanoconnect));

        orgCampaignAdapter.notifyDataSetChanged();
    }
}