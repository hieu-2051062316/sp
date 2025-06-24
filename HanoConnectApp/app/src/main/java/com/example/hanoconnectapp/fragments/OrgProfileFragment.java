package com.example.hanoconnectapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanoconnectapp.GuestScreenActivity;
import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.models.OrganizationProfileResponse;
import com.example.hanoconnectapp.networking.ApiService;
import com.example.hanoconnectapp.networking.RetrofitClient;
import com.example.hanoconnectapp.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrgProfileFragment extends Fragment {

    private TextView tvOrgProfileName, tvUpdateOrgInfo, tvLogout;
    private SessionManager sessionManager;
    private ApiService apiService;

    public OrgProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_org_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo
        sessionManager = new SessionManager(getContext());
        apiService = RetrofitClient.getApiService();

        // Ánh xạ views
        tvOrgProfileName = view.findViewById(R.id.tvOrgProfileName);
        tvUpdateOrgInfo = view.findViewById(R.id.tvUpdateOrgInfo);
        tvLogout = view.findViewById(R.id.tvLogout);

        // Thiết lập sự kiện
        tvLogout.setOnClickListener(v -> handleLogout());
        tvUpdateOrgInfo.setOnClickListener(v -> Toast.makeText(getContext(), "Chức năng đang phát triển", Toast.LENGTH_SHORT).show());

        // Tải dữ liệu profile
        fetchProfile();
    }

    private void fetchProfile() {
        int orgId = sessionManager.getOrganizationId();
        if (orgId == -1) {
            Toast.makeText(getContext(), "Lỗi phiên đăng nhập tổ chức", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.getOrganizationProfile(orgId).enqueue(new Callback<OrganizationProfileResponse>() {
            @Override
            public void onResponse(Call<OrganizationProfileResponse> call, Response<OrganizationProfileResponse> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    OrganizationProfileResponse profile = response.body();
                    tvOrgProfileName.setText(profile.getOrganizationName());
                    // Cập nhật các thông tin khác nếu có TextView tương ứng
                }
            }

            @Override
            public void onFailure(Call<OrganizationProfileResponse> call, Throwable t) {
                if(isAdded()) {
                    Toast.makeText(getContext(), "Lỗi tải thông tin tổ chức", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleLogout() {
        sessionManager.logoutUser();
        // Chuyển về màn hình đăng nhập và xóa các activity cũ
        Intent intent = new Intent(getActivity(), GuestScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}
