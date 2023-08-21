package com.example.animaladoptionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    private TextView textViewWelcome, textViewFullName, textviewDob, textViewEmail, textViewGender, textViewMobile;
    private ProgressBar progressBar;
    private String name, email, doB, gender, mobile;
    private FirebaseAuth authProfile;
    private static final String TAG = UserProfile.class.getSimpleName();
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavView);
        bottomNavView.setSelectedItemId(R.id.page4);
        bottomNavView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.page1:
                                startActivity(new Intent(getApplicationContext(),UserDashboard.class));
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.page2:
                                startActivity(new Intent(getApplicationContext(),UserFeedback.class));
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.page3:
                                startActivity(new Intent(getApplicationContext(),UserFavourite.class));
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.page4:
                                startActivity(new Intent(getApplicationContext(),UserProfile.class));
                                overridePendingTransition(0,0);
                                return true;
                        }
                        return false;
                    }
                });

        //refresh
        swipeToRefresh();

        textViewWelcome = findViewById(R.id.textView_show_welcome);
        textViewFullName = findViewById(R.id.textView_show_full_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewGender = findViewById(R.id.textView_show_gender);
        textViewMobile = findViewById(R.id.textView_show_mobile);
        textviewDob = findViewById(R.id.textviewDob);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(UserProfile.this, "Something went wrong! User's details are not available at the moment",
                    Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

        //Update profile
        Button buttonUpdateProfile = findViewById(R.id.update_profile);
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserProfile.this, "You update your detail now!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserProfile.this, UserProfileUpdate.class));

            }
        });

        //Logout
        Button buttonLogout = findViewById(R.id.btn_logout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authProfile.signOut();
                Toast.makeText(UserProfile.this, "Logged Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserProfile.this, UserLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    } //second col

    private void swipeToRefresh() {

        //lookk up for the swipe container
        swipeContainer = findViewById(R.id.swipeContainer);

        //setup refresh
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //code to refresh
                startActivity(getIntent());
                finish();
                overridePendingTransition(0, 0);
                swipeContainer.setRefreshing(false);
            }
        });

        //configure color refresh
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    //retrieve data
    private void showUserProfile(FirebaseUser firebaseUser) {

        String userID = firebaseUser.getUid();

        //Extract user reference from database for register
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
        referenceProfile.child(userID).child("User Details").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserClass readUserDetails = snapshot.getValue(UserClass.class);
                if (readUserDetails != null) {
                    name = firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    doB = readUserDetails.doB;
                    gender = readUserDetails.gender;
                    mobile = readUserDetails.mobile;

                    textViewWelcome.setText("Welcome " + name + "!");
                    textViewFullName.setText(name);
                    textviewDob.setText(doB);
                    textViewEmail.setText(email);
                    textViewGender.setText(gender);
                    textViewMobile.setText(mobile);

                } else {
                    Toast.makeText(UserProfile.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, "Something went wrong! User's details are not available at the moment", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}