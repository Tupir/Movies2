package com.example.android.moviesremake.retrofit.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PepovPC on 1/16/2018.
 */

public class MovieRetrofitReview {

    @SerializedName("author")
    private String autorName;

    @SerializedName("content")
    private String autorComment;


    public MovieRetrofitReview(String autorName, String autorComment){
        this.autorName = autorName;
        this.autorComment = autorComment;
    }


    public String getAutorName() {
        return autorName;
    }

    public void setAutorName(String autorName) {
        this.autorName = autorName;
    }

    public String getAutorComment() {
        return autorComment;
    }

    public void setAutorComment(String autorName) {
        this.autorComment = autorComment;
    }


}
