package com.example.giftingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

public class SeniorWishlistDashboard extends AppCompatActivity implements View.OnClickListener{

    private List<WishlistItem> itemList;
    private RecyclerView recyclerViewSeniorWishlist;
    private RecyclerView.Adapter adapter;
    private TextView toolbarSeniorWishlist_title;
    private Profile profile;

    FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_wishlist_dashboard);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        profile = (Profile) getIntent().getSerializableExtra("profile");

        toolbarSeniorWishlist_title = findViewById(R.id.toolbarSeniorWishlist_title);

        if(profile != null) {
            toolbarSeniorWishlist_title.setText(profile.getName() + "'s Wishlist");
        }


        findViewById(R.id.toolbarSeniorWishlist_back).setOnClickListener(this);

        recyclerViewSeniorWishlist = (RecyclerView) findViewById(R.id.recyclerViewSeniorWishlist);
        recyclerViewSeniorWishlist.setHasFixedSize(true);
        recyclerViewSeniorWishlist.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();


        //set adapter to the recycler view
        adapter = new SeniorWishListAdapter(this, itemList);
        recyclerViewSeniorWishlist.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        CollectionReference itemsCollectionRef = db.collection("wishlistItem");

        //useful video for queries: https://www.youtube.com/watch?v=691K6NPp2Y8
        Query itemQuery = itemsCollectionRef;
//                .whereEqualTo("id", FirebaseAuth.getInstance().getCurrentUser().getUid());

        itemQuery.whereEqualTo("profileID", profile.getID())
            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                    Toast.makeText(SeniorWishlistDashboard.this, "Query failed", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbarSeniorWishlist_back:
                Intent intentUpdateProfile = new Intent(this, SeniorViewProfileActivity.class);
                intentUpdateProfile.putExtra("profile", profile);
                finish();
                startActivity(intentUpdateProfile);
                break;

        }
    }

    public Profile getProfile(){
        return profile;
    }

}
