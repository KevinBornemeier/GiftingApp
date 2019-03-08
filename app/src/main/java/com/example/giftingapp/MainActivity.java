package com.example.giftingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.giftingapp.ProfileActivity;
import com.example.giftingapp.R;
import com.example.giftingapp.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/*
NOTES from 2/28 (KB) :
*Link to database: https://console.firebase.google.com/project/gifting-app/overview
*Tutorial to set up your Android settings to run the app: https://www.youtube.com/watch?time_continue=3&v=cNPCgJW8c-E
*Tutorial series I'm currently looking at to set up the app: https://www.youtube.com/watch?v=mF5MWLsb4cg&list=PLk7v1Z2rk4hi_LdvJ2V5-VvZfyfSdY5hy&t=0s&index=2
*
*
* TODO:  Finished login and create account.  Need to figure out queries,saving user information,etc...
 */




public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //variable declarations
    String email, password;
    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonSignIn;
    Button buttonCreateAccount;
    CheckBox checkBoxRememberMe;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    ImageView imageViewBackground;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize zoomed animation on login screen
        imageViewBackground = (ImageView) findViewById(R.id.imageViewBackground);
        imageViewBackground.animate().scaleX(2).scaleY(2).setDuration(3000).start();

        //initialize EditText, Buttons, etc...
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.textViewSignUp).setOnClickListener(this);
        findViewById(R.id.buttonSignIn).setOnClickListener(this);

    }

    private void userSignIn() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();


        /*
        Perform login validation checks.
         */

        //validate email entered by user
        if(email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email entered is not a valid email");
            editTextEmail.requestFocus();
            return;
        }

        //validate password entered by user.
        if(password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6) {
            editTextPassword.setError("Minimum length of password is 6");
            editTextPassword.requestFocus();
            return;
        }

        /*
        if we reach this point, both the email and password are valid. -> sign in
         */

        //set the progress bar visibility to visible.  Disable after sign in is complete.
        progressBar.setVisibility(View.VISIBLE);

        //sign in operation.
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    finish();
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    //clear all activities on the stack and open a new activity.
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    //Check to see if the user is currently logged in.
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }

    //on click method: switches layouts (views) upon clicking a button
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.textViewSignUp:
                finish();
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.buttonSignIn:
                userSignIn();

        }

    }



    //showToast method: displays text
    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
    }

    }

