package com.example.hanoconnectadmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class GuestScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.hanoconnectadmin.R.layout.activity_guest_screen);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) MaterialButton btnLogin = findViewById(com.example.hanoconnectadmin.R.id.btnLogin);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) MaterialButton btnRegister = findViewById(com.example.hanoconnectadmin.R.id.btnRegister);

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(GuestScreenActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(GuestScreenActivity.this, com.example.hanoconnectadmin.RegisterActivity.class);
            startActivity(intent);
        });
    }
}
