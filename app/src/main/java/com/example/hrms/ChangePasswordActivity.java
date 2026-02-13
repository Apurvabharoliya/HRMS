package com.example.hrms;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Button btnSendReset = findViewById(R.id.btnSendReset);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        btnSendReset.setOnClickListener(v -> {
            if (user != null && user.getEmail() != null) {
                FirebaseAuth.getInstance()
                        .sendPasswordResetEmail(user.getEmail())
                        .addOnSuccessListener(a ->
                                Toast.makeText(this,
                                        "Password reset link sent to email",
                                        Toast.LENGTH_LONG).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(this,
                                        "Failed to send reset link",
                                        Toast.LENGTH_SHORT).show());
            }
        });
    }
}