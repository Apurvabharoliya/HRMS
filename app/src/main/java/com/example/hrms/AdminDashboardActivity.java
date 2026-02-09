package com.example.hrms;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Button;
import com.example.hrms.ai.AIAssistantActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.hrms.todo.TodoAdapter;
import com.example.hrms.todo.TodoModel;
import java.util.ArrayList;
import java.util.List;



import java.util.ArrayList;

public class AdminDashboardActivity extends AppCompatActivity {

    private CardView cardManageUsers, cardLeaveRequests, cardAttendance, cardReports;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        TextView tvTotalEmployees = findViewById(R.id.tvTotalEmployees);
        TextView tvPendingLeaves = findViewById(R.id.tvPendingLeaves);
        Button btnAI = findViewById(R.id.btnAI);

        // Temporary static values (Firebase later)
        tvTotalEmployees.setText("25");
        tvPendingLeaves.setText("7");

        btnAI.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AIAssistantActivity.class);
            startActivity(intent);
        });


        // Initialize views
        barChart = findViewById(R.id.barChart);
        cardManageUsers = findViewById(R.id.cardManageUsers);
        cardLeaveRequests = findViewById(R.id.cardLeaveRequests);
        cardAttendance = findViewById(R.id.cardAttendance);
        cardReports = findViewById(R.id.cardReports);

        RecyclerView recyclerTodo = findViewById(R.id.recyclerTodo);

        List<TodoModel> todoList = new ArrayList<>();
        todoList.add(new TodoModel("Review leave requests", "PENDING"));
        todoList.add(new TodoModel("Approve new employees", "PENDING"));

        TodoAdapter todoAdapter = new TodoAdapter(todoList);
        recyclerTodo.setLayoutManager(new LinearLayoutManager(this));
        recyclerTodo.setAdapter(todoAdapter);


        setupBarChart();
        setupClickListeners();
    }

    private void setupBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 25)); // Employees
        entries.add(new BarEntry(1f, 12)); // Leave Requests
        entries.add(new BarEntry(2f, 20)); // Attendance

        BarDataSet dataSet = new BarDataSet(entries, "WorkZen Overview");
        dataSet.setDrawValues(true);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        barChart.setData(barData);

        // X-axis labels
        final String[] labels = {"Employees", "Leaves", "Attendance"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);

        barChart.invalidate(); // REQUIRED
    }

    private void setupClickListeners() {

        cardManageUsers.setOnClickListener(v ->
                Toast.makeText(this, "Manage Users clicked", Toast.LENGTH_SHORT).show());

        cardLeaveRequests.setOnClickListener(v ->
                Toast.makeText(this, "Leave Requests clicked", Toast.LENGTH_SHORT).show());

        cardAttendance.setOnClickListener(v ->
                Toast.makeText(this, "Attendance clicked", Toast.LENGTH_SHORT).show());

        cardReports.setOnClickListener(v ->
                Toast.makeText(this, "Reports clicked", Toast.LENGTH_SHORT).show());
    }
}
