package com.example.android.moviesremake;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesremake.data.MovieTableContents;
import com.example.android.moviesremake.utils.Movie;
import com.example.android.moviesremake.utils.MovieJsonParser;
import com.example.android.moviesremake.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.android.moviesremake.MainActivity.mLoadingIndicator;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<String>>{

    private static final int ID_DETAIL_LOADER = 166;

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


    public void insertMovieToDatabase(View view){
        ContentValues cv = new ContentValues();

        cv.put(MovieTableContents.MovieEntry.COLUMN_IMAGE, mForecast.getImage());
        cv.put(MovieTableContents.MovieEntry.COLUMN_TITLE, mForecast.getTitle());
        cv.put(MovieTableContents.MovieEntry.COLUMN_RELEASE, mForecast.getRelease());
        cv.put(MovieTableContents.MovieEntry.COLUMN_VOTE, mForecast.getVote());
        cv.put(MovieTableContents.MovieEntry.COLUMN_OVERVIEW, mForecast.getOverview());

        // checl if it's in database already
        /**
         * I am working here with getContentResolver, this method calls (query,update,delete,insert)
         *  functions that are implemented in Provider
         * getContentResolver calls code by URIs
         */
        Cursor cursor = this.getContentResolver().query(MovieTableContents.MovieEntry.CONTENT_URI,
                null,   //new String[] {"image"}
                MovieTableContents.MovieEntry.COLUMN_IMAGE + " = ? ",
                new String[]{mForecast.getImage()},
                null);

        if(cursor.moveToFirst()) {
            Toast.makeText(this, "You already marked this movie as favorite!",
                    Toast.LENGTH_LONG).show();
            return;
        }

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

    @Override
    public Loader<ArrayList<String>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<String>>(this) {

            ArrayList<String> trailers = null;

            /**
             * Subclasses of AsyncTaskLoader must implement this to take care of loading their data.
             * The same as onPreExecute() v AsyncTask
             */
            @Override
            protected void onStartLoading() {
                if (trailers != null) {
                    deliverResult(trailers);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            /**
             * This is the method of the AsyncTaskLoader that will load and parse the JSON data
             *
             * @return Movie data as an array of Movies.
             *         null if an error occurs
             */

            @Override
            public ArrayList<String> loadInBackground() {


                System.out.println(mForecast.getId());

                URL weatherRequestUrl = NetworkUtils.buildUrlForTrailer(Integer.toString(mForecast.getId()));

                try {
                    String jsonWeatherResponse = NetworkUtils
                            .getResponseFromHttpUrl(weatherRequestUrl);

                    return MovieJsonParser.getTrailers(jsonWeatherResponse);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }



            public void deliverResult(ArrayList<String> data) {
                trailers = data;
                super.deliverResult(trailers);
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<String>> loader, ArrayList<String> data) {
        System.out.println(Arrays.toString(data.toArray()));
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<String>> loader) {

    }
}
