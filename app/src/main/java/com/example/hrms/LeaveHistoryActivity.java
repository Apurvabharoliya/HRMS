package com.example.hrms;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class LeaveHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LeaveHistoryAdapter adapter;
    private List<LeaveHistoryModel> leaveList = new ArrayList<>();

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_history);

        recyclerView = findViewById(R.id.recyclerLeaveHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        adapter = new LeaveHistoryAdapter(this, leaveList);
        recyclerView.setAdapter(adapter);

        loadLeaveHistory();
    }

    private void loadLeaveHistory() {

        String uid = auth.getCurrentUser().getUid();

        db.collection("leave_requests")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    leaveList.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        LeaveHistoryModel leave =
                                doc.toObject(LeaveHistoryModel.class);
                        leave.setLeaveRequestId(doc.getId());
                        leaveList.add(leave);
                    }

                    // 🔥 Sort latest first
                    Collections.sort(leaveList, (l1, l2) -> {

                        if (l1.getAppliedAt() == null) return 1;
                        if (l2.getAppliedAt() == null) return -1;

                        return l2.getAppliedAt().compareTo(l1.getAppliedAt());
                    });

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}