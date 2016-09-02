package com.example.android.popularmovies.detail.reviews;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Review;

import java.util.ArrayList;

/**
 * Adapter to populate list of reviews
 */
public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Review> mReviews;
    private Activity activity;

    public ReviewsAdapter(Activity activity, ArrayList<Review> mReviews) {
        this.mReviews = mReviews;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(activity.getResources().getLayout(R.layout.list_item_review), parent, false);
        return new ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReviewViewHolder) {
            final Review currentReview = mReviews.get(position);
            ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;

            reviewViewHolder.author.setText(currentReview.getAuthor());
            reviewViewHolder.content.setText(currentReview.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView content;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            this.author = (TextView) itemView.findViewById(R.id.list_item_author_textview);
            this.content = (TextView) itemView.findViewById(R.id.list_item_content_textview);
        }
    }
}
