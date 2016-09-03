package com.example.android.popularmovies.asynctasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.data.MoviesDbHelper;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.utils.Constants;

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
 * Async Task to fetch reviews
 */
public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<Review>> {
    private static final String LOG_TAG = FetchReviewsTask.class.getSimpleName();
    public OnUpdateListener listener;
    private Context context;

    public FetchReviewsTask(Context context) {
        this.context = context;
    }

    public void setUpdateListener(OnUpdateListener listener) {
        this.listener = listener;
    }

    private ArrayList<Review> getTrailersDataFromJson(String trailerJsonStr) throws JSONException {
        final String OWM_RESULTS = "results";
        final String OWM_ID = "id";
        final String OWM_NAME = "author";
        final String OWM_KEY = "content";

        JSONObject trailerJSON = new JSONObject(trailerJsonStr);
        JSONArray trailersArray = trailerJSON.getJSONArray(OWM_RESULTS);

        ArrayList<Review> resultStrs = new ArrayList<>();

        final MoviesDbHelper dbHelper = new MoviesDbHelper(context);
        dbHelper.deleteAllReviewEntries();

        for (int i = 0; i < trailersArray.length(); i++) {
            String id;
            String author;
            String content;

            //Get JSON Object representing the movie trailer
            JSONObject trailer = trailersArray.getJSONObject(i);
            id = trailer.getString(OWM_ID);
            author = trailer.getString(OWM_NAME);
            content = trailer.getString(OWM_KEY);

            Review newReview = new Review(id, author, content);
            resultStrs.add(newReview);
            dbHelper.insertReview(id, author, content);
        }
        dbHelper.close();
        return resultStrs;
    }

    @Override
    protected ArrayList<Review> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String trailerJsonStr = null;

        try {
            final String APIKEY_PARAM = "api_key";

            Uri.Builder builtUri = new Uri.Builder();
            builtUri.scheme("https")
                    .authority(Constants.API_AUTHORITY)
                    .appendPath(Constants.API_VERSION)
                    .appendPath(Constants.API_CONTENT)
                    .appendPath(params[0])
                    .appendPath(Constants.API_RESOURCE_REVIEWS)
                    .appendQueryParameter(APIKEY_PARAM, BuildConfig.MOVIESDB_API_KEY).build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URL: " + builtUri.toString());

            //Create the request to moviesdb and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read the input stream into a string
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
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

            trailerJsonStr = buffer.toString();
//            Log.i(LOG_TAG, "Downloaded Data" + trailerJsonStr);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error", e);
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
            return getTrailersDataFromJson(trailerJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(ArrayList<Review> result) {
        if (result != null) {
            if (listener != null) {
                listener.OnUpdate(result);
            }
        }
    }

    public interface OnUpdateListener {
        void OnUpdate(ArrayList<Review> reviews);
    }
}
