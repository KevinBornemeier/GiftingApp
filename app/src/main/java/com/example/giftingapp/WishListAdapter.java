package com.example.giftingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;



public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ItemViewHolder>{

    private List<WishlistItem> itemList;
    private Context context;

    public WishListAdapter(Context context, List<WishlistItem> itemList) {
        this.itemList = itemList;
        this.context = context;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.activity_wishlist_row, viewGroup, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder viewHolder, int i) {
         WishlistItem item = itemList.get(i);

         //load imageURL into the imageView
         Glide.with(context).load(item.getImageURL()).into(viewHolder.wishlistItemImage);

         //set the title for each item within the dashboard
         viewHolder.wishlistItemTitle.setText(item.getTitle());

         //set the price for each item within the dashboard
         viewHolder.wishlistItemPrice.setText(item.getPrice());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView wishlistItemImage;
        TextView wishlistItemTitle;
        TextView wishlistItemPrice;

        public ItemViewHolder(View itemView){
            super(itemView);

            wishlistItemImage = itemView.findViewById(R.id.wishlistItemImage);
            wishlistItemTitle = itemView.findViewById(R.id.wishlistItemTitle);
            wishlistItemPrice = itemView.findViewById(R.id.wishlistItemPrice);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Oops! Feature still in progress!", Toast.LENGTH_SHORT).show();
            //TODO: add individual item views
        }
    }
}
