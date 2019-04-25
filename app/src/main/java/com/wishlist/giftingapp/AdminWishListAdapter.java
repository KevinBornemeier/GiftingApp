package com.wishlist.giftingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import com.wishlist.giftingapp.R;


class AdminWishListAdapter extends RecyclerView.Adapter<AdminWishListAdapter.ItemViewHolder>{

    private List<WishlistItem> itemList;
    private Context context;

    public AdminWishListAdapter(Context context, List<WishlistItem> itemList) {
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

         // Load imageURL into the imageView
         Glide.with(context).load(item.getImageURL()).into(viewHolder.wishlistItemImage);

         // Set the title for each item within the dashboard
         viewHolder.wishlistItemTitle.setText(item.getTitle());

         // Set the price for each item
         // If purchased, indicates with strikethrough on title and toggle purchase/available text/color on button
         if(item.getIsPurchased() == false) {
             viewHolder.wishlistItemPrice.setText(item.getPrice());
             viewHolder.buttonItemPurchased.setText("Mark as Purchased");
             viewHolder.wishlistItemPrice.setTextColor(0xFFF47C3A);
             viewHolder.buttonItemPurchased.setBackgroundColor(0xFF00BCD4);
             viewHolder.wishlistItemTitle.setPaintFlags(viewHolder.wishlistItemTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
         }
         else {
             viewHolder.wishlistItemPrice.setText("PURCHASED");
             viewHolder.wishlistItemPrice.setTextColor(0xFF2196F3);
             viewHolder.buttonItemPurchased.setText("Mark as Available");
             viewHolder.buttonItemPurchased.setBackgroundColor(0xFF2196F3);
             viewHolder.wishlistItemTitle.setPaintFlags(viewHolder.wishlistItemTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
         }

         viewHolder.item = item;

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView wishlistItemImage;
        TextView wishlistItemTitle;
        TextView wishlistItemPrice;
        Button buttonDeleteItem;
        Button buttonItemPurchased;
        WishlistItem item;

        public ItemViewHolder(View itemView){
            super(itemView);

            wishlistItemImage = itemView.findViewById(R.id.wishlistItemImage);
            wishlistItemTitle = itemView.findViewById(R.id.wishlistItemTitle);
            wishlistItemPrice = itemView.findViewById(R.id.wishlistItemPrice);
            buttonDeleteItem = itemView.findViewById(R.id.buttonDeleteItem);
            buttonItemPurchased = itemView.findViewById(R.id.buttonItemPurchased);

            itemView.setOnClickListener(this);
            buttonDeleteItem.setOnClickListener(this);
            buttonItemPurchased.setOnClickListener(this);
        }

        // Deleting an item from a wishlist
        private void deleteItem(){
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("wishlistItem").document(item.getId()).delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                itemList.remove(item);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(context,"Deletion unsuccessfull. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

        // Switches the value of the isPurchased field and saves it in the database
        private void togglePurchased(){
            item.setIsPurchased(!item.getIsPurchased());

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("wishlistItem")
                    .document(item.getId())
                    .update("isPurchased", item.getIsPurchased())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Item updated", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Updating item failed", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.buttonDeleteItem:
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Are you sure you want to delete this item?");
                    builder.setMessage("After deletion, this item can not be recovered.");

                    //if "Yes", confirm the delete.
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteItem();
                        }
                    });

                    //if "No", cancel the delete.
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing. No deletion.
                        }
                    });

                    //make the alert visable to the user.
                    AlertDialog ad = builder.create();
                    ad.show();
                    break;

                case R.id.buttonItemPurchased:
                    togglePurchased();
                    break;

                default:
                    Intent intent = new Intent(context, AddWishlistItemActivity.class);
                    intent.putExtra("item", item);
                    AdminWishlistDashboard dashboard = (AdminWishlistDashboard)context;
                    intent.putExtra("profile", dashboard.getProfile());
                    context.startActivity(intent);
                    break;

            }

        }
    }
}
