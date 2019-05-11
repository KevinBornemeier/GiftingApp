package com.wishlist.giftingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


/*
Adapter for the admin Dashboard.  This is required to set the profiles within the recycler view.
 */

public class AdminDashboardAdapter extends RecyclerView.Adapter<AdminDashboardAdapter.ViewHolder> {

    private List<Profile> profileList;
    private Context context;

    public AdminDashboardAdapter(Context context, List<Profile> profileList) {
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

        //load imageURL into the imageView
        Glide.with(context).load(profile.getImageUrl()).into(holder.imageView);

        //set the displayName for each Profile within the dashboard.
        holder.textViewDisplayName.setText(profile.getName());



    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textViewDisplayName;
        public ImageView imageView;



         ViewHolder (@NonNull View itemView) {
            super(itemView);

            textViewDisplayName = itemView.findViewById(R.id.textViewDisplayName);
            imageView = itemView.findViewById(R.id.imageView);

            //set an onClickListener to allow users to click on profiles in the dashboard to update.
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
             Profile profile = profileList.get(getAdapterPosition());
             Intent intent = new Intent(context, UpdateProfilePictureActivity.class);
             intent.putExtra("profile", profile);
             context.startActivity(intent);

        }
    }

}