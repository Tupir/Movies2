package com.example.android.moviesremake.details;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.moviesremake.R;
import com.example.android.moviesremake.realm.MovieRealm;
import com.example.android.moviesremake.utils.Movie;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static com.example.android.moviesremake.R.id.reviews;
import static com.example.android.moviesremake.R.id.trailers;

public class DetailActivity extends AppCompatActivity implements DetailAdapter.ForecastAdapterOnClickHandler{

    public static final int ID_TRAILER_LOADER = 163;

    public static final int ID_REVIEW_LOADER2 = 161;

    private Movie mForecast;
    private DetailAdapter mAdapter;
    private RecyclerView recycler;
    private Realm realm;


    @BindView(R.id.overview) TextView textOverview;
    @BindView(R.id.vote) TextView textVote;
    @BindView(R.id.release) TextView textRelease;
    @BindView(R.id.imageView1) ImageView imageView;

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

        realm = Realm.getDefaultInstance();

        // nastavenie recyclerview
        recycler = (RecyclerView) findViewById(trailers);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);

        mAdapter = new DetailAdapter(this, this);
        recycler.setAdapter(mAdapter);



        getSupportLoaderManager().initLoader(ID_TRAILER_LOADER, null, new TrailerAndReviewLoader(this, mAdapter, mForecast.getId()));

        // nastavenie recyclerview
        recycler = (RecyclerView) findViewById(reviews);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager2);
        recycler.setHasFixedSize(true);

        mAdapter = new DetailAdapter(this, this);
        recycler.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(ID_REVIEW_LOADER2, null, new TrailerAndReviewLoader(this, mAdapter, mForecast.getId()));
        

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

// maybe code an delete from database after clicking on it again?
    public void insertMovieToDatabase(View view){

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MovieRealm task = realm.where(MovieRealm.class).equalTo("image", mForecast.getImage()).findFirst();
                if(task == null){
                    task = realm.createObject(MovieRealm.class,  UUID.randomUUID().toString());
                    task.setImage(mForecast.getImage());
                }else{
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Already in favorites!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
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
