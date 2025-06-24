package com.example.hanoconnectapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoconnectapp.models.RegisterRequest;
import com.example.hanoconnectapp.networking.ApiService;
import com.example.hanoconnectapp.networking.RetrofitClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private String userRole;
    private EditText etEmail, etPassword, etFullName, etPhoneNumber, etDistrict;
    private EditText etOrgName, etAddress, etWebsite, etDescription;
    private LinearLayout layoutVolunteerFields, layoutOrgFields;
    private TextView labelFullName;
    private MaterialButton btnRegister;
    private ProgressBar progressBar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userRole = getIntent().getStringExtra("USER_ROLE");
        apiService = RetrofitClient.getApiService();

        setupViews();
        customizeUIForRole();
    }

    private void setupViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Ánh xạ các views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        labelFullName = findViewById(R.id.labelFullName);
        etFullName = findViewById(R.id.etFullName);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBarRegister);

        layoutVolunteerFields = findViewById(R.id.layoutVolunteerFields);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etDistrict = findViewById(R.id.etDistrict);

        layoutOrgFields = findViewById(R.id.layoutOrgFields);
        etOrgName = findViewById(R.id.etOrgName);
        etAddress = findViewById(R.id.etAddress);
        etWebsite = findViewById(R.id.etWebsite);
        etDescription = findViewById(R.id.etDescription);

        btnRegister.setOnClickListener(v -> handleRegistration());
    }

    private void customizeUIForRole() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if ("Organization".equalsIgnoreCase(userRole)) {
            toolbar.setTitle("Đăng ký Tổ chức");
            layoutOrgFields.setVisibility(View.VISIBLE);
            layoutVolunteerFields.setVisibility(View.GONE);
            labelFullName.setText("Tên người liên hệ (*)");
            etFullName.setHint("VD: Nguyễn Văn A");
        } else {
            toolbar.setTitle("Đăng ký Tình nguyện viên");
            layoutOrgFields.setVisibility(View.GONE);
            layoutVolunteerFields.setVisibility(View.VISIBLE);
            labelFullName.setText("Họ và Tên (*)");
            etFullName.setHint("VD: Nguyễn Văn A");
        }
    }

    private void handleRegistration() {
        if (!isFormValid()) {
            return;
        }

        showLoading(true);

        // Lấy dữ liệu từ form
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhoneNumber.getText().toString().trim();
        String district = etDistrict.getText().toString().trim();
        String orgName = etOrgName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String website = etWebsite.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // Tạo request
        RegisterRequest request = new RegisterRequest(email, password, userRole, fullName, phone, district, orgName, address, website, description);

        apiService.register(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_LONG).show();

                    // Chuyển thẳng về màn hình Login và xóa các màn hình đăng ký khỏi backstack
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMessage = "Đăng ký thất bại.";
                    if (response.errorBody() != null) {
                        try {
                            String errorJson = response.errorBody().string();
                            JsonObject jsonObject = new Gson().fromJson(errorJson, JsonObject.class);
                            if (jsonObject.has("message")) {
                                errorMessage = jsonObject.get("message").getAsString();
                            }
                        } catch (Exception e) {}
                    }
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showLoading(false);
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isFormValid() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String orgName = etOrgName.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            return false;
        }
        if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return false;
        }
        if (fullName.isEmpty()) {
            etFullName.setError("Trường này là bắt buộc");
            return false;
        }
        if ("Organization".equalsIgnoreCase(userRole) && orgName.isEmpty()) {
            etOrgName.setError("Tên tổ chức là bắt buộc");
            return false;
        }
        return true;
    }

    private void showLoading(boolean isLoading) {
        if(isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnRegister.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnRegister.setEnabled(true);
        }
    }
}
