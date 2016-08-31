package com.example.android.popularmovies.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.main.Movie;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Navendu Agarwal on 22-Jun-16.
 */
public class DetailFragment extends Fragment {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private String movieTitle;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();
        Movie movieDetail = (Movie) b.getParcelable("myMovie");

        /**Update Thumbnail image in detail view*/
        ImageView posterThumbnail = (ImageView) rootView.findViewById(R.id.detail_poster_imageview);
        Picasso.with(getContext())
                .load(movieDetail.getPosterURL())
                .into(posterThumbnail);
        /**Update Title Text in detail view*/
        TextView titleView = (TextView) rootView.findViewById(R.id.detail_title_textView);
        titleView.setText(movieDetail.getTitle());
        /**Update year Text in detail view*/
        TextView yearView = (TextView) rootView.findViewById(R.id.detail_year_textview);
        String year = movieDetail.getReleaseDate().substring(0, 4);
        yearView.setText(year);
        /**Update ratings Text in detail view*/
        String ratings = movieDetail.getRatings() + "/10";
        TextView ratingsView = (TextView) rootView.findViewById(R.id.detail_ratings_textview);
        ratingsView.setText(ratings);
        /**Update plot Text in detail view*/
        TextView plotView = (TextView) rootView.findViewById(R.id.detail_plot_textView);
        plotView.setText(movieDetail.getPlot());

        return rootView;
    }


}
