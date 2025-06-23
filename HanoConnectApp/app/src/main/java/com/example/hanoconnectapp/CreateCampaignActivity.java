package com.example.hanoconnectapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoconnectapp.models.OpportunityCreateRequest;
import com.example.hanoconnectapp.models.OpportunityResponseDto;
import com.example.hanoconnectapp.networking.ApiService;
import com.example.hanoconnectapp.networking.RetrofitClient;
import com.example.hanoconnectapp.util.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCampaignActivity extends AppCompatActivity {

    private EditText etCampaignName, etCampaignDescription, etLocation, etQuantity;
    private MaterialButton btnPostCampaign;
    private ProgressBar progressBar;

    private SessionManager sessionManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_campaign);

        // Khởi tạo
        sessionManager = new SessionManager(this);
        apiService = RetrofitClient.getApiService();

        // Ánh xạ views
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        etCampaignName = findViewById(R.id.etCampaignName);
        etCampaignDescription = findViewById(R.id.etCampaignDescription);
        etLocation = findViewById(R.id.etLocation);
        etQuantity = findViewById(R.id.etQuantity);
        btnPostCampaign = findViewById(R.id.btnPostCampaign);
        progressBar = findViewById(R.id.progressBarCreate);

        // Đặt sự kiện click cho nút đăng bài
        btnPostCampaign.setOnClickListener(v -> handlePostCampaign());
    }

    private void handlePostCampaign() {
        // Lấy dữ liệu từ các trường input
        String title = etCampaignName.getText().toString().trim();
        String description = etCampaignDescription.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();

        // Kiểm tra dữ liệu bắt buộc
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Tên hoạt động và Mô tả là bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy organizationId từ session
        int organizationId = sessionManager.getOrganizationId();
        if (organizationId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin tổ chức. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            return;
        }

        // Chuyển đổi số lượng sang Integer, có thể là null
        Integer quantity = quantityStr.isEmpty() ? null : Integer.parseInt(quantityStr);

        // --- Chú ý: Dữ liệu tạm thời để test ---
        // Do giao diện chưa có phần chọn, chúng ta sẽ gán cứng các giá trị này
        int causeId = 1; // Ví dụ: 1 là "Bảo vệ Môi trường"
        List<Integer> skillIds = Arrays.asList(1, 3); // Ví dụ: 1 là "Dạy học", 3 là "IT"

        // Hiển thị loading và vô hiệu hóa nút
        progressBar.setVisibility(View.VISIBLE);
        btnPostCampaign.setEnabled(false);

        // Tạo đối tượng request và gọi API
        OpportunityCreateRequest request = new OpportunityCreateRequest(organizationId, title, description, location, quantity, causeId, skillIds);
        Call<OpportunityResponseDto> call = apiService.createOpportunity(request);

        call.enqueue(new Callback<OpportunityResponseDto>() {
            @Override
            public void onResponse(Call<OpportunityResponseDto> call, Response<OpportunityResponseDto> response) {
                // Ẩn loading và kích hoạt lại nút
                progressBar.setVisibility(View.GONE);
                btnPostCampaign.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CreateCampaignActivity.this, "Đăng bài thành công!", Toast.LENGTH_LONG).show();
                    finish(); // Đóng activity sau khi thành công
                } else {
                    Toast.makeText(CreateCampaignActivity.this, "Đăng bài thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OpportunityResponseDto> call, Throwable t) {
                // Ẩn loading và kích hoạt lại nút
                progressBar.setVisibility(View.GONE);
                btnPostCampaign.setEnabled(true);
                Log.e("CreateCampaignAPI", "Lỗi kết nối", t);
                Toast.makeText(CreateCampaignActivity.this, "Lỗi kết nối, không thể đăng bài.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
