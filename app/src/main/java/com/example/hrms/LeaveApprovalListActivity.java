package com.example.hrms;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class LeaveApprovalListActivity extends AppCompatActivity {

    private RecyclerView rv;
    private LeaveApprovalAdapter adapter;
    private List<LeaveModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_approval_list);

        rv = findViewById(R.id.rvLeaveApproval);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LeaveApprovalAdapter(list, this::openDetail);
        rv.setAdapter(adapter);

        loadPendingLeaves();
    }

    private void loadPendingLeaves() {
        FirebaseFirestore.getInstance()
                .collection("leaves")
                .whereEqualTo("status", "PENDING")
                .get()
                .addOnSuccessListener(snapshot -> {
                    list.clear();
                    for (var doc : snapshot) {
                        LeaveModel model = doc.toObject(LeaveModel.class);
                        model.setId(doc.getId());   // ✅ NOW WORKS
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void openDetail(LeaveModel model) {
        Intent i = new Intent(this, LeaveApprovalDetailActivity.class);
        i.putExtra("leaveId", model.getId());
        startActivity(i);
    }
}
