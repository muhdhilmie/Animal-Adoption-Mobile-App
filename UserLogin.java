package com.example.animaladoptionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class UserLogin extends AppCompatActivity {

    private EditText editTextLoginEmail, editTextLoginPwd;
    private ProgressBar progressBar;
    private TextView textViewRegister;
    private FirebaseAuth authProfile;
    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        editTextLoginEmail = findViewById(R.id.editText);
        editTextLoginPwd = findViewById(R.id.editText1);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();

        //Register in login
        textViewRegister = findViewById(R.id.textview);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserLogin.this, "You can register now", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserLogin.this, UserRegister.class));
            }
        });

        //login user
        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = editTextLoginEmail.getText().toString();
                String textPwd = editTextLoginPwd.getText().toString();

                if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(UserLogin.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Email is required");
                    editTextLoginEmail.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(UserLogin.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError(" valid email is required");
                    editTextLoginEmail.requestFocus();
                }
                else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(UserLogin.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    editTextLoginPwd.setError("Password is required");
                    editTextLoginPwd.requestFocus();
                }
                else if (textPwd.length() < 6 ) {
                    Toast.makeText(UserLogin.this, "Password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    editTextLoginPwd.setError("Password is required");
                    editTextLoginPwd.requestFocus();
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail, textPwd);
                }
            }
        });
    }

    private void loginUser(String email, String pwd) {

        // Check if the email and password belong to a manager account
        if (email.equals("admin@animaladoption.com") && pwd.equals("admin123")) {

            // If the credentials are correct, start the dashboard activity for managers
            Toast.makeText(UserLogin.this, "You are logged in as a admin", Toast.LENGTH_LONG).show();
            startActivity(new Intent(UserLogin.this, AdminDashboard.class));
            return;

        }

        authProfile.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(UserLogin.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    //get instance curent user
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();
                    Toast.makeText(UserLogin.this, "You are logged in now", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(UserLogin.this, UserDashboard.class));

                }
                else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        editTextLoginEmail.setError("User does not exists or is no longer valid. Please register again.");
                        editTextLoginEmail.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        editTextLoginEmail.setError("Invalid credentials, Kindly, check and re-enter");
                        editTextLoginEmail.requestFocus();
                    } catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(UserLogin.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}