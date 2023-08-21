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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserCod extends AppCompatActivity {

    private EditText nameCOD, phoneCOD, addressCOD, timeCOD;
    private Button confirmCOD;
    private TextView namePet, sexPet, agePet, breedPet, conditionPet;
    private String idPet;
    private DatabaseReference ref;
    private FirebaseDatabase bk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cod);

        nameCOD = findViewById(R.id.nameCOD);
        phoneCOD = findViewById(R.id.phoneCOD);
        addressCOD = findViewById(R.id.addressCOD);
        timeCOD = findViewById(R.id.timeCOD);
        namePet = findViewById(R.id.namePet);
        sexPet = findViewById(R.id.sexPet);
        agePet = findViewById(R.id.agePet);
        breedPet = findViewById(R.id.breedPet);
        conditionPet = findViewById(R.id.conditionPet);
        confirmCOD = findViewById(R.id.confirmCOD);

        Intent intent = getIntent();
        String namePet1 = intent.getStringExtra("namePet");
        String sexPet1 = intent.getStringExtra("sexPet");
        String agePet1 = intent.getStringExtra("agePet");
        String breedPet1 = intent.getStringExtra("breedPet");
        String conditionPet1 = intent.getStringExtra("conditionPet");

        namePet.setText(namePet1);
        sexPet.setText(sexPet1);
        agePet.setText(agePet1);
        breedPet.setText(breedPet1);
        conditionPet.setText(conditionPet1);

        confirmCOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameCOD1 = nameCOD.getText().toString();
                String phoneCOD1 = phoneCOD.getText().toString();
                String addressCOD1 = addressCOD.getText().toString();
                String timeCOD1 = timeCOD.getText().toString();
                String namePet2 = namePet.getText().toString();
                String sexPet2 = sexPet.getText().toString();
                String agePet2 = agePet.getText().toString();
                String breedPet2 = breedPet.getText().toString();
                String conditionPet2 = conditionPet.getText().toString();

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference()
                        .child("Users")
                        .child(userId)
                        .child("My COD");
                String idCOD = ref.push().getKey();

                UserCodClass model = new UserCodClass(idCOD, nameCOD1, phoneCOD1, addressCOD1, timeCOD1,
                        namePet2, sexPet2, agePet2, breedPet2, conditionPet2);

                ref.child(idCOD).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Intent intent = new Intent(UserCod.this, UserCodSuccess.class);
                        intent.putExtra("idCOD", idCOD);
                        intent.putExtra("nameCOD", nameCOD1);
                        intent.putExtra("phoneCOD", phoneCOD1);
                        intent.putExtra("addressCOD", addressCOD1);
                        intent.putExtra("timeCOD", timeCOD1);
                        intent.putExtra("namePet", namePet2);
                        intent.putExtra("sexPet", sexPet2);
                        intent.putExtra("agePet", agePet2);
                        intent.putExtra("breedPet", breedPet2);
                        intent.putExtra("conditionPet", conditionPet2);

                        startActivity(intent);

                    }
                });

            }
        });

    }
}