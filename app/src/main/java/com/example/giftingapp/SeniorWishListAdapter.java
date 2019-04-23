package com.example.giftingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class SeniorWishListAdapter extends RecyclerView.Adapter<SeniorWishListAdapter.ItemViewHolder>{

    private List<WishlistItem> itemList;
    private Context context;

    public SeniorWishListAdapter(Context context, List<WishlistItem> itemList) {
        this.itemList = itemList;
        this.context = context;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.activity_senior_wishlist_row, viewGroup, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder viewHolder, int i) {
         WishlistItem item = itemList.get(i);

         //load imageURL into the imageView
         Glide.with(context).load(item.getImageURL()).into(viewHolder.seniorWishlistItemImage);

         //set the title for each item within the dashboard
         viewHolder.seniorWishlistItemTitle.setText(item.getTitle());

         //set the price for each item within the dashboard
         viewHolder.seniorWishlistItemPrice.setText(item.getPrice());

         viewHolder.item = item;

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView seniorWishlistItemImage;
        TextView seniorWishlistItemTitle;
        TextView seniorWishlistItemPrice;
        Button buttonAmazonLink;
        WishlistItem item;

        public ItemViewHolder(View itemView){
            super(itemView);

            seniorWishlistItemImage = itemView.findViewById(R.id.seniorWishlistItemImage);
            seniorWishlistItemTitle = itemView.findViewById(R.id.seniorWishlistItemTitle);
            seniorWishlistItemPrice = itemView.findViewById(R.id.seniorWishlistItemPrice);
            buttonAmazonLink = itemView.findViewById(R.id.buttonAmazonLink);

            itemView.setOnClickListener(this);
            buttonAmazonLink.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.buttonAmazonLink:
                    // TODO: create link to item's amazon page
                    break;

            }

        }
    }
}
