package com.example.hrms;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.*;

public class ApplyLeaveActivity extends AppCompatActivity {

    private Spinner spLeaveType;
    private TextView tvFromDate, tvToDate;
    private EditText etReason;
    private Button btnSubmit;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        spLeaveType = findViewById(R.id.spLeaveType);
        tvFromDate = findViewById(R.id.tvFromDate);
        tvToDate = findViewById(R.id.tvToDate);
        etReason = findViewById(R.id.etReason);
        btnSubmit = findViewById(R.id.btnSubmitLeave);

        setupLeaveTypes();

        btnSubmit.setOnClickListener(v -> submitLeave());
    }

    private void setupLeaveTypes() {
        List<String> types = Arrays.asList(
                "Earned Leave",
                "Sick Leave",
                "Casual Leave",
                "Maternity Leave",
                "Paternity Leave",
                "Loss of Pay"
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                types
        );
        spLeaveType.setAdapter(adapter);
    }

    private void submitLeave() {

        String leaveType = spLeaveType.getSelectedItem().toString();
        String fromDate = tvFromDate.getText().toString();
        String toDate = tvToDate.getText().toString();
        String reason = etReason.getText().toString().trim();

        if (reason.isEmpty()) {
            Toast.makeText(this, "Please provide a reason", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = auth.getCurrentUser().getUid();

        Map<String, Object> leave = new HashMap<>();
        leave.put("userId", uid);
        leave.put("leaveType", leaveType);
        leave.put("fromDate", fromDate);
        leave.put("toDate", toDate);
        leave.put("reason", reason);
        leave.put("status", "PENDING");
        leave.put("timestamp", new Date());

        db.collection("leaves")
                .add(leave)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(this, "Leave submitted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
