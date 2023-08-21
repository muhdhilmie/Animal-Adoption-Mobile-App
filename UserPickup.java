package com.example.animaladoptionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserPickup extends AppCompatActivity {

    private Button searchLoc;
    private String idPet;
    private TextView namePet, sexPet, agePet, breedPet, conditionPet, addressPet;
    private DatabaseReference ref;
    private FirebaseDatabase bk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pickup);

        searchLoc = findViewById(R.id.backHome);
        searchLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(UserPickup.this, "Search the location pickup", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserPickup.this, UserPickupLocation.class));

            }
        });

        namePet = findViewById(R.id.namePet);
        sexPet = findViewById(R.id.sexPet);
        agePet = findViewById(R.id.agePet);
        breedPet = findViewById(R.id.breedPet);
        conditionPet = findViewById(R.id.conditionPet);
        addressPet = findViewById(R.id.addressPet);

        Intent intent = getIntent();
        idPet = intent.getStringExtra("idPet");

     /*   String namePet1 = intent.getStringExtra("namePet");
        String sexPet1 = intent.getStringExtra("sexPet");
        String agePet1 = intent.getStringExtra("agePet");
        String breedPet1 = intent.getStringExtra("breedPet");
        String conditionPet1 = intent.getStringExtra("conditionPet");

        namePet.setText(namePet1);
        sexPet.setText(sexPet1);
        agePet.setText(agePet1);
        breedPet.setText(breedPet1);
        conditionPet.setText(conditionPet1); */

      bk = FirebaseDatabase.getInstance();
      ref = bk.getReference()
                .child("List Animal")
                .child(idPet);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get the trip data and set the values of the views in the layout
                AdminAddAnimalClass model = snapshot.getValue(AdminAddAnimalClass.class);
                namePet.setText(model.getNamePet());
                sexPet.setText(model.getSexPet());
                agePet.setText(model.getAgePet());
                breedPet.setText(model.getBreedPet());
                conditionPet.setText(model.getConditionPet());
                addressPet.setText(model.getAddressPet());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Toast.makeText(UserPickup.this, "Error loading details", Toast.LENGTH_SHORT).show();
            }
        });

    }
}