
package com.example.giftingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;



public class UpdateProfileInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextShoeSize;
    private EditText editTextShirtSize;
    private EditText editTextPantsSize;
    private EditText editTextFavoriteColor;
    private TextView textViewProfileName;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_profile_info);

        db = FirebaseFirestore.getInstance();




        editTextShoeSize = findViewById(R.id.EditTextEditShoeSize);
        editTextShirtSize = findViewById(R.id.EditTextEditShirtSize);
        editTextPantsSize = findViewById(R.id.EditTextEditPantsSize);
        editTextFavoriteColor = findViewById(R.id.EditTextEditFavoriteColor);
        textViewProfileName = findViewById(R.id.textViewProfileName);


        //set the editText name to display the profile name.
        String profileName = getIntent().getExtras().getString("profileName");
        textViewProfileName.setText("Enter " + profileName + "'s Info");


        findViewById(R.id.imageButtonBackArrow).setOnClickListener(this);
        findViewById(R.id.buttonSave).setOnClickListener(this);
    }




    //on click method: switches layouts (views) upon clicking a button
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.imageButtonBackArrow:
                finish();
                startActivity(new Intent(this, CreateNewProfilePictureActivity.class));
                break;

            case R.id.buttonSave:


                String shoeSize = editTextShoeSize.getText().toString().trim();
                String shirtSize = editTextShirtSize.getText().toString().trim();
                String pantsSize = editTextPantsSize.getText().toString().trim();
                String favoriteColor = editTextFavoriteColor.getText().toString().trim();
                //extract extra values from 'profile activity'
                String profileImageUrl = getIntent().getExtras().getString("profileImageUrl");
                String profileName = getIntent().getExtras().getString("profileName");



                //extract UserID
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String userID = currentFirebaseUser.getUid();


                //TODO: add an if input is validated statement to validate input before adding to collections.
                //collectionreference parameter specifies the collection name that all the fields will be stored in.
                CollectionReference dbProfiles = db.collection("profiles");

                //create Profile object with the values extracted from the textViews.
                Profile profile = new Profile(
                        userID, profileName, profileImageUrl, shoeSize, shirtSize, pantsSize, favoriteColor

                );


                dbProfiles.add(profile)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(UpdateProfileInfoActivity.this, "Profile added", Toast.LENGTH_LONG).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfileInfoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

                break;

        }

    }
}
