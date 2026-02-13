package com.example.hrms;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvGoRegister;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // UI
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoRegister = findViewById(R.id.tvGoRegister);

        btnLogin.setOnClickListener(v -> loginUser());

        tvGoRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter valid email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }

        // Firebase Auth Login
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                fetchUserRole(user.getUid());
                            }

                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Fetch role from Firestore and redirect accordingly
     */
    private void fetchUserRole(String uid) {

        db.collection("users")
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful() && task.getResult() != null) {

                        DocumentSnapshot document = task.getResult();
                        String role = document.getString("role");

                        if (role == null) {
                            Toast.makeText(this,
                                    "User role not assigned",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        switch (role) {

                            case "admin":
                                startActivity(new Intent(this, AdminDashboardActivity.class));
                                break;

                            case "hr":
                                startActivity(new Intent(this, HRDashboardActivity.class));
                                break;

//                            case "Manager":
//                                startActivity(new Intent(this, ManagerDashboardActivity.class));
//                                break;

                            case "employee":
                                startActivity(new Intent(this, EmployeeDashboardActivity.class));
                                break;

                            default:
                                Toast.makeText(this,
                                        "Unauthorized role",
                                        Toast.LENGTH_SHORT).show();
                                return;
                        }

                        finish();

                    } else {
                        Toast.makeText(this,
                                "Failed to retrieve user details",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
