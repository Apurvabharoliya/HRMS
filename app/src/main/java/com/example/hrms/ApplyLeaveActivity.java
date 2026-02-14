package com.example.hrms;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class ApplyLeaveActivity extends AppCompatActivity {

    private Spinner spLeaveType;
    private TextView tvFromDate, tvToDate, tvTotalDays, tvReasonCounter;
    private EditText etReason;
    private Button btnSubmit;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private SimpleDateFormat sdf;
    private Map<String, String> leaveIdMap = new HashMap<>();
    private List<String> leaveNames = new ArrayList<>();

    private Date selectedFromDate, selectedToDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        spLeaveType = findViewById(R.id.spLeaveType);
        tvFromDate = findViewById(R.id.tvFromDate);
        tvToDate = findViewById(R.id.tvToDate);
        tvTotalDays = findViewById(R.id.tvTotalDays);
        tvReasonCounter = findViewById(R.id.tvReasonCounter);
        etReason = findViewById(R.id.etReason);
        btnSubmit = findViewById(R.id.btnSubmitLeave);

        loadLeaveTypes();

        tvFromDate.setOnClickListener(v -> showDatePicker(true));
        tvToDate.setOnClickListener(v -> showDatePicker(false));

        btnSubmit.setOnClickListener(v -> validateAndSubmit());

        etReason.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                tvReasonCounter.setText(s.length() + "/300");
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    // ================= LOAD LEAVE TYPES =================
    private void loadLeaveTypes() {
        db.collection("leave_types").get()
                .addOnSuccessListener(query -> {
                    leaveNames.clear();
                    leaveIdMap.clear();

                    for (DocumentSnapshot doc : query) {
                        String name = doc.getString("leaveName");
                        String id = doc.getString("leaveTypeId");

                        leaveNames.add(name);
                        leaveIdMap.put(name, id);
                    }

                    spLeaveType.setAdapter(new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_dropdown_item,
                            leaveNames));
                });
    }

    // ================= DATE PICKER =================
    private void showDatePicker(boolean isFrom) {

        Calendar today = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, day) -> {

                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, day);

                    if (selected.before(today)) {
                        Toast.makeText(this, "Cannot select past date", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (isFrom) {
                        selectedFromDate = selected.getTime();
                        tvFromDate.setText(sdf.format(selectedFromDate));
                    } else {
                        selectedToDate = selected.getTime();
                        tvToDate.setText(sdf.format(selectedToDate));
                    }

                    calculateWorkingDays();
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    // ================= CALCULATE WORKING DAYS =================
    private void calculateWorkingDays() {

        if (selectedFromDate == null || selectedToDate == null) return;

        if (selectedFromDate.after(selectedToDate)) {
            Toast.makeText(this, "From date cannot be after To date", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(selectedFromDate);

        int workingDays = 0;

        while (!cal.getTime().after(selectedToDate)) {

            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

            if (dayOfWeek != Calendar.SUNDAY) {
                workingDays++;
            }

            cal.add(Calendar.DATE, 1);
        }

        tvTotalDays.setText("Total Leave Days: " + workingDays);
    }

    // ================= VALIDATE & SUBMIT =================
    private void validateAndSubmit() {

        if (selectedFromDate == null || selectedToDate == null) {
            Toast.makeText(this, "Select dates properly", Toast.LENGTH_SHORT).show();
            return;
        }

        String reason = etReason.getText().toString().trim();

        if (reason.length() < 10) {
            Toast.makeText(this, "Reason must be at least 10 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (reason.length() > 300) {
            Toast.makeText(this, "Reason too long", Toast.LENGTH_SHORT).show();
            return;
        }

        checkOverlapAndBalance();
    }

    // ================= CHECK OVERLAP =================
    private void checkOverlapAndBalance() {

        String uid = auth.getCurrentUser().getUid();

        db.collection("leave_requests")
                .whereEqualTo("userId", uid)
                .whereIn("status", Arrays.asList("Pending", "Approved"))
                .get()
                .addOnSuccessListener(query -> {

                    for (DocumentSnapshot doc : query) {

                        try {
                            Date existingFrom = sdf.parse(doc.getString("fromDate"));
                            Date existingTo = sdf.parse(doc.getString("toDate"));

                            if (!(selectedToDate.before(existingFrom) ||
                                    selectedFromDate.after(existingTo))) {

                                Toast.makeText(this,
                                        "You already have leave applied in selected dates",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }

                        } catch (Exception ignored) {}
                    }

                    checkLeaveBalance();
                });
    }

    // ================= CHECK LEAVE BALANCE =================
    // ================= CHECK LEAVE BALANCE =================
    private void checkLeaveBalance() {

        String uid = auth.getCurrentUser().getUid();

        if (spLeaveType.getSelectedItem() == null) {
            Toast.makeText(this, "Select leave type", Toast.LENGTH_SHORT).show();
            return;
        }

        String leaveName = spLeaveType.getSelectedItem().toString();
        String leaveTypeId = leaveIdMap.get(leaveName);

        if (leaveTypeId == null) {
            Toast.makeText(this, "Invalid leave type selected", Toast.LENGTH_SHORT).show();
            return;
        }

        long daysRequested = calculateRequestedDays();

        db.collection("leave_balances")
                .whereEqualTo("userId", uid)
                .whereEqualTo("leaveTypeID", leaveTypeId)
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    if (querySnapshot.isEmpty()) {
                        Toast.makeText(this,
                                "No leave balance found",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    DocumentSnapshot balanceDoc =
                            querySnapshot.getDocuments().get(0);

                    Long remainingLeaves =
                            balanceDoc.getLong("remainingLeaves");

                    if (remainingLeaves == null) {
                        Toast.makeText(this,
                                "Leave balance data error",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (daysRequested > remainingLeaves) {
                        Toast.makeText(this,
                                "Insufficient leave balance",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    saveLeave(leaveTypeId, daysRequested);

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Error checking leave balance",
                                Toast.LENGTH_SHORT).show());
    }

    private long calculateRequestedDays() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(selectedFromDate);

        int workingDays = 0;

        while (!cal.getTime().after(selectedToDate)) {

            if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                workingDays++;
            }

            cal.add(Calendar.DATE, 1);
        }

        return workingDays;
    }

    // ================= SAVE =================
    private void saveLeave(String leaveTypeId, long days) {

        String uid = auth.getCurrentUser().getUid();

        DocumentReference docRef =
                db.collection("leave_requests").document();

        Map<String, Object> leave = new HashMap<>();
        leave.put("leaveRequestId", docRef.getId());
        leave.put("userId", uid);
        leave.put("leaveTypeId", leaveTypeId);
        leave.put("fromDate", sdf.format(selectedFromDate));
        leave.put("toDate", sdf.format(selectedToDate));
        leave.put("days", days);
        leave.put("reason", etReason.getText().toString().trim());
        leave.put("status", "Pending");
        leave.put("approvedBy", "");
        leave.put("appliedAt", new Date());

        docRef.set(leave)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Leave submitted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}