package com.example.hrms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hrms.R;
import com.example.hrms.AdminDashboardActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar progressLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressLogin = findViewById(R.id.progressLogin);

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        progressLogin.setVisibility(View.VISIBLE);

        btnLogin.postDelayed(() -> {
            progressLogin.setVisibility(View.GONE);
            startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
            finish();
        }, 1200);
    }
}
