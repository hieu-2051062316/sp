// Đã cập nhật để import và sử dụng ReviewApplicationActivity.
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
import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.ReviewApplicationActivity;
import com.example.hanoconnectapp.models.ApplicantItem;
import java.util.List;

public class ApplicantAdapter extends RecyclerView.Adapter<ApplicantAdapter.ApplicantViewHolder> {

    private List<ApplicantItem> applicantList;
    private Context context;

    public ApplicantAdapter(Context context, List<ApplicantItem> applicantList) {
        this.context = context;
        this.applicantList = applicantList;
    }

    @NonNull
    @Override
    public ApplicantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant, parent, false);
        return new ApplicantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicantViewHolder holder, int position) {
        ApplicantItem item = applicantList.get(position);
        holder.tvApplicantName.setText(item.getName());
        holder.tvApplyDate.setText(item.getApplyDate());
        holder.ivApplicantAvatar.setImageResource(item.getAvatarResId());

        // Thiết lập sự kiện click cho toàn bộ item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReviewApplicationActivity.class);
            intent.putExtra("APPLICANT_DETAIL", item);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return applicantList != null ? applicantList.size() : 0;
    }

    public static class ApplicantViewHolder extends RecyclerView.ViewHolder {
        ImageView ivApplicantAvatar;
        TextView tvApplicantName;
        TextView tvApplyDate;

        public ApplicantViewHolder(@NonNull View itemView) {
            super(itemView);
            ivApplicantAvatar = itemView.findViewById(R.id.ivApplicantAvatar);
            tvApplicantName = itemView.findViewById(R.id.tvApplicantName);
            tvApplyDate = itemView.findViewById(R.id.tvApplyDate);
        }
    }
}
