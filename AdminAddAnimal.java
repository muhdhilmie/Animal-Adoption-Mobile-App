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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AdminAddAnimal extends AppCompatActivity {

    private EditText namePet, agePet, breedPet, conditionPet, addressPet;
    private Spinner sexPet, agetypePet;
    private Button registerPet, selectImg;
    private ImageView imgPet;
    private static final int REQUEST_CODE_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_animal);

        namePet = findViewById(R.id.namePet);
        sexPet = findViewById(R.id.sexPet);
        agePet = findViewById(R.id.agePet);
        breedPet = findViewById(R.id.breedPet);
        conditionPet = findViewById(R.id.conditionPet);
        agetypePet = findViewById(R.id.agetypePet);
        addressPet = findViewById(R.id.addressPet);
        selectImg = findViewById(R.id.button);
        registerPet = findViewById(R.id.submit);
        imgPet = findViewById(R.id.img1);

        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });

        registerPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String namePet1 = namePet.getText().toString();
                String sexPet1 = sexPet.getSelectedItem().toString();
                String agePet1 = agePet.getText().toString() +" "+ agetypePet.getSelectedItem().toString();
                String breedPet1 = breedPet.getText().toString();
                String conditionPet1 = conditionPet.getText().toString();
                String addressPet1 = addressPet.getText().toString();
                String statusPet1 = "Available";

                // Get a reference to the Firebase Storage location where you want to upload the image
                StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                        .child("List Animal").child(UUID.randomUUID().toString());

                // Get the image URI from the imageView
                Uri imageUri = imgPet.getTag() != null ? Uri.parse(imgPet.getTag().toString()) : null;

                // If an image was selected, upload it to Firebase Storage
                if (imageUri != null) {
                    // Use a listener to track the upload progress and show a progress dialog if desired
                    UploadTask uploadTask = storageRef.putFile(imageUri);
                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // Show a progress dialog if desired
                        }
                    });

                    // Once the upload is complete, get the download URL and save it to the Firebase Realtime Database
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Get the download URL from the storage reference and return it
                            return storageRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                // Save the download URL to the Firebase Realtime Database
                                String ImageUrl = task.getResult().toString();

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("List Animal");
                                String idPet = myRef.push().getKey();
                                AdminAddAnimalClass model = new AdminAddAnimalClass(idPet, namePet1, sexPet1, agePet1, breedPet1, conditionPet1, addressPet1, statusPet1, ImageUrl);

                                myRef.child(idPet).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(getApplicationContext(), "Add Pet Successfull ...", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AdminAddAnimal.this, AdminDashboard.class));
                                    }
                                });
                            } else {
                                // Handle errors
                            }
                        }
                    });
                } else {
                    // If no image was selected, save the trip without an image URL

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("List Animal");
                    String idPet = myRef.push().getKey();
                    AdminAddAnimalClass model = new AdminAddAnimalClass(idPet, namePet1, sexPet1, agePet1, breedPet1, conditionPet1, addressPet1, statusPet1, "");

                    myRef.child(idPet).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(getApplicationContext(), "Add Pet Successfull ...", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AdminAddAnimal.this, AdminDashboard.class));
                        }
                    });
                }

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
        }
    }

}