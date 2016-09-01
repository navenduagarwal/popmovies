package com.example.android.popularmovies.asynctasks;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.detail.TrailerAdapter;
import com.example.android.popularmovies.model.Trailer;
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
 * Service to download list of trailers
 */
public class FetchTrailersService extends IntentService{
    private TrailerAdapter trailerAdapter;
    private final String LOG_TAG = "TrailersService#";

    /**
     * Actions
     **/
    public static final String ACTION_DOWNLOAD = "action_download";
    public static final String ACTION_COMPLETED = "action_completed";
    public static final String ACTION_ERROR = "action_error";
    /**

    /**
     * Extras
     **/
    public static final String EXTRA_MOVIE_ID = "extra_movie_id";
    public static final String EXTRA_TRAILERS = "extra_trailers";
    public static final String EXTRA_DOWNLOAD_ERROR = "extra_download_ERROR";

    public FetchTrailersService(){
        super("TrailersService#");
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        filter.addAction(ACTION_COMPLETED);
        filter.addAction(ACTION_ERROR);
        return filter;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String movieId = intent.getStringExtra(EXTRA_MOVIE_ID);
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
                    .appendPath(movieId)
                    .appendPath(Constants.API_RESOURCE)
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
                return;
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
                return;
            }

            trailerJsonStr = buffer.toString();
            Log.i(LOG_TAG, "Downloaded Data" + trailerJsonStr);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error", e);
            return;
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
            getTrailersDataFromJson(trailerJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void getTrailersDataFromJson(String trailerJsonStr) throws JSONException {
        final String OWM_RESULTS = "results";
        final String OWM_NAME = "name";
        final String OWM_KEY = "key";
        final String OWM_SITE = "site";

       try{ JSONObject trailerJSON = new JSONObject(trailerJsonStr);
        JSONArray trailersArray = trailerJSON.getJSONArray(OWM_RESULTS);

        ArrayList<Trailer> resultStrs = new ArrayList<>();

        for (int i = 0; i < trailersArray.length(); i++) {
            String name;
            String key;
            String site;

            //Get JSON Object representing the movie trailer
            JSONObject trailer = trailersArray.getJSONObject(i);
            name = trailer.getString(OWM_NAME);
            key = trailer.getString(OWM_KEY);
            site = trailer.getString(OWM_SITE);

            if (site.equals(Constants.KEY_TRAILER_SITE)) {
                Trailer newTrailer = new Trailer(name, key, site);
                resultStrs.add(newTrailer);
            }
        }
           Log.d(LOG_TAG,"Sending intent");
           Intent broadcast = new Intent(ACTION_COMPLETED);
           broadcast.addCategory(Intent.CATEGORY_DEFAULT);
           broadcast.putExtra(EXTRA_TRAILERS,resultStrs);
           LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcast);
    } catch (JSONException e){
       Log.e(LOG_TAG, e.getMessage(), e);
           e.printStackTrace();
       }
    }
}
