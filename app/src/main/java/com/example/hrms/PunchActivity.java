package com.example.hrms;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PunchActivity extends AppCompatActivity {

    private TextView tvCurrentStatus, tvPunchInTime, tvWorkingDuration;
    private Button btnPunchAction;

    private FirebaseFirestore db;
    private String uid, today, docId;

    private String currentStatus = "NotStarted"; // NotStarted | Working | Completed
    private long punchInTimestamp = 0;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punch);

        tvCurrentStatus = findViewById(R.id.tvCurrentStatus);
        tvPunchInTime = findViewById(R.id.tvPunchInTime);
        tvWorkingDuration = findViewById(R.id.tvWorkingDuration);
        btnPunchAction = findViewById(R.id.btnPunchAction);

        db = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        docId = uid + "_" + today;

        loadTodayAttendance();

        btnPunchAction.setOnClickListener(v -> togglePunch());
    }

    private void loadTodayAttendance() {
        db.collection("attendance")
                .document(docId)
                .get()
                .addOnSuccessListener(snapshot -> {

                    if (snapshot.exists()) {

                        Long checkIn = snapshot.getLong("checkInTimestamp");
                        Long checkOut = snapshot.getLong("checkOutTimestamp");
                        String status = snapshot.getString("status");

                        if (checkIn != null) {
                            punchInTimestamp = checkIn;
                        }

                        if ("Working".equals(status)) {
                            currentStatus = "Working";
                            handler.post(updateTimerRunnable);
                        }
                        else if ("Completed".equals(status)) {
                            currentStatus = "Completed";
                        }
                        else {
                            currentStatus = "NotStarted";
                        }

                    } else {
                        currentStatus = "NotStarted";
                    }

                    updateUI();
                });
    }

    private void togglePunch() {
        if ("NotStarted".equals(currentStatus)) {
            punchIn();
        }
        else if ("Working".equals(currentStatus)) {
            punchOut();
        }
    }

    private void punchIn() {

        punchInTimestamp = System.currentTimeMillis();
        currentStatus = "Working";

        Map<String, Object> data = new HashMap<>();
        data.put("userId", uid);
        data.put("date", today);
        data.put("checkInTimestamp", punchInTimestamp);
        data.put("checkOutTimestamp", null);
        data.put("status", "Working");

        db.collection("attendance")
                .document(docId)
                .set(data);

        handler.post(updateTimerRunnable);
        updateUI();

        Toast.makeText(this, "Punched In Successfully", Toast.LENGTH_SHORT).show();
    }

    private void punchOut() {

        long punchOutTimestamp = System.currentTimeMillis();
        currentStatus = "Completed";

        db.collection("attendance")
                .document(docId)
                .update(
                        "checkOutTimestamp", punchOutTimestamp,
                        "status", "Completed"
                );

        handler.removeCallbacks(updateTimerRunnable);
        updateUI();

        Toast.makeText(this, "Workday Completed", Toast.LENGTH_SHORT).show();
    }

    private void updateUI() {

        if ("Working".equals(currentStatus)) {

            tvCurrentStatus.setText("Status: Punched In");
            tvCurrentStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

            String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    .format(new Date(punchInTimestamp));
            tvPunchInTime.setText("Punch In: " + time);

            btnPunchAction.setText("Punch Out");
            btnPunchAction.setEnabled(true);
        }

        else if ("Completed".equals(currentStatus)) {

            tvCurrentStatus.setText("Status: Workday Completed");
            tvCurrentStatus.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));

            String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    .format(new Date(punchInTimestamp));
            tvPunchInTime.setText("Punch In: " + time);

            tvWorkingDuration.setText("Working Time: Completed");

            btnPunchAction.setText("Completed");
            btnPunchAction.setEnabled(false);
        }

        else {

            tvCurrentStatus.setText("Status: Not Punched In");
            tvCurrentStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            tvPunchInTime.setText("Punch In: --");
            tvWorkingDuration.setText("Working Time: 00:00:00");

            btnPunchAction.setText("Punch In");
            btnPunchAction.setEnabled(true);
        }
    }

    private Runnable updateTimerRunnable = new Runnable() {
        @Override
        public void run() {

            if ("Working".equals(currentStatus)) {

                long diff = System.currentTimeMillis() - punchInTimestamp;
                long seconds = diff / 1000;
                long hrs = seconds / 3600;
                long mins = (seconds % 3600) / 60;
                long secs = seconds % 60;

                tvWorkingDuration.setText(String.format(
                        Locale.getDefault(),
                        "Working Time: %02d:%02d:%02d",
                        hrs, mins, secs
                ));

                handler.postDelayed(this, 1000);
            }
        }
    };
}