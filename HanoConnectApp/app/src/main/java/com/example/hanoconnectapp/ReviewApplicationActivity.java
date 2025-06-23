// File Activity mới để hiển thị chi tiết ứng viên.
package com.example.hanoconnectapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hanoconnectapp.models.ApplicantItem;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class ReviewApplicationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_application);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        ImageView ivAvatar = findViewById(R.id.ivApplicantAvatar);
        TextView tvName = findViewById(R.id.tvApplicantName);
        TextView tvDate = findViewById(R.id.tvApplyDate);
        Button btnViewCv = findViewById(R.id.btnViewCv);
        MaterialButton btnReject = findViewById(R.id.btnReject);
        MaterialButton btnAccept = findViewById(R.id.btnAccept);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Nhận dữ liệu ứng viên từ Intent
        ApplicantItem applicant = (ApplicantItem) getIntent().getSerializableExtra("APPLICANT_DETAIL");

        if (applicant != null) {
            toolbar.setTitle("Hồ sơ ứng viên");
            ivAvatar.setImageResource(applicant.getAvatarResId());
            tvName.setText(applicant.getName());
            tvDate.setText(applicant.getApplyDate());

            // Sự kiện click nút Xem CV
            btnViewCv.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(applicant.getCvUrl()));
                startActivity(browserIntent);
            });

            // Sự kiện click các nút khác (tạm thời)
            btnAccept.setOnClickListener(v -> {
                Toast.makeText(this, "Đã chấp nhận " + applicant.getName(), Toast.LENGTH_SHORT).show();
                finish();
            });

            btnReject.setOnClickListener(v -> {
                Toast.makeText(this, "Đã từ chối " + applicant.getName(), Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}
