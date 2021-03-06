
package com.wishlist.giftingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

/*
This activity allows the admin user to update profile info.  This class essentially checks to see the text
inputted into the text boxes and updates the fields within the database accordingly.
 */
public class UpdateProfileInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextShoeSize;
    private EditText editTextShirtSize;
    private EditText editTextPantsSize;
    private EditText editTextFavoriteColor;
    private TextView textViewProfileName;

    Animation frombottom;
    Button infoSave;


    private Profile profile;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_info);

        db = FirebaseFirestore.getInstance();

        profile = (Profile) getIntent().getSerializableExtra("profile");

        editTextShoeSize = findViewById(R.id.EditTextEditShoeSize);
        editTextShirtSize = findViewById(R.id.EditTextEditShirtSize);
        editTextPantsSize = findViewById(R.id.EditTextEditPantsSize);
        editTextFavoriteColor = findViewById(R.id.EditTextEditFavoriteColor);
        textViewProfileName = findViewById(R.id.toolbar_title_profile);


        //set the editText name to display the profile name.
        String profileName = getIntent().getExtras().getString("profileName");
        textViewProfileName.setText(profile.getName() + "'s Info");

        //autofill the rest of the data with what is currently in the database.
        editTextShirtSize.setText(profile.getShirtSize());
        editTextPantsSize.setText(profile.getPantsSize());
        editTextShoeSize.setText(profile.getShoeSize());
        editTextFavoriteColor.setText(profile.getFavoriteColor());



        findViewById(R.id.imageViewProfile_BackButton).setOnClickListener(this);
        findViewById(R.id.buttonSave).setOnClickListener(this);


        //initialize animation
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        infoSave = (Button) findViewById(R.id.buttonSave);
        infoSave.startAnimation(frombottom);



    }




    //on click method: switches layouts (views) upon clicking a button
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.imageViewProfile_BackButton:
                this.finish();
                break;

            case R.id.buttonSave:
                //upon save, we update the profile with whatever is currently inside the editText fields.
                profile.setShoeSize(editTextShoeSize.getText().toString().trim());
                profile.setShirtSize(editTextShirtSize.getText().toString().trim());
                profile.setPantsSize(editTextPantsSize.getText().toString().trim());
                profile.setFavoriteColor(editTextFavoriteColor.getText().toString().trim());

                //update name and imageUrl in the 'profile' collection
                db.collection("profiles").document(profile.getID())
                        .update("shoeSize", profile.getShoeSize(), "shirtSize", profile.getShirtSize(),
                                "pantsSize", profile.getPantsSize(), "favoriteColor", profile.getFavoriteColor()
                        );
                Toast.makeText(this,"Info updated.", Toast.LENGTH_SHORT).show();


                break;

        }

    }
}
