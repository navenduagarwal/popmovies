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
            if (mReviews.size() > 1) {
                reviewViewHolder.author.setText(String.format(activity.getResources().getString(R.string.format_multiple_reviews_author), position + 1, currentReview.getAuthor()));
            } else {
                reviewViewHolder.author.setText(String.format(activity.getResources().getString(R.string.format_single_reviews_author), currentReview.getAuthor()));

            }
            String content = currentReview.getContent().replace("\\r\\n", "\n");
            reviewViewHolder.content.setText(content);
        }
    }


    @Override
    public int getItemCount() {
        return mReviews.size();
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
