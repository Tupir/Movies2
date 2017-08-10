package com.example.android.moviesremake;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesremake.data.MovieTableContents;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.android.moviesremake.R.id.overview;

public class FavoriteDetailActivity extends AppCompatActivity implements  LoaderManager.LoaderCallbacks<Cursor>{


    public static final String[] MAIN_MOVIE_PROJECTION = {
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


    private static final int ID_DETAIL_LOADER = 16;
    private Uri mUri;

    @Bind(overview) TextView textOverview;
    @Bind(R.id.vote) TextView textVote;
    @Bind(R.id.release) TextView textRelease;
    @Bind(R.id.imageView1) ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_relative);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this); // before setText


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
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {


        switch (loaderId) {

            case ID_DETAIL_LOADER:

                return new CursorLoader(this,
                        mUri,
                        MAIN_MOVIE_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            return;
        }

        String image = data.getString(INDEX_MOVIE_IMAGE);

        Picasso.with(this)
                .load(image)
                .placeholder(R.drawable.no_image)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .error(R.drawable.no_image)
                .into(imageView);

        String overview = data.getString(INDEX_MOVIE_MAX_OVERVIEW);
        textOverview.setText(overview);

        String release = data.getString(INDEX_MOVIE_RELEASE);
        textRelease.setText(release);

        float vote = data.getFloat(INDEX_MOVIE_VOTE);
        String voteStr = Float.toString(vote);
        textVote.setText(voteStr);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
