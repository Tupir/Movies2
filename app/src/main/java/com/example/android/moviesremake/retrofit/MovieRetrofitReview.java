package com.example.android.moviesremake.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PepovPC on 1/16/2018.
 */

public class MovieRetrofitReview {

    @SerializedName("author")
    private List<String> autorName = new ArrayList<>();

    @SerializedName("content")
    private List<String> autorComment = new ArrayList<>();


    public MovieRetrofitReview(List<String> autorName, List<String> autorComment){
        this.autorName = autorName;
        this.autorComment = autorComment;
    }


    public List<String> getAutorName() {
        return autorName;
    }

    public void setAutorName(List<String> autorName) {
        this.autorName = autorName;
    }


    public List<String> getAutorComment() {
        return autorComment;
    }

    public void setAutorComment(List<String> autorComment) {
        this.autorComment = autorComment;
    }



}
