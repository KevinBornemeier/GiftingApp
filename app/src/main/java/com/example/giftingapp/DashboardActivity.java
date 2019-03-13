package com.example.giftingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/*
TODO: 1)change the add button to a 'plus' picture. 2)work with views and extract data from one to another. 3)dynamically add profiles with extracted data.
 */

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        findViewById(R.id.imageButtonAddProfile).setOnClickListener(this);
    }


    //on click method: switches layouts (views) upon clicking a button
    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.imageButtonAddProfile:
                finish();
                startActivity(new Intent(this, ProfileActivity.class));
                break;

        }

    }

}

