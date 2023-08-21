package com.example.animaladoptionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class UserDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavView);
        bottomNavView.setSelectedItemId(R.id.page1);
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

       /* RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        UserListAnimalAdapter mAdapter = new UserListAnimalAdapter(new LinkedList<AdminAddAnimalClass>(), UserDashboard.this);
        mRecyclerView.setAdapter(mAdapter); */
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        UserListAnimalAdapter mAdapter = new UserListAnimalAdapter(new LinkedList<AdminAddAnimalClass>(), UserDashboard.this, 2);
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

                Toast.makeText(UserDashboard.this, "Error", Toast.LENGTH_SHORT).show();
            }

        });

    }
}