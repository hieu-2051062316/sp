package com.example.hanoconnectapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hanoconnectapp.models.ApplicantResponse;
import com.example.hanoconnectapp.models.UpdateApplicationStatusRequest;
import com.example.hanoconnectapp.networking.ApiService;
import com.example.hanoconnectapp.networking.RetrofitClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewApplicationActivity extends AppCompatActivity {

    private ApiService apiService;
    private ApplicantResponse applicant;
    private ProgressBar progressBar;
    private MaterialButton btnAccept, btnReject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_application);

        // Khởi tạo
        apiService = RetrofitClient.getApiService();

        // Ánh xạ View
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        ImageView ivAvatar = findViewById(R.id.ivApplicantAvatar);
        TextView tvName = findViewById(R.id.tvApplicantName);
        TextView tvDate = findViewById(R.id.tvApplyDate);
        Button btnViewCv = findViewById(R.id.btnViewCv);
        btnAccept = findViewById(R.id.btnAccept);
        btnReject = findViewById(R.id.btnReject);
        // Cần thêm ProgressBar vào layout
        // progressBar = findViewById(R.id.progressBarReview);


        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Nhận đối tượng ApplicantResponse từ Intent
        applicant = (ApplicantResponse) getIntent().getSerializableExtra("APPLICANT_DETAIL");

        if (applicant != null) {
            populateApplicantData(applicant, ivAvatar, tvName, tvDate);

            // Sự kiện click nút Xem CV
            btnViewCv.setOnClickListener(v -> {
                if (applicant.getCvUrl() != null && !applicant.getCvUrl().isEmpty()){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(applicant.getCvUrl()));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(this, "Ứng viên không cung cấp CV.", Toast.LENGTH_SHORT).show();
                }
            });

            // Sự kiện click nút Chấp nhận/Từ chối
            btnAccept.setOnClickListener(v -> updateStatus("Accepted"));
            btnReject.setOnClickListener(v -> updateStatus("Rejected"));
        }
    }

    private void populateApplicantData(ApplicantResponse applicant, ImageView ivAvatar, TextView tvName, TextView tvDate) {
        // Dùng Glide để hiển thị avatar placeholder
        Glide.with(this)
                .load(R.drawable.ic_person_placeholder)
                .circleCrop()
                .into(ivAvatar);

        tvName.setText(applicant.getVolunteerName());

        // Format lại ngày tháng cho dễ nhìn
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime dateTime = LocalDateTime.parse(applicant.getApplicationTime(), inputFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            tvDate.setText("Nộp ngày: " + dateTime.format(outputFormatter));
        } catch (Exception e) {
            tvDate.setText("Nộp ngày: " + applicant.getApplicationTime());
        }
    }

    private void updateStatus(String status) {
        if(applicant == null) return;

        showLoading(true);

        UpdateApplicationStatusRequest request = new UpdateApplicationStatusRequest(status);
        Call<ResponseBody> call = apiService.updateApplicationStatus(applicant.getApplicationId(), request);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(ReviewApplicationActivity.this, "Đã " + (status.equals("Accepted") ? "chấp nhận" : "từ chối") + " đơn.", Toast.LENGTH_SHORT).show();
                    // Đặt kết quả để activity trước có thể refresh
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ReviewApplicationActivity.this, "Cập nhật thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showLoading(false);
                Log.e("ReviewAppAPI", "Lỗi kết nối", t);
                Toast.makeText(ReviewApplicationActivity.this, "Lỗi kết nối.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            // if(progressBar != null) progressBar.setVisibility(View.VISIBLE);
            btnAccept.setEnabled(false);
            btnReject.setEnabled(false);
        } else {
            // if(progressBar != null) progressBar.setVisibility(View.GONE);
            btnAccept.setEnabled(true);
            btnReject.setEnabled(true);
        }
    }
}
