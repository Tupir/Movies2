package com.example.android.moviesremake;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.moviesremake.details.DetailActivity;
import com.example.android.moviesremake.favorites.FavoriteActivity;
import com.example.android.moviesremake.retrofit.ApiClient;
import com.example.android.moviesremake.retrofit.ApiInterface;
import com.example.android.moviesremake.retrofit.MovieRetrofit;
import com.example.android.moviesremake.retrofit.MoviesResponse;
import com.example.android.moviesremake.settings.SettingsActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements
        MovieAdapter.adapterOnClickHandler,     // recycler view click handler
        SharedPreferences.OnSharedPreferenceChangeListener {     // settings change

    private static final String TAG = MainActivity.class.getSimpleName();
    private MovieAdapter mAdapter;
    private RecyclerView recycler;
    public static ProgressBar mLoadingIndicator;
    // COMPLETED (4) Add a private static boolean flag for preference updates and initialize it to false
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    public static String API_KEY = "c88f3eabe09958ae472c9cd7e20b38aa";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // if there is no connection
        if (!isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(), "Connection not available", Toast.LENGTH_SHORT).show();
        }

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);  // loader

        // nastavenie recyclerview
        recycler = (RecyclerView) findViewById(R.id.rv_numbers);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);

        // vyplnit recyclerview datami
        mAdapter = new MovieAdapter(getBaseContext(), this);
        recycler.setAdapter(mAdapter);


        //RxAsyncTask(this);
        retrofitApiCall();

        /*
         * Register MainActivity as an OnPreferenceChangedListener to receive a callback when a
         * SharedPreference has changed. Please note that we must unregister MainActivity as an
         * OnSharedPreferenceChanged listener in onDestroy to avoid any memory leaks.
         */
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

    }


//    public void RxAsyncTask(Context context){
//
//        Observable.fromCallable(() -> {
//
//            String locationQuery = NetworkUtils
//                    .getSearchQuery(context);
//            System.out.println(locationQuery);
//            URL weatherRequestUrl = NetworkUtils.buildUrl(locationQuery);
//            try {
//                String jsonWeatherResponse = NetworkUtils
//                        .getResponseFromHttpUrl(weatherRequestUrl);
//                return MovieJsonParser.getMovieDataFromJson(jsonWeatherResponse);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe((result) -> {
//                    MainActivity.mLoadingIndicator.setVisibility(View.INVISIBLE);
//                    mAdapter.setMoviesData(result);
//                });
//
//    }


    public void retrofitApiCall(){

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Wrong API key", Toast.LENGTH_LONG).show();
            return;
        }

        mLoadingIndicator.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call;

        if(ApiClient.getSearchQuery(this).equals("popular")){
            call = apiService.getPopularMovies(API_KEY);
        }else{
            call = apiService.getTopRatedMovies(API_KEY);
        }

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse>call, Response<MoviesResponse> response) {
                List<MovieRetrofit> movies = response.body().getResults();
                System.out.println("Movie sice is: " + movies.size());
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mAdapter.setMoviesData(movies);
            }

            @Override
            public void onFailure(Call<MoviesResponse>call, Throwable t) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                Log.e(TAG, t.toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.action_refresh) {
            //need to set adapter for null and call LoaderManager again
            mAdapter.setMoviesData(null);
            retrofitApiCall();
            return true;
        }

        if (id == R.id.action_settings) {
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_favorite) {
            intent = new Intent(this, FavoriteActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * OnStart is called when the Activity is coming into view. This happens when the Activity is
     * first created, but also happens when the Activity is returned to from another Activity. We
     * are going to use the fact that onStart is called when the user returns to this Activity to
     * check if the location setting or the preferred units setting has changed. If it has changed,
     * we are going to perform a new query.
     */
    @Override
    protected void onStart() {
        super.onStart();

        /*
         * If the preferences for location or units have changed since the user was last in
         * MainActivity, perform another query and set the flag to false.
         * This isn't the ideal solution because there really isn't a need to perform another
         * GET request just to change the units, but this is the simplest solution that gets the
         * job done for now.
         * */
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            Log.d("", "onStart: preferences were updated");
            retrofitApiCall();
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /* Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks. */
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    // it's called always when settings are changed
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }

    @Override
    public void onClick(MovieRetrofit movieRetrofit) {
        Context context = this;
        Intent intentToStartDetailActivity = new Intent(context, DetailActivity.class);
        intentToStartDetailActivity.putExtra("movies", movieRetrofit);
        startActivity(intentToStartDetailActivity);
    }


}
