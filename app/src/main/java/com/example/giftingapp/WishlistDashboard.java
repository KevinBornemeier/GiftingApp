package com.example.giftingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class WishlistDashboard extends AppCompatActivity implements View.OnClickListener{

    private List<WishlistItem> itemList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private TextView toolbar_title_wishlist;
    private Profile profile;

    FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_dashboard);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        profile = (Profile) getIntent().getSerializableExtra("profile");

        toolbar_title_wishlist = findViewById(R.id.toolbar_title_wishlist);

        if(profile != null) {
            toolbar_title_wishlist.setText(profile.getName() + "'s Wishlist");
        }


        findViewById(R.id.imageViewAdd_wishlist).setOnClickListener(this);
        findViewById(R.id.imageViewWishlist_BackButton).setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();


        //set adapter to the recycler view
        adapter = new WishListAdapter(this, itemList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        CollectionReference itemsCollectionRef = db.collection("wishlistItem");

        //useful video for queries: https://www.youtube.com/watch?v=691K6NPp2Y8
        Query itemQuery = itemsCollectionRef;
//                .whereEqualTo("id", FirebaseAuth.getInstance().getCurrentUser().getUid());

        itemQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {

                    for(QueryDocumentSnapshot document: task.getResult()){
                        WishlistItem item = document.toObject(WishlistItem.class);
                        itemList.add(item);
                    }

                    adapter.notifyDataSetChanged();

                }
                else{
                    Toast.makeText(WishlistDashboard.this, "Query failed", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageViewAdd_wishlist:
                Intent intent = new Intent(this, AddWishlistItemActivity.class);
                intent.putExtra("profile", profile);
                finish();
                startActivity(intent);
                break;

            //back to dashboard
            case R.id.imageViewWishlist_BackButton:
                Intent intentUpdateProfile = new Intent(this, UpdateProfilePictureActivity.class);
                intentUpdateProfile.putExtra("profile", profile);
                finish();
                startActivity(intentUpdateProfile);
                break;

        }
    }

}
