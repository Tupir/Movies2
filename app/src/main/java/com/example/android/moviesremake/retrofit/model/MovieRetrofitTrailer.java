package com.example.android.moviesremake.retrofit.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PepovPC on 1/16/2018.
 */

public class MovieRetrofitTrailer {

    @SerializedName("name")
    private String trailerName;

    @SerializedName("source")
    private String youtubeSource;


    public MovieRetrofitTrailer(String trailerName, String youtubeSource){
        this.trailerName = trailerName;
        this.youtubeSource = youtubeSource;
    }


    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public String getYoutubeSource() {
        return youtubeSource;
    }

    public void setYoutubeSource(String youtubeSource) {
        this.youtubeSource = youtubeSource;
    }


}
