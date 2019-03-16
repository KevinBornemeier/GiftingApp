package com.example.giftingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;


public class UpdateProfileWishlistActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewProfileName;
    private Profile profile;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_wishlist);

        textViewProfileName = findViewById(R.id.textViewProfileName);

        db = FirebaseFirestore.getInstance();
        profile = (Profile) getIntent().getSerializableExtra("profile");

        textViewProfileName.setText(profile.getName() + "'s Wishlist");

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
