package com.example.hrms;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AttendanceHistoryActivity extends AppCompatActivity {

    private RecyclerView rv;
    private AttendanceHistoryAdapter adapter;
    private List<AttendanceModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);

        rv = findViewById(R.id.rvAttendanceHistory);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AttendanceHistoryAdapter(list);
        rv.setAdapter(adapter);

        loadAttendance();
    }

    private void loadAttendance() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("attendance")
                .whereEqualTo("userId", uid)
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
