package com.example.animaladoptionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdminAddAnimalDetails extends AppCompatActivity {

    private EditText namePet, agePet, breedPet, conditionPet, addressPet;
    private Spinner sexPet, agetypePet;
    private Button updatePet, selectImg;
    private ImageView imgPet, deletePet;
    private static final int REQUEST_CODE_IMAGE = 101;
    private String idPet;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_animal_details);

        Intent intent = getIntent();
        idPet = intent.getStringExtra("idPet");

        namePet = findViewById(R.id.namePet);
        sexPet = findViewById(R.id.sexPet);
        agePet = findViewById(R.id.agePet);
        breedPet = findViewById(R.id.breedPet);
        conditionPet = findViewById(R.id.conditionPet);
        agetypePet = findViewById(R.id.agetypePet);
        addressPet = findViewById(R.id.addressPet);
        selectImg = findViewById(R.id.button);
        updatePet = findViewById(R.id.submit);
        deletePet = findViewById(R.id.imgdelete);
        imgPet = findViewById(R.id.img1);

        // Load the details from Firebase
        FirebaseDatabase bk = FirebaseDatabase.getInstance();
        ref = bk.getReference()
                .child("List Animal")
                .child(idPet);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get the trip data and set the values of the views in the layout
                AdminAddAnimalClass model = snapshot.getValue(AdminAddAnimalClass.class);
                namePet.setText(model.getNamePet());
                agePet.setText(model.getAgePet());
                breedPet.setText(model.getBreedPet());
                conditionPet.setText(model.getConditionPet());
                addressPet.setText(model.getAddressPet());
                Glide.with(AdminAddAnimalDetails.this)
                        .load(model.getImgPet())
                        .into(imgPet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Toast.makeText(AdminAddAnimalDetails.this, "Error loading details", Toast.LENGTH_SHORT).show();
            }
        });

        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });

        updatePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the updated values from the EditText views
                String namePet1 = namePet.getText().toString();
                String sexPet1 = sexPet.getSelectedItem().toString();
                String agePet1 = agePet.getText().toString() +" "+ agetypePet.getSelectedItem().toString();
                String breedPet1 = breedPet.getText().toString();
                String conditionPet1 = conditionPet.getText().toString();
                String addressPet1 = addressPet.getText().toString();

                // Update the details in Firebase
                ref.child("namePet").setValue(namePet1);
                ref.child("sexPet").setValue(sexPet1);
                ref.child("agePet").setValue(agePet1);
                ref.child("breedPet").setValue(breedPet1);
                ref.child("conditionPet").setValue(conditionPet1);
                ref.child("addressPet").setValue(addressPet1);

                // Show a success message
                Toast.makeText(AdminAddAnimalDetails.this, "details updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminAddAnimalDetails.this, AdminDashboard.class);
                startActivity(intent);
            }
        });

        deletePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete from Firebase
                ref.removeValue();

                // Show a success message and finish the activity
                Toast.makeText(AdminAddAnimalDetails.this, "Delete Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminAddAnimalDetails.this, AdminDashboard.class);
                overridePendingTransition(0,0);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imgPet.setImageURI(imageUri);
            imgPet.setTag(imageUri.toString());

            uploadImageToFirebaseStorage(imageUri);
        }

    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("List Animal/" + idPet + ".jpg");

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL of the uploaded image
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                // Update the 'imgPet' field in the Firebase Realtime Database with the download URL
                                ref.child("imgPet").setValue(downloadUri.toString());
                                Glide.with(AdminAddAnimalDetails.this)
                                        .load(downloadUri)
                                        .into(imgPet);
                                Toast.makeText(AdminAddAnimalDetails.this, "Image updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminAddAnimalDetails.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}