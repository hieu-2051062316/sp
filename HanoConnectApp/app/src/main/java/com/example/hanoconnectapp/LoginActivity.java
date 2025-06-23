package com.example.hanoconnectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoconnectapp.models.LoginRequest;
import com.example.hanoconnectapp.models.LoginResponse;
import com.example.hanoconnectapp.networking.ApiService;
import com.example.hanoconnectapp.networking.RetrofitClient;
import com.example.hanoconnectapp.util.SessionManager; // Import lớp SessionManager
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private ProgressBar progressBar;
    private SessionManager sessionManager; // Khai báo session manager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        // progressBar = findViewById(R.id.progressBar_login);

        sessionManager = new SessionManager(getApplicationContext()); // Khởi tạo session manager

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            performLogin(email, password);
        });
    }

    private void performLogin(String email, String password) {
        // btnLogin.setEnabled(false);
        // progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = RetrofitClient.getApiService();
        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<LoginResponse> call = apiService.login(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                // btnLogin.setEnabled(true);
                // progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    LoginResponse loginResponse = response.body();

                    // --- BẮT ĐẦU THAY ĐỔI ---
                    // Lưu thông tin phiên đăng nhập
                    sessionManager.createLoginSession(loginResponse.getUserId());
                    // --- KẾT THÚC THAY ĐỔI ---


                    // Chuyển sang MainActivity và "gửi kèm" vai trò của người dùng
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USER_ROLE", loginResponse.getRole());
                    startActivity(intent);
                    finish(); // Đóng LoginActivity để người dùng không quay lại được
                } else {
                    Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // btnLogin.setEnabled(true);
                // progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
