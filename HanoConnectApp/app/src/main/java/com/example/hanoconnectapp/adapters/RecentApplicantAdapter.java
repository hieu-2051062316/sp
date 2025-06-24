package com.example.hanoconnectapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hanoconnectapp.R;
import com.example.hanoconnectapp.models.RecentApplicantResponse;
import java.util.List;

public class RecentApplicantAdapter extends RecyclerView.Adapter<RecentApplicantAdapter.ViewHolder> {
    private List<RecentApplicantResponse> recentApplicants;
    public RecentApplicantAdapter(List<RecentApplicantResponse> data) {
        this.recentApplicants = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecentApplicantResponse applicant = recentApplicants.get(position);
        holder.text1.setText(applicant.getVolunteerName());
        holder.text2.setText("đã ứng tuyển vào: " + applicant.getOpportunityTitle());
    }

    @Override
    public int getItemCount() {
        return recentApplicants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
