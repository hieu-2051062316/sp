package com.example.hanoconnectapp;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class ApplyActivity extends AppCompatActivity {

    private TextView tvCampaignName;
    private EditText etCvLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        tvCampaignName = findViewById(R.id.tvCampaignName);
        etCvLink = findViewById(R.id.etCvLink);
        MaterialButton btnSubmitApplication = findViewById(R.id.btnSubmitApplication);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        String campaignName = getIntent().getStringExtra("OPPORTUNITY_NAME");
        if (campaignName != null) {
            tvCampaignName.setText("Ứng tuyển cho: " + campaignName);
        }

        btnSubmitApplication.setOnClickListener(v -> {
            String cvLink = etCvLink.getText().toString().trim();

            if (cvLink.isEmpty() || !Patterns.WEB_URL.matcher(cvLink).matches()) {
                Toast.makeText(this, "Vui lòng nhập một đường dẫn hợp lệ", Toast.LENGTH_SHORT).show();
            } else {
                // Tạm thời hiển thị Toast thành công
                // TODO: Gọi API để gửi đơn với cvLink
                Toast.makeText(this, "Đã gửi đơn ứng tuyển thành công!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}