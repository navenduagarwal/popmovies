package com.example.android.popularmovies.main;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.GridView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.asynctasks.FetchMovieTasks;
import com.example.android.popularmovies.detail.DetailActivity;
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
        FetchMovieTasks movieTasks = new FetchMovieTasks(moviesAdapter);
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
        mGridView = (GridView) rootView.findViewById(R.id.listview_movies);
        mGridView.setAdapter(moviesAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movieDetail = moviesAdapter.getItem(position);
                Log.d("navendu movie fragment", movieDetail.toString());
//                Intent intent = new Intent(getActivity(), DetailActivity.class);
//                intent.putExtra(Constants.KEY_MOVIE, movieDetail);
//                startActivity(intent);
                ((Callback)getActivity()).onItemSelected(movieDetail);
                mPosition = position;
            }
        });

        if(savedInstanceState !=null && savedInstanceState.containsKey(Constants.SELECTED_KEY)){
            mPosition = savedInstanceState.getInt(Constants.SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mPosition != GridView.INVALID_POSITION){
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