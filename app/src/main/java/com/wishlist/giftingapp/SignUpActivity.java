package com.wishlist.giftingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import com.wishlist.giftingapp.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    Animation frombottom, fromtop;
    Button buttonCreateAccountSignUp;
    TextView textViewSignUp;
    RadioButton radioButtonSenior;
    RadioButton radioButtonAdministrator;

    String userType;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initialize animations
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
//        fromtop = AnimationUtils.loadAnimation(this,R.anim.fromtop);

        buttonCreateAccountSignUp = (Button) findViewById(R.id.buttonCreateAccountSignUp);
        textViewSignUp = (TextView) findViewById(R.id.textViewCreateAccountLoginReturn);

        buttonCreateAccountSignUp.startAnimation(frombottom);
        textViewSignUp.startAnimation(frombottom);



        editTextEmail = (EditText) findViewById(R.id.editTextEmailSignUp);
        editTextPassword = (EditText) findViewById(R.id.editTextPasswordSignUp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        radioButtonSenior = findViewById(R.id.radioButtonSenior);
        radioButtonAdministrator = findViewById(R.id.radioButtonAdministrator);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonCreateAccountSignUp).setOnClickListener(this);
        findViewById(R.id.textViewCreateAccountLoginReturn).setOnClickListener(this);

    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();


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
        if we reach this point, both the email and password are valid. -> check for user type.
         */

        if(radioButtonAdministrator.isChecked()) {
            userType = "Administrator";
        }
        else if(radioButtonSenior.isChecked()) {
            userType = "Senior";
        }
        else {
            Toast.makeText(getApplicationContext(), "Please select a user type.", Toast.LENGTH_SHORT).show();
            return;
        }

        //user type has been selected and email/password are valid -> create the account and launch the dashboard.
        //create the account.

        //set the progress bar visibility to visible.  Disable after registration is complete.
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()) {
                    finish();
                    Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();

                    /*
                    At this point, userType should be set accordingly.  Now send the user to the correct activity.
                    */

                    if(userType.equals("Senior")) {
                        finish();
                        Intent intent = new Intent(SignUpActivity.this, SeniorDashboardActivity.class);
                        intent.putExtra("userType", userType);
                        startActivity(intent);

                    }
                    else {
                        finish();
                        Intent intent = new Intent(SignUpActivity.this, AdminDashboardActivity.class);
                        intent.putExtra("userType", userType);
                        intent.putExtra("adminEmail", email);
                        startActivity(intent);

                    }


//                    Intent intent = new Intent(getBaseContext(), AdminDashboardActivity.class);
//                    intent.putExtra("userType", userType);
//                    startActivity(intent);
                }
                else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "This email is already registered.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(getApplicationContext(), "Registration error -- try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //on click method: switches layouts (views) upon clicking a button
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.buttonCreateAccountSignUp:
                registerUser();

                break;
            case R.id.textViewCreateAccountLoginReturn:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;

        }

    }

}



//OnClickListener method: extracts input within the EditText when the Button is pressed.
//        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            email = editTextUsername.getText().toString();
//            password = editTextPassword.getText().toString();
//
//            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(task.isSuccessful()) {
//                        Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        Toast.makeText(getApplicationContext(), "Registration error -- try again", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//}