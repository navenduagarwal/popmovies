package com.example.android.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Defines tables and columns required to maintain movies and reviews
 */
public class MoviesContract {

    /**
     * inner class that defines the table contents of movie
     */
    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RATINGS = "ratings";
        public static final String COLUMN_POSTER_URL = "poster_url";
        public static final String COLUMN_PLOT = "plot";
        public static final String COLUMN_RELEASE_DATE = "release_date";
    }

    public static final class ReviewEntry implements BaseColumns {
        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
    }
}
