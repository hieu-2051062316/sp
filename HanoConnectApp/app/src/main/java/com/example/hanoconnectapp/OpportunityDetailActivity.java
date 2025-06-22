package com.example.hanoconnectapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hanoconnectapp.models.OpportunityResponseDto;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.stream.Collectors;

public class OpportunityDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunity_detail);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        TextView tvOrgName = findViewById(R.id.tvOrgName);
        TextView tvOpportunityTitle = findViewById(R.id.tvOpportunityTitle);
        TextView tvOpportunityDescription = findViewById(R.id.tvOpportunityDescription);
        TextView tvLocation = findViewById(R.id.tvLocation);
        TextView tvTime = findViewById(R.id.tvTime);
        TextView tvSkills = findViewById(R.id.tvSkills);

        // Nhận đối tượng từ Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("OPPORTUNITY_DETAIL")) {
            OpportunityResponseDto opportunity = (OpportunityResponseDto) intent.getSerializableExtra("OPPORTUNITY_DETAIL");

            if (opportunity != null) {
                // Gán dữ liệu lên các View
                tvOrgName.setText(opportunity.getOrganizationName());
                toolbar.setTitle(opportunity.getOrganizationName());
                tvOpportunityTitle.setText(opportunity.getTitle());
                tvOpportunityDescription.setText(opportunity.getDescription());
                tvLocation.setText("Địa điểm: " + opportunity.getLocation());

                // Định dạng lại thời gian (nếu cần) và hiển thị
                String time = "Thời gian: " + (opportunity.getStartDate() != null ? opportunity.getStartDate().substring(0, 10) : "Linh hoạt");
                tvTime.setText(time);

                // Nối các kỹ năng thành một chuỗi
                if (opportunity.getSkills() != null && !opportunity.getSkills().isEmpty()) {
                    String skillsText = opportunity.getSkills().stream()
                            .map(skill -> skill.getSkillName())
                            .collect(Collectors.joining(", "));
                    tvSkills.setText("Kỹ năng yêu cầu: " + skillsText);
                } else {
                    tvSkills.setText("Kỹ năng yêu cầu: Không yêu cầu cụ thể");
                }
            }
        }
    }
}
