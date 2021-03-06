package com.wishlist.giftingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;

/*
This class corresponds to the activity that the senior user sees when he/she first taps on a profile
within the seniorDashnoardActivity.
 */
public class SeniorViewProfileActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText;
    ImageView imageView;
    ProgressBar progressBar;
    private static final int CHOOSE_IMAGE = 101;
    String  displayName;
    TextView textView;
    private List<Profile> profileList;

    Button profileInfo;
    Button viewWishlist;
    Animation frombottom;

    FirebaseAuth mAuth;

    Uri uriProfileImage;

    String name;
    String imageUrl;
    String shoeSize;
    String shirtSize;
    String pantsSize;
    String favoriteColor;

    private FirebaseFirestore db;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_view_profile);



        //initialize animation
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        profileInfo = (Button) findViewById(R.id.buttonEditInfo_Senior);
        profileInfo.startAnimation(frombottom);

        viewWishlist = (Button) findViewById(R.id.buttonEditWishlist_Senior);
        viewWishlist.startAnimation(frombottom);



        //this grabs the profile that is clicked from the dashboard.
        db = FirebaseFirestore.getInstance();
        profile = (Profile) getIntent().getSerializableExtra("profile");
        name = profile.getName();
        imageUrl = profile.getImageUrl();
        shoeSize = profile.getShoeSize();
        shirtSize = profile.getShirtSize();
        pantsSize = profile.getPantsSize();
        favoriteColor = profile.getFavoriteColor();



        mAuth = FirebaseAuth.getInstance();
        textView = (TextView) findViewById(R.id.toolbarEditProfile);
        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);







        //set the picture and the name with the selected profile.
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);

        textView.setText(name);




        findViewById(R.id.buttonEditInfo_Senior).setOnClickListener(this);
        findViewById(R.id.buttonEditWishlist_Senior).setOnClickListener(this);
        findViewById(R.id.imageViewEditProfileBackButton).setOnClickListener(this);





    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //grab data from the showImageChoose Intent. Validate it.
        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //data.getData(): returns a 'Uri' of the image.
            uriProfileImage = data.getData();

            //get the selected image and display to the image view
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);
                //now we have the image but its local.  Now upload the image to Firebase storage.


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    //method to allow the user to select an image.
    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select profile image."), CHOOSE_IMAGE);
    }




    //on click method: switches layouts (views) upon clicking a button
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.buttonEditInfo_Senior:
                Intent intent = new Intent(this, SeniorViewInfoActivity.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
                break;
            case R.id.buttonEditWishlist_Senior:

                Intent intentWishlist = new Intent(this, SeniorWishlistDashboard.class);
                intentWishlist.putExtra("profile", profile);
                startActivity(intentWishlist);
                break;


            //back to dashboard
            case R.id.imageViewEditProfileBackButton:
                finish();
                startActivity(new Intent(this, SeniorDashboardActivity.class));
                break;

        }

    }

}
