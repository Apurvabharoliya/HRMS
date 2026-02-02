package com.example.hrms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class HRDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrdashboard);
        SessionGuard.enforcePunch(this);

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
