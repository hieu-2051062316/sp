package com.example.hanoconnectapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterOrganizationActivity extends AppCompatActivity {

    private TextInputEditText etOrgName;
    private TextInputEditText etOrgEmail;
    private TextInputEditText etOrgPassword;
    private TextInputEditText etOrgConfirmPassword;
    private TextInputEditText etOrgContactPerson;
    private TextInputEditText etOrgPhoneNumber;
    private TextInputEditText etOrgAddress;
    private TextInputEditText etOrgWebsite;
    private TextInputEditText etOrgDescription;
    private Button btnRegisterOrganization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_organization);

        // Ánh xạ các thành phần UI
        etOrgName = findViewById(R.id.etOrgName);
        etOrgEmail = findViewById(R.id.etOrgEmail);
        etOrgPassword = findViewById(R.id.etOrgPassword);
        etOrgConfirmPassword = findViewById(R.id.etOrgConfirmPassword);
        etOrgContactPerson = findViewById(R.id.etOrgContactPerson);
        etOrgPhoneNumber = findViewById(R.id.etOrgPhoneNumber);
        etOrgAddress = findViewById(R.id.etOrgAddress);
        etOrgWebsite = findViewById(R.id.etOrgWebsite);
        etOrgDescription = findViewById(R.id.etOrgDescription);
        btnRegisterOrganization = findViewById(R.id.btnRegisterOrganization);

        // Xử lý sự kiện nút Đăng ký
        btnRegisterOrganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerOrganization();
            }
        });
    }

    private void registerOrganization() {
        String orgName = etOrgName.getText().toString().trim();
        String orgEmail = etOrgEmail.getText().toString().trim();
        String password = etOrgPassword.getText().toString().trim();
        String confirmPassword = etOrgConfirmPassword.getText().toString().trim();
        String contactPerson = etOrgContactPerson.getText().toString().trim();
        String phoneNumber = etOrgPhoneNumber.getText().toString().trim();
        String address = etOrgAddress.getText().toString().trim();
        String website = etOrgWebsite.getText().toString().trim();
        String description = etOrgDescription.getText().toString().trim();

        if (orgName.isEmpty() || orgEmail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                contactPerson.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu và xác nhận mật khẩu không khớp.", Toast.LENGTH_LONG).show();
            return;
        }

        // TODO: Validate email format, phone number format, website format

        // TODO: Gửi dữ liệu đăng ký lên API Back-end
        // Bạn sẽ cần tạo một DTO (Data Transfer Object) để gửi dữ liệu này
        // Ví dụ: OrganizationRegistrationDto
        Log.d("RegisterOrg", "Tên tổ chức: " + orgName);
        Log.d("RegisterOrg", "Email: " + orgEmail);
        Log.d("RegisterOrg", "Người liên hệ: " + contactPerson);
        Log.d("RegisterOrg", "SĐT: " + phoneNumber);
        Log.d("RegisterOrg", "Địa chỉ: " + address);
        Log.d("RegisterOrg", "Website: " + website);
        Log.d("RegisterOrg", "Mô tả: " + description);

        Toast.makeText(this, "Đăng ký Tổ chức thành công (Demo)! Chờ Admin xác thực.", Toast.LENGTH_SHORT).show();
        // Sau khi đăng ký thành công, có thể chuyển hướng người dùng
        // finish(); // Đóng Activity hiện tại
    }
}
