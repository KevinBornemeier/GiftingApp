package com.wishlist.giftingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.wishlist.giftingapp.R;

public class SeniorAddAdminActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextEnterEmail;
    String adminEmail;
    private FirebaseFirestore db;
    FirebaseAuth mAuth;

    Animation frombottom;
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_add_admin);


        //initialize animation
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.startAnimation(frombottom);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        editTextEnterEmail = findViewById(R.id.editTextEnterEmail);

        findViewById(R.id.buttonSave).setOnClickListener(this);
        findViewById(R.id.imageViewAddadminBackButton).setOnClickListener(this);


        //query the DB to auto-update the text field to whatever is currently in place of the email(s).

        CollectionReference usersCollectionRef = db.collection("users");

        Query usersQuery = usersCollectionRef
                .whereEqualTo("userId", mAuth.getCurrentUser().getUid());
        usersQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {

                    for(QueryDocumentSnapshot document: task.getResult()){
                        User user = document.toObject(User.class);
                        editTextEnterEmail.setText(user.getAdminEmail());
                        break;

                    }


                }
                else{
                    Toast.makeText(SeniorAddAdminActivity.this, "Query failed", Toast.LENGTH_LONG).show();
                }

            }
        });


    }


    //on click method: switches layouts (views) upon clicking a button
    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.buttonSave:

                adminEmail = editTextEnterEmail.getText().toString().trim();



                CollectionReference usersCollectionRef = db.collection("users");

                Query usersQuery = usersCollectionRef
                        .whereEqualTo("userId", mAuth.getCurrentUser().getUid());
                usersQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {

                            for(QueryDocumentSnapshot document: task.getResult()){
                                User user = document.toObject(User.class);
                                Toast.makeText(SeniorAddAdminActivity.this,document.getId(), Toast.LENGTH_SHORT).show();

                                db.collection("users").document(document.getId())
                                        .update("adminEmail", adminEmail);
                                Toast.makeText(SeniorAddAdminActivity.this,"Admin updated.", Toast.LENGTH_SHORT).show();
                                break;

                            }


                        }
                        else{
                            Toast.makeText(SeniorAddAdminActivity.this, "Query failed", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                //clear stack and return to the senior dashboard
                //clear the android stack after logging out.
                Intent intent = new Intent(this, SeniorDashboardActivity.class);
                intent.putExtra("finish", true); // if you are checking for this in your other Activities
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("refresh", "refresh");
                startActivity(intent);
                finish();


            //back to dashboard
            case R.id.imageViewAddadminBackButton:
                finish();
                startActivity(new Intent(this, SeniorDashboardActivity.class));
                break;
        }

    }
}
