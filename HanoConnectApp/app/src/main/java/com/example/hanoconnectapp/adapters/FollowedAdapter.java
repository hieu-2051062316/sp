package com.example.hanoconnectapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.models.MyApplicationResponse;
import java.util.List;

public class FollowedAdapter extends RecyclerView.Adapter<FollowedAdapter.FollowedViewHolder> {

    private List<MyApplicationResponse> followedList;
    private Context context;

    public FollowedAdapter(Context context, List<MyApplicationResponse> followedList) {
        this.context = context;
        this.followedList = followedList;
    }

    @NonNull
    @Override
    public FollowedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_followed, parent, false);
        return new FollowedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowedViewHolder holder, int position) {
        MyApplicationResponse item = followedList.get(position);
        holder.tvCampaignName.setText(item.getOpportunityTitle());
        holder.tvStatus.setText(item.getStatus());

        // Cập nhật màu sắc cho status để dễ phân biệt
        switch (item.getStatus().toLowerCase()) {
            case "accepted":
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_accepted));
                break;
            case "rejected":
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_rejected));
                break;
            case "pending":
            default:
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_pending));
                break;
        }

        // Sử dụng Glide để hiển thị logo placeholder
        Glide.with(context)
                .load(R.drawable.logo_hanoconnect)
                .circleCrop()
                .into(holder.ivCampaignLogo);
    }

    @Override
    public int getItemCount() {
        return followedList != null ? followedList.size() : 0;
    }

    public static class FollowedViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCampaignLogo;
        TextView tvCampaignName;
        TextView tvStatus;

        public FollowedViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCampaignLogo = itemView.findViewById(R.id.ivCampaignLogo);
            tvCampaignName = itemView.findViewById(R.id.tvCampaignName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
