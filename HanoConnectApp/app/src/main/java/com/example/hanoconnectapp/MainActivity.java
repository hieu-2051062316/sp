package com.example.hanoconnectapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoconnectapp.models.OpportunityResponseDto;
import com.example.hanoconnectapp.networking.ApiService;
import com.example.hanoconnectapp.networking.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button btnFetchData;
    private TextView tvApiResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFetchData = findViewById(R.id.btnFetchData);
        tvApiResult = findViewById(R.id.tvApiResult);

        btnFetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchOpportunities();
            }
        });
    }

    private void fetchOpportunities() {
        tvApiResult.setText("Đang tải dữ liệu...");
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<OpportunityResponseDto>> call = apiService.getOpportunities();

        call.enqueue(new Callback<List<OpportunityResponseDto>>() {
            @Override
            public void onResponse(Call<List<OpportunityResponseDto>> call, Response<List<OpportunityResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OpportunityResponseDto> opportunities = response.body();
                    tvApiResult.setText("Thành công! Lấy được " + opportunities.size() + " cơ hội.");
                    Log.d("API_SUCCESS", "Opportunity đầu tiên: " + opportunities.get(0).getTitle());
                } else {
                    tvApiResult.setText("Lỗi: " + response.code());
                    Log.e("API_ERROR", "Lỗi khi gọi API: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<OpportunityResponseDto>> call, Throwable t) {
                tvApiResult.setText("Thất bại: " + t.getMessage());
                Log.e("API_FAILURE", "Toàn bộ request thất bại.", t);
            }
        });
    }
}
