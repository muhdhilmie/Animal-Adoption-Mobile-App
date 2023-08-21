package com.example.animaladoptionapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class UserListAnimalDetails extends AppCompatActivity {

    private TextView namePet, sexPet, agePet, breedPet, conditionPet;
    private Button adoptPet;
    private ImageView addFavourite, imgPet;
    private String idPet;
    private DatabaseReference ref;
    private FirebaseDatabase bk;
    private String imgPetUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_animal_details);

        Intent intent = getIntent();
        idPet = intent.getStringExtra("idPet");

        namePet = findViewById(R.id.namePet);
        sexPet = findViewById(R.id.sexPet);
        agePet = findViewById(R.id.agePet);
        breedPet = findViewById(R.id.breedPet);
        conditionPet = findViewById(R.id.conditionPet);
        adoptPet = findViewById(R.id.adoptPet);
        addFavourite = findViewById(R.id.imgaddfav);
        imgPet = findViewById(R.id.img1);

        // Load the details from Firebase
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
                Glide.with(UserListAnimalDetails.this)
                        .load(model.getImgPet())
                        .into(imgPet);

                imgPetUrl = model.getImgPet();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Toast.makeText(UserListAnimalDetails.this, "Error loading details", Toast.LENGTH_SHORT).show();
            }
        });

        adoptPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the layout for the dialog
                LayoutInflater inflater = LayoutInflater.from(UserListAnimalDetails.this);
                View dialogView = inflater.inflate(R.layout.dialog_adoption_options, null);

                // Get the RadioGroup and RadioButton views
                RadioGroup radioGroupOptions = dialogView.findViewById(R.id.radioGroupOptions);
                RadioButton radioButtonCOD = dialogView.findViewById(R.id.radioButtonCOD);
                RadioButton radioButtonPickUp = dialogView.findViewById(R.id.radioButtonPickUp);

                AlertDialog.Builder builder = new AlertDialog.Builder(UserListAnimalDetails.this);
                builder.setView(dialogView);
                builder.setTitle("Choose Adoption Option");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedOptionId = radioGroupOptions.getCheckedRadioButtonId();

                        if (selectedOptionId == radioButtonCOD.getId()) {
                            // User selected COD option

                            String namePet2 = namePet.getText().toString();
                            String sexPet2 = sexPet.getText().toString();
                            String agePet2 = agePet.getText().toString();
                            String breedPet2 = breedPet.getText().toString();
                            String conditionPet2 = conditionPet.getText().toString();

                            Intent intent = new Intent(UserListAnimalDetails.this, UserCod.class);
                            intent.putExtra("namePet", namePet2);
                            intent.putExtra("sexPet", sexPet2);
                            intent.putExtra("agePet", agePet2);
                            intent.putExtra("breedPet", breedPet2);
                            intent.putExtra("conditionPet", conditionPet2);

                            startActivity(intent);

                        } else if (selectedOptionId == radioButtonPickUp.getId()) {

                            // User selected Pick Up option

                           /* String namePet3 = namePet.getText().toString();
                            String sexPet3 = sexPet.getText().toString();
                            String agePet3 = agePet.getText().toString();
                            String breedPet3 = breedPet.getText().toString();
                            String conditionPet3 = conditionPet.getText().toString();

                            Intent intent = new Intent(UserListAnimalDetails.this, UserPickup.class);
                            intent.putExtra("namePet", namePet3);
                            intent.putExtra("sexPet", sexPet3);
                            intent.putExtra("agePet", agePet3);
                            intent.putExtra("breedPet", breedPet3);
                            intent.putExtra("conditionPet", conditionPet3);
                            startActivity(intent); */
                            //startActivity(new Intent(UserListAnimalDetails.this, UserPickup.class));

                            Intent intent = new Intent(UserListAnimalDetails.this, UserPickup.class);
                            intent.putExtra("idPet", idPet);
                            startActivity(intent);
                        }

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();
            }
        });

        addFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String namePet1 = namePet.getText().toString();
                String sexPet1 = sexPet.getText().toString();
                String agePet1 = agePet.getText().toString();
                String breedPet1 = breedPet.getText().toString();
                String conditionPet1 = conditionPet.getText().toString();

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference favouriteRef = bk.getReference()
                        .child("Users")
                        .child(userId)
                        .child("My Favourite");

                UserFavouriteClass model = new UserFavouriteClass(idPet, namePet1, sexPet1, agePet1, breedPet1, conditionPet1, imgPetUrl);

                favouriteRef.child(idPet).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(getApplicationContext(), "My Favourite ...", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }



}