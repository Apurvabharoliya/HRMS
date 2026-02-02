package com.example.hrms;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LeaveStatusAdapter
        extends RecyclerView.Adapter<LeaveStatusAdapter.LeaveViewHolder> {

    private final List<LeaveModel> list;

    public LeaveStatusAdapter(List<LeaveModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public LeaveViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leave_status, parent, false);
        return new LeaveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull LeaveViewHolder holder, int position) {

        LeaveModel m = list.get(position);

        holder.tvType.setText(m.getLeaveType());
        holder.tvDates.setText(m.getFromDate() + " → " + m.getToDate());
        holder.tvReason.setText(m.getReason());
        holder.tvStatus.setText(m.getStatus());

        switch (m.getStatus()) {
            case "APPROVED":
                holder.tvStatus.setTextColor(Color.parseColor("#16A34A"));
                break;
            case "REJECTED":
                holder.tvStatus.setTextColor(Color.parseColor("#DC2626"));
                break;
            default:
                holder.tvStatus.setTextColor(Color.parseColor("#F59E0B"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class LeaveViewHolder extends RecyclerView.ViewHolder {

        TextView tvType, tvDates, tvReason, tvStatus;

        LeaveViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvLeaveType);
            tvDates = itemView.findViewById(R.id.tvLeaveDates);
            tvReason = itemView.findViewById(R.id.tvLeaveReason);
            tvStatus = itemView.findViewById(R.id.tvLeaveStatus);
        }
    }
}
