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
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.android.moviesremake.MainActivity.mLoadingIndicator;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<String>>,
        DetailAdapter.ForecastAdapterOnClickHandler{

    private static final int ID_DETAIL_LOADER = 163;

    private Movie mForecast;
    private DetailAdapter mAdapter;
    private RecyclerView recycler;

    List<List<String>> reviews = new ArrayList<>();
    @Bind(R.id.overview) TextView textOverview;
    @Bind(R.id.vote) TextView textVote;
    @Bind(R.id.release) TextView textRelease;
    @Bind(R.id.imageView1) ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_relative);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this); // before setText

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movies")) {
                mForecast = intentThatStartedThisActivity.getParcelableExtra("movies");
                setTitle(mForecast.getTitle());
                Picasso.with(this).load(mForecast.getImage()).into(imageView);
                textRelease.setText(mForecast.getRelease());
                textVote.setText(mForecast.getVote()+"/10".toString());
                textOverview.setText(mForecast.getOverview());
            }
        }

        // nastavenie recyclerview
        recycler = (RecyclerView) findViewById(R.id.trailers);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);

        mAdapter = new DetailAdapter(this, this);
        recycler.setAdapter(mAdapter);

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

        /**
         * Checking if movie is in database already
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

                URL videos = NetworkUtils.buildUrlForTrailer(Integer.toString(mForecast.getId()), "videos");
                URL weatherRequestUr2 = NetworkUtils.buildUrlForTrailer(Integer.toString(mForecast.getId()), "reviews");

                try {
                    String jsonWeatherResponse = NetworkUtils   // JSON for trailer
                            .getResponseFromHttpUrl(videos);

                    String jsonWeatherResponse1 = NetworkUtils     // JSON for reviews
                            .getResponseFromHttpUrl(weatherRequestUr2);

                    reviews = MovieJsonParser.getReviews(jsonWeatherResponse1);

                    trailers = MovieJsonParser.getTrailers(jsonWeatherResponse);

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Movie does not have data yet");
                }
                return trailers;
            }



            public void deliverResult(ArrayList<String> data) {
                trailers = data;
                super.deliverResult(trailers);
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<String>> loader, ArrayList<String> data) {
        if(data == null || reviews == null){    // if there is still not trailer/review on web page
            return;
        }
        System.out.println(Arrays.toString(data.toArray()));
        System.out.println(Arrays.deepToString(reviews.toArray()));
        String str  = reviews.get(0).get(0);
        String str1  = reviews.get(0).get(1);
        mAdapter.setTrailerData(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<String>> loader) {

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
