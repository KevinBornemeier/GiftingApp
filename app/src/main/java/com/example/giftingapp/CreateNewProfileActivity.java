package com.example.giftingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;

/*
This is the activity for account creation. Not for updates
 */

public class CreateNewProfileActivity extends AppCompatActivity implements View.OnClickListener{

    String adminEmail;
    EditText editText;
    ImageView imageView;
    ProgressBar progressBar;
    private static final int CHOOSE_IMAGE = 101;
    String profileImageUrl, displayName;
    TextView textView;
    private List<Profile> profileList;
    Button newProfile;
    Animation frombottom;

    FirebaseAuth mAuth;

    Uri uriProfileImage;


    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        adminEmail = "test";


//        RelativeLayout relativeLayout = new RelativeLayout(this);
//        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.MATCH_PARENT
//        );
//        relativeLayout.setLayoutParams(relativeParams);
//        setContentView(relativeLayout);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_profile);

        findViewById(R.id.imageViewNewProfileBackButton).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        editText = (EditText) findViewById(R.id.editTextDisplayName);
        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        db = FirebaseFirestore.getInstance();


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();

            }
        });
       // loadUserInformation();

        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });


        //initialize animation
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        newProfile = (Button) findViewById(R.id.buttonSave);
        newProfile.startAnimation(frombottom);


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



    /*
    * TODO Note from Mike
    * Right now the app will only determine a successful profile creation if the user enters a name
    * AND uploads a photo.
    *
    * This method save the users profile information and stores it in firebase.
    */
    private void saveUserInformation() {
        displayName = editText.getText().toString();

        if(displayName.isEmpty()) {
            editText.setError("Name required.");
            editText.requestFocus();
            return;
        }

        final FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null && profileImageUrl != null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();
            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                /*
                                At this point, the user has successfully uploaded a photo and entered a profile name.
                                The rest of the information (Wishlist and Info) is an empty string.
                                The account will be created -- the user will need to tap on the profile in the dashboard
                                to edit each profile's Wishlist and Info.
                                 */
                                //fill in profile data.
                                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                assert currentFirebaseUser != null;
                                final String userID = currentFirebaseUser.getUid();
                                final String Id = "";

                                //extract current user's (administrator) email to be linked to profile.

                                //verify that the user is an admin.  If not, send them to the senior view.
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
                                                //Toast.makeText(MainActivity.this, user.getUserType(), Toast.LENGTH_LONG).show();

                                                adminEmail = user.getAdminEmail();

                                                /*
                                                At this point, adminEmail should be set accordingly.  Now set the profiel.
                                                */
                                                final Profile newProfile = new Profile(Id,userID, displayName, profileImageUrl, "", "", "", "", adminEmail);

                                                //upload newProfile to database.
                                                //TODO: add an if input is validated statement to validate input before adding to collections.
                                                //collectionreference parameter specifies the collection name that all the fields will be stored in.
                                                final CollectionReference dbProfiles = db.collection("profiles");

                                                dbProfiles.add(newProfile)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Toast.makeText(CreateNewProfileActivity.this, "Profile Created", Toast.LENGTH_LONG).show();
                                                                newProfile.setID(documentReference.getId());
                                                                db.collection("profiles").document(documentReference.getId()).update("id", documentReference.getId());
                                                /*
                                                After newProfile is created, exit the activity and launch the dashboard.
                                                */

                                                                //clear the android stack after logging out.
                                                                Intent intent = new Intent(CreateNewProfileActivity.this, AdminDashboardActivity.class);
                                                                intent.putExtra("finish", true); // if you are checking for this in your other Activities
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(intent);
                                                                finish();


                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(CreateNewProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                                                    }
                                                });

                                            }
                                        }
                                    }
                                });



                            }
                        }
                    });
        }
        /*
        After newProfile is created, exit the activity and launch the dashboard.
        */
        //startActivity(new Intent(this, AdminDashboardActivity.class));
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
                            progressBar.setVisibility(View.GONE);


                            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    profileImageUrl = uri.toString();
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


    //Create a menu button on the toolbar.
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return true;
//    }

    //Dropdown menu, currently the only option is to logout to main sign in screen.
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

    //on click method: switches layouts (views) upon clicking a button
    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.imageViewNewProfileBackButton:
                finish();
                startActivity(new Intent(this, AdminDashboardActivity.class));
                break;

        }

    }



}
