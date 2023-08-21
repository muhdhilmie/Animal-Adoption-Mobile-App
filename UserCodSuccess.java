package com.example.animaladoptionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserCodSuccess extends AppCompatActivity {

    private EditText nameCOD, phoneCOD, addressCOD, timeCOD;
    private TextView namePet, sexPet, agePet, breedPet, conditionPet;
    private DatabaseReference ref;
    private FirebaseDatabase bk;
    private String idCOD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cod_success);

        nameCOD = findViewById(R.id.nameCOD);
        phoneCOD = findViewById(R.id.phoneCOD);
        addressCOD = findViewById(R.id.addressCOD);
        timeCOD = findViewById(R.id.timeCOD);
        namePet = findViewById(R.id.namePet);
        sexPet = findViewById(R.id.sexPet);
        agePet = findViewById(R.id.agePet);
        breedPet = findViewById(R.id.breedPet);
        conditionPet = findViewById(R.id.conditionPet);

        Intent intent = getIntent();
        idCOD = intent.getStringExtra("idCOD");

        String nameCOD1 = intent.getStringExtra("nameCOD");
        String phoneCOD1 = intent.getStringExtra("phoneCOD");
        String addressCOD1 = intent.getStringExtra("addressCOD");
        String timeCOD1 = intent.getStringExtra("timeCOD");
        String namePet1 = intent.getStringExtra("namePet");
        String sexPet1 = intent.getStringExtra("sexPet");
        String agePet1 = intent.getStringExtra("agePet");
        String breedPet1 = intent.getStringExtra("breedPet");
        String conditionPet1 = intent.getStringExtra("conditionPet");

        nameCOD.setText(nameCOD1);
        phoneCOD.setText(phoneCOD1);
        addressCOD.setText(addressCOD1);
        timeCOD.setText(timeCOD1);
        namePet.setText(namePet1);
        sexPet.setText(sexPet1);
        agePet.setText(agePet1);
        breedPet.setText(breedPet1);
        conditionPet.setText(conditionPet1);

        Button backHome = findViewById(R.id.backHome);
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserCodSuccess.this, UserDashboard.class));
            }
        });

    }
}