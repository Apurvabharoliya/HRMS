package com.example.hrms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Button;
import com.example.hrms.ai.AIAssistantActivity;


import androidx.appcompat.app.AppCompatActivity;

public class HRDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrdashboard);
        SessionGuard.enforcePunch(this);
        TextView tvTotalEmployees = findViewById(R.id.tvTotalEmployees);
        TextView tvPendingLeaves = findViewById(R.id.tvPendingLeaves);
        Button btnAI = findViewById(R.id.btnAI);

        // Temporary values (Firebase later)
        tvTotalEmployees.setText("18");
        tvPendingLeaves.setText("4");

        btnAI.setOnClickListener(v -> {
            Intent intent = new Intent(HRDashboardActivity.this, AIAssistantActivity.class);
            startActivity(intent);
        });


        LinearLayout actionLeave = findViewById(R.id.actionLeaveApprovals);
        LinearLayout actionEmployees = findViewById(R.id.actionEmployees);
        LinearLayout actionPolicies = findViewById(R.id.actionPolicies);
        LinearLayout actionAttendance = findViewById(R.id.actionAttendanceReport);

        actionLeave.setOnClickListener(v ->
                startActivity(new Intent(this, LeaveApprovalListActivity.class)));

        actionEmployees.setOnClickListener(v ->
                startActivity(new Intent(this, EmployeeListActivity.class)));

        actionPolicies.setOnClickListener(v ->
                startActivity(new Intent(this, PolicyListActivity.class)));

        actionAttendance.setOnClickListener(v ->
                startActivity(new Intent(this, AttendanceReportActivity.class)));
    }
}
