package com.example.android.popularmovies.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.detail.DetailActivity;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

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
 * Fragment for main page listing
 */

public class MoviesFragment extends Fragment {
    private MoviesAdapter moviesAdapter;
    public MoviesFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle main events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_movies, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateMovies() {
        FetchMovieTasks movieTasks = new FetchMovieTasks();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));
        movieTasks.execute(sortBy);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        int resultLength = movieList.size();
//        String [] imagesURL = new String[resultLength];
//        String [] movieTitle = new String[resultLength];
//        for (int i=0;i<resultLength;i++){
//            imagesURL[i]=movieList.get(i).getPosterURL();
//            movieTitle[i]=movieList.get(i).getTitle();
//        }

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        mForecastAdapter =
//                new ArrayAdapter<String>(
//                        getActivity(), // The current context (this activity)
//                        R.layout.list_item_movies, // The name of the layout ID.
//                        R.id.list_item_movies_textview, // The ID of the textview to populate.
//                        new ArrayList<String>());

        moviesAdapter = new MoviesAdapter(getContext(), new ArrayList<Movie>());
        GridView listView = (GridView) rootView.findViewById(R.id.listview_movies);
        listView.setAdapter(moviesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movieDetail = moviesAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Movie mParcel = movieDetail;
                intent.putExtra("myMovie", mParcel);
                startActivity(intent);
            }
        });

        return rootView;
    }


    public class MoviesAdapter extends ArrayAdapter<Movie> {

        public MoviesAdapter(Context context, ArrayList<Movie> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Movie movie = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movies, parent, false);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.movieThumbnail);
            Picasso.with(getContext())
                    .load(movie.getPosterURL())
                    .placeholder(R.drawable.placeholder_poster)
                    .fit()
                    .into(imageView);
            return convertView;
        }
    }


    public class FetchMovieTasks extends AsyncTask<String, Void, ArrayList<Movie>> {

        private final String LOG_TAG = FetchMovieTasks.class.getSimpleName();

//        private String getReadableDateString(Date time) {
//            // Because the API returns a unix timestamp (measured in seconds),
//            // it must be converted to milliseconds in order to be converted to valid date.
//            //also setting timezone to be UTC
//            TimeZone timeZone = TimeZone.getTimeZone("UTC");
//            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
//            shortenedDateFormat.setTimeZone(timeZone);
//            return shortenedDateFormat.format(time);
//        }

        private ArrayList<Movie> getMoviesDataFromJson(String moviesJsonStr)
                throws JSONException {
            final String OWM_RESULTS = "results";
            final String OWM_POSTER_PATH = "poster_path";
            final String OWM_TITLE = "original_title";
            final String OWM_PLOT = "overview";
            final String OWM_RATINGS = "vote_average";
            final String OWM_RELEASE_DATE = "release_date";

            JSONObject forecastJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = forecastJson.getJSONArray(OWM_RESULTS);

            ArrayList<Movie> resultStrs = new ArrayList<>();

            for (int i = 0; i < moviesArray.length(); i++) {
                String title;
                String ratings;
                String posterURL;
                String plot;
                String releaseDate;
                //Get JSON Object representing the movie
                JSONObject movie = moviesArray.getJSONObject(i);
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
                Movie newMovie = new Movie(title, ratings, posterURL, plot, releaseDate);
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
                Log.i("MoviesFragment.java ", "Downloaded Data " + movieJsonStr);
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
//                mForecastAdapter.clear();
                moviesAdapter.clear();
                for (Movie moviesStr : result) {
//                    movieList.add(moviesStr);
//                    mForecastAdapter.add(moviesStr.getPosterURL());
                    moviesAdapter.add(moviesStr);
                }

            }
        }
    }
}