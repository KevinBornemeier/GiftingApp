package com.example.giftingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;




public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Profile> profileList;

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        findViewById(R.id.buttonAddNewProfile).setOnClickListener(this);

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

        //get() will return all documents
        db.collection("profiles").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                          @Override
                                          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                              if(!queryDocumentSnapshots.isEmpty()) {
                                                  List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                  //now convert documentsnapshot to a profile object.

                                                  for(DocumentSnapshot d : list){
                                                      Profile p = d.toObject(Profile.class);
                                                      p.setId(d.getId());
                                                      profileList.add(p);
                                                      //now all profiles from firestore are loaded into the profileList.

                                                  }

                                                  //tell the recyclerview to reload with new data.
                                                  adapter.notifyDataSetChanged();
                                              }

                                          }
                                      }
                );





    }


    //on click method: switches layouts (views) upon clicking a button
    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.buttonAddNewProfile:
                finish();
                startActivity(new Intent(this, CreateNewProfilePictureActivity.class));
                break;

        }

    }

}

