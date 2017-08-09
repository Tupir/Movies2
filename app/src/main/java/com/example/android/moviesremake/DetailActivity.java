package com.example.android.moviesremake;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesremake.data.MovieTableContents;
import com.example.android.moviesremake.utils.Movie;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

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

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movies")) {
                mForecast = intentThatStartedThisActivity.getParcelableExtra("movies");
                Picasso.with(this).load(mForecast.getImage()).into(imageView);
                textRelease.setText(mForecast.getRelease());
                textVote.setText(mForecast.getVote().toString());
                textOverview.setText(mForecast.getOverview());
            }
        }


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


    public void addMovieToDatabase(View view){
        ContentValues cv = new ContentValues();

        cv.put(MovieTableContents.MovieEntry.COLUMN_IMAGE, mForecast.getImage());
        cv.put(MovieTableContents.MovieEntry.COLUMN_TITLE, mForecast.getTitle());
        cv.put(MovieTableContents.MovieEntry.COLUMN_RELEASE, mForecast.getRelease());
        cv.put(MovieTableContents.MovieEntry.COLUMN_VOTE, mForecast.getVote());
        cv.put(MovieTableContents.MovieEntry.COLUMN_OVERVIEW, mForecast.getOverview());

        // check if exists
//        Uri forMovieClicked = MovieTableContents.MovieEntry.buildOneMovieUri(mForecast.getImage());
//        query?
        // vytvor funkciu v MovieProvider, getColumntIndex == -1

        this.getContentResolver().insert(
                MovieTableContents.MovieEntry.CONTENT_URI,
                cv);
    }

    public void testingButton(View view){


        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.youtube.com/watch?v=xw-QIZZwDBg"));

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
