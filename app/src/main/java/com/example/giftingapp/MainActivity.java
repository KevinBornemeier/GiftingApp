package com.example.giftingapp;

import android.content.Intent;
import android.content.SharedPreferences;
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
* TODO:  Finished login and create account. Implement forgot password option - Mike
*
 */

/*
3/11 Changes: *changed loadup from profile activity to dashboard activity.

*'add new profile' button set as black arrow as a temp.
 */




public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Variable declarations
    String email, password;
    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonSignIn;
    Button buttonCreateAccount;
    CheckBox checkBoxRememberMe;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    ImageView imageViewBackground;

    private CheckBox mCheckBoxRemember;
    private SharedPreferences mPrefs;
    private static final String PREFS_EMAIL = "PrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = getSharedPreferences(PREFS_EMAIL, MODE_PRIVATE);

        //initialize zoomed animation on login screen
        imageViewBackground = (ImageView) findViewById(R.id.imageViewBackground);
        imageViewBackground.animate().scaleX(2).scaleY(2).setDuration(3000).start();

        //initialize EditText, Buttons, etc...
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        mCheckBoxRemember = (CheckBox) findViewById(R.id.checkBoxRememberMe);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.textViewSignUp).setOnClickListener(this);
        findViewById(R.id.buttonSignIn).setOnClickListener(this);
        findViewById(R.id.checkBoxRememberMe).setOnClickListener(this);

        //Retrieve stored user data.
        getPreferencesData();

    }

    //Restore user login from cached data, otherwise display error.
    private void getPreferencesData() {
        SharedPreferences sp = getSharedPreferences(PREFS_EMAIL, MODE_PRIVATE);
        if(sp.contains("pref_email")) {
            String user = sp.getString("pref_email", "Email not found.");
            editTextEmail.setText(user.toString());
        }
        if(sp.contains("pref_pass")) {
            String passwd = sp.getString("pref_pass", "Password not found.");
            editTextPassword.setText(passwd.toString());
        }
        if(sp.contains("pref_check")) {
            Boolean bool = sp.getBoolean("pref_check", false);
            mCheckBoxRemember.setChecked(bool);
        }
    }

    //Validate user login information, if the user is a first time user then store their credentials.
    private void userSignIn() {
        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();

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

        //Store the user login information in cache, otherwise clear the data.
        if(mCheckBoxRemember.isChecked()) {
            Boolean boolIsChecked = mCheckBoxRemember.isChecked();
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString("pref_email", email);
            editor.putString("pref_pass", password);
            editor.putBoolean("pref_check", boolIsChecked);
            editor.apply();
        } else {
            mPrefs.edit().clear().apply();
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
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
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
            startActivity(new Intent(this, DashboardActivity.class));
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

