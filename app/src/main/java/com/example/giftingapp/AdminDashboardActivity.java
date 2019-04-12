package com.example.giftingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    String userType;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Profile> profileList;

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //check to see if the intent has a string Extra "userType".  If it does, we need to create a new user to the
        //'users' collection.
        if (getIntent().hasExtra("userType")) {
            userType = getIntent().getStringExtra("userType");

            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            assert currentFirebaseUser != null;
            String userID = currentFirebaseUser.getUid();

            //make user profile object to be inserted into the database.
            User newUser = new User(userID,userType);

            CollectionReference dbUsers = db.collection("users");

            dbUsers.add(newUser)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(AdminDashboardActivity.this, "User Added", Toast.LENGTH_LONG).show();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminDashboardActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }








        findViewById(R.id.imageViewAdd).setOnClickListener(this);
        findViewById(R.id.textViewLogout).setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        profileList = new ArrayList<>();

        //dummy data test
//        for(int i = 0; i<=10; i++) {
//            ListItem listItem = new ListItem(
//                "heading" + (i+1),
//                "dummy text"
//            );
//
//            listItems.add(listItem);
//        }

        //set adapter to the recycler view
        adapter = new MyAdapter(this, profileList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        CollectionReference profilesCollectionRef = db.collection("profiles");


        //useful video for queries: https://www.youtube.com/watch?v=691K6NPp2Y8
        Query profilesQuery = profilesCollectionRef
                .whereEqualTo("userID", FirebaseAuth.getInstance().getCurrentUser().getUid());

        profilesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {

                    for(QueryDocumentSnapshot document: task.getResult()){
                        Profile profile = document.toObject(Profile.class);
                        profileList.add(profile);
                    }

                    adapter.notifyDataSetChanged();

                }
                else{
                    Toast.makeText(AdminDashboardActivity.this, "Query failed", Toast.LENGTH_LONG).show();
                }

            }
        });


//        //get() will return all documents
//        db.collection("profiles").get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                          @Override
//                                          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                              if(!queryDocumentSnapshots.isEmpty()) {
//                                                  List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//                                                  //now convert documentsnapshot to a profile object.
//
//                                                  for(DocumentSnapshot d : list){
//                                                      Profile p = d.toObject(Profile.class);
//                                                      p.setId(d.getId());
//                                                      profileList.add(p);
//                                                      //now all profiles from firestore are loaded into the profileList.
//
//                                                  }
//
//                                                  //tell the recyclerview to reload with new data.
//                                                  adapter.notifyDataSetChanged();
//                                              }
//
//                                          }
//                                      }
//                );





    }


    //on click method: switches layouts (views) upon clicking a button
    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.imageViewAdd:
                finish();
                startActivity(new Intent(this, CreateNewProfileActivity.class));
                break;
            //logout
            case R.id.textViewLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;

        }

    }

}

