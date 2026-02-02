package com.example.hrms;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EmployeeListActivity extends AppCompatActivity {

    private List<EmployeeModel> list = new ArrayList<>();
    private List<EmployeeModel> filtered = new ArrayList<>();
    private EmployeeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        RecyclerView rv = findViewById(R.id.rvEmployees);
        EditText etSearch = findViewById(R.id.etSearchEmployee);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EmployeeAdapter(filtered);
        rv.setAdapter(adapter);

        loadEmployees();

        etSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            public void onTextChanged(CharSequence s, int st, int b, int c) {
                filter(s.toString());
            }
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadEmployees() {
        FirebaseFirestore.getInstance()
                .collection("users")
                .get()
                .addOnSuccessListener(snapshot -> {
                    list.clear();
                    filtered.clear();
                    for (var doc : snapshot) {
                        EmployeeModel m = doc.toObject(EmployeeModel.class);
                        list.add(m);
                        filtered.add(m);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void filter(String q) {
        filtered.clear();
        for (EmployeeModel m : list) {
            if (m.getName().toLowerCase().contains(q.toLowerCase())) {
                filtered.add(m);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
