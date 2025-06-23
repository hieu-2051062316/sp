package com.example.hanoconnectapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hanoconnectapp.ApplyActivity;
import com.example.hanoconnectapp.OpportunityDetailActivity;
import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.models.OpportunityResponseDto;
import com.google.android.material.button.MaterialButton;
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

        // Gán dữ liệu
        holder.tvOrgName.setText(opportunity.getOrganizationName());
        holder.tvOpportunityTitle.setText(opportunity.getTitle());
        holder.tvOpportunityDescription.setText(opportunity.getDescription());

        // Sự kiện click cho toàn bộ thẻ -> Mở màn hình chi tiết
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OpportunityDetailActivity.class);
            intent.putExtra("OPPORTUNITY_DETAIL", opportunity);
            context.startActivity(intent);
        });

        // Sự kiện click riêng cho nút "ỨNG TUYỂN" -> Mở màn hình nộp đơn
        holder.btnApply.setOnClickListener(v -> {
            Intent applyIntent = new Intent(context, ApplyActivity.class);
            applyIntent.putExtra("OPPORTUNITY_ID", opportunity.getOpportunityId());
            applyIntent.putExtra("OPPORTUNITY_NAME", opportunity.getTitle());
            context.startActivity(applyIntent);
        });

        // Sự kiện click riêng cho nút "QUAN TÂM" (tạm thời)
        holder.btnFollow.setOnClickListener(v -> {
            Toast.makeText(context, "Đã thêm vào danh sách quan tâm!", Toast.LENGTH_SHORT).show();
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
        MaterialButton btnApply, btnFollow; // Thêm khai báo cho các nút

        public OpportunityViewHolder(@NonNull View itemView) {
            super(itemView);
            ivOrgLogo = itemView.findViewById(R.id.ivOrgLogo);
            tvOrgName = itemView.findViewById(R.id.tvOrgName);
            tvOpportunityTitle = itemView.findViewById(R.id.tvOpportunityTitle);
            tvOpportunityDescription = itemView.findViewById(R.id.tvOpportunityDescription);
            btnApply = itemView.findViewById(R.id.btnApply); // Tìm các nút bằng ID
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }
}
