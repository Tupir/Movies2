package com.example.android.moviesremake;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesremake.data.MovieTableContents;
import com.example.android.moviesremake.utils.Movie;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FavoriteDetailActivity extends AppCompatActivity implements  LoaderManager.LoaderCallbacks<Cursor>{


    public static final String[] MAIN_FORECAST_PROJECTION = {
            MovieTableContents.MovieEntry.COLUMN_IMAGE,
            MovieTableContents.MovieEntry.COLUMN_OVERVIEW,
            MovieTableContents.MovieEntry.COLUMN_RELEASE,
            MovieTableContents.MovieEntry.COLUMN_TITLE,
            MovieTableContents.MovieEntry.COLUMN_VOTE,
    };


    public static final int INDEX_MOVIE_IMAGE = 0;
    public static final int INDEX_MOVIE_MAX_OVERVIEW = 1;
    public static final int INDEX_MOVIE_RELEASE = 2;
    public static final int INDEX_MOVIE_TITLE = 3;
    public static final int INDEX_MOVIE_VOTE = 4;


    private static final int ID_DETAIL_LOADER = 353;
    private Uri mUri;

    private Movie mForecast;
    @Bind(R.id.overview) TextView textOverview;
    @Bind(R.id.vote) TextView textVote;
    @Bind(R.id.release) TextView textRelease;
    @Bind(R.id.imageView1) ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this); // before setText

//        Intent intentThatStartedThisActivity = getIntent();
//        if (intentThatStartedThisActivity != null) {
//            if (intentThatStartedThisActivity.hasExtra("movies")) {
//                mForecast = intentThatStartedThisActivity.getParcelableExtra("movies");
//                Picasso.with(this).load(mForecast.getImage()).into(imageView);
//                textRelease.setText(mForecast.getRelease());
//                textVote.setText(mForecast.getVote().toString());
//                textOverview.setText(mForecast.getOverview());
//            }
//        }


        mUri = getIntent().getData();
        if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");
        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Back button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
