package com.example.android.moviesremake;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;

import com.example.android.moviesremake.utils.Movie;
import com.example.android.moviesremake.utils.MovieJsonParser;
import com.example.android.moviesremake.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import static com.example.android.moviesremake.MainActivity.mLoadingIndicator;

/**
 * Created by PepovPC on 7/21/2017.
 */

// is ok to do it on Cursor Loader,but try to meanwhile save all other movie items into DB as SERVICE?
public class MovieLoader implements LoaderManager.LoaderCallbacks<ArrayList<Movie>>{

    public MovieAdapter mAdapter;
    private Context context;

    MovieLoader(Context context, MovieAdapter mAdapter){
        this.context = context;
        this.mAdapter = mAdapter;
    }



    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Movie>>(context) {

            /* This Movie array will hold and help cache our movie data */
            ArrayList<Movie> movies = null;

            /**
             * Subclasses of AsyncTaskLoader must implement this to take care of loading their data.
             * The same as onPreExecute() v AsyncTask
             */
            @Override
            protected void onStartLoading() {
                if (movies != null) {
                    deliverResult(movies);
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
            public ArrayList<Movie> loadInBackground() {


                String locationQuery = NetworkUtils
                        .getSearchQuery(context);

                System.out.println(locationQuery);

                URL weatherRequestUrl = NetworkUtils.buildUrl(locationQuery);

                try {
                    String jsonWeatherResponse = NetworkUtils
                            .getResponseFromHttpUrl(weatherRequestUrl);



                    return MovieJsonParser.getMovieDataFromJson(jsonWeatherResponse);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            /**
             * Sends the result of the load to the registered listener.
             *
             * @param data The result of the load
             */
            public void deliverResult(ArrayList<Movie> data) {
                movies = data;
                super.deliverResult(movies);
            }



        };
    }


    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        MainActivity.mLoadingIndicator.setVisibility(View.INVISIBLE);
        mAdapter.setMoviesData(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }
}
