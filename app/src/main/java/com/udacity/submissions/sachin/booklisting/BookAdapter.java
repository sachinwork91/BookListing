package com.udacity.submissions.sachin.booklisting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sachin on 2018-04-20.
 */

public class BookAdapter extends ArrayAdapter<BookDetails>{

    private Context ctx;
    ImageView bookImageview;

    public BookAdapter(Context ctx, ArrayList<BookDetails> bookDetails){
        super(ctx, 0 , bookDetails);
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        BookDetails bookdetail = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(ctx).inflate(R.layout.booksrowdetails, parent, false);
        }

        TextView authors = (TextView) convertView.findViewById(R.id.authors );
        TextView title = (TextView) convertView.findViewById(R.id.bookTitle );
        bookImageview = (ImageView) convertView.findViewById(R.id.bookImage);
        Picasso.get().load(bookdetail.getImageUrl()).into(bookImageview);
        authors.setText(bookdetail.getAuthor());
        title.setText(bookdetail.getTitle() + "price "+ bookdetail.getPrice() + " rating"+ bookdetail.getRating());
        return convertView;
    }














}
