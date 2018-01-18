package com.example.android.moviesremake.utils;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.android.moviesremake.MainActivity;
import com.example.android.moviesremake.details.DetailAdapter;
import com.example.android.moviesremake.retrofit.ApiClient;
import com.example.android.moviesremake.retrofit.ApiInterface;
import com.example.android.moviesremake.retrofit.model.MovieRetrofitReview;
import com.example.android.moviesremake.retrofit.model.MovieRetrofitTrailer;
import com.example.android.moviesremake.retrofit.response.ReviewResponse;
import com.example.android.moviesremake.retrofit.response.TrailerResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HelperRxClass {

    int id;
    Context context;
    DetailAdapter mAdapter;
    RecyclerView recycler;
    RecyclerView recycler2;
    DetailAdapter.ForecastAdapterOnClickHandler dp;

    public HelperRxClass(int id, Context context, DetailAdapter mAdapter, RecyclerView recycler,
                         RecyclerView recycler2, DetailAdapter.ForecastAdapterOnClickHandler dp){
        this.id = id;
        this.context = context;
        this.mAdapter = mAdapter;
        this.recycler = recycler;
        this.recycler2 = recycler2;
        this.dp = dp;

    }

    public void setReviews(){

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(context);
        recycler.setLayoutManager(layoutManager2);
        recycler.setHasFixedSize(true);

        mAdapter = new DetailAdapter(context, dp);
        recycler.setAdapter(mAdapter);

        if (MainActivity.API_KEY.isEmpty()) {
            Toast.makeText(context, "Wrong API key", Toast.LENGTH_LONG).show();
            return;
        }

        MainActivity.mLoadingIndicator.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Observable<ReviewResponse> call;
        call = apiService.getMovieReviews(id, MainActivity.API_KEY);
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

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(context);
        recycler2.setLayoutManager(layoutManager2);
        recycler2.setHasFixedSize(true);

        mAdapter = new DetailAdapter(context, dp);
        recycler2.setAdapter(mAdapter);

        if (MainActivity.API_KEY.isEmpty()) {
            Toast.makeText(context, "Wrong API key", Toast.LENGTH_LONG).show();
            return;
        }

        MainActivity.mLoadingIndicator.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Observable<TrailerResponse> call;
        call = apiService.getMovieTrailers(id, MainActivity.API_KEY);
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherData -> {
                    List<MovieRetrofitTrailer> movies = weatherData.getResultsForTrailer();
                    System.out.println("Trailer size is: " + movies.size());
                    MainActivity.mLoadingIndicator.setVisibility(View.INVISIBLE);
                    mAdapter.setTrailerData(movies);
                });
    }


}
