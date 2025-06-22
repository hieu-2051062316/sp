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
import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.adapters.NotificationAdapter;
import com.example.hanoconnectapp.models.NotificationItem;
import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView rvNotifications;
    private NotificationAdapter notificationAdapter;
    private List<NotificationItem> notificationList = new ArrayList<>();

    public NotificationFragment() {
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

        rvNotifications = view.findViewById(R.id.rvOpportunities);
        view.findViewById(R.id.progressBar).setVisibility(View.GONE); // Ẩn progress bar đi

        setupRecyclerView();
        loadDummyData();
    }

    private void setupRecyclerView() {
        notificationAdapter = new NotificationAdapter(notificationList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvNotifications.setLayoutManager(layoutManager);
        rvNotifications.setAdapter(notificationAdapter);
        rvNotifications.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
    }

    private void loadDummyData() {
        notificationList.clear();
        notificationList.add(new NotificationItem("Mùa Hè Xanh 2025", "Bạn đã nhận được lịch hẹn phỏng vấn, ấn để xem thêm...", R.drawable.logo_hanoconnect, true));
        notificationList.add(new NotificationItem("Tên chiến dịch 2", "Thông báo mẫu cho phần này.", R.drawable.logo_hanoconnect, false));
        notificationList.add(new NotificationItem("Mùa Hè Xanh 2025", "Đơn của bạn đang được xem xét.", R.drawable.logo_hanoconnect, true));
        notificationList.add(new NotificationItem("Chiến dịch ví dụ 2", "Thông báo của chiến dịch có thể như thế này", R.drawable.logo_hanoconnect, false));
        notificationList.add(new NotificationItem("Mùa Hè Xanh 2025", "Cảm ơn bạn đã quan tâm chiến dịch của chúng tôi.", R.drawable.logo_hanoconnect, false));

        notificationAdapter.notifyDataSetChanged();
    }
}