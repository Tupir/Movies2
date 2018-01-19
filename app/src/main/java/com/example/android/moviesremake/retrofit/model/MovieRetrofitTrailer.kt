package com.example.android.moviesremake.retrofit.model

import com.google.gson.annotations.SerializedName

/**
 * Created by PepovPC on 1/16/2018.
 */

class MovieRetrofitTrailer(
        @field:SerializedName("name") var trailerName: String?,
        @field:SerializedName("source") var youtubeSource: String?)
