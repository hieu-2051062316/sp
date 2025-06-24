package com.example.hanoconnectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button btnManageVolunteers;
    private Button btnManageOrganizations;
    private Button btnVerifyOrganizations;
    private Button btnManageOpportunities;
    private Button btnManageCategories;
    private Button btnViewReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Ánh xạ các thành phần UI
        btnManageVolunteers = findViewById(R.id.btnManageVolunteers);
        btnManageOrganizations = findViewById(R.id.btnManageOrganizations);
        btnVerifyOrganizations = findViewById(R.id.btnVerifyOrganizations);
        btnManageOpportunities = findViewById(R.id.btnManageOpportunities);
        btnManageCategories = findViewById(R.id.btnManageCategories);
        btnViewReports = findViewById(R.id.btnViewReports);

        // Thiết lập sự kiện click cho các nút
        btnManageVolunteers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Chuyển đến màn hình quản lý tình nguyện viên
                Toast.makeText(AdminDashboardActivity.this, "Chuyển đến Quản lý TNV", Toast.LENGTH_SHORT).show();
            }
        });

        btnManageOrganizations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Chuyển đến màn hình quản lý tổ chức
                Toast.makeText(AdminDashboardActivity.this, "Chuyển đến Quản lý Tổ chức", Toast.LENGTH_SHORT).show();
            }
        });

        btnVerifyOrganizations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Chuyển đến màn hình xác thực tổ chức mới
                Toast.makeText(AdminDashboardActivity.this, "Chuyển đến Xác thực Tổ chức", Toast.LENGTH_SHORT).show();
            }
        });

        btnManageOpportunities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Chuyển đến màn hình kiểm duyệt cơ hội tình nguyện
                Toast.makeText(AdminDashboardActivity.this, "Chuyển đến Kiểm duyệt Cơ hội", Toast.LENGTH_SHORT).show();
            }
        });

        btnManageCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Chuyển đến màn hình quản lý kỹ năng và lĩnh vực
                Toast.makeText(AdminDashboardActivity.this, "Chuyển đến Quản lý Kỹ năng & Lĩnh vực", Toast.LENGTH_SHORT).show();
            }
        });

        btnViewReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Chuyển đến màn hình xem báo cáo thống kê
                Toast.makeText(AdminDashboardActivity.this, "Chuyển đến Xem Báo cáo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
