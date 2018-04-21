package com.udacity.submissions.sachin.booklisting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailedDescription extends AppCompatActivity {

    TextView titletv;
    TextView authortv;
    TextView descriptiontv;
    ImageView bookimageView;
    TextView pricetv;
    TextView ratingtv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_description);

        titletv = (TextView) findViewById(R.id.detailedbookTitle);
        authortv = (TextView) findViewById(R.id.detailedAuthor);
        descriptiontv = (TextView) findViewById(R.id.description);
        pricetv = (TextView) findViewById(R.id.detailedPrice);
        ratingtv = (TextView) findViewById(R.id.detailedRating);
        bookimageView = (ImageView) findViewById(R.id.detailedImageView);

        //Fetching the details of the current book
        Intent intent = getIntent();
        BookDetails currentBook = intent.getExtras().getParcelable("currentBook");

        titletv.setText(currentBook.getTitle());
        authortv.setText(currentBook.getAuthor());
        descriptiontv.setText(currentBook.getDescription());
      //  pricetv.setText(currentBook.getPrice());
        descriptiontv.setMovementMethod(new ScrollingMovementMethod());
        ratingtv.setText(currentBook.getRating());

        Picasso.get().load(currentBook.getImageUrl()).into(bookimageView);
    }
}
