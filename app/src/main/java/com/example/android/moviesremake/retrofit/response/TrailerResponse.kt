package com.example.android.moviesremake.retrofit.response

import com.example.android.moviesremake.retrofit.model.MovieRetrofitTrailer
import com.google.gson.annotations.SerializedName

/**
 * Created by PepovPC on 1/17/2018.
 */

class TrailerResponse {


    @SerializedName("youtube")
    val resultsForTrailer: List<MovieRetrofitTrailer>? = null

}