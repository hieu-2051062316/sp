package com.example.hanoconnectapp.networking;

import com.example.hanoconnectapp.models.ApplicantResponse;
import com.example.hanoconnectapp.models.ApplyRequest;
import com.example.hanoconnectapp.models.Cause;
import com.example.hanoconnectapp.models.LoginRequest;
import com.example.hanoconnectapp.models.LoginResponse;
import com.example.hanoconnectapp.models.OpportunityCreateRequest;
import com.example.hanoconnectapp.models.OpportunityResponseDto;
import com.example.hanoconnectapp.models.SkillDto;
import com.example.hanoconnectapp.models.UpdateApplicationStatusRequest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // APIs cho Opportunity
    @GET("api/Opportunity")
    Call<List<OpportunityResponseDto>> getOpportunities();

    @GET("api/Opportunity/{id}")
    Call<OpportunityResponseDto> getOpportunityById(@Path("id") int opportunityId);

    @POST("api/Opportunity")
    Call<OpportunityResponseDto> createOpportunity(@Body OpportunityCreateRequest opportunityRequest);

    @GET("api/Opportunity/by-organization/{organizationId}")
    Call<List<OpportunityResponseDto>> getOpportunitiesByOrganization(@Path("organizationId") int organizationId);


    // APIs cho Application
    @POST("api/applications/apply")
    Call<Void> createApplication(@Body ApplyRequest applyRequest);

    @GET("api/applications/opportunity/{opportunityId}")
    Call<List<ApplicantResponse>> getApplicantsForOpportunity(@Path("opportunityId") int opportunityId);

    @PUT("api/applications/{applicationId}/status")
    Call<ResponseBody> updateApplicationStatus(@Path("applicationId") int applicationId, @Body UpdateApplicationStatusRequest statusRequest);


    // API cho Auth
    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);


    // APIs lấy danh mục
    @GET("api/Causes")
    Call<List<Cause>> getCauses();

    @GET("api/Skills")
    Call<List<SkillDto>> getSkills();
}
