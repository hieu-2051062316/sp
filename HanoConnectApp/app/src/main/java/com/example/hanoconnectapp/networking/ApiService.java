package com.example.hanoconnectapp.networking;

import com.example.hanoconnectapp.models.ApplyRequest;
import com.example.hanoconnectapp.models.LoginRequest;
import com.example.hanoconnectapp.models.LoginResponse;
import com.example.hanoconnectapp.models.OpportunityCreateRequest; // Import model mới
import com.example.hanoconnectapp.models.OpportunityResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/Opportunity")
    Call<List<OpportunityResponseDto>> getOpportunities();

    @GET("api/Opportunity/{id}")
    Call<OpportunityResponseDto> getOpportunityById(@Path("id") int opportunityId);

    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/applications/apply")
    Call<Void> createApplication(@Body ApplyRequest applyRequest);

    // --- BẮT ĐẦU THÊM MỚI ---
    @POST("api/Opportunity")
    Call<OpportunityResponseDto> createOpportunity(@Body OpportunityCreateRequest opportunityRequest);
    // --- KẾT THÚC THÊM MỚI ---
}
