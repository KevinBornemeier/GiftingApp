package com.example.giftingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;



public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private List<Profile> profileList;
    private Context context;

    public MyAdapter(Context context, List<Profile> profileList) {
        this.profileList = profileList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.list_profile, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Profile profile = profileList.get(position);

        holder.textViewDisplayName.setText(profile.getName());
        holder.textViewDisplayUserID.setText("UserID: " + profile.getUserID());
        holder.textViewDisplayImageUrl.setText("ImageUrl: " + profile.getImageUrl());
        holder.textViewDisplayShirtSize.setText("Shirt size: " + profile.getShirtSize());
        holder.textViewDisplayPantsSize.setText("Pants size: " + profile.getPantsSize());
        holder.textViewDisplayShoeSize.setText("Shoe size: " + profile.getShoeSize());
        holder.textViewDisplayFavoriteColor.setText("Favorite color: " + profile.getFavoriteColor());


    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewDisplayName;
        public TextView textViewDisplayUserID;
        public TextView textViewDisplayImageUrl;
        public TextView textViewDisplayShirtSize;
        public TextView textViewDisplayPantsSize;
        public TextView textViewDisplayShoeSize;
        public TextView textViewDisplayFavoriteColor;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewDisplayName = itemView.findViewById(R.id.textViewDisplayName);
            textViewDisplayUserID = itemView.findViewById(R.id.textViewDisplayUserID);
            textViewDisplayImageUrl = itemView.findViewById(R.id.textViewDisplayImageUrl);
            textViewDisplayShirtSize = itemView.findViewById(R.id.textViewDisplayShirtSize);
            textViewDisplayPantsSize = itemView.findViewById(R.id.textViewDisplayPantsSize);
            textViewDisplayShoeSize = itemView.findViewById(R.id.textViewDisplayShoeSize);
            textViewDisplayFavoriteColor = itemView.findViewById(R.id.textViewDisplayFavoriteColor);
        }
    }

}