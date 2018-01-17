package com.example.android.moviesremake.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PepovPC on 1/16/2018.
 */

public class MovieRetrofitReview {

    @SerializedName("author")
    private String autorName;

//    @SerializedName("content")
//    private List<String> autorComment = new ArrayList<>();


    public MovieRetrofitReview(String autorName){
        this.autorName = autorName;
        //this.autorComment = autorComment;
    }


    public String getAutorName() {
        return autorName;
    }

    public void setAutorName(String autorName) {
        this.autorName = autorName;
    }


//    public List<String> getAutorComment() {
//        return autorComment;
//    }
//
//    public void setAutorComment(List<String> autorComment) {
//        this.autorComment = autorComment;
//    }


}
