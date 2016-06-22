package com.example.android.popularmovies;

/**
 * Created by navendux on 21-Jun-16.
 */
public class Movie {
    private String title;
    private String ratings;
    private String posterURL;
    private String plot;
    private String releaseDate;

    public Movie(String title,
                 String ratings,
                 String posterURL,
                 String plot,
                 String releaseDate) {
        this.title = title;
        this.ratings = ratings;
        this.posterURL = posterURL;
        this.plot = plot;
        this.releaseDate = releaseDate;
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
}