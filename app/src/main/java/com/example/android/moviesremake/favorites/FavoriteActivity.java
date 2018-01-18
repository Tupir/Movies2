package com.example.android.moviesremake.favorites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.example.android.moviesremake.R;
import com.example.android.moviesremake.details.FavoriteDetailActivity;
import com.example.android.moviesremake.realm.MovieRealm;

import io.realm.Realm;
import io.realm.RealmResults;

public class FavoriteActivity extends AppCompatActivity implements
        FavoriteRealmAdapter.ForecastAdapterOnClickHandler {


    private RecyclerView recycler;
    public static ProgressBar mLoadingIndicator;

    private Realm realm;
    FavoriteRealmAdapter adapter;
    RealmResults<MovieRealm> tasks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My favorites");

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);  // loader


        realm = Realm.getDefaultInstance();
        tasks = realm.where(MovieRealm.class).findAll();
        adapter = new FavoriteRealmAdapter(tasks, true, this, this);


        // nastavenie recyclerview
        recycler = (RecyclerView) findViewById(R.id.rv_numbers);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);

        recycler.setAdapter(adapter);

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
            adapter.setMoviesData(tasks);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(MovieRealm movieRealm) {
        Context context = this;
        Intent intentToStartDetailActivity = new Intent(context, FavoriteDetailActivity.class);
        int x = movieRealm.getMovieID();
        intentToStartDetailActivity.putExtra("id", x);

        startActivity(intentToStartDetailActivity);
    }
}
