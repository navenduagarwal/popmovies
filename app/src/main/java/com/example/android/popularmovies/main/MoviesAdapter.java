package com.example.android.popularmovies.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Adapter to populate main view
 */
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
