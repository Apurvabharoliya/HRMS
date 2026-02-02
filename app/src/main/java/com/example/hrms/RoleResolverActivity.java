package com.example.hrms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RoleResolverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String uid = auth.getCurrentUser().getUid();

        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) {
                        Toast.makeText(this, "User profile not found", Toast.LENGTH_SHORT).show();
                        auth.signOut();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                        return;
                    }

                    String role = doc.getString("role");

                    if ("admin".equalsIgnoreCase(role)) {
                        startActivity(new Intent(this, AdminDashboardActivity.class));
                    } else if ("hr".equalsIgnoreCase(role)) {
                        startActivity(new Intent(this, HRDashboardActivity.class));
                    } else {
                        startActivity(new Intent(this, EmployeeDashboardActivity.class));
                    }

                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    auth.signOut();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                });
    }
}
