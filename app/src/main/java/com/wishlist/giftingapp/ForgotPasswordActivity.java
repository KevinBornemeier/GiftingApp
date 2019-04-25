package com.wishlist.giftingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.wishlist.giftingapp.R;

/*
    Note on 3/18/19

    This activity has the user enter their email for the password recovery email to be sent to.
    Most of the work is done through Firebase.

 */


public class ForgotPasswordActivity extends AppCompatActivity {

//    Toolbar toolbar;
    ProgressBar progress;
    EditText userEmail;
    Button userPass;
    TextView backLink;
    Animation frombottom;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //initialize animation for password recovery
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);

        userPass = (Button) findViewById(R.id.forgotPassword);
        userPass.startAnimation(frombottom);

        backLink = (TextView) findViewById(R.id.backToMain);
        backLink.startAnimation(frombottom);

//        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        userEmail = (EditText) findViewById(R.id.userEmail);
        userPass = (Button) findViewById(R.id.forgotPassword);
        backLink = (TextView) findViewById(R.id.backToMain);

//        toolbar.setTitle("Forgot Password");

        firebaseAuth = FirebaseAuth.getInstance();

        backLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
            }
        });

        userPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(userEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Recovery Email Sent", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
