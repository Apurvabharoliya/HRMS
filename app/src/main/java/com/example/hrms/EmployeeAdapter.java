package com.example.hrms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmployeeAdapter
        extends RecyclerView.Adapter<EmployeeAdapter.VH> {

    private final List<EmployeeModel> list;

    public EmployeeAdapter(List<EmployeeModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_employee, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int p) {
        EmployeeModel m = list.get(p);

        h.tvName.setText(m.getName());
        h.tvRole.setText("Role: " + m.getRole());
        h.tvStatus.setText(m.getStatus());

        h.tvStatus.setTextColor(
                "Active".equals(m.getStatus())
                        ? 0xFF16A34A
                        : 0xFFDC2626
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        TextView tvName, tvRole, tvStatus;

        VH(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvEmployeeName);
            tvRole = v.findViewById(R.id.tvEmployeeRole);
            tvStatus = v.findViewById(R.id.tvEmployeeStatus);
        }
    }
}
