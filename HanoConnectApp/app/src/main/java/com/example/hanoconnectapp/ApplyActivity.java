package com.example.hanoconnectapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoconnectapp.models.ApplyRequest;
import com.example.hanoconnectapp.networking.ApiService;
import com.example.hanoconnectapp.networking.RetrofitClient;
import com.example.hanoconnectapp.util.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplyActivity extends AppCompatActivity {

    private TextView tvCampaignName;
    private EditText etCvLink;
    private MaterialButton btnSubmitApplication;
    private ProgressBar progressBar;

    private ApiService apiService;
    private SessionManager sessionManager;
    private int opportunityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        // Khởi tạo các đối tượng cần thiết
        apiService = RetrofitClient.getApiService();
        sessionManager = new SessionManager(this);

        // Ánh xạ View
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        tvCampaignName = findViewById(R.id.tvCampaignName);
        etCvLink = findViewById(R.id.etCvLink);
        btnSubmitApplication = findViewById(R.id.btnSubmitApplication);
        progressBar = findViewById(R.id.progressBarApply); // Ánh xạ ProgressBar


        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Lấy dữ liệu từ Intent
        opportunityId = getIntent().getIntExtra("OPPORTUNITY_ID", -1);
        String campaignName = getIntent().getStringExtra("OPPORTUNITY_NAME");

        if (campaignName != null) {
            tvCampaignName.setText("Ứng tuyển cho: " + campaignName);
        }
        if (opportunityId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID của cơ hội.", Toast.LENGTH_LONG).show();
            finish(); // Đóng activity nếu không có ID
        }


        btnSubmitApplication.setOnClickListener(v -> submitApplication());
    }

    private void submitApplication() {
        String cvLink = etCvLink.getText().toString().trim();

        if (cvLink.isEmpty() || !Patterns.WEB_URL.matcher(cvLink).matches()) {
            Toast.makeText(this, "Vui lòng nhập một đường dẫn CV hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy userId của tình nguyện viên đã đăng nhập
        int volunteerUserId = sessionManager.getUserId();
        if (volunteerUserId == -1) {
            Toast.makeText(this, "Lỗi: Phiên đăng nhập không hợp lệ. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            // Chuyển về màn hình Login
            Intent intent = new Intent(ApplyActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }

        // Vô hiệu hóa nút bấm và hiển thị loading
        btnSubmitApplication.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        // Tạo request body và gọi API
        ApplyRequest request = new ApplyRequest(opportunityId, volunteerUserId, cvLink);
        Call<Void> call = apiService.createApplication(request);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Kích hoạt lại nút bấm và ẩn loading
                btnSubmitApplication.setEnabled(true);
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Toast.makeText(ApplyActivity.this, "Đã gửi đơn ứng tuyển thành công!", Toast.LENGTH_LONG).show();
                    finish(); // Đóng màn hình sau khi thành công
                } else if (response.code() == 409) { // 409 Conflict
                    Toast.makeText(ApplyActivity.this, "Bạn đã ứng tuyển vào cơ hội này rồi.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(ApplyActivity.this, "Gửi đơn thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Kích hoạt lại nút bấm và ẩn loading
                btnSubmitApplication.setEnabled(true);
                progressBar.setVisibility(View.GONE);

                Log.e("ApplyAPI", "Lỗi kết nối", t);
                Toast.makeText(ApplyActivity.this, "Lỗi kết nối, không thể gửi đơn.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
