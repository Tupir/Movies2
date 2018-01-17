package com.example.android.moviesremake.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by PepovPC on 1/16/2018.
 */

public class MoviesResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<MovieRetrofit> results;

//    @SerializedName("results")
//    private List<MovieRetrofitReview> resultsReview;

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieRetrofit> getResults() {
        return results;
    }

//    public List<MovieRetrofitReview> getResultsForReview() {
//        return resultsReview;
//    }

    public void setResults(List<MovieRetrofit> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }



    public class Obycaj{


            @SerializedName("results")
    private List<MovieRetrofitReview> resultsReview;

        public List<MovieRetrofitReview> getResultsForReview() {
            return resultsReview;
        }


    }


}
