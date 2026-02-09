package com.example.hrms;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hrms.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2200; // 2.2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.imgLogo);

        // Scale animation (subtle, professional)
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.85f, 1.0f,
                0.85f, 1.0f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(900);
        scaleAnimation.setFillAfter(true);

        // Fade animation
        AlphaAnimation fadeAnimation = new AlphaAnimation(0f, 1f);
        fadeAnimation.setDuration(900);
        fadeAnimation.setFillAfter(true);

        // Start animations
        logo.startAnimation(scaleAnimation);
        logo.startAnimation(fadeAnimation);

        // Move to Login screen after delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
    }
}
