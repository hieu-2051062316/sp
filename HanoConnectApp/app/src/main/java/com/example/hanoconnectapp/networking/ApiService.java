package com.example.hanoconnectapp.networking;

import com.example.hanoconnectapp.models.ApplicantResponse;
import com.example.hanoconnectapp.models.ApplyRequest;
import com.example.hanoconnectapp.models.Cause;
import com.example.hanoconnectapp.models.LoginRequest;
import com.example.hanoconnectapp.models.LoginResponse;
import com.example.hanoconnectapp.models.MyApplicationResponse;
import com.example.hanoconnectapp.models.OpportunityCreateRequest;
import com.example.hanoconnectapp.models.OpportunityResponseDto;
import com.example.hanoconnectapp.models.OrganizationProfileResponse;
import com.example.hanoconnectapp.models.RegisterRequest;
import com.example.hanoconnectapp.models.SkillDto;
import com.example.hanoconnectapp.models.UpdateApplicationStatusRequest;
import com.example.hanoconnectapp.models.VolunteerProfileResponse;
import com.example.hanoconnectapp.models.VolunteerProfileUpdateRequest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("api/opportunity/search")
    Call<List<OpportunityResponseDto>> searchOpportunities(@Query("keyword") String keyword);


    // APIs cho Application
    @POST("api/applications/apply")
    Call<Void> createApplication(@Body ApplyRequest applyRequest);

    @GET("api/applications/opportunity/{opportunityId}")
    Call<List<ApplicantResponse>> getApplicantsForOpportunity(@Path("opportunityId") int opportunityId);

    @PUT("api/applications/{applicationId}/status")
    Call<ResponseBody> updateApplicationStatus(@Path("applicationId") int applicationId, @Body UpdateApplicationStatusRequest statusRequest);

    @GET("api/applications/my-applications/{volunteerUserId}")
    Call<List<MyApplicationResponse>> getMyApplications(@Path("volunteerUserId") int volunteerUserId);


    // API cho Auth
    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/Auth/register")
    Call<ResponseBody> register(@Body RegisterRequest registerRequest);


    // APIs lấy danh mục (Causes, Skills)
    @GET("api/Causes")
    Call<List<Cause>> getCauses();

    @GET("api/Skills")
    Call<List<SkillDto>> getSkills();


    // API lấy và cập nhật thông tin Profile
    @GET("api/users/{userId}/profile")
    Call<VolunteerProfileResponse> getVolunteerProfile(@Path("userId") int userId);

    @PUT("api/users/{userId}/profile")
    Call<ResponseBody> updateVolunteerProfile(@Path("userId") int userId, @Body VolunteerProfileUpdateRequest request);

    @GET("api/organizations/{orgId}/profile")
    Call<OrganizationProfileResponse> getOrganizationProfile(@Path("orgId") int orgId);
}
