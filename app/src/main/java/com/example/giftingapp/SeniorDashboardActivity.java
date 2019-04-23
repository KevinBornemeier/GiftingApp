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




public class SeniorDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    String userType;
    String adminEmail;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Profile> profileList;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_dashboard);

        if (getIntent().hasExtra("refresh")) {
            //clear the android stack after logging out.
            Intent intent = new Intent(this, SeniorDashboardActivity.class);
            intent.putExtra("finish", true); // if you are checking for this in your other Activities
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //check to see if the intent has a string Extra "userType".  If it does, we need to create a new user to the
        //'users' collection.
        if (getIntent().hasExtra("userType")) {
            userType = getIntent().getStringExtra("userType");

            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            assert currentFirebaseUser != null;
            String userID = currentFirebaseUser.getUid();

            String id = "";

            //make user profile object to be inserted into the database.
            final User newUser = new User(id, userID,userType, "");

            CollectionReference dbUsers = db.collection("users");

            dbUsers.add(newUser)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            newUser.setID(documentReference.getId());
                            db.collection("users").document(documentReference.getId()).update("id", documentReference.getId());
                            Toast.makeText(SeniorDashboardActivity.this, newUser.getID(), Toast.LENGTH_LONG).show();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SeniorDashboardActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }




        findViewById(R.id.textViewLogout).setOnClickListener(this);
        findViewById(R.id.imageViewAdd).setOnClickListener(this);


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

        //query for the current user's (senior) associated admin email.
        CollectionReference usersCollectionRef = db.collection("users");

        //useful video for queries: https://www.youtube.com/watch?v=691K6NPp2Y8

        Query userTypeQuery = usersCollectionRef
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());



        userTypeQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {

                    for(QueryDocumentSnapshot document: task.getResult()){
                        User user = document.toObject(User.class);
                        //Toast.makeText(MainActivity.this, user.getUserType(), Toast.LENGTH_LONG).show();

                        adminEmail = user.getAdminEmail();
                        /*
                        At this point, adminEmail should be set accordingly.
                        */

                        //set adapter to the recycler view
                        adapter = new SeniorDashboardAdapter(SeniorDashboardActivity.this, profileList);
                        recyclerView.setAdapter(adapter);

                        db = FirebaseFirestore.getInstance();

                        CollectionReference profilesCollectionRef = db.collection("profiles");


                        //NOTE: .whereequalto() hardcoded to adminTest@gmail.com's UserID for now.
                        //useful video for queries: https://www.youtube.com/watch?v=691K6NPp2Y8
                        Query profilesQuery = profilesCollectionRef
                                .whereEqualTo("adminEmail", adminEmail);

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
                                    Toast.makeText(SeniorDashboardActivity.this, "Query failed", Toast.LENGTH_LONG).show();
                                }

                            }
                        });




                    }
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
                startActivity(new Intent(this, SeniorAddAdminActivity.class));
                break;

            //logout
            case R.id.textViewLogout:
                FirebaseAuth.getInstance().signOut();


                //clear the android stack after logging out.
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("finish", true); // if you are checking for this in your other Activities
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;

        }

    }

}

