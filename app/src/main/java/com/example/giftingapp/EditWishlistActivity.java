package com.example.giftingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class EditWishlistActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wishlist);

        findViewById(R.id.imageButtonBackArrow).setOnClickListener(this);
    }



    //on click method: switches layouts (views) upon clicking a button
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.imageButtonBackArrow:
                finish();
                startActivity(new Intent(this, ProfileActivity.class));
                break;

        }

    }

}
