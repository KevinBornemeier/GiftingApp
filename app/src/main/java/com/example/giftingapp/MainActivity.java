package com.example.giftingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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
* TODO: 1)set up password restrictions for creating new users. 2) create new UI layout for creating users. 3) Set up user login
 */




public class MainActivity extends AppCompatActivity {

    //variable declarations
    String email = "UsernameTEST";
    String password = "PasswordTEST";

    EditText editTextUsername;
    EditText editTextPassword;

    Button buttonSignIn;
    Button buttonCreateAccount;

    CheckBox checkBoxRememberMe;

    private FirebaseAuth mAuth;



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

        //initialize EditText, Buttons, etc...
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);

        mAuth = FirebaseAuth.getInstance();



        //OnClickListener method: extracts input within the EditText when the Button is pressed.
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Registration error -- try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//




                //TEST
                //showToast method: displays text
                //showToast(username);
                //showToast(password);

            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();

                //showToast method: displays text
                showToast(email);
                //showToast(password);
            }
        });
    }

    //showToast method: displays text
    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
    }

    }

