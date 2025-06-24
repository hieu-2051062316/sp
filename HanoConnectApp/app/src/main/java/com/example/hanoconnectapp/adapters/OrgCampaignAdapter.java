package com.example.hanoconnectapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.ViewApplicantsActivity;
import com.example.hanoconnectapp.models.OpportunityResponseDto;
import com.google.android.material.button.MaterialButton;
import java.util.List;

// Adapter này giờ sẽ làm việc với OpportunityResponseDto
public class OrgCampaignAdapter extends RecyclerView.Adapter<OrgCampaignAdapter.OrgCampaignViewHolder> {

    private List<OpportunityResponseDto> campaignList;
    private Context context;

    public OrgCampaignAdapter(Context context, List<OpportunityResponseDto> campaignList) {
        this.context = context;
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
        OpportunityResponseDto item = campaignList.get(position);
        holder.tvCampaignName.setText(item.getTitle());
        // Hiện tại chưa có API lấy số lượng đơn, nên ta tạm để Status
        holder.tvCampaignStatus.setText("Trạng thái: " + item.getStatus());

        // Sử dụng Glide để tải logo
        Glide.with(context)
                .load(item.getOrganizationLogoUrl())
                .placeholder(R.drawable.logo_hanoconnect)
                .error(R.drawable.logo_hanoconnect)
                .into(holder.ivCampaignLogo);

        // Thiết lập sự kiện click cho nút "Xem đơn"
        holder.btnViewApplicants.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewApplicantsActivity.class);
            // Truyền ID và Tên thật của Opportunity
            intent.putExtra("OPPORTUNITY_ID", item.getOpportunityId());
            intent.putExtra("OPPORTUNITY_NAME", item.getTitle());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return campaignList != null ? campaignList.size() : 0;
    }

    public static class OrgCampaignViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCampaignLogo;
        TextView tvCampaignName;
        TextView tvCampaignStatus;
        MaterialButton btnViewApplicants;
        MaterialButton btnEditCampaign;

        public OrgCampaignViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCampaignLogo = itemView.findViewById(R.id.ivCampaignLogo);
            tvCampaignName = itemView.findViewById(R.id.tvCampaignName);
            tvCampaignStatus = itemView.findViewById(R.id.tvCampaignStatus);
            btnViewApplicants = itemView.findViewById(R.id.btnViewApplicants);
            btnEditCampaign = itemView.findViewById(R.id.btnEditCampaign);
        }
    }
}
