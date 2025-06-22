package com.example.hanoconnectapp.networking;

import com.example.hanoconnectapp.models.LoginRequest;
import com.example.hanoconnectapp.models.LoginResponse;
import com.example.hanoconnectapp.models.OpportunityResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("api/Opportunity")
    Call<List<OpportunityResponseDto>> getOpportunities();

    // Endpoint mới cho việc đăng nhập
    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}