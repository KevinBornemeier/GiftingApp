package com.example.giftingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AddWishlistItemActivity extends AppCompatActivity implements View.OnClickListener {

    EditText itemURLText;
    Button submitURLButton;
    TextView scrapperOutputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_wishlist_item);

        itemURLText = (EditText) findViewById(R.id.itemURLText);
        submitURLButton = (Button) findViewById(R.id.submitURLButton);
        scrapperOutputView = (TextView) findViewById(R.id.scrapperOutputView);

        submitURLButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String url = itemURLText.getText().toString();
        String result = "::";

        scrapperOutputView.setText("Starting...");

        try {
            Document doc = Jsoup.connect(url).get();

            result = doc.html();

            /*
            //Elements divs = doc.select("div");
            Elements divs = doc.getElementsByTag("div");

            for(Element el : divs){
                result += el.html() + "\n";
            }
            */

        }catch (Exception e){
            result = e.getMessage();
        }

        scrapperOutputView.setText("Done!");
        scrapperOutputView.setText(result);
    }
}
