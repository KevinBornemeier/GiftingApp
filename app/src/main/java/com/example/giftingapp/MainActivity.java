package com.example.giftingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/*
NOTES from 2/28 (KB) :
*Link to database: https://console.firebase.google.com/project/gifting-app/overview
*Tutorial to set up your Android settings to run the app: https://www.youtube.com/watch?time_continue=3&v=cNPCgJW8c-E
*Tutorial series I'm currently looking at to set up the app: https://www.youtube.com/watch?v=mF5MWLsb4cg&list=PLk7v1Z2rk4hi_LdvJ2V5-VvZfyfSdY5hy&t=0s&index=2
*
* TODO: 1)set up password restrictions for creating new users. 2) create new UI layout for creating users. 3) Set up user login
 */




public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //variable declarations
    String email = "UsernameTEST";
    String password = "PasswordTEST";

    EditText editTextUsername;
    EditText editTextPassword;

    Button buttonSignIn;
    Button buttonCreateAccount;

    CheckBox checkBoxRememberMe;





    /*
    unfinished code: check to see if the user is currently signed in.
     */
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        findViewById(R.id.buttonCreateAccount).setOnClickListener(this);





        //initialize EditText, Buttons, etc...
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);





//        buttonSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                email = editTextUsername.getText().toString();
//                password = editTextPassword.getText().toString();
//
//                //showToast method: displays text
//                showToast(email);
//                //showToast(password);
//            }
//        });
    }

    //on click method: switches layouts (views) upon clicking a button
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.buttonCreateAccount:
                startActivity(new Intent(this, SignUpActivity.class));
                break;

        }

    }



    //showToast method: displays text
    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
    }

    }

