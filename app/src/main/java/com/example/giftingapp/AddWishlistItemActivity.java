package com.example.giftingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
                String url = itemURLText.getText().toString();
                String result = "";

                scraperOutputView.setText("Starting...");

                try {
                    Document doc = Jsoup.connect(url).get();

                    //get item's title
                    final String title = doc.getElementById("productTitle").text();

                    //get item's price
                    final String price = doc.getElementById("priceblock_ourprice").text();

                    //get item's photo
                    final String imageURL = doc.getElementById("landingImage").absUrl("src");
//                    final String imageBase64 = doc.getElementById("landingImage").attr("src");

                    result += "Product Title: " + title + "\n\n" + "Price: " + price + "\n\n" + "Image URL: " + imageURL;

                } catch (Exception e) {
                    result += "\n" + e.getMessage();
                }

                final String output = result;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scraperOutputView.setText(output);
                    }
                });

            }
        }).start();
    }

}

