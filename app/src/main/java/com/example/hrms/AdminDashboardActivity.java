package com.example.hrms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        SessionGuard.enforcePunch(this);

        LinearLayout actionRoles = findViewById(R.id.actionRoles);
        LinearLayout actionOrg = findViewById(R.id.actionOrganization);
        LinearLayout actionPolicies = findViewById(R.id.actionSystemPolicies);
        LinearLayout actionLogout = findViewById(R.id.actionLogout);

        actionRoles.setOnClickListener(v ->
                startActivity(new Intent(this, AddUserActivity.class)));

        actionOrg.setOnClickListener(v ->
                startActivity(new Intent(this, OrganizationSetupActivity.class)));

        actionPolicies.setOnClickListener(v ->
                startActivity(new Intent(this, PolicyListActivity.class)));

        actionLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
