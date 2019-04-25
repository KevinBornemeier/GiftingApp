package com.wishlist.giftingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import com.wishlist.giftingapp.AddWishlistItemActivity;
import com.wishlist.giftingapp.Profile;

import com.wishlist.giftingapp.R;


public class UpdateProfileWishlistActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewProfileName;
    private Profile profile;
    private FirebaseFirestore db;
    private Button scraperLinkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_wishlist);

        textViewProfileName = findViewById(R.id.textViewProfileName);

        db = FirebaseFirestore.getInstance();
        profile = (Profile) getIntent().getSerializableExtra("profile");

        textViewProfileName.setText(profile.getName() + "'s Wishlist");

        scraperLinkButton = findViewById(R.id.scraperLinkButton);
        scraperLinkButton.setOnClickListener(this);

        findViewById(R.id.imageButtonBackArrow).setOnClickListener(this);
    }



    //on click method: switches layouts (views) upon clicking a button
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.imageButtonBackArrow:
                this.finish();
                break;

            case R.id.scraperLinkButton:
                startActivity(new Intent(this, AddWishlistItemActivity.class));
                break;

        }

    }

}
