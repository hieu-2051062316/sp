package com.example.hanoconnectapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.adapters.NotificationAdapter;
import com.example.hanoconnectapp.models.NotificationResponse;
import com.example.hanoconnectapp.networking.ApiService;
import com.example.hanoconnectapp.networking.RetrofitClient;
import com.example.hanoconnectapp.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrgNotificationFragment extends Fragment {

    private RecyclerView rvOrgNotifications;
    private NotificationAdapter adapter;
    private List<NotificationResponse> notificationList = new ArrayList<>();
    private ProgressBar progressBar;
    private SessionManager sessionManager;
    private ApiService apiService;

    public OrgNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Tái sử dụng layout của fragment_home
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ và khởi tạo
        rvOrgNotifications = view.findViewById(R.id.rvOpportunities);
        progressBar = view.findViewById(R.id.progressBar);
        sessionManager = new SessionManager(getContext());
        apiService = RetrofitClient.getApiService();

        setupRecyclerView();
    }

    // Tải lại dữ liệu khi quay lại tab
    @Override
    public void onResume() {
        super.onResume();
        fetchNotifications();
    }

    private void setupRecyclerView() {
        // Tái sử dụng NotificationAdapter cho cả 2 luồng
        adapter = new NotificationAdapter(notificationList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvOrgNotifications.setLayoutManager(layoutManager);
        rvOrgNotifications.setAdapter(adapter);
        rvOrgNotifications.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
    }

    private void fetchNotifications() {
        progressBar.setVisibility(View.VISIBLE);
        int userId = sessionManager.getUserId(); // Lấy userId của tài khoản tổ chức
        if (userId == -1) {
            Toast.makeText(getContext(), "Lỗi phiên đăng nhập.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        apiService.getNotificationsForUser(userId).enqueue(new Callback<List<NotificationResponse>>() {
            @Override
            public void onResponse(Call<List<NotificationResponse>> call, Response<List<NotificationResponse>> response) {
                progressBar.setVisibility(View.GONE);
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    notificationList.clear();
                    notificationList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    if(notificationList.isEmpty()){
                        Toast.makeText(getContext(), "Bạn chưa có thông báo nào.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<NotificationResponse>> call, Throwable t) {
                if(isAdded()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Lỗi tải thông báo.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
