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
        // Chúng ta có thể dùng lại layout của fragment_home vì nó cũng chỉ chứa 1 RecyclerView
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Chú ý: Dùng lại ID của RecyclerView từ fragment_home
        rvFollowed = view.findViewById(R.id.rvOpportunities);
        // Ẩn ProgressBar vì chúng ta không tải dữ liệu từ mạng ở bước này
        view.findViewById(R.id.progressBar).setVisibility(View.GONE);

        setupRecyclerView();
        loadDummyData();
    }

    private void setupRecyclerView() {
        followedAdapter = new FollowedAdapter(followedList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFollowed.setLayoutManager(layoutManager);
        rvFollowed.setAdapter(followedAdapter);
        // Thêm đường kẻ ngang giữa các item để dễ phân biệt
        rvFollowed.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
    }

    private void loadDummyData() {
        followedList.clear();
        followedList.add(new FollowedOpportunity("Mùa Hè Xanh 2025", "Có lịch phỏng vấn", R.drawable.logo_hanoconnect));
        followedList.add(new FollowedOpportunity("Hà Nội Của Tôi", "Đang chờ xét duyệt", R.drawable.logo_hanoconnect));
        followedList.add(new FollowedOpportunity("Dọn dẹp Hồ Gươm", "Đã ứng tuyển", R.drawable.logo_hanoconnect));
        followedList.add(new FollowedOpportunity("Trung thu cho em", "Đã từ chối", R.drawable.logo_hanoconnect));
        followedList.add(new FollowedOpportunity("Giảng dạy cuối tuần", "Đã được chấp nhận", R.drawable.logo_hanoconnect));

        followedAdapter.notifyDataSetChanged();
    }
}
