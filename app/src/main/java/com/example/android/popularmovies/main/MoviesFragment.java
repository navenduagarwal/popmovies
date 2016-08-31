package com.example.android.popularmovies.main;

import android.content.Intent;
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

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.asynctasks.FetchMovieTasks;
import com.example.android.popularmovies.detail.DetailActivity;
import com.example.android.popularmovies.model.Movie;

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

}