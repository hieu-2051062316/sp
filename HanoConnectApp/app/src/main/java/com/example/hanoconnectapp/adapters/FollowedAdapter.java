package com.example.hanoconnectapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.models.FollowedOpportunity;

import java.util.List;

public class FollowedAdapter extends RecyclerView.Adapter<FollowedAdapter.FollowedViewHolder> {

    private List<FollowedOpportunity> followedList;

    public FollowedAdapter(List<FollowedOpportunity> followedList) {
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
        FollowedOpportunity item = followedList.get(position);
        holder.tvCampaignName.setText(item.getCampaignName());
        holder.tvStatus.setText(item.getStatus());
        holder.ivCampaignLogo.setImageResource(item.getLogoResId());
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