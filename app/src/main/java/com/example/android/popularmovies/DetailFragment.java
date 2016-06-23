package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
//        Intent intent = getActivity().getIntent();
//        if(intent!=null && intent.hasExtra(Intent.EXTRA_TEXT)){
//            movieTitle = intent.getStringExtra(Intent.EXTRA_TEXT);
//            TextView detailView= (TextView)rootView.findViewById(R.id.detail_text);
//            detailView.setText(movieTitle);
//        }
        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();
        Movie movieDetail = (Movie) b.getParcelable("myMovie");
        String movieTitle = movieDetail.getTitle();
        TextView detailView = (TextView) rootView.findViewById(R.id.detail_text);
        detailView.setText(movieTitle);
        return rootView;
    }


}
