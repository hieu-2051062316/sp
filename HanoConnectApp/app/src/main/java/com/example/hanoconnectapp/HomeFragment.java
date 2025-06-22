package com.example.hanoconnectapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.example.hanoconnectapp.adapters.OpportunityAdapter;
import com.example.hanoconnectapp.models.OpportunityResponseDto;
import java.util.ArrayList;
import java.util.List;

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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvOpportunities = view.findViewById(R.id.rvOpportunities);
        progressBar = view.findViewById(R.id.progressBar);

        setupRecyclerView();
        loadDummyData(); // <--- THAY ĐỔI QUAN TRỌNG: Gọi hàm tải dữ liệu giả
    }

    private void setupRecyclerView() {
        opportunityAdapter = new OpportunityAdapter(opportunityList);
        rvOpportunities.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOpportunities.setAdapter(opportunityAdapter);
    }

    // HÀM MỚI: TẠO VÀ HIỂN THỊ DỮ LIỆU GIẢ
    private void loadDummyData() {
        progressBar.setVisibility(View.GONE); // Ẩn progress bar
        rvOpportunities.setVisibility(View.VISIBLE); // Hiện RecyclerView

        opportunityList.clear(); // Xóa dữ liệu cũ nếu có

        // Tạo một vài cơ hội giả
        opportunityList.add(new OpportunityResponseDto(
                "Giới thiệu chiến dịch tình nguyện Hà Nội của tôi:",
                "Hà Nội Của Tôi",
                "Chiến dịch tình nguyện Hà Nội của tôi là hành trình kết nối những trái tim nhiệt huyết vì cộng đồng..."
        ));
        opportunityList.add(new OpportunityResponseDto(
                "Mùa Hè Xanh 2025 - Lên đường cống hiến!",
                "Mùa Hè Xanh 2025",
                "Những bước chân tình nguyện lại lên đường, mang theo nhiệt huyết tuổi trẻ đến với các vùng quê khó khăn..."
        ));
        opportunityList.add(new OpportunityResponseDto(
                "Dạy học cho trẻ em vùng cao",
                "Quỹ Ước Mơ",
                "Chương trình mang kiến thức và niềm vui đến cho các em nhỏ tại các điểm trường khó khăn nhất."
        ));

        opportunityAdapter.notifyDataSetChanged(); // Báo cho Adapter cập nhật lại giao diện
    }

    /*
    // HÀM GỌI API THẬT - TẠM THỜI ĐƯỢC CHÚ THÍCH (COMMENT OUT)
    private void fetchOpportunities() {
        progressBar.setVisibility(View.VISIBLE);
        rvOpportunities.setVisibility(View.GONE);

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<OpportunityResponseDto>> call = apiService.getOpportunities();

        call.enqueue(new Callback<List<OpportunityResponseDto>>() {
            @Override
            public void onResponse(Call<List<OpportunityResponseDto>> call, Response<List<OpportunityResponseDto>> response) {
                if (isAdded()) {
                    progressBar.setVisibility(View.GONE);
                    rvOpportunities.setVisibility(View.VISIBLE);

                    if (response.isSuccessful() && response.body() != null) {
                        opportunityList.clear();
                        opportunityList.addAll(response.body());
                        opportunityAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<OpportunityResponseDto>> call, Throwable t) {
                if (isAdded()) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
    */
}
