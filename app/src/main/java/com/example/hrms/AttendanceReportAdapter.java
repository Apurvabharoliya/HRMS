package com.example.hrms;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AttendanceReportAdapter
        extends RecyclerView.Adapter<AttendanceReportAdapter.VH> {

    private final List<AttendanceModel> list;

    public AttendanceReportAdapter(List<AttendanceModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance_report, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int p) {
        AttendanceModel m = list.get(p);

        h.tvEmp.setText("Employee: " + m.getUserId());

        String info = "In: " + m.getPunchIn() + " | Out: "
                + (m.getPunchOut() == null ? "--" : m.getPunchOut());
        h.tvInfo.setText(info);

        if (m.getPunchIn() != null) {
            h.tvStatus.setText("Present");
            h.tvStatus.setTextColor(Color.parseColor("#16A34A"));
        } else {
            h.tvStatus.setText("Absent");
            h.tvStatus.setTextColor(Color.parseColor("#DC2626"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        TextView tvEmp, tvInfo, tvStatus;

        VH(View v) {
            super(v);
            tvEmp = v.findViewById(R.id.tvEmployeeId);
            tvInfo = v.findViewById(R.id.tvPunchInfo);
            tvStatus = v.findViewById(R.id.tvAttendanceStatus);
        }
    }
}
