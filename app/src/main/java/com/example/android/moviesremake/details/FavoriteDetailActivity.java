package com.example.android.moviesremake.details;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesremake.R;
import com.example.android.moviesremake.data.MovieTableContents;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.moviesremake.R.id.overview;
import static com.example.android.moviesremake.R.id.reviews;
import static com.example.android.moviesremake.R.id.trailers;

public class FavoriteDetailActivity extends AppCompatActivity implements  LoaderManager.LoaderCallbacks<Cursor>,
        DetailAdapter.ForecastAdapterOnClickHandler{


    public static final String[] MAIN_MOVIE_PROJECTION = {
            MovieTableContents.MovieEntry.COLUMN_IMAGE,
            MovieTableContents.MovieEntry.COLUMN_OVERVIEW,
            MovieTableContents.MovieEntry.COLUMN_RELEASE,
            MovieTableContents.MovieEntry.COLUMN_TITLE,
            MovieTableContents.MovieEntry.COLUMN_VOTE,
            MovieTableContents.MovieEntry.COLUMN_ID,
    };


    public static final int INDEX_MOVIE_IMAGE = 0;
    public static final int INDEX_MOVIE_MAX_OVERVIEW = 1;
    public static final int INDEX_MOVIE_RELEASE = 2;
    public static final int INDEX_MOVIE_TITLE = 3;
    public static final int INDEX_MOVIE_VOTE = 4;
    public static final int INDEX_MOVIE_ID = 5;


    private static final int ID_DETAIL_LOADER = 16;
    private Uri mUri;
    private DetailAdapter mAdapter;
    private RecyclerView recycler;
    int movieId;

    @BindView(overview) TextView textOverview;
    @BindView(R.id.vote) TextView textVote;
    @BindView(R.id.release) TextView textRelease;
    @BindView(R.id.imageView1) ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_relative);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this); // before setText

        View b = findViewById(R.id.favorite_button);
        b.setVisibility(View.GONE);

        mUri = getIntent().getData();
        if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");
        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);

        movieId = getMovieId(getIntent().getStringExtra("id")); // returns ID of movie

        // nastavenie recyclerview
        recycler = (RecyclerView) findViewById(trailers);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);

        mAdapter = new DetailAdapter(this, this);
        recycler.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(DetailActivity.ID_TRAILER_LOADER, null, new TrailerAndReviewLoader(this, mAdapter, movieId));

        // nastavenie recyclerview
        recycler = (RecyclerView) findViewById(reviews);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager2);
        recycler.setHasFixedSize(true);

        mAdapter = new DetailAdapter(this, this);
        recycler.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(DetailActivity.ID_REVIEW_LOADER2, null, new TrailerAndReviewLoader(this, mAdapter, movieId));



    }


    public int getMovieId(String query){
        Cursor cursor = this.getContentResolver().
                query(MovieTableContents.MovieEntry.CONTENT_URI,
                null,   //new String[] {"image"}
                MovieTableContents.MovieEntry.COLUMN_IMAGE + " = ? ",
                new String[]{query},
                null);

        if(cursor.moveToFirst()) {
            System.out.println(cursor.getInt(INDEX_MOVIE_ID));
            return cursor.getInt(INDEX_MOVIE_ID);
        }
        System.out.println("Error finding an ID of movie");
        return 0;
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
        textVote.setText(voteStr+"/10 ");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.youtube.com/watch?v="+url));

        // Always use string resources for UI text. This says something like "Share this photo with"
        String title = (String) getResources().getText(R.string.chooser_title);
        // Create and start the chooser

        Intent chooser = Intent.createChooser(intent, title);

        try {
            this.startActivity(chooser);
        } catch (ActivityNotFoundException ex) {
            intent.setPackage("com.android.chrome");
            this.startActivity(chooser);
        }
    }


}
