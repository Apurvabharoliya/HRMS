package com.example.hrms;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class LeaveApprovalDetailActivity extends AppCompatActivity {

    private String leaveId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_approval_detail);

        leaveId = getIntent().getStringExtra("leaveId");

        TextView tvInfo = findViewById(R.id.tvDetailInfo);
        Button btnApprove = findViewById(R.id.btnApprove);
        Button btnReject = findViewById(R.id.btnReject);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("leaves").document(leaveId)
                .get()
                .addOnSuccessListener(d ->
                        tvInfo.setText(d.getData().toString()));

        btnApprove.setOnClickListener(v ->
                updateStatus(db, "APPROVED"));

        btnReject.setOnClickListener(v ->
                updateStatus(db, "REJECTED"));
    }

    private void updateStatus(FirebaseFirestore db, String status) {
        db.collection("leaves").document(leaveId)
                .update("status", status)
                .addOnSuccessListener(a -> finish());
    }
}
