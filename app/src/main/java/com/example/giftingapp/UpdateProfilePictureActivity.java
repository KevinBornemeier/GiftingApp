package com.example.giftingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;

public class UpdateProfilePictureActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText;
    ImageView imageView;
    ProgressBar progressBar;
    private static final int CHOOSE_IMAGE = 101;
    String  displayName;
    TextView textView;
    private List<Profile> profileList;

    FirebaseAuth mAuth;

    Uri uriProfileImage;

    String name;
    String imageUrl;
    String shoeSize;
    String shirtSize;
    String pantsSize;
    String favoriteColor;
    Button editUpdate;
    Button profileInfo;
    Button profileWishlist;
    Button profileDelete;
    Animation frombottom;

    private FirebaseFirestore db;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


//        RelativeLayout relativeLayout = new RelativeLayout(this);
//        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.MATCH_PARENT
//        );
//        relativeLayout.setLayoutParams(relativeParams);
//        setContentView(relativeLayout);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_picture);

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
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        editText = (EditText) findViewById(R.id.editTextDisplayName);
        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();

            }
        });
        loadUserInformation();

        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });

        findViewById(R.id.buttonEditInfo).setOnClickListener(this);
        findViewById(R.id.buttonEditWishlist).setOnClickListener(this);
        findViewById(R.id.buttonDeleteProfile).setOnClickListener(this);
        findViewById(R.id.imageViewEditProfileBackButton).setOnClickListener(this);


        //initialize animation for buttons
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);

        editUpdate = (Button) findViewById(R.id.buttonSave);
        editUpdate.startAnimation(frombottom);

        profileInfo = (Button) findViewById(R.id.buttonEditInfo);
        profileInfo.startAnimation(frombottom);

        profileWishlist = (Button) findViewById(R.id.buttonEditWishlist);
        profileWishlist.startAnimation(frombottom);

        profileDelete = (Button) findViewById(R.id.buttonDeleteProfile);
        profileDelete.startAnimation(frombottom);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

    }


    // Pull user information from the database including username, profile picture, and status of email verification.
    // TODO: Implement password recovery and email changing options for users.

    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(imageUrl)
                        .into(imageView);
            }
            if (user.getDisplayName() != null) {
                editText.setText(name);
            }
        }


    }

    /*
     * TODO Note from Mike
     * Right now the app will only determine a successful profile creation if the user enters a name
     * AND uploads a photo.
     *
     * This method save the users profile information and stores it in firebase.
     */
    private void saveUserInformation() {
        //update name to whatever is entered in the text field.
        name = editText.getText().toString();

        if(name.isEmpty()) {
            editText.setError("Name required.");
            editText.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null && imageUrl != null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .setPhotoUri(Uri.parse(imageUrl))
                    .build();
            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(UpdateProfilePictureActivity.this,"Profile updated.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        //update name and imageUrl in the 'profile' collection
        db.collection("profiles").document(profile.getID())
                .update("name", name, "imageUrl", imageUrl);

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

                uploadImageToFireBaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFireBaseStorage() {
        progressBar.setVisibility(View.VISIBLE);

        //storage reference
        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis() + ".jpg");

        if(uriProfileImage != null) {

            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = uri.toString();

                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Image Upload Successful", Toast.LENGTH_SHORT).show();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

//            progressBar.setVisibility(View.VISIBLE);
//            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    progressBar.setVisibility(View.GONE);
//
//                    profileImageUrl = profileImageRef.getDownloadUrl().toString();
//                }
//            })
//            .addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(CreateNewProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
        }

    }


//    //Create a menu button on the toolbar.
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    //Dropdown menu, currently the only option is to logout to main sign in screen.
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.menuLogout:
//                FirebaseAuth.getInstance().signOut();
//                finish();
//                startActivity(new Intent(this, MainActivity.class));
//                break;
//        }
//        return true;
//    }

    //method to allow the user to select an image.
    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select profile image."), CHOOSE_IMAGE);
    }


    private void deleteProfile(){
        db.collection("profiles").document(profile.getID()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UpdateProfilePictureActivity.this,"Profile deleted.", Toast.LENGTH_LONG).show();

                            //clear the android stack after logging out.
                            Intent intent = new Intent(UpdateProfilePictureActivity.this, AdminDashboardActivity.class);
                            intent.putExtra("finish", true); // if you are checking for this in your other Activities
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        }
                    }
                });

    }

    //on click method: switches layouts (views) upon clicking a button
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.buttonEditInfo:
                Intent intent = new Intent(this,UpdateProfileInfoActivity.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
                break;
            case R.id.buttonEditWishlist:
                //finish();

                Intent intentWishlist = new Intent(this, AdminWishlistDashboard.class);
                intentWishlist.putExtra("profile", profile);
                startActivity(intentWishlist);
                break;

            case R.id.buttonDeleteProfile:

                /*
                Deletion process -- create 'alert dialogue' to ask the user to confirm the profile deletion.
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure you want to delete this profile?");
                builder.setMessage("After deletion, this profile can not be recovered.");

                //if "Yes", confirm the delete.
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProfile();
                    }
                });

                //if "No", cancel the delete.
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing. No deletion.
                    }
                });

                //make the alert visable to the user.
                AlertDialog ad = builder.create();
                ad.show();

                break;

                //back to dashboard
                case R.id.imageViewEditProfileBackButton:
                    finish();
                    startActivity(new Intent(this, AdminDashboardActivity.class));
                    break;

        }

    }

}
