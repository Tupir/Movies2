package com.example.android.moviesremake.retrofit.response

import com.example.android.moviesremake.retrofit.model.MovieRetrofitReview
import com.google.gson.annotations.SerializedName

/**
 * Created by PepovPC on 1/17/2018.
 */

class ReviewResponse {


    @SerializedName("results")
    val resultsForReview: List<MovieRetrofitReview>? = null


}