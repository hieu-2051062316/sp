package com.example.hanoconnectadmin;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoconnectadmin.models.RegisterRequest;
import com.example.hanoconnectadmin.networking.ApiService;
import com.example.hanoconnectadmin.networking.RetrofitClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private String userRole;
    private EditText etOrgName, etFullName, etEmail, etPassword;
    private LinearLayout layoutOrgName;
    private TextView labelFullName;
    private MaterialButton btnRegister;
    private ProgressBar progressBar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userRole = getIntent().getStringExtra("USER_ROLE");
        apiService = RetrofitClient.getApiService(this);

        // Ánh xạ views
        setupViews();

        // Tùy chỉnh giao diện dựa trên vai trò
        customizeUIForRole();
    }

    private void setupViews() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        layoutOrgName = findViewById(R.id.layoutOrgName);
        etOrgName = findViewById(R.id.etOrgName);
        labelFullName = findViewById(R.id.labelFullName);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBarRegister);

        btnRegister.setOnClickListener(v -> handleRegistration());
    }

    private void customizeUIForRole() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if ("Organization".equalsIgnoreCase(userRole)) {
            toolbar.setTitle("Đăng ký Tổ chức");
            layoutOrgName.setVisibility(View.VISIBLE);
            labelFullName.setText("Tên người liên hệ (*)");
        } else {
            toolbar.setTitle("Đăng ký Tình nguyện viên");
            layoutOrgName.setVisibility(View.GONE);
            labelFullName.setText("Họ và Tên (*)");
        }
    }

    private void handleRegistration() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String orgName = etOrgName.getText().toString().trim();

        // Validation
        if (!isFormValid(email, password, fullName, orgName)) {
            return;
        }

        showLoading(true);

        RegisterRequest request = new RegisterRequest(email, password, userRole, fullName, orgName);
        apiService.register(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_LONG).show();
                    finish(); // Quay lại màn hình chọn vai trò, hoặc có thể chuyển thẳng đến Login
                } else {
                    // Cố gắng đọc lỗi từ body
                    String errorMessage = "Đăng ký thất bại. Vui lòng thử lại.";
                    if (response.errorBody() != null) {
                        try {
                            // Đây là cách đơn giản, trong thực tế cần parse JSON
                            errorMessage = response.errorBody().string();
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

    private boolean isFormValid(String email, String password, String fullName, String orgName) {
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
