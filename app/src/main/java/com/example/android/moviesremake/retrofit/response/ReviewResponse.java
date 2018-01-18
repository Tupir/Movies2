package com.example.android.moviesremake.retrofit.response;

import com.example.android.moviesremake.retrofit.model.MovieRetrofitReview;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by PepovPC on 1/17/2018.
 */

public class ReviewResponse {


    @SerializedName("results")
    private List<MovieRetrofitReview> resultsReview;

    public List<MovieRetrofitReview> getResultsForReview() {
        return resultsReview;
    }


}