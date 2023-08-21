package com.example.animaladoptionapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;

public class UserFavouriteAdapter extends RecyclerView.Adapter<UserFavouriteAdapter.ViewHolder> {

    LinkedList<UserFavouriteClass> listPet;
    private Context context;

    public UserFavouriteAdapter(LinkedList<UserFavouriteClass> listPet, Context context) {
        this.listPet = listPet;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_favourite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserFavouriteClass model = listPet.get(position);

        holder.namePet.setText(model.getNamePet());
        Glide.with(context)
                .load(model.getImgPet())
                .into(holder.imgPet);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UserListAnimalDetails.class);
                intent.putExtra("idPet", model.getIdPet());
                context.startActivity(intent);
            }
        });

       holder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference()
                                .child("Users")
                                .child(userId)
                                .child("My Favourite");

                        ref.child(model.getIdPet()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(context, "remove", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(context, "failed to remove", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).setMessage("Are sure you want to remove?");
                builder.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return listPet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView namePet;
        ImageView imgPet, imgdelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            namePet = itemView.findViewById(R.id.namePet);
            imgPet = itemView.findViewById(R.id.img);
            imgdelete = itemView.findViewById(R.id.imgdelete);
        }
    }
}
