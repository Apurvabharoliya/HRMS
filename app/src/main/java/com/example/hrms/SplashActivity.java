package com.example.hrms;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;


import com.google.firebase.FirebaseApp;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hrms.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        new Handler(Looper.getMainLooper()).postDelayed(this::navigateNext, 2500);
    }

    private void navigateNext() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // User not logged in
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            // User logged in → role check later
            startActivity(new Intent(this, LoginActivity.class));
            // (Dashboard routing will be added after login screen)
        }
        finish();
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
