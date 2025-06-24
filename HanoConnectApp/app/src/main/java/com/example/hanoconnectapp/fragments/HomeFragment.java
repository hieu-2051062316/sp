package com.example.hanoconnectapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
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
    private TextView tvNoResults;
    private SearchView searchView;
    private OpportunityAdapter opportunityAdapter;
    private List<OpportunityResponseDto> opportunityList = new ArrayList<>();
    private ApiService apiService;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo và ánh xạ
        apiService = RetrofitClient.getApiService();
        rvOpportunities = view.findViewById(R.id.rvOpportunities);
        progressBar = view.findViewById(R.id.progressBar);
        tvNoResults = view.findViewById(R.id.tvNoResults);
        searchView = view.findViewById(R.id.searchView);

        setupRecyclerView();
        setupSearchView();

        // Tải tất cả cơ hội khi fragment được tạo lần đầu
        performSearch(null);
    }

    private void setupRecyclerView() {
        opportunityAdapter = new OpportunityAdapter(getContext(), opportunityList);
        rvOpportunities.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOpportunities.setAdapter(opportunityAdapter);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Người dùng nhấn nút tìm kiếm trên bàn phím
                performSearch(query);
                searchView.clearFocus(); // Ẩn bàn phím
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Khi người dùng xóa hết chữ, tải lại toàn bộ danh sách
                if (newText.isEmpty()) {
                    performSearch(null);
                }
                return false;
            }
        });
    }

    private void performSearch(String keyword) {
        progressBar.setVisibility(View.VISIBLE);
        tvNoResults.setVisibility(View.GONE);
        rvOpportunities.setVisibility(View.GONE);

        Call<List<OpportunityResponseDto>> call;

        // Kiểm tra xem có từ khóa không để gọi API tương ứng
        if (keyword != null && !keyword.trim().isEmpty()) {
            call = apiService.searchOpportunities(keyword);
        } else {
            call = apiService.getOpportunities();
        }

        call.enqueue(new Callback<List<OpportunityResponseDto>>() {
            @Override
            public void onResponse(Call<List<OpportunityResponseDto>> call, Response<List<OpportunityResponseDto>> response) {
                if (!isAdded()) return; // Đảm bảo fragment còn tồn tại

                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    opportunityList.clear();
                    opportunityList.addAll(response.body());
                    opportunityAdapter.notifyDataSetChanged();

                    // Kiểm tra và hiển thị thông báo nếu không có kết quả
                    if (opportunityList.isEmpty()) {
                        tvNoResults.setVisibility(View.VISIBLE);
                        rvOpportunities.setVisibility(View.GONE);
                    } else {
                        tvNoResults.setVisibility(View.GONE);
                        rvOpportunities.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OpportunityResponseDto>> call, Throwable t) {
                if (!isAdded()) return;

                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                Log.e("HomeFragment", "API call failed: ", t);
            }
        });
    }
}
