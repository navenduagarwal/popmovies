package com.example.android.popularmovies.detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.asynctasks.FetchTrailersService;
import com.example.android.popularmovies.asynctasks.FetchTrailersTask;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.utils.Constants;

import java.util.ArrayList;

/**
 * Detail Fragment
 */
public class DetailFragment extends Fragment {
    static final String LOG_TAG = DetailFragment.class.getSimpleName();
    public static final String DETAIL_MOVIE = "MOVIE";
    private Movie movieDetail;
    private RecyclerView mRecycleViewTrailer;
    private TrailerAdapter mTrailerAdapter;
    private BroadcastReceiver mDownloadReceiver;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            movieDetail = arguments.getParcelable(Constants.KEY_MOVIE_1);
            Log.d("Movie", movieDetail.toString());
            mRecycleViewTrailer = (RecyclerView) rootView.findViewById(R.id.recycle_view_trailers);
            mRecycleViewTrailer.setHasFixedSize(true);
            mRecycleViewTrailer.setLayoutManager(new LinearLayoutManager(getActivity()));
            updateTrailers();
        }

//        Trailer trailer = new Trailer("navendu","4oyhnqSQtHI","YouTube");
//        ArrayList<Trailer> testArray = new ArrayList<>();
//        testArray.add(trailer);
//        mRecycleViewTrailer.setAdapter(new TrailerAdapter(getContext(), testArray, movieDetail));
//        mRecycleViewTrailer.invalidate();

        return rootView;
    }

    public void updateTrailers() {

        FetchTrailersTask trailersTask = new FetchTrailersTask(getContext(), mRecycleViewTrailer, movieDetail);
        trailersTask.execute(movieDetail.getId());
    }

}
