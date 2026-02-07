package com.example.hrms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    // UI components
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progessBar); // matches XML id

        // Firebase init
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Basic validation
        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        // UI state
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        fetchUserRole(user.getUid());
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);
                    Toast.makeText(LoginActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    private void fetchUserRole(@NonNull String uid) {
        firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    progressBar.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);

                    if (!documentSnapshot.exists()) {
                        Toast.makeText(this,
                                "User record not found",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    String role = documentSnapshot.getString("role");

                    if (role == null) {
                        Toast.makeText(this,
                                "Role not assigned",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    navigateByRole(role);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);
                    Toast.makeText(this,
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    private void navigateByRole(String role) {
        Intent intent;

        switch (role) {
            case "ADMIN":
                intent = new Intent(this, AdminDashboardActivity.class);
                break;

            case "HR":
                intent = new Intent(this, HRDashboardActivity.class);
                break;

            default:
                intent = new Intent(this, EmployeeDashboardActivity.class);
                break;
        }

        startActivity(intent);
        finish();
    }
}
