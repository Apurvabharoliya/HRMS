package com.example.hrms;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class ApplyLeaveActivity extends AppCompatActivity {

    private Spinner spLeaveType;
    private TextView tvFromDate, tvToDate;
    private EditText etReason;
    private Button btnSubmit;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private Calendar calendar;
    private SimpleDateFormat sdf;

    private List<String> leaveNames = new ArrayList<>();
    private Map<String, String> leaveIdMap = new HashMap<>();

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

        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        loadLeaveTypes();

        tvFromDate.setOnClickListener(v -> showDatePicker(tvFromDate));
        tvToDate.setOnClickListener(v -> showDatePicker(tvToDate));

        btnSubmit.setOnClickListener(v -> submitLeave());
    }

    // ================= LOAD LEAVE TYPES FROM FIRESTORE =================
    private void loadLeaveTypes() {

        db.collection("leave_types")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    leaveNames.clear();
                    leaveIdMap.clear();

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {

                        String name = doc.getString("leaveName");
                        String id = doc.getString("leaveTypeId");

                        leaveNames.add(name);
                        leaveIdMap.put(name, id);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_dropdown_item,
                            leaveNames
                    );

                    spLeaveType.setAdapter(adapter);
                });
    }

    // ================= DATE PICKER =================
    private void showDatePicker(TextView target) {

        Calendar today = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, dayOfMonth);

                    if (selected.before(today)) {
                        Toast.makeText(this, "Cannot select past date", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    target.setText(sdf.format(selected.getTime()));
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    // ================= SUBMIT =================
    private void submitLeave() {

        if (spLeaveType.getSelectedItem() == null) {
            Toast.makeText(this, "Select leave type", Toast.LENGTH_SHORT).show();
            return;
        }

        String leaveName = spLeaveType.getSelectedItem().toString();
        String leaveTypeId = leaveIdMap.get(leaveName);
        String fromDate = tvFromDate.getText().toString();
        String toDate = tvToDate.getText().toString();
        String reason = etReason.getText().toString().trim();

        if (fromDate.equals("Select start date")) {
            Toast.makeText(this, "Select From Date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (toDate.equals("Select end date")) {
            Toast.makeText(this, "Select To Date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (reason.isEmpty()) {
            Toast.makeText(this, "Enter reason", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            Date from = sdf.parse(fromDate);
            Date to = sdf.parse(toDate);

            if (from.after(to)) {
                Toast.makeText(this, "From date cannot be after To date", Toast.LENGTH_SHORT).show();
                return;
            }

            long diff = to.getTime() - from.getTime();
            long days = (diff / (1000 * 60 * 60 * 24)) + 1;

            saveLeave(leaveTypeId, fromDate, toDate, reason, days);

        } catch (Exception e) {
            Toast.makeText(this, "Invalid Date", Toast.LENGTH_SHORT).show();
        }
    }

    // ================= SAVE TO FIRESTORE =================
    private void saveLeave(String leaveTypeId, String fromDate,
                           String toDate, String reason, long days) {

        String uid = auth.getCurrentUser().getUid();

        DocumentReference docRef = db.collection("leave_requests").document();

        Map<String, Object> leave = new HashMap<>();
        leave.put("leaveRequestId", docRef.getId());
        leave.put("userId", uid);
        leave.put("leaveTypeId", leaveTypeId);
        leave.put("fromDate", fromDate);
        leave.put("toDate", toDate);
        leave.put("days", days);
        leave.put("reason", reason);
        leave.put("status", "Pending");
        leave.put("approvedBy", "");
        leave.put("appliedAt", new Date());

        docRef.set(leave)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Leave submitted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}