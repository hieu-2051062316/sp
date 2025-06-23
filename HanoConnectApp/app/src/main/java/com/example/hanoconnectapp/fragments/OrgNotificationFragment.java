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
import com.example.hanoconnectapp.adapters.OrgNotificationAdapter;
import com.example.hanoconnectapp.models.NotificationItem;
import java.util.ArrayList;
import java.util.List;

public class OrgNotificationFragment extends Fragment {

    private RecyclerView rvOrgNotifications;
    private OrgNotificationAdapter adapter;
    private List<NotificationItem> notificationList = new ArrayList<>();

    public OrgNotificationFragment() {
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

        rvOrgNotifications = view.findViewById(R.id.rvOpportunities);
        view.findViewById(R.id.progressBar).setVisibility(View.GONE);

        setupRecyclerView();
        loadDummyData();
    }

    private void setupRecyclerView() {
        adapter = new OrgNotificationAdapter(notificationList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvOrgNotifications.setLayoutManager(layoutManager);
        rvOrgNotifications.setAdapter(adapter);
        rvOrgNotifications.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
    }

    private void loadDummyData() {
        notificationList.clear();
        notificationList.add(new NotificationItem("Mùa Hè Xanh 2025", "Chiến dịch của bạn đã đóng.", R.drawable.logo_hanoconnect, true));
        notificationList.add(new NotificationItem("Mùa Hè Xanh 2025", "Chiến dịch của bạn đã nhận được 123 đơn ứng tuyển.", R.drawable.logo_hanoconnect, true));
        notificationList.add(new NotificationItem("Chiến dịch ví dụ 2", "Thông báo của chiến dịch có thể như thế này", R.drawable.logo_hanoconnect, false));
        notificationList.add(new NotificationItem("Mùa Hè Xanh 2025", "Chiến dịch của bạn đã được đăng tải.", R.drawable.logo_hanoconnect, false));

        adapter.notifyDataSetChanged();
    }
}
