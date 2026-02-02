package com.example.hrms;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PunchActivity extends AppCompatActivity {

    private TextView tvCurrentStatus, tvPunchInTime, tvWorkingDuration;
    private Button btnPunchAction;

    private boolean isPunchedIn = false;
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

        btnPunchAction.setOnClickListener(v -> togglePunch());

        updateUI();
    }

    private void togglePunch() {
        if (!isPunchedIn) {
            punchInTimestamp = System.currentTimeMillis();
            isPunchedIn = true;
            handler.post(updateTimerRunnable);
        } else {
            isPunchedIn = false;
            handler.removeCallbacks(updateTimerRunnable);
        }
        updateUI();
    }

    private void updateUI() {
        if (isPunchedIn) {
            tvCurrentStatus.setText("Status: Punched In");
            tvCurrentStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

            String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    .format(new Date(punchInTimestamp));
            tvPunchInTime.setText("Punch In: " + time);

            btnPunchAction.setText("Punch Out");
        } else {
            tvCurrentStatus.setText("Status: Not Punched In");
            tvCurrentStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            tvPunchInTime.setText("Punch In: --");
            tvWorkingDuration.setText("Working Time: 00:00:00");

            btnPunchAction.setText("Punch In");
        }
    }

    private Runnable updateTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPunchedIn) {
                long diff = System.currentTimeMillis() - punchInTimestamp;
                long seconds = diff / 1000;
                long hrs = seconds / 3600;
                long mins = (seconds % 3600) / 60;
                long secs = seconds % 60;

                String time = String.format(
                        Locale.getDefault(),
                        "Working Time: %02d:%02d:%02d",
                        hrs, mins, secs
                );
                tvWorkingDuration.setText(time);

                handler.postDelayed(this, 1000);
            }
        }
    };
}
