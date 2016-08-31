package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Data Model for movie
 */
public class Movie implements Parcelable {
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private String id;
    private String title;
    private String ratings;
    private String posterURL;
    private String plot;
    private String releaseDate;

    public Movie(String id, String title, String ratings, String posterURL, String plot, String releaseDate) {
        super();
        this.id = id;
        this.title = title;
        this.ratings = ratings;
        this.posterURL = posterURL;
        this.plot = plot;
        this.releaseDate = releaseDate;
    }


    public Movie() {
        super();
    }

    public Movie(Parcel p) {
        id = p.readString();
        title = p.readString();
        ratings = p.readString();
        posterURL = p.readString();
        plot = p.readString();
        releaseDate = p.readString();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(ratings);
        dest.writeString(posterURL);
        dest.writeString(plot);
        dest.writeString(releaseDate);
    }

    public String getId() {
        return id;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public String getTitle() {
        return title;
    }

    public String getPlot() {
        return plot;
    }

    public String getRatings() {
        return ratings;
    }

}