package com.example.hanoconnectadmin;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ChooseRoleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);

        MaterialButton btnVolunteer = findViewById(R.id.btnVolunteer);
        MaterialButton btnOrganization = findViewById(R.id.btnOrganization);

        btnVolunteer.setOnClickListener(v -> {
            startRegisterActivity("Volunteer");
        });

        btnOrganization.setOnClickListener(v -> {
            startRegisterActivity("Organization");
        });
    }

    private void startRegisterActivity(String role) {
        Intent intent = new Intent(ChooseRoleActivity.this, com.example.hanoconnectadmin.RegisterActivity.class);
        intent.putExtra("USER_ROLE", role);
        startActivity(intent);
    }
}
