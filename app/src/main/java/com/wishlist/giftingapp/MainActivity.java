package com.wishlist.giftingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/*
Main Activity, AKA the start-up page for the app.
*
 */






public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String email, password;
    EditText editTextEmail;
    EditText editTextPassword;
    TextView forgotPassword;
    Button buttonSignIn;
    TextView signUp;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference userRef;
    String userType = "Administrator"; //set to admin by default.
    String testUserID;

    Animation frombottom;

    ImageView imageViewBackground;

    private CheckBox mCheckBoxRemember;
    private SharedPreferences mPrefs;
    private static final String PREFS_EMAIL = "PrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = getSharedPreferences(PREFS_EMAIL, MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();

        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);

        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignIn.startAnimation(frombottom);

        signUp = (TextView) findViewById(R.id.textViewSignUp);
        signUp.startAnimation(frombottom);

        forgotPassword = (TextView) findViewById(R.id.textViewForgotPassword);
        forgotPassword.startAnimation(frombottom);


        //initialize EditText, Buttons, etc...
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        forgotPassword = (TextView) findViewById(R.id.textViewForgotPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.textViewForgotPassword).setOnClickListener(this);
        findViewById(R.id.textViewSignUp).setOnClickListener(this);
        findViewById(R.id.buttonSignIn).setOnClickListener(this);

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

                    /*
                    If sign in is successful, we will check the UUID.  We will then queury the
                    database for the usertype associated with the UUID.  After obtaining the user type,
                    the user will be sent to the appropriate view within the app.
                     */

                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    assert currentFirebaseUser != null;
                    final String userID = currentFirebaseUser.getUid();

                    db = FirebaseFirestore.getInstance();

                    CollectionReference usersCollectionRef = db.collection("users");

                    //useful video for queries: https://www.youtube.com/watch?v=691K6NPp2Y8

                    Query userTypeQuery = usersCollectionRef
                            .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());



                    userTypeQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {

                                for(QueryDocumentSnapshot document: task.getResult()){
                                    User user = document.toObject(User.class);

                                    userType = user.getUserType();
                                    testUserID = user.getUserId();

                                    /*
                                    At this point, userType should be set accordingly.  Now send the user to the correct activity.
                                    */

                                    if(userType.equals("Senior")) {
                                        finish();
                                        Intent intent = new Intent(MainActivity.this, SeniorDashboardActivity.class);
                                        //clear all activities on the stack and open a new activity.
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);

                                    }
                                    else {
                                        finish();
                                        Intent intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                                        //clear all activities on the stack and open a new activity.
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);

                                    }
                                }



                            }
                            else{
                                Toast.makeText(MainActivity.this, "Query failed -- failed to find userType", Toast.LENGTH_LONG).show();
                            }

                        }
                    });



                }
                else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
            }
        });

    }

    //Check to see if the user is currently logged in.
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, AdminDashboardActivity.class));
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
                break;
            case R.id.textViewForgotPassword:
                finish();
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;

        }

    }

    //showToast method: displays text within the activity.
    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
    }

    }

