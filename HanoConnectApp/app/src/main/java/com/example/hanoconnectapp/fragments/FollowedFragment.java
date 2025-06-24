package com.example.hanoconnectapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.adapters.FollowedAdapter;
import com.example.hanoconnectapp.models.MyApplicationResponse;
import com.example.hanoconnectapp.networking.ApiService;
import com.example.hanoconnectapp.networking.RetrofitClient;
import com.example.hanoconnectapp.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowedFragment extends Fragment {

    private RecyclerView rvFollowed;
    private FollowedAdapter followedAdapter;
    private List<MyApplicationResponse> followedList = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView tvNoResults;
    private SessionManager sessionManager;
    private ApiService apiService;

    public FollowedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Dùng lại layout của fragment_home vì cấu trúc tương tự
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFollowed = view.findViewById(R.id.rvOpportunities);
        progressBar = view.findViewById(R.id.progressBar);
        tvNoResults = view.findViewById(R.id.tvNoResults); // Ánh xạ view
        sessionManager = new SessionManager(getContext());
        apiService = RetrofitClient.getApiService();

        tvNoResults.setText("Bạn chưa ứng tuyển vào cơ hội nào"); // Tùy chỉnh text
        setupRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchMyApplications();
    }

    private void setupRecyclerView() {
        followedAdapter = new FollowedAdapter(getContext(), followedList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFollowed.setLayoutManager(layoutManager);
        rvFollowed.setAdapter(followedAdapter);
        rvFollowed.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
    }

    private void fetchMyApplications() {
        progressBar.setVisibility(View.VISIBLE);
        rvFollowed.setVisibility(View.GONE);
        tvNoResults.setVisibility(View.GONE);

        int volunteerId = sessionManager.getUserId();

        if (volunteerId == -1) {
            Toast.makeText(getContext(), "Lỗi xác thực người dùng.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        apiService.getMyApplications(volunteerId).enqueue(new Callback<List<MyApplicationResponse>>() {
            @Override
            public void onResponse(Call<List<MyApplicationResponse>> call, Response<List<MyApplicationResponse>> response) {
                progressBar.setVisibility(View.GONE);
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    followedList.clear();
                    followedList.addAll(response.body());
                    followedAdapter.notifyDataSetChanged();

                    if(followedList.isEmpty()) {
                        tvNoResults.setVisibility(View.VISIBLE);
                    } else {
                        rvFollowed.setVisibility(View.VISIBLE);
                    }
                } else {
                    if(isAdded()) Toast.makeText(getContext(), "Không thể tải danh sách theo dõi.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MyApplicationResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                if(isAdded()) {
                    Toast.makeText(getContext(), "Lỗi kết nối.", Toast.LENGTH_SHORT).show();
                    Log.e("FollowedFragment", "API call failed: ", t);
                }
            }
        });
    }
}
