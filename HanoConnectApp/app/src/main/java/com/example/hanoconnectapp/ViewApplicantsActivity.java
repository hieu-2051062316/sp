package com.example.hanoconnectapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hanoconnectapp.adapters.ApplicantAdapter;
import com.example.hanoconnectapp.models.ApplicantItem;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.ArrayList;
import java.util.List;

public class ViewApplicantsActivity extends AppCompatActivity {

    private RecyclerView rvApplicants;
    private ApplicantAdapter adapter;
    private List<ApplicantItem> applicantList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_applicants);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        rvApplicants = findViewById(R.id.rvApplicants);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        String campaignName = getIntent().getStringExtra("CAMPAIGN_NAME");
        if (campaignName != null) {
            toolbar.setTitle("Đơn ứng tuyển: " + campaignName);
        }

        setupRecyclerView();
        loadDummyData();
    }

    private void setupRecyclerView() {
        // Cung cấp context cho Adapter
        adapter = new ApplicantAdapter(this, applicantList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvApplicants.setLayoutManager(layoutManager);
        rvApplicants.setAdapter(adapter);
        rvApplicants.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
    }

    private void loadDummyData() {
        applicantList.clear();
        applicantList.add(new ApplicantItem("Nguyễn Văn A", "Nộp ngày: 23/06/2025", "https://google.com", R.drawable.ic_person_placeholder));
        applicantList.add(new ApplicantItem("Trần Thị B", "Nộp ngày: 22/06/2025", "https://google.com", R.drawable.ic_person_placeholder));
        applicantList.add(new ApplicantItem("Lê Văn C", "Nộp ngày: 21/06/2025", "https://google.com", R.drawable.ic_person_placeholder));
        adapter.notifyDataSetChanged();
    }
}
