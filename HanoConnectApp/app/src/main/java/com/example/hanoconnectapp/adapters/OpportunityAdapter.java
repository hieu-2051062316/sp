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
import com.example.hanoconnectapp.OpportunityDetailActivity;
import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.models.OpportunityResponseDto;
import java.util.List;

public class OpportunityAdapter extends RecyclerView.Adapter<OpportunityAdapter.OpportunityViewHolder> {

    private List<OpportunityResponseDto> opportunityList;
    private Context context;

    public OpportunityAdapter(Context context, List<OpportunityResponseDto> opportunityList) {
        this.context = context;
        this.opportunityList = opportunityList;
    }

    @NonNull
    @Override
    public OpportunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_opportunity, parent, false);
        return new OpportunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpportunityViewHolder holder, int position) {
        OpportunityResponseDto opportunity = opportunityList.get(position);

        holder.tvOrgName.setText(opportunity.getOrganizationName());
        holder.tvOpportunityTitle.setText(opportunity.getTitle());
        holder.tvOpportunityDescription.setText(opportunity.getDescription());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OpportunityDetailActivity.class);
            // Gửi toàn bộ đối tượng opportunity đi
            intent.putExtra("OPPORTUNITY_DETAIL", opportunity);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return opportunityList != null ? opportunityList.size() : 0;
    }

    public static class OpportunityViewHolder extends RecyclerView.ViewHolder {
        ImageView ivOrgLogo;
        TextView tvOrgName;
        TextView tvOpportunityTitle;
        TextView tvOpportunityDescription;

        public OpportunityViewHolder(@NonNull View itemView) {
            super(itemView);
            ivOrgLogo = itemView.findViewById(R.id.ivOrgLogo);
            tvOrgName = itemView.findViewById(R.id.tvOrgName);
            tvOpportunityTitle = itemView.findViewById(R.id.tvOpportunityTitle);
            tvOpportunityDescription = itemView.findViewById(R.id.tvOpportunityDescription);
        }
    }
}