package com.example.animaladoptionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRegister extends AppCompatActivity {

    private EditText editTextRegisterFullName, editTextRegisterEmail, editTextRegisterDoB, editTextRegisterMobile, editTextRegisterPwd, editTextRegisterConfirmPwd;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;
    private DatePickerDialog picker;
    private TextView textViewRegisterLogin;
    private static final String TAG = "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        progressBar = findViewById(R.id.progressBar);
        editTextRegisterFullName = findViewById(R.id.editText_register_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDoB = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_hp);
        editTextRegisterPwd = findViewById(R.id.editText_register_pwd);
        editTextRegisterConfirmPwd = findViewById(R.id.editText_register_confirm_pwd);

        //Radio button gender
        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();

        //Setting up datepicker
        editTextRegisterDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //date picker dialog
                picker = new DatePickerDialog(UserRegister.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextRegisterDoB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        //login in register
        textViewRegisterLogin = findViewById(R.id.textView_register_login);
        textViewRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserRegister.this, "You can login now", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserRegister.this, UserLogin.class));
            }
        });

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedGenderId= radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected = findViewById(selectedGenderId);

                String textFullName= editTextRegisterFullName.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textDoB = editTextRegisterDoB.getText().toString();
                String textMobile = editTextRegisterMobile.getText().toString();
                String textPwd = editTextRegisterPwd.getText().toString();
                String textConfirmPwd = editTextRegisterConfirmPwd.getText().toString();
                String textGender;

                //Validate mobile
                String mobileRegex = "[0-9][0-9]{9}";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(textMobile);

                if (TextUtils.isEmpty(textFullName)){
                    Toast.makeText(UserRegister.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
                    editTextRegisterFullName.setError("Full name is required");
                    editTextRegisterFullName.requestFocus();
                }
                else if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(UserRegister.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Email is required");
                    editTextRegisterEmail.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(UserRegister.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Email is required");
                    editTextRegisterEmail.requestFocus();
                }
                else if (TextUtils.isEmpty(textDoB)) {
                    Toast.makeText(UserRegister.this, "Please insert your date of birth", Toast.LENGTH_SHORT).show();
                    editTextRegisterDoB.setError("Date of Birth is required");
                    editTextRegisterDoB.requestFocus();
                }
                else if (radioGroupRegisterGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(UserRegister.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                    radioButtonRegisterGenderSelected.setError("Gender is required");
                    radioButtonRegisterGenderSelected.requestFocus();
                }
                else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(UserRegister.this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("Phone number  is required");
                    editTextRegisterMobile.requestFocus();
                }
                else if (textMobile.length() != 10) {
                    Toast.makeText(UserRegister.this, "Please re-enter your mobile number", Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("Mobile number should be 10 digits");
                    editTextRegisterMobile.requestFocus();
                }
                else if (!mobileMatcher.find()){
                    Toast.makeText(UserRegister.this, "Please re-enter your mobile number", Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("Mobile number is not valid");
                    editTextRegisterMobile.requestFocus();
                }
                else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(UserRegister.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Password is required");
                    editTextRegisterPwd.requestFocus();
                }
                else if (textPwd.length() < 6 ) {
                    Toast.makeText(UserRegister.this, "Password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Password too weak");
                    editTextRegisterPwd.requestFocus();
                }
                else if (TextUtils.isEmpty(textConfirmPwd)){
                    Toast.makeText(UserRegister.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                    editTextRegisterConfirmPwd.setError("Password Confirmation is required");
                    editTextRegisterConfirmPwd.requestFocus();
                }
                else if (!textPwd.equals(textConfirmPwd)) {
                    Toast.makeText(UserRegister.this, "Please use same password", Toast.LENGTH_SHORT).show();
                    editTextRegisterConfirmPwd.setError("Password Confirmation is required");
                    editTextRegisterConfirmPwd.requestFocus();

                    editTextRegisterPwd.clearComposingText();
                    editTextRegisterConfirmPwd.clearComposingText();
                }
                else {
                    textGender = radioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName, textEmail, textDoB, textGender, textMobile, textPwd);
                }

            }
        });
    } // second col
    // Register using credential
    private void registerUser(String textFullName, String textEmail, String textDoB, String textGender, String textMobile, String textPwd) {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        //create user profile
        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(UserRegister.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser firebaseuser = auth.getCurrentUser();

                    //update display name of user
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                    firebaseuser.updateProfile(profileChangeRequest);

                    // user data into realtime database
                    UserClass writeUserDetails = new UserClass(textFullName, textEmail, textDoB, textGender, textMobile);

                    //extract user reference from database for register user
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(firebaseuser.getUid())
                            .child("User Details");

                    referenceProfile.setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                Toast.makeText(UserRegister.this, " User registered successfully", Toast.LENGTH_LONG).show();

                                //open user profile after succes
                                Intent intent = new Intent (UserRegister.this, UserDashboard.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish(); // close register

                            } else {

                                Toast.makeText(UserRegister.this, " User registered failed. Please try again", Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
                else{
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        editTextRegisterPwd.setError("Your password is too weak, kindly use a mix of alphabets, numbers, and special characters");
                        editTextRegisterPwd.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        editTextRegisterEmail.setError("Your email is invalid or already in use. kindly re-enter");
                        editTextRegisterEmail.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e) {
                        editTextRegisterEmail.setError("User is already registered with this email. Use another email.");
                        editTextRegisterEmail.requestFocus();
                    } catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(UserRegister.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

} // first col
