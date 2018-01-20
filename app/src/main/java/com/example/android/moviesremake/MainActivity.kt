package com.example.android.moviesremake

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.android.moviesremake.details.DetailActivity
import com.example.android.moviesremake.favorites.FavoriteActivity
import com.example.android.moviesremake.retrofit.ApiClient
import com.example.android.moviesremake.retrofit.ApiInterface
import com.example.android.moviesremake.retrofit.model.MovieRetrofit
import com.example.android.moviesremake.retrofit.response.MoviesResponse
import com.example.android.moviesremake.settings.SettingsActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MovieAdapter.adapterOnClickHandler, // recycler view click handler
        SharedPreferences.OnSharedPreferenceChangeListener {     // settings change
    private lateinit var mAdapter: MovieAdapter

    // Add a private static boolean flag for preference updates and initialize it to false
    private var PREFERENCES_HAVE_BEEN_UPDATED = false

    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // if there is no connection
        if (!isNetworkAvailable) {
            Toast.makeText(applicationContext, "Connection not available", Toast.LENGTH_SHORT).show()
        }

        val layoutManager = GridLayoutManager(this, 2)
        rv_numbers.layoutManager = layoutManager
        rv_numbers.setHasFixedSize(true)

        // vyplnit recyclerview datami
        mAdapter = MovieAdapter(baseContext, this)
        rv_numbers.adapter = mAdapter

        retrofitApiCall()

        /*
         * Register MainActivity as an OnPreferenceChangedListener to receive a callback when a
         * SharedPreference has changed. Please note that we must unregister MainActivity as an
         * OnSharedPreferenceChanged listener in onDestroy to avoid any memory leaks.
         */
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this)

    }


    fun retrofitApiCall() {

        if (API_KEY.isEmpty()) {
            Toast.makeText(applicationContext, "Wrong API key", Toast.LENGTH_LONG).show()
            return
        }

        pb_loading_indicator.visibility = View.VISIBLE
        val apiService = ApiClient.client!!.create(ApiInterface::class.java)
        val call: Observable<MoviesResponse>

        if (ApiClient.getSearchQuery(this) == "popular") {
            call = apiService.getPopularMovies(API_KEY)
        } else {
            call = apiService.getTopRatedMovies(API_KEY)
        }


        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { weatherData ->
                    val movies = weatherData.results
                    println("Movie size is: " + movies!!.size)
                    pb_loading_indicator.visibility = View.INVISIBLE
                    mAdapter!!.setMoviesData(movies)
                }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.forecast, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val intent: Intent

        if (id == R.id.action_refresh) {
            //need to set adapter for null and call LoaderManager again
            retrofitApiCall()
            return true
        }

        if (id == R.id.action_settings) {
            intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }

        if (id == R.id.action_favorite) {
            intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
            return true
        }


        return super.onOptionsItemSelected(item)
    }

    /**
     * OnStart is called when the Activity is coming into view. This happens when the Activity is
     * first created, but also happens when the Activity is returned to from another Activity. We
     * are going to use the fact that onStart is called when the user returns to this Activity to
     * check if the location setting or the preferred units setting has changed. If it has changed,
     * we are going to perform a new query.
     */
    override fun onStart() {
        super.onStart()

        /*
         * If the preferences for location or units have changed since the user was last in
         * MainActivity, perform another query and set the flag to false.
         * This isn't the ideal solution because there really isn't a need to perform another
         * GET request just to change the units, but this is the simplest solution that gets the
         * job done for now.
         * */
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            Log.d("", "onStart: preferences were updated")
            retrofitApiCall()
            PREFERENCES_HAVE_BEEN_UPDATED = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        /* Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks. */
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this)
    }

    // it's called always when settings are changed
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        PREFERENCES_HAVE_BEEN_UPDATED = true
    }

    override fun onClick(movieRetrofit: MovieRetrofit) {
        val context = this
        val intentToStartDetailActivity = Intent(context, DetailActivity::class.java)
        intentToStartDetailActivity.putExtra("movies", movieRetrofit)
        startActivity(intentToStartDetailActivity)
    }


    companion object {
        val API_KEY = "c88f3eabe09958ae472c9cd7e20b38aa"
    }


}
