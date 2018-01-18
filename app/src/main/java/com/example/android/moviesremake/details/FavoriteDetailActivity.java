package com.example.android.moviesremake.details;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesremake.R;
import com.example.android.moviesremake.realm.MovieRealm;
import com.example.android.moviesremake.utils.HelperRxClass;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static com.example.android.moviesremake.R.id.overview;
import static com.example.android.moviesremake.R.id.reviews;
import static com.example.android.moviesremake.R.id.trailers;

public class FavoriteDetailActivity extends AppCompatActivity implements DetailAdapter.ForecastAdapterOnClickHandler{

    private DetailAdapter mAdapter;
    private RecyclerView recycler;
    private RecyclerView recycler2;
    private int movieId;
    private Realm realm;
    MovieRealm movie;

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


        movieId = getIntent().getIntExtra("id", 0); // ID of movie
        if (movieId == 0) throw new NullPointerException("ID for DetailActivity cannot be null");

        realm = Realm.getDefaultInstance();
        movie = realm.where(MovieRealm.class).equalTo("movieID", movieId).findFirst();

        // set data
        Picasso.with(this)
                .load(movie.getImage())
                .placeholder(R.drawable.no_image)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .error(R.drawable.no_image)
                .into(imageView);

        textOverview.setText(movie.getOverview());
        textRelease.setText(movie.getRelease());
        textVote.setText(movie.getVote()+"/10 ");


        recycler = (RecyclerView) findViewById(reviews);
        recycler2 = (RecyclerView) findViewById(trailers);
        HelperRxClass hp = new HelperRxClass(movieId , this, mAdapter, recycler, recycler2, this);
        hp.setReviews();

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
