package com.example.android.moviesremake.details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;

import com.example.android.moviesremake.utils.MovieJsonParser;
import com.example.android.moviesremake.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.moviesremake.MainActivity.mLoadingIndicator;
/**
 * Created by PepovPC on 8/12/2017.
 */

public class TrailerAndReviewLoader implements LoaderManager.LoaderCallbacks<List<List<String>>> {

    public DetailAdapter mAdapter;
    public DetailAdapter mAdapter1;
    private Context context;
    int movieId;

    TrailerAndReviewLoader(Context context, DetailAdapter mAdapter, int movieId){
        this.context = context;
        this.mAdapter = mAdapter;
        this.movieId = movieId;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<List<String>>> onCreateLoader(final int id, Bundle args) {

        if(id != DetailActivity.ID_TRAILER_LOADER)
            mAdapter1 = mAdapter;

        return new AsyncTaskLoader<List<List<String>>>(context) {

            List<List<String>> reviews = null;  // must be null because of forceLoad

            /**
             * Subclasses of AsyncTaskLoader must implement this to take care of loading their data.
             * The same as onPreExecute() v AsyncTask
             */
            @Override
            protected void onStartLoading() {
                if (reviews != null) {
                    deliverResult(reviews);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }


            @Override
            public List<List<String>> loadInBackground() {


                System.out.println(movieId);

                URL videos = NetworkUtils.buildUrlForTrailer(Integer.toString(movieId), "videos");
                URL weatherRequestUr2 = NetworkUtils.buildUrlForTrailer(Integer.toString(movieId), "reviews");
                reviews = new ArrayList<>();

                try {

                    if(id == DetailActivity.ID_TRAILER_LOADER) {
                        String jsonWeatherResponse = NetworkUtils   // JSON for trailer
                                .getResponseFromHttpUrl(videos);

                        reviews = MovieJsonParser.getTrailers(jsonWeatherResponse, reviews);
                    }
                    else {
                        String jsonWeatherResponse1 = NetworkUtils     // JSON for reviews
                                .getResponseFromHttpUrl(weatherRequestUr2);

                        reviews = MovieJsonParser.getReviews(jsonWeatherResponse1, reviews);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Movie does not have data yet");
                }
                return reviews;
            }


            public void deliverResult(List<List<String>> data) {
                reviews = data;
                super.deliverResult(reviews);
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<List<List<String>>> loader, List<List<String>> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if(data == null){    // if there is still not trailer/review on web page
            return;
        }

        if(loader.getId()== DetailActivity.ID_TRAILER_LOADER)
            mAdapter.setTrailerData(data);
        else
            mAdapter1.setTrailerData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<List<String>>> loader) {

    }


}
