package com.example.android.moviesremake.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by PepovPC on 1/17/2018.
 */

public class Pokus{


    @SerializedName("results")
    private List<MovieRetrofitReview> resultsReview;

    public List<MovieRetrofitReview> getResultsForReview() {
        return resultsReview;
    }


}