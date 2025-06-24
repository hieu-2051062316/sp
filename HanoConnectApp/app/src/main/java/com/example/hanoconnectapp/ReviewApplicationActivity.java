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
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.hanoconnectapp.models.ApplicantResponse;
import com.example.hanoconnectapp.models.UpdateApplicationStatusRequest;
import com.example.hanoconnectapp.models.VolunteerProfileResponse;
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
    private MaterialButton btnAccept, btnReject;

    // Các view mới cho thông tin chi tiết
    private TextView tvEmail, tvDistrict, tvSkills, tvCauses;

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

        // Ánh xạ các view mới
        tvEmail = findViewById(R.id.tvEmail);
        tvDistrict = findViewById(R.id.tvDistrict);
        tvSkills = findViewById(R.id.tvSkills);
        tvCauses = findViewById(R.id.tvCauses);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Nhận đối tượng ApplicantResponse từ Intent
        applicant = (ApplicantResponse) getIntent().getSerializableExtra("APPLICANT_DETAIL");

        if (applicant != null) {
            populateBasicData(applicant, ivAvatar, tvName, tvDate);

            // Sửa lỗi: Đặt sự kiện click cho nút Xem CV
            btnViewCv.setOnClickListener(v -> {
                if (applicant.getCvUrl() != null && !applicant.getCvUrl().isEmpty()){
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(applicant.getCvUrl()));
                        startActivity(browserIntent);
                    } catch (Exception e) {
                        Toast.makeText(this, "Không thể mở liên kết CV.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Ứng viên không cung cấp CV.", Toast.LENGTH_SHORT).show();
                }
            });

            // Sự kiện click nút Chấp nhận/Từ chối
            btnAccept.setOnClickListener(v -> updateStatus("Accepted"));
            btnReject.setOnClickListener(v -> updateStatus("Rejected"));

            // Gọi API để lấy thông tin chi tiết của profile
            fetchVolunteerProfile(applicant.getVolunteerUserId());

        } else {
            Toast.makeText(this, "Không thể tải thông tin ứng viên.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // Hiển thị các thông tin cơ bản có sẵn
    private void populateBasicData(ApplicantResponse applicant, ImageView ivAvatar, TextView tvName, TextView tvDate) {
        Glide.with(this)
                .load(R.drawable.ic_person_placeholder)
                .circleCrop()
                .into(ivAvatar);

        tvName.setText(applicant.getVolunteerName());

        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime dateTime = LocalDateTime.parse(applicant.getApplicationTime(), inputFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            tvDate.setText("Nộp ngày: " + dateTime.format(outputFormatter));
        } catch (Exception e) {
            tvDate.setText("Nộp ngày: " + applicant.getApplicationTime());
        }
    }

    // Gọi API để lấy thông tin chi tiết
    private void fetchVolunteerProfile(int volunteerId) {
        apiService.getVolunteerProfile(volunteerId).enqueue(new Callback<VolunteerProfileResponse>() {
            @Override
            public void onResponse(Call<VolunteerProfileResponse> call, Response<VolunteerProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateDetailedData(response.body());
                }
            }

            @Override
            public void onFailure(Call<VolunteerProfileResponse> call, Throwable t) {
                // Có thể hiển thị thông báo lỗi nếu cần
            }
        });
    }

    // Hiển thị các thông tin chi tiết lên UI
    private void populateDetailedData(VolunteerProfileResponse profile) {
        tvEmail.setText("Email: " + (profile.getEmail() != null ? profile.getEmail() : "N/A"));
        tvDistrict.setText("Quận/Huyện: " + (profile.getDistrict() != null ? profile.getDistrict() : "N/A"));
        tvSkills.setText("Kỹ năng: " + (profile.getSkills() != null && !profile.getSkills().isEmpty() ? String.join(", ", profile.getSkills()) : "Chưa cập nhật"));
        tvCauses.setText("Quan tâm: " + (profile.getCauses() != null && !profile.getCauses().isEmpty() ? String.join(", ", profile.getCauses()) : "Chưa cập nhật"));
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
                    setResult(RESULT_OK); // Đặt kết quả để activity trước có thể refresh
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
        btnAccept.setEnabled(!isLoading);
        btnReject.setEnabled(!isLoading);
        // Có thể thêm ProgressBar và quản lý ở đây
    }
}
