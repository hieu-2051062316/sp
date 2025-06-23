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
import com.example.hanoconnectapp.ReviewApplicationActivity;
import com.example.hanoconnectapp.models.ApplicantResponse; // Sử dụng model mới

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ApplicantAdapter extends RecyclerView.Adapter<ApplicantAdapter.ApplicantViewHolder> {

    private List<ApplicantResponse> applicantList;
    private Context context;

    // Sửa constructor để nhận List<ApplicantResponse>
    public ApplicantAdapter(Context context, List<ApplicantResponse> applicantList) {
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
        ApplicantResponse item = applicantList.get(position);
        holder.tvApplicantName.setText(item.getVolunteerName());

        // Format lại ngày tháng cho dễ nhìn
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime dateTime = LocalDateTime.parse(item.getApplicationTime(), inputFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            holder.tvApplyDate.setText("Nộp ngày: " + dateTime.format(outputFormatter));
        } catch (Exception e) {
            holder.tvApplyDate.setText("Nộp ngày: " + item.getApplicationTime());
        }

        // Tạm thời vẫn dùng ảnh placeholder
        Glide.with(context)
                .load(R.drawable.ic_person_placeholder)
                .circleCrop()
                .into(holder.ivApplicantAvatar);

        // Thiết lập sự kiện click cho toàn bộ item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReviewApplicationActivity.class);
            // Gửi toàn bộ đối tượng ApplicantResponse qua Intent
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
