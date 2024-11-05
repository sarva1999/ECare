package com.example.e_carefinal;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.e_carefinal.DataRetrivalClass.UserDetails;
import com.example.e_carefinal.PatientFragments.MyAppointmentFragment;
import com.example.e_carefinal.PatientFragments.PatientSearchDiseaseFragment;
import com.example.e_carefinal.PatientFragments.PatientSearchDoctorsFragment;
import com.example.e_carefinal.PatientFragments.PendingAppointmentFragment;

public class PatientMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_patient);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(PatientMainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Check if the user is logged in
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("UserDetails")
                    .child(auth.getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            progressDialog.dismiss();
                            if (snapshot.exists()) {
                                UserDetails userDetails = snapshot.getValue(UserDetails.class);
                                if (userDetails != null && userDetails.getUserType().trim().equalsIgnoreCase("Patient")) {
                                    // Set values to UI elements
                                    ReusableFunctionsAndObjects.setValues(userDetails.getFirstName() + " " + userDetails.getLastName(), userDetails.getEmail(), userDetails.getMobileNo());
                                    TextView name = findViewById(R.id.name);
                                    TextView balance = findViewById(R.id.textUserBalance);
                                    name.setText(userDetails.getFirstName() + " " + userDetails.getLastName());
                                    name = findViewById(R.id.iniTv);
                                    name.setText(userDetails.getFirstName().charAt(0) + "" + userDetails.getLastName().charAt(0));
                                    balance.setText("Balance : " + userDetails.getUserBalance() + " Rs");

                                    // Set up the drawer layout
                                    drawerLayout = findViewById(R.id.drawer_layout);
                                    Toolbar toolbar = findViewById(R.id.toolBar);
                                    setSupportActionBar(toolbar);
                                    navigationView = findViewById(R.id.navigation_view);
                                    navigationView.setNavigationItemSelectedListener(PatientMainActivity.this);

                                    // Switch for Dark Mode
                                    SwitchCompat switchCompat = (SwitchCompat) navigationView.getMenu().findItem(R.id.nav_switch).getActionView();
                                    if (switchCompat != null) {
                                        switchCompat.setChecked(getSharedPreferences("STORAGE", MODE_PRIVATE).getBoolean("IS_DARKMODE_ENABLED", false));
                                        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                                                getSharedPreferences("STORAGE", MODE_PRIVATE).edit().putBoolean("IS_DARKMODE_ENABLED", isChecked).apply();
                                            }
                                        });
                                    }
                                }
                            } else {
                                progressDialog.dismiss();
                                showAlert("User details not found.");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                            showAlert("Database error: " + error.getMessage());
                        }
                    });
        } else {
            progressDialog.dismiss();
            showAlert("User not logged in.");
            // Optionally, redirect to login activity
            startActivity(new Intent(PatientMainActivity.this, LoginActivity.class));
            finish();
        }
    }

    // Method to show alert dialog
    private void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // Implement navigation logic based on item ID
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
