package com.example.hrms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Button;
import com.example.hrms.ai.AIAssistantActivity;


import androidx.appcompat.app.AppCompatActivity;

public class EmployeeDashboardActivity extends AppCompatActivity {

    private TextView tvWorkStatus;
    private ProgressBar progressWorkday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);
        SessionGuard.enforcePunch(this);
        TextView tvAttendancePercent = findViewById(R.id.tvAttendancePercent);
        TextView tvLeaveBalance = findViewById(R.id.tvLeaveBalance);
        Button btnAI = findViewById(R.id.btnAI);

        // Temporary static data
        tvAttendancePercent.setText("92%");
        tvLeaveBalance.setText("6");

        btnAI.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeDashboardActivity.this, AIAssistantActivity.class);
            startActivity(intent);
        });


        tvWorkStatus = findViewById(R.id.tvWorkStatus);
        progressWorkday = findViewById(R.id.progressWorkday);

        LinearLayout actionPunch = findViewById(R.id.actionPunch);
        LinearLayout actionLeave = findViewById(R.id.actionLeave);
        LinearLayout actionAttendance = findViewById(R.id.actionAttendance);
        LinearLayout actionProfile = findViewById(R.id.actionProfile);

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

        actionProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
    }
}
