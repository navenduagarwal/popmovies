package com.example.android.popularmovies.detail;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to populate Trailer list view
 */
public class TrailerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private ArrayList<Trailer> trailers;
    private Context context;
    private Movie mMovie;

    public TrailerAdapter(Context context, ArrayList<Trailer> trailers, Movie movie) {
        this.trailers = trailers;
        this.context = context;
        this.mMovie = movie;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER) {
            View v = LayoutInflater.from (parent.getContext ()).inflate (context.getResources().getLayout(R.layout.list_item_detail_header), parent, false);
            return new HeaderViewHolder (v);
        } else if(viewType == TYPE_ITEM) {
            View v = LayoutInflater.from (parent.getContext ()).inflate (context.getResources().getLayout(R.layout.list_item_trailer), parent, false);
            return new TrailerViewHolder (v);
        }
        return null;
    }

    private Trailer getItem (int position) {
        return trailers.get (position);
    }

    @Override
    public int getItemCount() {
        return trailers.size() +1;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

                    /**Update Thumbnail image in detail view*/
        Picasso.with(context)
                .load(mMovie.getPosterURL())
                .into(headerHolder.posterThumbnail);
        /**Update Title Text in detail view*/
        headerHolder.titleView.setText(mMovie.getTitle());
        /**Update year Text in detail view*/
        String year = mMovie.getReleaseDate().substring(0, 4);
        headerHolder.yearView.setText(year);
        /**Update ratings Text in detail view*/
        String ratings = mMovie.getRatings() + "/10";
        headerHolder.ratingsView.setText(ratings);
        /**Update plot Text in detail view*/
        headerHolder.plotView.setText(mMovie.getPlot());

       }
        else if  (holder instanceof TrailerViewHolder) {
            final Trailer currentItem = getItem(position -1);
            TrailerViewHolder trailerViewHolder = (TrailerViewHolder) holder;
            trailerViewHolder.name.setText(currentItem.getName());
            trailerViewHolder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    watchYoutubeVideo(context,currentItem.getKey());
                }
            });
        }
    }

    //    need to override this method
    @Override
    public int getItemViewType (int position) {
        if(isPositionHeader (position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader (int position) {
        return position == 0;
    }

    public static class TrailerViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        FrameLayout container;

        public TrailerViewHolder (View itemView){
            super (itemView);
            this.name = (TextView) itemView.findViewById(R.id.list_item_name_textview);
            this.container = (FrameLayout) itemView.findViewById(R.id.container_list_item);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        ImageView posterThumbnail;
        TextView titleView,yearView,ratingsView,plotView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            posterThumbnail = (ImageView) itemView.findViewById(R.id.detail_poster_imageview);
            titleView = (TextView) itemView.findViewById(R.id.detail_title_textView);
            yearView = (TextView) itemView.findViewById(R.id.detail_year_textview);
            ratingsView = (TextView) itemView.findViewById(R.id.detail_ratings_textview);
            plotView = (TextView) itemView.findViewById(R.id.detail_plot_textView);
        }
    }


    public static void watchYoutubeVideo(Context context, String id) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            context.startActivity(intent);
        }
    }

}
