package com.example.android.popularmovies.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.SettingsActivity;
import com.example.android.popularmovies.detail.DetailActivity;
import com.example.android.popularmovies.detail.DetailFragment;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.Constants;

/**
 * Main activity
 */
public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback{

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private static final String TAG = MainActivity.class.getSimpleName();
    public static boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(findViewById(R.id.movies_detail_container) != null){
            mTwoPane = true;
            if (savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movies_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

       MoviesFragment moviesFragment = ((MoviesFragment) getSupportFragmentManager()
               .findFragmentById(R.id.fragment_movies));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            // In two pane mode, show the detail view in this activity  by
            // adding or replacing the detail fragment using
            // fragment transaction.
            Log.d("Hello", "Two Pane");

            Bundle args = new Bundle();
            args.putParcelable(Constants.KEY_MOVIE_1, movie);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movies_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Log.d("Hello", "Single Pane");
            Intent intent = new Intent(this, DetailActivity.class);
                    intent.putExtra(Constants.KEY_MOVIE,movie);
            startActivity(intent);
        }
    }
}
