package com.example.android.moviesremake.retrofit.response

import com.example.android.moviesremake.retrofit.model.MovieRetrofit
import com.google.gson.annotations.SerializedName

/**
 * Created by PepovPC on 1/16/2018.
 */

class MoviesResponse {

    @SerializedName("page")
    var page: Int = 0

    @SerializedName("results")
    var results: List<MovieRetrofit>? = null


    @SerializedName("total_results")
    var totalResults: Int = 0

    @SerializedName("total_pages")
    var totalPages: Int = 0


}
