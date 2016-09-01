package com.example.android.popularmovies.detail;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.asynctasks.FetchTrailersTask;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.Constants;

/**
 * Detail Fragment
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Movie> {
    public static final String DETAIL_MOVIE = "MOVIE";
    static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final int DETAIL_LOADER = 0;
    private Movie movieDetail;
    private RecyclerView mRecycleViewTrailer;
    private TrailerAdapter mTrailerAdapter;
    private BroadcastReceiver mDownloadReceiver;


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

        Bundle arguments = getArguments();
        if (arguments != null) {
            movieDetail = arguments.getParcelable(Constants.KEY_MOVIE_1);
            if (movieDetail == null) {
                return null;
            }
            mRecycleViewTrailer = (RecyclerView) rootView.findViewById(R.id.recycle_view_trailers);
            mRecycleViewTrailer.setHasFixedSize(true);
            mRecycleViewTrailer.setLayoutManager(new LinearLayoutManager(getActivity()));
            updateTrailers();
        } else {
            return null;
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {

    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {

    }
}
