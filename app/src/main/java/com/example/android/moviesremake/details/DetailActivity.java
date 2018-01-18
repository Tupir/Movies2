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

import com.example.android.moviesremake.MainActivity;
import com.example.android.moviesremake.R;
import com.example.android.moviesremake.realm.MovieRealm;
import com.example.android.moviesremake.retrofit.ApiClient;
import com.example.android.moviesremake.retrofit.ApiInterface;
import com.example.android.moviesremake.retrofit.MovieRetrofit;
import com.example.android.moviesremake.retrofit.MovieRetrofitReview;
import com.example.android.moviesremake.retrofit.MovieRetrofitTrailer;
import com.example.android.moviesremake.retrofit.ReviewResponse;
import com.example.android.moviesremake.retrofit.TrailerResponse;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

import static com.example.android.moviesremake.R.id.reviews;
import static com.example.android.moviesremake.R.id.trailers;

public class DetailActivity extends AppCompatActivity implements DetailAdapter.ForecastAdapterOnClickHandler{

    public static final int ID_TRAILER_LOADER = 163;

    public static final int ID_REVIEW_LOADER2 = 161;

    private MovieRetrofit movie;
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

        setReviews();

    }

    public void setReviews(){

        recycler = (RecyclerView) findViewById(reviews);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager2);
        recycler.setHasFixedSize(true);

        mAdapter = new DetailAdapter(this, this);
        recycler.setAdapter(mAdapter);

        if (MainActivity.API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Wrong API key", Toast.LENGTH_LONG).show();
            return;
        }

        MainActivity.mLoadingIndicator.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Observable<ReviewResponse> call;
        call = apiService.getMovieReviews(movie.getId(), MainActivity.API_KEY);
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherData -> {
                    List<MovieRetrofitReview> movies = weatherData.getResultsForReview();
                    System.out.println("Review size is: " + movies.size());
                    MainActivity.mLoadingIndicator.setVisibility(View.INVISIBLE);
                    mAdapter.setReviewData(movies);
                    setTrailers();
                });
    }

    public void setTrailers(){

        recycler = (RecyclerView) findViewById(trailers);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager2);
        recycler.setHasFixedSize(true);

        mAdapter = new DetailAdapter(this, this);
        recycler.setAdapter(mAdapter);

        if (MainActivity.API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Wrong API key", Toast.LENGTH_LONG).show();
            return;
        }

        MainActivity.mLoadingIndicator.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Observable<TrailerResponse> call;
        call = apiService.getMovieTrailers(movie.getId(), MainActivity.API_KEY);
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherData -> {
                    List<MovieRetrofitTrailer> movies = weatherData.getResultsForTrailer();
                    System.out.println("Trailer size is: " + movies.size());
                    MainActivity.mLoadingIndicator.setVisibility(View.INVISIBLE);
                    mAdapter.setTrailerData(movies);
                });
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
