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

/*
This class allows senior user to connect to thier admin account(s).  The senior users has the option of entering
admin emails (up to 4) and hitting the save button to update their "AdminEmail" fields within the database.
 */

public class SeniorAddAdminActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextEnterEmail1;
    EditText editTextEnterEmail2;
    EditText editTextEnterEmail3;
    EditText editTextEnterEmail4;
    String adminEmail1;
    String adminEmail2;
    String adminEmail3;
    String adminEmail4;
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

        editTextEnterEmail1 = findViewById(R.id.editTextEnterEmail1);
        editTextEnterEmail2 = findViewById(R.id.editTextEnterEmail2);
        editTextEnterEmail3 = findViewById(R.id.editTextEnterEmail3);
        editTextEnterEmail4 = findViewById(R.id.editTextEnterEmail4);

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
                        editTextEnterEmail1.setText(user.getAdminEmail1());
                        editTextEnterEmail2.setText(user.getAdminEmail2());
                        editTextEnterEmail3.setText(user.getAdminEmail3());
                        editTextEnterEmail4.setText(user.getAdminEmail4());
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

                adminEmail1 = editTextEnterEmail1.getText().toString().trim();
                adminEmail2 = editTextEnterEmail2.getText().toString().trim();
                adminEmail3 = editTextEnterEmail3.getText().toString().trim();
                adminEmail4 = editTextEnterEmail4.getText().toString().trim();



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
                                        .update("adminEmail1", adminEmail1, "adminEmail2", adminEmail2,
                                                "adminEmail3", adminEmail3, "adminEmail4", adminEmail4);
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
