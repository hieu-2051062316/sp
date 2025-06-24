package com.example.hanoconnectapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.models.NotificationResponse;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationResponse> notificationList;

    public NotificationAdapter(List<NotificationResponse> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationResponse item = notificationList.get(position);

        // Hiển thị message, không cần tên chiến dịch nữa vì message đã đủ chi tiết
        holder.tvMessage.setText(item.getMessage());
        holder.tvCampaignName.setVisibility(View.GONE); // Ẩn đi TextView không cần thiết

        // Dùng logo mặc định
        Glide.with(holder.itemView.getContext())
                .load(R.drawable.logo_hanoconnect)
                .circleCrop()
                .into(holder.ivCampaignLogo);

        // Đặt trạng thái đã đọc/chưa đọc
        holder.itemView.setActivated(!item.isRead());
    }

    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCampaignLogo;
        TextView tvCampaignName;
        TextView tvMessage;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCampaignLogo = itemView.findViewById(R.id.ivCampaignLogo);
            tvCampaignName = itemView.findViewById(R.id.tvCampaignName);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }
}
