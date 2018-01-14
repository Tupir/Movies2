package com.example.android.moviesremake.favorites;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.example.android.moviesremake.R;
import com.example.android.moviesremake.data.MovieDbHelper;
import com.example.android.moviesremake.data.MovieTableContents;
import com.example.android.moviesremake.details.FavoriteDetailActivity;

public class FavoriteActivity extends AppCompatActivity implements
        FavoriteAdapter.ForecastAdapterOnClickHandler,    // recycler view click handler
        LoaderManager.LoaderCallbacks<Cursor>{    // AsyncTask



    // Create a String array containing the names of the desired data columns from our ContentProvider
    /*
     * The columns of data that we are interested in displaying within our MainActivity's list of
     * weather data.
     */
    public static final String[] MAIN_FORECAST_PROJECTION = {
            MovieTableContents.MovieEntry.COLUMN_IMAGE,
            MovieTableContents.MovieEntry.COLUMN_ID,
    };

    //  COMPLETED (17) Create constant int values representing each column name's position above
    /*
     * We store the indices of the values in the array of Strings above to more quickly be able to
     * access the data from our query. If the order of the Strings above changes, these indices
     * must be adjusted to match the order of the Strings.
     */
    public static final int INDEX_MOVIE_IMAGE = 0;
    public static final int INDEX_MOVIE_ID = 1;




    private FavoriteAdapter mAdapter;
    private RecyclerView recycler;
    private int mPosition = RecyclerView.NO_POSITION;
    public static ProgressBar mLoadingIndicator;
    public static final int FORECAST_LOADER_ID = 22;    // special ID for LoaderManager
    private SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My favorites");

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);  // loader

        //HelperClass.insertFakeData(this);
        MovieDbHelper dbHelper = MovieDbHelper.getInstance(this);
        mDb = dbHelper.getWritableDatabase();
        getAllFavoriteMovies();

        // nastavenie recyclerview
        recycler = (RecyclerView) findViewById(R.id.rv_numbers);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);

        // vyplnit recyclerview datami
        mAdapter = new FavoriteAdapter(this, this);
        recycler.setAdapter(mAdapter);


        /**
         * Nasledujuce riadky nahradzuju tento riadok:  new FetchMovieTask().execute();
         */
        int loaderId = FORECAST_LOADER_ID;

        getSupportLoaderManager().initLoader(loaderId, null,this);

    }



    private void getAllFavoriteMovies() {
        mDb.query(
                MovieTableContents.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }



    /**
     * Override metoda z rozhrania, ktore sa nachadza v Adapteri
     * Urcuje, co sa ma stat po stlaceni na konkretny view
     */
    @Override
    public void onClick(String image) {
        Context context = this;
        Intent intentToStartDetailActivity = new Intent(context, FavoriteDetailActivity.class);

        Uri forMovieClicked = MovieTableContents.MovieEntry.buildOneMovieUri(image);
        intentToStartDetailActivity.setData(forMovieClicked);
        intentToStartDetailActivity.putExtra("id", image);
        startActivity(intentToStartDetailActivity);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {

//          COMPLETED (22) If the loader requested is our forecast loader, return the appropriate CursorLoader
            case FavoriteActivity.FORECAST_LOADER_ID:
                /* URI for all rows of weather data in our weather table */
                Uri forecastQueryUri = MovieTableContents.MovieEntry.CONTENT_URI;
                /* Sort order: Ascending by date */
//                String sortOrder = MovieTableContents.MovieEntry.COLUMN_DATE + " ASC";



                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_FORECAST_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }


    }

    /**
     * Called when a previously created loader has finished its load.
     * The same as onPostExecute in AsyncLoad
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter.setCursor(data);
//      COMPLETED (29) If mPosition equals RecyclerView.NO_POSITION, set it to 0
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
//      COMPLETED (30) Smooth scroll the RecyclerView to mPosition
        recycler.smoothScrollToPosition(mPosition);

//      COMPLETED (31) If the Cursor's size is not equal to 0, call showWeatherDataView
        if (data.getCount() != 0) ;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.favorite, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        if (id == R.id.action_refresh) {
            //need to set adapter for null and call LoaderManager again
            mAdapter.setCursor(null);
            getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
