package com.example.hrms;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SessionGuard {

    public static void enforcePunch(Activity activity) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            activity.startActivity(new Intent(activity, LoginActivity.class));
            activity.finish();
            return;
        }

        String uid = FirebaseAuth.getInstance().getUid();
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());

        FirebaseFirestore.getInstance()
                .collection("attendance")
                .document(uid + "_" + today)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists() || doc.getString("punchIn") == null) {
                        activity.startActivity(new Intent(activity, PunchActivity.class));
                        activity.finish();
                    }
                });
    }
}
