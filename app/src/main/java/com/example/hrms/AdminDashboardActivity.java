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

import java.util.ArrayList;

public class AdminDashboardActivity extends AppCompatActivity {

    private CardView cardManageUsers, cardLeaveRequests, cardAttendance, cardReports;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize views
        barChart = findViewById(R.id.barChart);
        cardManageUsers = findViewById(R.id.cardManageUsers);
        cardLeaveRequests = findViewById(R.id.cardLeaveRequests);
        cardAttendance = findViewById(R.id.cardAttendance);
        cardReports = findViewById(R.id.cardReports);

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
