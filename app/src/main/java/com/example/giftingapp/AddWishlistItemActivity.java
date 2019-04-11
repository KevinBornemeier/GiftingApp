package com.example.giftingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class AddWishlistItemActivity extends AppCompatActivity implements View.OnClickListener {

    EditText itemURLText;
    Button submitURLButton;
    TextView scraperOutputView;
    ImageView scraperProductImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_wishlist_item);

        itemURLText = (EditText) findViewById(R.id.itemURLText);
        submitURLButton = (Button) findViewById(R.id.submitURLButton);
        scraperOutputView = (TextView) findViewById(R.id.scraperOutputView);
        scraperProductImage = (ImageView) findViewById(R.id.scraperProductImage);

        submitURLButton.setOnClickListener(this);
    }



    //web scraper testing
    @Override
    public void onClick(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = itemURLText.getText().toString(); //convert url from EditText box to a string
                String result = "";
                String imageBase64 = "";

                scraperOutputView.setText("Starting...");

                try {
                    Document doc = Jsoup.connect(url) // Connect to the given URL, copy html data and store in variable "doc"
//                          .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:65.0) Gecko/20100101 Firefox/65.0")
                            .userAgent("Mozilla")
                            .get();
                    Element e;

                    //get item's title
                    e = doc.getElementById("productTitle");
                    final String title = (e != null) ? e.text() : "(title not found)";

                    //get item's price
                    e = doc.getElementById("priceblock_ourprice");
                    final String price = (e != null) ? e.text() : "(price not found)";

                    //get item's photo
                    e = doc.getElementById("landingImage");
                    final String imageURL = (e != null) ? e.absUrl("src") : "(image not found)"; //doesn't actually work
                    imageBase64 = (e != null) ? e.attr("src") : "(image not found)";

                    result += "Product Title: " + title + "\n\n" + "Price: " + price + "\n\n" + "Image URL: (todo)";
                    result += "\nIMG URL: " + imageURL + "\nIMG B64: " + imageBase64;

                } catch (Exception e) {
                    result += "\n" + e.getMessage();
                }

                final String output = result;
                final byte[] imageBytes = convertB64(imageBase64);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scraperOutputView.setText(output);
                        loadImage(imageBytes);
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

