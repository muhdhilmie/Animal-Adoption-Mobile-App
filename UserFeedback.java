package com.example.animaladoptionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserFeedback extends AppCompatActivity {

    private ImageView sentFeedback;
    private EditText textFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);

        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavView);
        bottomNavView.setSelectedItemId(R.id.page2);
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

        sentFeedback = findViewById(R.id.imgsent);
        textFeedback = findViewById(R.id.textview2);

        sentFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textFeedback1 = textFeedback.getText().toString();

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference()
                        .child("Users")
                        .child(userId)
                        .child("My Feedback");
                String idFeedback = ref.push().getKey();

                UserFeedbackClass model = new UserFeedbackClass(idFeedback, textFeedback1);

                ref.child(idFeedback).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        textFeedback1.trim();

                        Toast.makeText(getApplicationContext(), "Succes Sent Feedback", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}