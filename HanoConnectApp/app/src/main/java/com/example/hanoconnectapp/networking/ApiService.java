package com.example.hanoconnectapp.networking;

import com.example.hanoconnectapp.models.ApplyRequest;
import com.example.hanoconnectapp.models.Cause;
import com.example.hanoconnectapp.models.LoginRequest;
import com.example.hanoconnectapp.models.LoginResponse;
import com.example.hanoconnectapp.models.OpportunityCreateRequest;
import com.example.hanoconnectapp.models.OpportunityResponseDto;
import com.example.hanoconnectapp.models.SkillDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // Các API liên quan đến Opportunity
    @GET("api/Opportunity")
    Call<List<OpportunityResponseDto>> getOpportunities();

    @GET("api/Opportunity/{id}")
    Call<OpportunityResponseDto> getOpportunityById(@Path("id") int opportunityId);

    @POST("api/Opportunity")
    Call<OpportunityResponseDto> createOpportunity(@Body OpportunityCreateRequest opportunityRequest);


    // API xác thực
    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);


    // API ứng tuyển
    @POST("api/applications/apply")
    Call<Void> createApplication(@Body ApplyRequest applyRequest);


    // API lấy các danh mục
    @GET("api/Causes")
    Call<List<Cause>> getCauses();

    @GET("api/Skills")
    Call<List<SkillDto>> getSkills();
}
