package com.example.hrms;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class LeaveStatusActivity extends AppCompatActivity {

    private RecyclerView rvLeaveStatus;
    private LeaveStatusAdapter adapter;
    private List<LeaveModel> leaveList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_status);

        rvLeaveStatus = findViewById(R.id.rvLeaveStatus);
        rvLeaveStatus.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LeaveStatusAdapter(leaveList);
        rvLeaveStatus.setAdapter(adapter);

        loadLeaveStatus();
    }

    private void loadLeaveStatus() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("leaves")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    leaveList.clear();
                    for (var doc : snapshot) {
                        LeaveModel model = doc.toObject(LeaveModel.class);
                        leaveList.add(model);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
