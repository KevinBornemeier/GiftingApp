package com.example.giftingapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class AddWishlistItemActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextURL;
    EditText editTextTitle;
    EditText editTextPrice;
    Button buttonSubmit;
    Button buttonSave;
    ImageView backbutton;
//    TextView scraperOutputView;
    ImageView scraperProductImage;
    private Profile profile;
    private Context context;

    WishlistItem item;
    byte[] imageData;

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_wishlist_item);

        db = FirebaseFirestore.getInstance();

        item = new WishlistItem();

        editTextURL = (EditText) findViewById(R.id.editTextURL);
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        backbutton = (ImageView) findViewById(R.id.imageView_Add_Back);
//        scraperOutputView = (TextView) findViewById(R.id.scraperOutputView);
        scraperProductImage = (ImageView) findViewById(R.id.scraperProductImage);

        buttonSubmit.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        backbutton.setOnClickListener(this);

        profile = (Profile) getIntent().getSerializableExtra("profile");


        // There will be an item in the intent for editing items
        if(getIntent().hasExtra("item")){
            item = (WishlistItem) getIntent().getSerializableExtra("item");
            editTextPrice.setText(item.getPrice());
            editTextTitle.setText(item.getTitle());
            editTextURL.setText(item.getItemURL());
            loadImage(item.getImageURL());

            TextView title = findViewById(R.id.textView_Add_Wishlist);
            title.setText("Edit Wishlist Item");
        }

    }



    // If "submit" button is pressed, run the web scrapper.
    // Otherwise, if "save item" button is pressed, store it in db
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSave:
                save();
                break;
            case R.id.buttonSubmit:
                runScraper();
                break;
            case R.id.imageView_Add_Back:
                goBack();
                break;
        }
    }


    private void goBack(){
        Intent intent = new Intent(this, AdminWishlistDashboard.class);
        intent.putExtra("profile", profile);
        startActivity(intent);
    }


    private void save(){
        // TODO: Check if missing image and/or blank fields

        // TODO: Show progress spinner, disable buttons

        // Figure out if saving new or updating existing item
        if(item.getId() == null){
            if(imageData != null){
                // Have image, so upload
                saveNewImageUpload();
            } else {
                // No image, just save data
                saveNewData();
            }
        } else {
            updateItem();
        }
    }


    private void saveNewImageUpload(){
        String path = "wishlistItems/" + System.currentTimeMillis() + ".jpg";
        final StorageReference itemRef = FirebaseStorage.getInstance().getReference(path);

        itemRef.putBytes(imageData)
                .addOnSuccessListener(AddWishlistItemActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        itemRef.getDownloadUrl().addOnCompleteListener(AddWishlistItemActivity.this, new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                item.setImageURL(task.getResult().toString());
                                saveNewData();
                            }
                        });
                    }
                })
                .addOnFailureListener(AddWishlistItemActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddWishlistItemActivity.this, "Uploading image failed", Toast.LENGTH_SHORT).show();
                        // TODO: Hide progress spinner
                    }
                });
    }


    /*
    * Item's are saved and queried based on profileID's of the users.
    * */
    private void saveNewData(){
        item.setPrice(editTextPrice.getText().toString().trim());
        item.setTitle(editTextTitle.getText().toString().trim());
        item.setItemURL(editTextURL.getText().toString().trim());

        db.collection("wishlistItem").add(item)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        item.setId(documentReference.getId());
                        db.collection("wishlistItem").document(item.getId()).update("id", item.getId(), "profileID", profile.getID(), "isPurchased", false);
                        Toast.makeText(AddWishlistItemActivity.this, "Item Saved", Toast.LENGTH_SHORT).show();
                        goBack();
                        // TODO: Hide progress spinner
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddWishlistItemActivity.this, "Saving item failed", Toast.LENGTH_SHORT).show();
                        // TODO: Hide progress spinner
                    }
                });

    }

    // TODO: Currently not being used. Will implement update item feature later
    private void updateItem(){
        // Only allowed to update text fields, not picture
        item.setPrice(editTextPrice.getText().toString().trim());
        item.setTitle(editTextTitle.getText().toString().trim());
        item.setItemURL(editTextURL.getText().toString().trim());

        db.collection("wishlistItem")
                .document(item.getId())
                .update("price", item.getPrice(),
                            "title", item.getTitle(),
                            "itemURL", item.getItemURL())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddWishlistItemActivity.this, "Item updated", Toast.LENGTH_SHORT).show();
                        goBack();
                        // TODO: Hide progress spinner
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddWishlistItemActivity.this, "Updating item failed", Toast.LENGTH_SHORT).show();
                        // TODO: Hide progress spinner
                    }
                });
    }


    private void runScraper(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = editTextURL.getText().toString(); //convert url from EditText box to a string
//                String result = "";
                String price = "";
                String title = "";
                String imageBase64 = "";

//                scraperOutputView.setText("Starting...");

                try {
                    Document doc = Jsoup.connect(url) // Connect to the given URL, copy html data and store in variable "doc"
//                          .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:65.0) Gecko/20100101 Firefox/65.0")
                            .userAgent("Mozilla")
                            .get();
                    Element e;

                    // Get item's title
                    e = doc.getElementById("productTitle");
                    title = (e != null) ? e.text() : "(title not found)";

                    // Get item's price
                    e = doc.getElementById("priceblock_ourprice");
                    price = (e != null) ? e.text() : "(price not found)";

                    // Get item's photo
                    e = doc.getElementById("landingImage");
                    final String imageURL = (e != null) ? e.absUrl("src") : "(image not found)"; //doesn't actually work
                    imageBase64 = (e != null) ? e.attr("src") : "(image not found)";

//                    result += "Product Title: " + title + "\n\n" + "Price: " + price + "\n\n" + "Image URL: (todo)";
//                    result += "\nIMG URL: " + imageURL + "\nIMG B64: " + imageBase64;

                } catch (Exception e) {
                    e.getMessage();
//                    result += "\n" + e.getMessage();
                }

//                final String output = result;
                //TODO: need to add textViews for "price" and "title"
                final String itemPrice = "Price: " + price;
                final String itemTitle = "Title: " + title;
                final byte[] imageBytes = convertB64(imageBase64);
                imageData = imageBytes;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        scraperOutputView.setText(output);
                        loadImage(imageBytes);
                        editTextTitle.setText(itemTitle);
                        editTextPrice.setText(itemPrice);
                    }
                });

            }
        }).start();

    }


    private byte[] convertB64(String b64){
        String token = "data:image/jpeg;base64,";
        return Base64.decode(b64.substring(token.length()), Base64.DEFAULT);
    }



    // Set image from a converted base 64 image
    private void loadImage(byte[] bytes){
        Glide.with(AddWishlistItemActivity.this)
                .asBitmap()
                .load(bytes)
                .into(scraperProductImage);
    }



    // Set image from a url string
    private void loadImage(String image){
        Glide.with(AddWishlistItemActivity.this)
                .load(image)
                .into(scraperProductImage);
    }

}
