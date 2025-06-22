package com.example.hanoconnectapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.models.NotificationItem;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationItem> notificationList;

    public NotificationAdapter(List<NotificationItem> notificationList) {
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
        NotificationItem item = notificationList.get(position);
        holder.tvCampaignName.setText(item.getCampaignName());
        holder.tvMessage.setText(item.getMessage());
        holder.ivCampaignLogo.setImageResource(item.getLogoResId());

        // Đặt trạng thái activated để selector đổi màu nền
        holder.itemView.setActivated(item.isNew());
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