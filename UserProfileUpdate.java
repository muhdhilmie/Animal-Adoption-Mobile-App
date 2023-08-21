package com.example.animaladoptionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserProfileUpdate extends AppCompatActivity {

    private EditText editTextUpdateName, editTextUpdateDoB, editTextUpdateMobile;
    private RadioGroup radioGroupUpdateGender;
    private RadioButton radioButtonUpdateGenderSelected;
    private String textFullName, textEmail, textDoB, textGender, textMobile;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;
    private TextView viewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_update);

        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavView);
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

        progressBar = findViewById(R.id.progressBar);
        editTextUpdateName = findViewById(R.id.editText_Update_profile_name);
        editTextUpdateDoB = findViewById(R.id.editText_update_profile_dob);
        editTextUpdateMobile = findViewById(R.id.editText_update_profile_mobile);
        viewEmail = findViewById(R.id.viewEmail);

        radioGroupUpdateGender = findViewById(R.id.radio_group_update_gender);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        //show profile
        showProfile(firebaseUser);


        //Setting up datepicker
        editTextUpdateDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //extrac different variables

                String textSADoB[] = textDoB.split("/");

                int day = Integer.parseInt(textSADoB[0]);
                int month = Integer.parseInt(textSADoB[1]) - 1;
                int year = Integer.parseInt(textSADoB[2]);

                DatePickerDialog picker;

                //date picker dialog
                picker = new DatePickerDialog(UserProfileUpdate.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextUpdateDoB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        //update profile
        Button buttonUpdateProfile = findViewById(R.id.button_update_profile);
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateProfile(firebaseUser);
            }
        });


    } //secondcol

    private void updateProfile(FirebaseUser firebaseUser) {

        int selectedGenderID = radioGroupUpdateGender.getCheckedRadioButtonId();
        radioButtonUpdateGenderSelected = findViewById(selectedGenderID);

        //Validate mobile
        String mobileRegex = "[0-9][0-9]{9}";
        Matcher mobileMatcher;
        Pattern mobilePattern = Pattern.compile(mobileRegex);
        mobileMatcher = mobilePattern.matcher(textMobile);

        if (TextUtils.isEmpty(textFullName)){
            Toast.makeText(UserProfileUpdate.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
            editTextUpdateName.setError("Full name is required");
            editTextUpdateName.requestFocus();
        }
        else if (TextUtils.isEmpty(textDoB)) {
            Toast.makeText(UserProfileUpdate.this, "Please insert your date of birth", Toast.LENGTH_SHORT).show();
            editTextUpdateDoB.setError("Date of Birth is required");
            editTextUpdateDoB.requestFocus();
        }
        else if (TextUtils.isEmpty(radioButtonUpdateGenderSelected.getText())) {
            Toast.makeText(UserProfileUpdate.this, "Please select your gender", Toast.LENGTH_SHORT).show();
            radioButtonUpdateGenderSelected.setError("Gender is required");
            radioButtonUpdateGenderSelected.requestFocus();
        }
        else if (TextUtils.isEmpty(textMobile)) {
            Toast.makeText(UserProfileUpdate.this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
            editTextUpdateMobile.setError("Phone number  is required");
            editTextUpdateMobile.requestFocus();
        }
        else if (textMobile.length() != 10) {
            Toast.makeText(UserProfileUpdate.this, "Please re-enter your mobile number", Toast.LENGTH_SHORT).show();
            editTextUpdateMobile.setError("Mobile number should be 10 digits");
            editTextUpdateMobile.requestFocus();
        }
        else if (!mobileMatcher.find()){
            Toast.makeText(UserProfileUpdate.this, "Please re-enter your mobile number", Toast.LENGTH_SHORT).show();
            editTextUpdateMobile.setError("Mobile number is not valid");
            editTextUpdateMobile.requestFocus();
        }
        else {
            //obtain data by user enter
            textGender = radioButtonUpdateGenderSelected.getText().toString();
            textEmail = viewEmail.getText().toString();
            textFullName = editTextUpdateName.getText().toString();
            textDoB = editTextUpdateDoB.getText().toString();
            textMobile = editTextUpdateMobile.getText().toString();

            //enter user data into the firebase real time, set up dependencies
            UserClass writeUserDetails = new UserClass(textFullName, textEmail, textDoB, textGender, textMobile);

            String userID = firebaseUser.getUid();

            //extract user reference "Register Users"
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(userID)
                    .child("User Details");

            progressBar.setVisibility(View.VISIBLE);

            referenceProfile.setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        //setting new display name
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().
                                setDisplayName(textFullName).build();

                        firebaseUser.updateProfile(profileUpdates);

                        Toast.makeText(UserProfileUpdate.this, "Updating Successful!", Toast.LENGTH_SHORT).show();

                        //stop user for returning update profile
                        Intent intent = new Intent(UserProfileUpdate.this, UserProfile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else {

                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            Toast.makeText(UserProfileUpdate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    //fetch data from firebase and display
    private void showProfile(FirebaseUser firebaseUser) {

        String userIDofRegistered = firebaseUser.getUid();

        //extract user reference from database for " register user"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userIDofRegistered)
                .child("User Details");

        progressBar.setVisibility(View.VISIBLE);

        referenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserClass readWriteUserDetails = snapshot.getValue(UserClass.class);

                if(readWriteUserDetails != null) {
                    textFullName =firebaseUser.getDisplayName();
                    textDoB = readWriteUserDetails.doB;
                    textGender = readWriteUserDetails.gender;
                    textMobile = readWriteUserDetails.mobile;
                    textEmail = readWriteUserDetails.email;

                    editTextUpdateName.setText(textFullName);
                    editTextUpdateDoB.setText(textDoB);
                    editTextUpdateMobile.setText(textMobile);
                    viewEmail.setText(textEmail);

                    //show gender through radio button
                    if (textGender.equals("Male")) {

                        radioButtonUpdateGenderSelected = findViewById(R.id.radio_male);
                    }
                    else {

                        radioButtonUpdateGenderSelected = findViewById(R.id.radio_female);
                    }
                    radioButtonUpdateGenderSelected.setChecked(true);
                }
                else {

                    Toast.makeText(UserProfileUpdate.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UserProfileUpdate.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}