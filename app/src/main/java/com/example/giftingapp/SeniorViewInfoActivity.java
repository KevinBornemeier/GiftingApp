
package com.example.giftingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;



public class SeniorViewInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView editTextShoeSize;
    private TextView editTextShirtSize;
    private TextView editTextPantsSize;
    private TextView editTextFavoriteColor;
    private TextView textViewProfileName;



    private Profile profile;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_view_info);

        db = FirebaseFirestore.getInstance();

        profile = (Profile) getIntent().getSerializableExtra("profile");





        editTextShoeSize = findViewById(R.id.EditTextEditShoeSize);
        editTextShirtSize = findViewById(R.id.EditTextEditShirtSize);
        editTextPantsSize = findViewById(R.id.EditTextEditPantsSize);
        editTextFavoriteColor = findViewById(R.id.EditTextEditFavoriteColor);
        textViewProfileName = findViewById(R.id.textViewProfileName);


        //set the editText name to display the profile name.
        String profileName = getIntent().getExtras().getString("profileName");
        textViewProfileName.setText(profile.getName() + "'s Info");
        //autofill the rest of the data with what is currently in the database.
        editTextShirtSize.setText(profile.getShirtSize());
        editTextPantsSize.setText(profile.getPantsSize());
        editTextShoeSize.setText(profile.getShoeSize());
        editTextFavoriteColor.setText(profile.getFavoriteColor());



        findViewById(R.id.imageButtonBackArrow).setOnClickListener(this);

    }




    //on click method: switches layouts (views) upon clicking a button
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.imageButtonBackArrow:
                this.finish();
                break;
        }

    }
}