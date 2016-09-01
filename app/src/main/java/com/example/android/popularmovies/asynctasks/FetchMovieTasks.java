package com.example.android.popularmovies.asynctasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Async Task to fetch movies from themoviedb.org
 */
public class FetchMovieTasks extends AsyncTask<String, Void, ArrayList<Movie>> {

    private final String LOG_TAG = FetchMovieTasks.class.getSimpleName();

    public OnUpdateListener listener;

    public void setUpdateListener(OnUpdateListener listener) {
        this.listener = listener;
    }

    private ArrayList<Movie> getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {
        final String OWM_RESULTS = "results";
        final String OWM_ID = "id";
        final String OWM_POSTER_PATH = "poster_path";
        final String OWM_TITLE = "original_title";
        final String OWM_PLOT = "overview";
        final String OWM_RATINGS = "vote_average";
        final String OWM_RELEASE_DATE = "release_date";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(OWM_RESULTS);

        ArrayList<Movie> resultStrs = new ArrayList<>();

        for (int i = 0; i < moviesArray.length(); i++) {
            String id;
            String title;
            String ratings;
            String posterURL;
            String plot;
            String releaseDate;
            //Get JSON Object representing the movie
            JSONObject movie = moviesArray.getJSONObject(i);
            id = movie.getString(OWM_ID);
            title = movie.getString(OWM_TITLE);
            ratings = movie.getString(OWM_RATINGS);
            plot = movie.getString(OWM_PLOT);
            releaseDate = movie.getString(OWM_RELEASE_DATE);

            Uri.Builder builtUri = new Uri.Builder();
            builtUri.scheme("http")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w185")
                    .appendEncodedPath(movie.getString(OWM_POSTER_PATH))
                    .build();

            posterURL = builtUri.toString();
            Movie newMovie = new Movie(id, title, ratings, posterURL, plot, releaseDate);
            resultStrs.add(newMovie);
        }
        return resultStrs;
    }

    @Override

    protected ArrayList<Movie> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;
//            int numResults = 7;

        try {
//                final String FORECAST_BASE_URL = "https://api.themoviedb.org/3/movie/now_playing?";
            final String APIKEY_PARAM = "api_key";

            Uri.Builder builtUri = new Uri.Builder();
            builtUri.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(params[0])
                    .appendQueryParameter(APIKEY_PARAM, BuildConfig.MOVIESDB_API_KEY).build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URL : " + builtUri.toString());
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();
//            Log.i("MoviesFragment.java ", "Downloaded Data " + movieJsonStr);
//                if (movieJsonStr.length() < numResults) {
//                    numResults = movieJsonStr.length();
//                }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }

        }
        try {
            return getMoviesDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(ArrayList<Movie> result) {
        if (result != null) {
            if (listener != null) {
                listener.OnUpdate(result);
            }
        }
    }

    public interface OnUpdateListener {
        void OnUpdate(ArrayList<Movie> movies);
    }


}