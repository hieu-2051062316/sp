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
import com.example.hanoconnectapp.adapters.FollowedAdapter;
import com.example.hanoconnectapp.models.FollowedOpportunity;
import java.util.ArrayList;
import java.util.List;

public class FollowedFragment extends Fragment {

    private RecyclerView rvFollowed;
    private FollowedAdapter followedAdapter;
    private List<FollowedOpportunity> followedList = new ArrayList<>();

    public FollowedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout, chúng ta có thể dùng lại layout của HomeFragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFollowed = view.findViewById(R.id.rvOpportunities); // Dùng lại ID từ fragment_home

        setupRecyclerView();
        loadDummyData();
    }

    private void setupRecyclerView() {
        followedAdapter = new FollowedAdapter(followedList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFollowed.setLayoutManager(layoutManager);
        rvFollowed.setAdapter(followedAdapter);
        // Thêm đường kẻ ngang giữa các item
        rvFollowed.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
    }

    private void loadDummyData() {
        followedList.clear();
        followedList.add(new FollowedOpportunity("Mùa Hè Xanh 2025", "Có lịch phỏng vấn", R.drawable.logo_hanoconnect));
        followedList.add(new FollowedOpportunity("Hà Nội Của Tôi", "Đang chờ xét duyệt", R.drawable.logo_hanoconnect));
        followedList.add(new FollowedOpportunity("Dọn dẹp Hồ Gươm", "Đã ứng tuyển", R.drawable.logo_hanoconnect));
        followedList.add(new FollowedOpportunity("Trung thu cho em", "Đã từ chối", R.drawable.logo_hanoconnect));

        followedAdapter.notifyDataSetChanged();
    }
}
