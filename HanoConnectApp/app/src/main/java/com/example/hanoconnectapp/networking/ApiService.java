package com.example.hanoconnectapp.networking;

import com.example.hanoconnectapp.models.OpportunityResponseDto;
<<<<<<< Updated upstream
=======
import com.example.hanoconnectapp.models.OrganizationProfileResponse;
import com.example.hanoconnectapp.models.RegisterRequest;
import com.example.hanoconnectapp.models.SkillDto;
import com.example.hanoconnectapp.models.UpdateApplicationStatusRequest;
import com.example.hanoconnectapp.models.VolunteerProfileResponse;
>>>>>>> Stashed changes

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/Opportunity")
    Call<List<OpportunityResponseDto>> getOpportunities();
<<<<<<< Updated upstream
=======

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


    // API lấy thông tin Profile
    @GET("api/users/{userId}/profile")
    Call<VolunteerProfileResponse> getVolunteerProfile(@Path("userId") int userId);

    @GET("api/organizations/{orgId}/profile")
    Call<OrganizationProfileResponse> getOrganizationProfile(@Path("orgId") int orgId);
>>>>>>> Stashed changes
}
