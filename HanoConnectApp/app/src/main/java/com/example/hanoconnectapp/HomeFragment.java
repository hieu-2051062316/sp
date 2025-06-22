package com.example.hanoconnectapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.example.hanoconnectapp.adapters.OpportunityAdapter;
import com.example.hanoconnectapp.models.OpportunityResponseDto;
import com.example.hanoconnectapp.networking.ApiService;
import com.example.hanoconnectapp.networking.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView rvOpportunities;
    private ProgressBar progressBar;
    private OpportunityAdapter opportunityAdapter;
    private List<OpportunityResponseDto> opportunityList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvOpportunities = view.findViewById(R.id.rvOpportunities);
        progressBar = view.findViewById(R.id.progressBar);

        setupRecyclerView();
        fetchOpportunities();
    }

    private void setupRecyclerView() {
        opportunityAdapter = new OpportunityAdapter(opportunityList);
        rvOpportunities.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOpportunities.setAdapter(opportunityAdapter);
    }

    private void fetchOpportunities() {
        progressBar.setVisibility(View.VISIBLE);
        rvOpportunities.setVisibility(View.GONE);

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<OpportunityResponseDto>> call = apiService.getOpportunities();

        call.enqueue(new Callback<List<OpportunityResponseDto>>() {
            @Override
            public void onResponse(Call<List<OpportunityResponseDto>> call, Response<List<OpportunityResponseDto>> response) {
                if (isAdded()) { // Kiểm tra xem Fragment còn được gắn vào Activity không
                    progressBar.setVisibility(View.GONE);
                    rvOpportunities.setVisibility(View.VISIBLE);

                    if (response.isSuccessful() && response.body() != null) {
                        opportunityList.clear();
                        opportunityList.addAll(response.body());
                        opportunityAdapter.notifyDataSetChanged();
                        Log.d("API_SUCCESS", "Data loaded into HomeFragment's RecyclerView.");
                    } else {
                        Log.e("API_ERROR", "API call failed with code: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<OpportunityResponseDto>> call, Throwable t) {
                if (isAdded()) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("API_FAILURE", "API request failed.", t);
                }
            }
        });
    }
}