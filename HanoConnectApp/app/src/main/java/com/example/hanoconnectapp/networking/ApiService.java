package com.example.hanoconnectapp.networking;

import com.example.hanoconnectapp.models.OpportunityResponseDto;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/Opportunity")
    Call<List<OpportunityResponseDto>> getOpportunities();
}
