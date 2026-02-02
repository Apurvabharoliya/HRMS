package com.example.hrms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LeaveApprovalAdapter
        extends RecyclerView.Adapter<LeaveApprovalAdapter.VH> {

    public interface OnLeaveClick {
        void onClick(LeaveModel model);
    }

    private final List<LeaveModel> list;
    private final OnLeaveClick listener;

    public LeaveApprovalAdapter(List<LeaveModel> list, OnLeaveClick listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leave_approval, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int p) {
        LeaveModel m = list.get(p);

        h.tvEmp.setText("Employee: " + m.getUserId());
        h.tvType.setText(m.getLeaveType());
        h.tvDates.setText(m.getFromDate() + " → " + m.getToDate());
        h.tvStatus.setText(m.getStatus());

        h.itemView.setOnClickListener(v -> listener.onClick(m));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        TextView tvEmp, tvType, tvDates, tvStatus;

        VH(View v) {
            super(v);
            tvEmp = v.findViewById(R.id.tvEmployeeId);
            tvType = v.findViewById(R.id.tvLeaveType);
            tvDates = v.findViewById(R.id.tvLeaveDates);
            tvStatus = v.findViewById(R.id.tvLeaveStatus);
        }
    }
}
