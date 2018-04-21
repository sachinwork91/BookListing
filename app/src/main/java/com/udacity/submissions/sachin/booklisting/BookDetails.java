package com.udacity.submissions.sachin.booklisting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sachin on 2018-04-20.
 */

public class BookDetails implements Parcelable {
    private String title;
    private String author;
    private String imageUrl;
    private int price;
    private String description ;
    private String rating ;


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }





    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public BookDetails createFromParcel(Parcel in) {
            return new BookDetails(in);
        }

        public BookDetails[] newArray(int size) {
            return new BookDetails[size];
        }
    };

    public BookDetails(String title, String author, String imageUrl, int price, String description, String rating) {
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        this.price = price;
        this.description = description;
        this.rating = rating;
    }

    //Parcelling Books
    //Ordering is Important
    public BookDetails(Parcel in) {
        this.title = in.readString();
        this.author =  in.readString();
        this.imageUrl = in.readString();
        this.price = in.readInt();
        this.description = in.readString();
        this.rating = in.readString();
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
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.author );
        parcel.writeString(this.imageUrl);
        parcel.writeInt(this.price);
        parcel.writeString(this.description);
        parcel.writeString(this.rating);

    }


}
