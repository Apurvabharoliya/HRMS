package com.example.hrms;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddUserActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etDepartment, etEmployeeId;
    private RadioGroup rgRole;
    private Button btnCreateUser;

    // Primary Auth (Admin stays logged in)
    private FirebaseAuth primaryAuth;

    // Secondary Auth (Used to create new users)
    private FirebaseAuth secondaryAuth;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Bind UI
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etDepartment = findViewById(R.id.etDepartment);
        etEmployeeId = findViewById(R.id.etEmployeeId);
        rgRole = findViewById(R.id.rgRole);
        btnCreateUser = findViewById(R.id.btnCreateUser);

        primaryAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize SECONDARY FirebaseAuth
        FirebaseOptions options = FirebaseApp.getInstance().getOptions();
        FirebaseApp secondaryApp;

        try {
            secondaryApp = FirebaseApp.initializeApp(this, options, "SecondaryApp");
        } catch (IllegalStateException e) {
            secondaryApp = FirebaseApp.getInstance("SecondaryApp");
        }

        secondaryAuth = FirebaseAuth.getInstance(secondaryApp);

        btnCreateUser.setOnClickListener(v -> validateAndCreateUser());
    }

    private void validateAndCreateUser() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String department = etDepartment.getText().toString().trim();
        String empId = etEmployeeId.getText().toString().trim();

        // Basic validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()
                || department.isEmpty() || empId.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedRoleId = rgRole.getCheckedRadioButtonId();
        if (selectedRoleId == -1) {
            Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show();
            return;
        }

        String role;
        if (selectedRoleId == R.id.rbEmployee) {
            role = "employee";
        } else if (selectedRoleId == R.id.rbManager) {
            role = "manager";
        } else {
            role = "hr";
        }

        // 🔥 Check if Employee ID already exists
        db.collection("users")
                .whereEqualTo("emp_id", empId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this,
                                "Employee ID already exists",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        createUserInAuth(name, email, password, department, empId, role);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Error checking Employee ID: " + e.getMessage(),
                                Toast.LENGTH_LONG).show()
                );
    }

    private void createUserInAuth(String name,
                                  String email,
                                  String password,
                                  String department,
                                  String empId,
                                  String role) {

        secondaryAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {

                    FirebaseUser newUser = authResult.getUser();
                    if (newUser == null) {
                        Toast.makeText(this,
                                "User creation failed",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String uid = newUser.getUid();

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("emp_id", empId);
                    userData.put("name", name);
                    userData.put("email", email);
                    userData.put("role", role);
                    userData.put("department", department);
                    userData.put("status", "active");
                    userData.put("createdBy",
                            primaryAuth.getCurrentUser() != null
                                    ? primaryAuth.getCurrentUser().getUid()
                                    : "system");

                    db.collection("users")
                            .document(uid)
                            .set(userData)
                            .addOnSuccessListener(aVoid -> {

                                Toast.makeText(this,
                                        "User added successfully",
                                        Toast.LENGTH_LONG).show();

                                secondaryAuth.signOut(); // Keep admin logged in
                                finish();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this,
                                            "Firestore error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show()
                            );

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Auth error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show()
                );
    }
}