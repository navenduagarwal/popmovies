package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * data structure for reviews with parcelable
 */
public class Review implements Parcelable{
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>(){
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }
    };

    private String author;
    private String content;

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public Review() {
        super();
    }

    public Review(Parcel p){
        author = p.readString();
        content = p.readString();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
