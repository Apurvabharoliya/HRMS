package com.example.hrms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EmployeeDashboardActivity extends AppCompatActivity {

    private TextView tvWorkStatus, tvGreeting;
    private ProgressBar progressWorkday;

    private FirebaseFirestore db;
    private String uid, today, docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);

        tvWorkStatus = findViewById(R.id.tvWorkStatus);
        tvGreeting = findViewById(R.id.tvGreeting);
        progressWorkday = findViewById(R.id.progressWorkday);

        LinearLayout actionPunch = findViewById(R.id.actionPunch);
        LinearLayout actionLeave = findViewById(R.id.actionLeave);
        LinearLayout actionLeaveHistory = findViewById(R.id.actionLeaveHistory);
        LinearLayout actionAttendance = findViewById(R.id.actionAttendance);
        LinearLayout actionProfile = findViewById(R.id.actionProfile);

        db = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());

        docId = uid + "_" + today;

        setGreeting();
        loadTodayAttendance();

        actionPunch.setOnClickListener(v ->
                startActivity(new Intent(this, PunchActivity.class)));

        actionLeave.setOnClickListener(v ->
                startActivity(new Intent(this, ApplyLeaveActivity.class)));

        actionLeaveHistory.setOnClickListener(v ->
                startActivity(new Intent(this, LeaveHistoryActivity.class)));

        actionAttendance.setOnClickListener(v ->
                startActivity(new Intent(this, AttendanceHistoryActivity.class)));

        actionProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
    }

    private void setGreeting() {
        int hour = new Date().getHours();

        if (hour < 12) {
            tvGreeting.setText("Good Morning");
        } else if (hour < 17) {
            tvGreeting.setText("Good Afternoon");
        } else {
            tvGreeting.setText("Good Evening");
        }
    }

    private void loadTodayAttendance() {

        db.collection("attendance")
                .document(docId)
                .get()
                .addOnSuccessListener(snapshot -> {

                    if (snapshot.exists()) {

                        String status = snapshot.getString("status");
                        Long checkIn = snapshot.getLong("checkInTimestamp");
                        Long checkOut = snapshot.getLong("checkOutTimestamp");

                        if ("Working".equals(status) && checkIn != null) {

                            tvWorkStatus.setText("You are punched in");

                            long diff = System.currentTimeMillis() - checkIn;
                            long hours = diff / (1000 * 60 * 60);

                            progressWorkday.setProgress((int) hours);

                        } else if ("Completed".equals(status)
                                && checkIn != null
                                && checkOut != null) {

                            tvWorkStatus.setText("Workday Completed");

                            long diff = checkOut - checkIn;
                            long hours = diff / (1000 * 60 * 60);

                            progressWorkday.setProgress((int) hours);

                        } else {
                            tvWorkStatus.setText("You are not punched in");
                            progressWorkday.setProgress(0);
                        }

                    } else {
                        tvWorkStatus.setText("You are not punched in");
                        progressWorkday.setProgress(0);
                    }

                })
                .addOnFailureListener(e -> {
                    tvWorkStatus.setText("Unable to load status");
                    progressWorkday.setProgress(0);
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTodayAttendance(); // refresh when coming back from PunchActivity
    }
}