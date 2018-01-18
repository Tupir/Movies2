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
import android.widget.Toast;

import com.example.android.moviesremake.R;
import com.example.android.moviesremake.realm.MovieRealm;
import com.example.android.moviesremake.retrofit.model.MovieRetrofit;
import com.example.android.moviesremake.utils.HelperRxClass;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static com.example.android.moviesremake.R.id.reviews;
import static com.example.android.moviesremake.R.id.trailers;

public class DetailActivity extends AppCompatActivity implements DetailAdapter.ForecastAdapterOnClickHandler{

    private MovieRetrofit movie;
    private DetailAdapter mAdapter;
    private RecyclerView recycler;
    private RecyclerView recycler2;
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
        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movies")) {
                movie = intentThatStartedThisActivity.getParcelableExtra("movies");
                setTitle(movie.getTitle());
                Picasso.with(this).load(movie.getImage()).into(imageView);
                textRelease.setText(movie.getRelease());
                textVote.setText(movie.getVote()+"/10".toString());
                textOverview.setText(movie.getOverview());
            }
        }

        realm = Realm.getDefaultInstance();

        //http://api.themoviedb.org/3/movie/346364/reviews?api_key=c88f3eabe09958ae472c9cd7e20b38aa


        recycler = (RecyclerView) findViewById(reviews);
        recycler2 = (RecyclerView) findViewById(trailers);
        HelperRxClass hp = new HelperRxClass(movie.getId(), this, mAdapter, recycler, recycler2, this);
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

// maybe code an delete from database after clicking on it again?
    public void insertMovieToDatabase(View view){

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MovieRealm task = realm.where(MovieRealm.class).equalTo("image", movie.getImage()).findFirst();
                if(task == null){
                    task = realm.createObject(MovieRealm.class,  UUID.randomUUID().toString());
                    task.setImage(movie.getImage());
                    task.setMovieID(movie.getId());
                    task.setOverview(movie.getOverview());
                    task.setRelease(movie.getRelease());
                    task.setVote(movie.getVote());
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
