package com.example.android.popularmovies.detail;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to populate Trailer list view
 */
public class TrailerAdapter extends ArrayAdapter<Trailer> {
    private Context mContext;

    public TrailerAdapter(Context context, ArrayList<Trailer> objects) {
        super(context, 0, objects);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Trailer trailer = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_trailer, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.list_item_name_textview);
        name.setText(trailer.getName());

        FrameLayout container = (FrameLayout) convertView.findViewById(R.id.container_list_item);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watchYoutubeVideo(mContext, trailer.getKey());
            }
        });
        return convertView;
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
