package com.example.animaladoptionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class AdminDashboard extends AppCompatActivity {

    private RelativeLayout registerAnimal;
    private ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        logout = findViewById(R.id.imglogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UserLogin.class));
                overridePendingTransition(0,0);
            }
        });

        registerAnimal = findViewById(R.id.layout);
        registerAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminAddAnimal.class));
                overridePendingTransition(0,0);
            }
        });

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdminAddAnimalAdapter mAdapter = new AdminAddAnimalAdapter(new LinkedList<AdminAddAnimalClass>(), AdminDashboard.this);
        mRecyclerView.setAdapter(mAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("List Animal");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                LinkedList<AdminAddAnimalClass> eventList = new LinkedList<>();

                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    AdminAddAnimalClass event = eventSnapshot.getValue(AdminAddAnimalClass.class);
                    eventList.addFirst(event);
                }
                mAdapter.listPet = eventList;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error

                Toast.makeText(AdminDashboard.this, "Error", Toast.LENGTH_SHORT).show();
            }

        });

    }
}