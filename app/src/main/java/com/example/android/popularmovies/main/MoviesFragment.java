package com.example.android.popularmovies.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.asynctasks.FetchMovieTasks;
import com.example.android.popularmovies.data.MoviesDbHelper;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.Constants;

import java.util.ArrayList;

/**
 * Fragment for main page listing
 */

public class MoviesFragment extends Fragment {
    private MoviesAdapter moviesAdapter;
    private GridView mGridView;
    private int mPosition = GridView.INVALID_POSITION;
    private ProgressBar mProgressBar;

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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));
        if (sortBy.equals(getResources().getString(R.string.pref_sort_favorites))) {

            final MoviesDbHelper moviesDbHelper = new MoviesDbHelper(getContext());
            ArrayList<Movie> result = moviesDbHelper.getAllMoviesData();
            if (result != null) {
                updateViewWithResults(result);
            } else {
                mProgressBar.setVisibility(View.GONE);
                mGridView.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.toast_no_favourite_data), Toast.LENGTH_LONG).show();
                if (MainActivity.mTwoPane) {
                    ((Callback) getActivity()).onItemSelected(null);
                }
            }

        } else {

            FetchMovieTasks movieTasks = new FetchMovieTasks();
            movieTasks.setUpdateListener(new FetchMovieTasks.OnUpdateListener() {
                @Override
                public void OnUpdate(ArrayList<Movie> result) {
                    updateViewWithResults(result);
                }
            });

            movieTasks.execute(sortBy);
        }
    }


    public void updateViewWithResults(ArrayList<Movie> result) {

        //updating adapter
        moviesAdapter.clear();
        for (Movie moviesStr : result) {
            moviesAdapter.add(moviesStr);
        }
        //To autoclick first movie
        if (mPosition != GridView.INVALID_POSITION) {
            mGridView.smoothScrollToPosition(mPosition);
        } else if (MainActivity.mTwoPane) {
            mGridView.post(new Runnable() {
                @Override
                public void run() {
                    mGridView.performItemClick(mGridView, 0, mGridView.getAdapter().getItemId(0));
                }
            });
        }
        mProgressBar.setVisibility(View.GONE);
        mGridView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (moviesAdapter != null) {
            moviesAdapter.clear();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.main_progressbar);

        moviesAdapter = new MoviesAdapter(getContext(), new ArrayList<Movie>());
        mGridView = (GridView) rootView.findViewById(R.id.listview_movies);
        mGridView.setAdapter(moviesAdapter);
        mProgressBar.setVisibility(View.VISIBLE);
        mGridView.setVisibility(View.GONE);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movieDetail = moviesAdapter.getItem(position);

                ((Callback) getActivity()).onItemSelected(movieDetail);
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(Constants.SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(Constants.SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onItemSelected(Movie movie);
    }

}