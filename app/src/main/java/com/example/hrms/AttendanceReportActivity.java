package com.example.hrms;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AttendanceReportActivity extends AppCompatActivity {

    private RecyclerView rv;
    private AttendanceReportAdapter adapter;
    private List<AttendanceModel> list = new ArrayList<>();
    private TextView tvSelectDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);

        tvSelectDate = findViewById(R.id.tvSelectDate);
        rv = findViewById(R.id.rvAttendanceReport);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AttendanceReportAdapter(list);
        rv.setAdapter(adapter);

        // TEMP DATE (date picker can be added later)
        loadReportForDate("2025-01-12");
    }

    private void loadReportForDate(String date) {
        FirebaseFirestore.getInstance()
                .collection("attendance")
                .whereEqualTo("date", date)
                .get()
                .addOnSuccessListener(snapshot -> {
                    list.clear();
                    for (var doc : snapshot) {
                        AttendanceModel model = doc.toObject(AttendanceModel.class);
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
