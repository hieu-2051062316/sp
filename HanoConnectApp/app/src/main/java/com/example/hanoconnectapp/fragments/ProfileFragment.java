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

import com.example.hanoconnectapp.EditProfileActivity;
import com.example.hanoconnectapp.GuestScreenActivity;
import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.models.VolunteerProfileResponse;
import com.example.hanoconnectapp.networking.ApiService;
import com.example.hanoconnectapp.networking.RetrofitClient;
import com.example.hanoconnectapp.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private TextView tvProfileName, tvUpdateInfo, tvLogout, tvEmail, tvSkills, tvCauses;
    private SessionManager sessionManager;
    private ApiService apiService;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo
        sessionManager = new SessionManager(getContext());
        apiService = RetrofitClient.getApiService();

        // Ánh xạ views
        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvSkills = view.findViewById(R.id.tvSkills);
        tvCauses = view.findViewById(R.id.tvCauses);
        tvUpdateInfo = view.findViewById(R.id.tvUpdateInfo);
        tvLogout = view.findViewById(R.id.tvLogout);

        // Thiết lập sự kiện
        tvUpdateInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        tvLogout.setOnClickListener(v -> handleLogout());
    }

    // Tải lại dữ liệu khi quay lại màn hình này
    @Override
    public void onResume() {
        super.onResume();
        fetchProfile();
    }

    private void fetchProfile() {
        int userId = sessionManager.getUserId();
        if (userId == -1) {
            Toast.makeText(getContext(), "Lỗi phiên đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.getVolunteerProfile(userId).enqueue(new Callback<VolunteerProfileResponse>() {
            @Override
            public void onResponse(Call<VolunteerProfileResponse> call, Response<VolunteerProfileResponse> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    VolunteerProfileResponse profile = response.body();
                    tvProfileName.setText(profile.getFullName());
                    tvEmail.setText(profile.getEmail());
                    tvSkills.setText(String.join(", ", profile.getSkills()));
                    tvCauses.setText(String.join(", ", profile.getCauses()));
                }
            }

            @Override
            public void onFailure(Call<VolunteerProfileResponse> call, Throwable t) {
                if(isAdded()) {
                    Toast.makeText(getContext(), "Lỗi tải thông tin cá nhân", Toast.LENGTH_SHORT).show();
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
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
