package com.example.hanoconnectapp.networking;

import com.example.hanoconnectapp.models.OpportunityResponseDto;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/Opportunity")
    Call<List<OpportunityResponseDto>> getOpportunities();

    // Bạn có thể thêm các endpoint khác ở đây sau
    // Ví dụ: @GET("api/Opportunity/{id}")
    // Call<OpportunityResponseDto> getOpportunityById(@Path("id") int id);
}
