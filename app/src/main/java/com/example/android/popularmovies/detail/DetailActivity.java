package com.example.android.popularmovies.detail;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.detail.reviews.ReviewsDialogFragment;
import com.example.android.popularmovies.utils.Constants;

/**
 * Detail Page Activity
 */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detail);
        // Add this line in order for this fragment to handle menu events.
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(Constants.KEY_MOVIE_1,getIntent().getParcelableExtra(Constants.KEY_MOVIE));

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            Log.d("Hello Detail Activity", "testin");

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movies_detail_container, fragment)
                    .commit();
        }
    }

    public void showReviewsDialog(View view) {

        DialogFragment dialogFragment = new ReviewsDialogFragment();
        dialogFragment.show(DetailActivity.this.getFragmentManager(), "ReviewsDialogFragment");
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.detail, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            startActivity(new Intent(this, SettingsActivity.class));
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//
//    }



}
