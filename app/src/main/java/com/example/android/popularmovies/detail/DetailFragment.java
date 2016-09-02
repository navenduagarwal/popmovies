package com.example.android.popularmovies.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.asynctasks.FetchReviewsTask;
import com.example.android.popularmovies.asynctasks.FetchTrailersTask;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.utils.Constants;

import java.util.ArrayList;

/**
 * Detail Fragment
 */
public class DetailFragment extends Fragment {
    static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private Movie movieDetail;
    private RecyclerView mRecycleViewTrailer;
    private TrailerAdapter mTrailerAdapter;
    private ArrayList<Review> mReviews;
    private ProgressBar progressBar;


    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.detail_progressbar);

        Bundle arguments = getArguments();
        if (arguments != null) {
            movieDetail = arguments.getParcelable(Constants.KEY_MOVIE_1);
            if (movieDetail == null) {
                return null;
            }
            mRecycleViewTrailer = (RecyclerView) rootView.findViewById(R.id.recycle_view_trailers);
            mRecycleViewTrailer.setHasFixedSize(true);
            mRecycleViewTrailer.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecycleViewTrailer.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            updateReviews();
        } else {
            return null;
        }
        return rootView;
    }

    public void updateReviews() {
        FetchReviewsTask reviewsTask = new FetchReviewsTask();
        reviewsTask.setUpdateListener(new FetchReviewsTask.OnUpdateListener() {
            @Override
            public void OnUpdate(ArrayList<Review> reviews) {
                if (reviews != null) {
                    mReviews = reviews;
                    updateTrailers();
                    Log.d(LOG_TAG, "Reviews" + reviews.size());
                }
            }
        });
        reviewsTask.execute(movieDetail.getId());
    }

    public void updateTrailers() {
        FetchTrailersTask trailersTask = new FetchTrailersTask();
        trailersTask.setUpdateListener(new FetchTrailersTask.OnUpdateListener() {
            @Override
            public void OnUpdate(ArrayList<Trailer> trailers) {
                mTrailerAdapter = new TrailerAdapter(getContext(), trailers, movieDetail, mReviews);
                mRecycleViewTrailer.setAdapter(mTrailerAdapter);
                mRecycleViewTrailer.invalidate();
                mRecycleViewTrailer.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
        trailersTask.execute(movieDetail.getId());
    }

}
