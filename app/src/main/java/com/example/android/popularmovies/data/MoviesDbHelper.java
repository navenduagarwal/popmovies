package com.example.android.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.popularmovies.data.MoviesContract.MovieEntry;
import com.example.android.popularmovies.data.MoviesContract.ReviewEntry;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;

import java.util.ArrayList;

/**
 * Manages a local database for favourite movies and selected movie reviews
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "movies.db";
    private static final String LOG_TAG = MoviesDbHelper.class.getSimpleName();
    //whenever we change the database schema, we must increment the database version
    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                MovieEntry.COLUMN_ID + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_PLOT + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTER_URL + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RATINGS + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL" + ");";

        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                ReviewEntry.COLUMN_ID + " TEXT NOT NULL, " +
                ReviewEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL" + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int olderVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void deleteDatabase(Context context) {
        context.deleteDatabase(MovieEntry.TABLE_NAME);
        context.deleteDatabase(ReviewEntry.TABLE_NAME);
    }

    public boolean insertMovie(String id, String title, String ratings, String posterURL, String plot, String releaseDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newMovieValues = new ContentValues();
        Long movieRowId;
        boolean result;

        newMovieValues.put(MovieEntry.COLUMN_ID, id);
        newMovieValues.put(MovieEntry.COLUMN_TITLE, title);
        newMovieValues.put(MovieEntry.COLUMN_RATINGS, ratings);
        newMovieValues.put(MovieEntry.COLUMN_POSTER_URL, posterURL);
        newMovieValues.put(MovieEntry.COLUMN_PLOT, plot);
        newMovieValues.put(MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        movieRowId = db.insert(MovieEntry.TABLE_NAME, null, newMovieValues);
        if (movieRowId != -1) {
            Log.i(LOG_TAG, "Movie inserted successfully");
            result = true;
        } else {
            Log.e(LOG_TAG, "Movie insert failed");
            result = false;
        }
        db.close();
        return result;
    }

    public boolean insertReview(String id, String movieId, String author, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newReviewValues = new ContentValues();
        Long reviewRowId;
        boolean result;

        newReviewValues.put(ReviewEntry.COLUMN_ID, id);
        newReviewValues.put(ReviewEntry.COLUMN_MOVIE_ID, movieId);
        newReviewValues.put(ReviewEntry.COLUMN_AUTHOR, author);
        newReviewValues.put(ReviewEntry.COLUMN_CONTENT, content);
        reviewRowId = db.insert(ReviewEntry.TABLE_NAME, null, newReviewValues);
        if (reviewRowId != -1) {
            Log.i(LOG_TAG, "Review inserted successfully");
            result = true;
        } else {
            Log.e(LOG_TAG, "Review insert failed");
            result = false;
        }
        db.close();
        return result;
    }

    //Get Movie Data from table
    public Movie getMovieData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.query(
                MovieEntry.TABLE_NAME,
                null, // all the columns
                MovieEntry.COLUMN_ID + " = " + id,
                null, //values for where clause
                null, //column to group by
                null, //column to filter by row groups
                null //sort order
        );
        result.moveToFirst();
        String title = result.getString(result.getColumnIndex(MovieEntry.COLUMN_TITLE));
        String ratings = result.getString(result.getColumnIndex(MovieEntry.COLUMN_RATINGS));
        String posterURL = result.getString(result.getColumnIndex(MovieEntry.COLUMN_POSTER_URL));
        String plot = result.getString(result.getColumnIndex(MovieEntry.COLUMN_PLOT));
        String releaseDate = result.getString(result.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE));
        Movie movie = new Movie(id, title, ratings, posterURL,
                plot, releaseDate);
        db.close();
        result.close();
        return movie;
    }

    //Get Review Data from table
    public Review getReviewData(String movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.query(
                ReviewEntry.TABLE_NAME,
                null, // all the columns
                ReviewEntry.COLUMN_MOVIE_ID + " = " + movieId,
                null, //values for where clause
                null, //column to group by
                null, //column to filter by row groups
                null //sort order
        );
        result.moveToFirst();
        String id = result.getString(result.getColumnIndex(ReviewEntry.COLUMN_ID));
        String author = result.getString(result.getColumnIndex(ReviewEntry.COLUMN_AUTHOR));
        String content = result.getString(result.getColumnIndex(ReviewEntry.COLUMN_CONTENT));

        Review review = new Review(id, author, content);
        db.close();
        result.close();
        return review;
    }

    //Delete all table entries
    public int deleteAllMovieEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        int result;
        result = db.delete(MovieEntry.TABLE_NAME, null, null);
        db.close();
        return result;
    }

    //Delete all table entries
    public int deleteAllReviewEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        int result;
        result = db.delete(ReviewEntry.TABLE_NAME, null, null);
        db.close();
        return result;
    }

    //Delete one table entry for a movie
    public boolean deleteSingleMovieRecord(String movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result;
        result = db.delete(MovieEntry.TABLE_NAME, MovieEntry.COLUMN_ID + "= ?", new String[]{movieId});
        db.close();
        return (result > 0);
    }

    //Delete review entries for movie
    public boolean deleteSingleReviewRecord(String movieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result;
        result = db.delete(ReviewEntry.TABLE_NAME, ReviewEntry.COLUMN_MOVIE_ID + "= ?", new String[]{movieId});
        db.close();
        return (result > 0);
    }

    public ArrayList<Movie> getAllMoviesData() {
        ArrayList<Movie> movieList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor getList = db.rawQuery("SELECT * FROM " + MovieEntry.TABLE_NAME, null);
        getList.moveToFirst();
        while (!getList.isAfterLast()) {
            String id = getList.getString(getList.getColumnIndex(MovieEntry.COLUMN_ID));
            String title = getList.getString(getList.getColumnIndex(MovieEntry.COLUMN_TITLE));
            String ratings = getList.getString(getList.getColumnIndex(MovieEntry.COLUMN_RATINGS));
            String posterURL = getList.getString(getList.getColumnIndex(MovieEntry.COLUMN_POSTER_URL));
            String plot = getList.getString(getList.getColumnIndex(MovieEntry.COLUMN_PLOT));
            String releaseDate = getList.getString(getList.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE));
            Movie movie = new Movie(id, title, ratings, posterURL,
                    plot, releaseDate);
            movieList.add(movie);
            getList.moveToNext();
        }
        getList.close();
        return movieList;
    }

    public ArrayList<Review> getAllReviewsData() {
        ArrayList<Review> reviewsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor getList = db.rawQuery("SELECT * FROM " + ReviewEntry.TABLE_NAME, null);
        getList.moveToFirst();
        while (!getList.isAfterLast()) {
            String id = getList.getString(getList.getColumnIndex(ReviewEntry.COLUMN_ID));
            String author = getList.getString(getList.getColumnIndex(ReviewEntry.COLUMN_AUTHOR));
            String content = getList.getString(getList.getColumnIndex(ReviewEntry.COLUMN_CONTENT));
            Review review = new Review(id, author, content);
            reviewsList.add(review);
            getList.moveToNext();
        }
        getList.close();
        return reviewsList;
    }
}
