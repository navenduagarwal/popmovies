package com.example.android.popularmovies.detail.reviews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.utils.Constants;

import java.util.ArrayList;

/**
 * Shows Reviews in Dialog Fragment
 */
public class ReviewsDialogFragment extends DialogFragment {

    ArrayList<Review> mReviews;
    RecyclerView recyclerView;
    ReviewsAdapter mReviewsAdapter;

    /**
     * Public static constructor that creates fragment and
     * passes a bundle with data into it when adapter is created
     */
    public static ReviewsDialogFragment newInstance(ArrayList<Review> reviews) {
        ReviewsDialogFragment reviewsDialogFragment = new ReviewsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.KEY_REVIEWS, reviews);
        reviewsDialogFragment.setArguments(bundle);
        return reviewsDialogFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReviews = getArguments().getParcelableArrayList(Constants.KEY_REVIEWS);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_reviews, null);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view_reviews);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mReviewsAdapter = new ReviewsAdapter(getActivity(), mReviews);
        recyclerView.setAdapter(mReviewsAdapter);

        builder.setView(rootView)
                .setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ReviewsDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
