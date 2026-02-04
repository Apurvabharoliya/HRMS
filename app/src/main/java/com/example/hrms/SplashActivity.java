package com.example.hrms;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.firebase.FirebaseApp;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hrms.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseApp.initializeApp(this);

        new Handler().postDelayed(() -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                // User already logged in
                startActivity(new Intent(this, LoginActivity.class));
                // role routing will be added later
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        }, 2000);
    }
}
