package com.example.animaladoptionapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.LinkedList;

public class UserListAnimalAdapter extends RecyclerView.Adapter<UserListAnimalAdapter.ViewHolder> {

    LinkedList<AdminAddAnimalClass> listPet;
    private Context context;
    private int numColumns;

    /*public UserListAnimalAdapter(LinkedList<AdminAddAnimalClass> listPet, Context context) {
        this.listPet = listPet;
        this.context = context;
    } */
    public UserListAnimalAdapter(LinkedList<AdminAddAnimalClass> listPet, Context context, int numColumns) {
        this.listPet = listPet;
        this.context = context;
        this.numColumns = numColumns;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_animal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AdminAddAnimalClass model = listPet.get(position);

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

    }

    @Override
    public int getItemCount() {
        return listPet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView namePet;
        ImageView imgPet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            namePet = itemView.findViewById(R.id.namePet);
            imgPet = itemView.findViewById(R.id.img);
        }
    }
}
