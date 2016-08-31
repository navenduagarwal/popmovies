package com.example.android.popularmovies.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Detail Fragment
 */
public class DetailFragment extends Fragment {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private String movieTitle;
    private ImageView posterThumbnail;
    private TextView titleView, yearView, ratingsView, plotView;

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

        initializeScreen(rootView);

        /**Update Thumbnail image in detail view*/
        Picasso.with(getContext())
                .load(movieDetail.getPosterURL())
                .into(posterThumbnail);
        /**Update Title Text in detail view*/
        titleView.setText(movieDetail.getTitle());
        /**Update year Text in detail view*/
        String year = movieDetail.getReleaseDate().substring(0, 4);
        yearView.setText(year);
        /**Update ratings Text in detail view*/
        String ratings = movieDetail.getRatings() + "/10";
        ratingsView.setText(ratings);
        /**Update plot Text in detail view*/
        plotView.setText(movieDetail.getPlot());
        return rootView;
    }

    public void initializeScreen(View rootView) {
        posterThumbnail = (ImageView) rootView.findViewById(R.id.detail_poster_imageview);
        titleView = (TextView) rootView.findViewById(R.id.detail_title_textView);
        yearView = (TextView) rootView.findViewById(R.id.detail_year_textview);
        ratingsView = (TextView) rootView.findViewById(R.id.detail_ratings_textview);
        plotView = (TextView) rootView.findViewById(R.id.detail_plot_textView);
    }


}
