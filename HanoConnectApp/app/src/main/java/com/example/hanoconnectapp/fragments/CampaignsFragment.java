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
import android.widget.ProgressBar;

import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.adapters.OrgCampaignAdapter;
import com.example.hanoconnectapp.models.OrgCampaignItem;

import java.util.ArrayList;
import java.util.List;

public class CampaignsFragment extends Fragment {

    private RecyclerView rvCampaigns;
    private OrgCampaignAdapter orgCampaignAdapter;
    private List<OrgCampaignItem> campaignList = new ArrayList<>();
    private ProgressBar progressBar;

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

        setupRecyclerView();
        loadDummyData();
    }

    private void setupRecyclerView() {
        orgCampaignAdapter = new OrgCampaignAdapter(campaignList);
        rvCampaigns.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCampaigns.setAdapter(orgCampaignAdapter);
    }

    private void loadDummyData() {
        progressBar.setVisibility(View.GONE);
        rvCampaigns.setVisibility(View.VISIBLE);

        campaignList.clear();
        campaignList.add(new OrgCampaignItem("Mùa Hè Xanh 2025", "Có 123 đơn ứng tuyển", R.drawable.logo_hanoconnect));
        campaignList.add(new OrgCampaignItem("Chiến dịch ví dụ", "Có 368 đơn ứng tuyển", R.drawable.logo_hanoconnect));
        campaignList.add(new OrgCampaignItem("Tên chiến dịch mẫu 1", "Đã đóng chiến dịch", R.drawable.logo_hanoconnect));
        campaignList.add(new OrgCampaignItem("Chiến dịch ví dụ 3", "Đã đóng chiến dịch", R.drawable.logo_hanoconnect));
        campaignList.add(new OrgCampaignItem("Tên chiến dịch mẫu 2", "Đã đóng chiến dịch", R.drawable.logo_hanoconnect));
        campaignList.add(new OrgCampaignItem("Tên chiến dịch 2", "Thông báo mẫu cho phần này", R.drawable.logo_hanoconnect));


        orgCampaignAdapter.notifyDataSetChanged();
    }
}
