package com.example.hrms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView tvName = findViewById(R.id.tvProfileName);
        TextView tvRole = findViewById(R.id.tvProfileRole);
        TextView tvEmail = findViewById(R.id.tvProfileEmail);

        LinearLayout edit = findViewById(R.id.actionEditProfile);
        LinearLayout logout = findViewById(R.id.actionLogout);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(d -> {
                    tvName.setText(d.getString("name"));
                    tvRole.setText("Role: " + d.getString("role"));
                    tvEmail.setText(d.getString("email"));
                });

        edit.setOnClickListener(v ->
                startActivity(new Intent(this, EditProfileActivity.class)));

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
