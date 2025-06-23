package com.example.hanoconnectapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hanoconnectapp.adapters.ApplicantAdapter;
import com.example.hanoconnectapp.models.ApplicantResponse;
import com.example.hanoconnectapp.networking.ApiService;
import com.example.hanoconnectapp.networking.RetrofitClient;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewApplicantsActivity extends AppCompatActivity {

    private RecyclerView rvApplicants;
    private ApplicantAdapter adapter;
    private List<ApplicantResponse> applicantList = new ArrayList<>();
    private ProgressBar progressBar;
    private ApiService apiService;
    private int opportunityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_applicants);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        rvApplicants = findViewById(R.id.rvApplicants);
        // Cần thêm ProgressBar vào layout activity_view_applicants.xml
        // progressBar = findViewById(R.id.progressBarViewApplicants);

        apiService = RetrofitClient.getApiService();

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Nhận dữ liệu từ Intent
        opportunityId = getIntent().getIntExtra("OPPORTUNITY_ID", -1);
        String campaignName = getIntent().getStringExtra("OPPORTUNITY_NAME");
        if (campaignName != null) {
            toolbar.setTitle("Đơn ứng tuyển: " + campaignName);
        }

        if (opportunityId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID chiến dịch.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupRecyclerView();
        fetchApplicants(); // Gọi API thật
    }

    private void setupRecyclerView() {
        adapter = new ApplicantAdapter(this, applicantList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvApplicants.setLayoutManager(layoutManager);
        rvApplicants.setAdapter(adapter);
        rvApplicants.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
    }

    private void fetchApplicants() {
        // if(progressBar != null) progressBar.setVisibility(View.VISIBLE);

        apiService.getApplicantsForOpportunity(opportunityId).enqueue(new Callback<List<ApplicantResponse>>() {
            @Override
            public void onResponse(Call<List<ApplicantResponse>> call, Response<List<ApplicantResponse>> response) {
                // if(progressBar != null) progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    applicantList.clear();
                    applicantList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if(applicantList.isEmpty()){
                        Toast.makeText(ViewApplicantsActivity.this, "Chưa có ứng viên nào.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ViewApplicantsActivity.this, "Lỗi tải danh sách ứng viên.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ApplicantResponse>> call, Throwable t) {
                // if(progressBar != null) progressBar.setVisibility(View.GONE);
                Toast.makeText(ViewApplicantsActivity.this, "Lỗi kết nối.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
