package com.example.hanoconnectapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.models.OrgCampaignItem;

import java.util.List;

public class OrgCampaignAdapter extends RecyclerView.Adapter<OrgCampaignAdapter.OrgCampaignViewHolder> {

    private List<OrgCampaignItem> campaignList;

    public OrgCampaignAdapter(List<OrgCampaignItem> campaignList) {
        this.campaignList = campaignList;
    }

    @NonNull
    @Override
    public OrgCampaignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_org_campaign, parent, false);
        return new OrgCampaignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrgCampaignViewHolder holder, int position) {
        OrgCampaignItem item = campaignList.get(position);
        holder.tvCampaignName.setText(item.getCampaignName());
        holder.tvCampaignStatus.setText(item.getStatus());
        holder.ivCampaignLogo.setImageResource(item.getLogoResId());
    }

    @Override
    public int getItemCount() {
        return campaignList != null ? campaignList.size() : 0;
    }

    public static class OrgCampaignViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCampaignLogo;
        TextView tvCampaignName;
        TextView tvCampaignStatus;

        public OrgCampaignViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCampaignLogo = itemView.findViewById(R.id.ivCampaignLogo);
            tvCampaignName = itemView.findViewById(R.id.tvCampaignName);
            tvCampaignStatus = itemView.findViewById(R.id.tvCampaignStatus);
        }
    }
}
