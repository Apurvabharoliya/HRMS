package com.example.hrms;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        EditText etName = findViewById(R.id.etEditName);
        Button btnSave = findViewById(R.id.btnSaveProfile);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (name.isEmpty()) return;

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(uid)
                    .update("name", name)
                    .addOnSuccessListener(a -> {
                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                        finish();
                    });
        });
    }
}
