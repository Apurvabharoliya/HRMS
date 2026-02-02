package com.example.hrms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EmployeeDashboardActivity extends AppCompatActivity {

    private TextView tvWorkStatus;
    private ProgressBar progressWorkday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);
        SessionGuard.enforcePunch(this);

        tvWorkStatus = findViewById(R.id.tvWorkStatus);
        progressWorkday = findViewById(R.id.progressWorkday);

        LinearLayout actionPunch = findViewById(R.id.actionPunch);
        LinearLayout actionLeave = findViewById(R.id.actionLeave);
        LinearLayout actionAttendance = findViewById(R.id.actionAttendance);

        // TEMP (will be Firebase-driven later)
        boolean isPunchedIn = false;
        int hoursWorked = 0;

        if (isPunchedIn) {
            tvWorkStatus.setText("You are punched in");
            progressWorkday.setProgress(hoursWorked);
        } else {
            tvWorkStatus.setText("You are not punched in");
            progressWorkday.setProgress(0);
        }

        actionPunch.setOnClickListener(v ->
                startActivity(new Intent(this, PunchActivity.class)));

        actionLeave.setOnClickListener(v ->
                startActivity(new Intent(this, ApplyLeaveActivity.class)));

        actionAttendance.setOnClickListener(v ->
                startActivity(new Intent(this, AttendanceHistoryActivity.class)));
    }
}
