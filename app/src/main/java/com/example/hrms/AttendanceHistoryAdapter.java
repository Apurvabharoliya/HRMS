package com.example.hrms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AttendanceHistoryAdapter
        extends RecyclerView.Adapter<AttendanceHistoryAdapter.VH> {

    private final List<AttendanceModel> list;

    public AttendanceHistoryAdapter(List<AttendanceModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int p) {
        AttendanceModel m = list.get(p);

        h.tvDate.setText(m.getDate());
        h.tvIn.setText("Punch In: " + m.getPunchIn());
        h.tvOut.setText(
                m.getPunchOut() == null
                        ? "Punch Out: --"
                        : "Punch Out: " + m.getPunchOut()
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        TextView tvDate, tvIn, tvOut;

        VH(View v) {
            super(v);
            tvDate = v.findViewById(R.id.tvDate);
            tvIn = v.findViewById(R.id.tvPunchIn);
            tvOut = v.findViewById(R.id.tvPunchOut);
        }
    }
}
