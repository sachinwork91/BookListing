package com.udacity.submissions.sachin.booklisting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sachin on 2018-04-20.
 */

public class BookDetails implements Parcelable {
    private String title;
    private String author;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public BookDetails createFromParcel(Parcel in) {
            return new BookDetails(in);
        }

        public BookDetails[] newArray(int size) {
            return new BookDetails[size];
        }
    };

    public BookDetails(String title, String author, String bookImageUrl) {
        this.title = title;

        this.author = author;
    }


    //Parcelling Books
    //Ordering is Important
    public BookDetails(Parcel in) {
        this.title = in.readString();
        this.author =  in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookDetails(String title, String author) {
        this.title = title;
        this.author = author;
    }

    @Override
    public String toString() {
        return "BookDetails{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.author );

    }


}
