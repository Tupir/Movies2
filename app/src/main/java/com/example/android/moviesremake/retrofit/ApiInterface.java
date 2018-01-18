package com.example.android.moviesremake.retrofit;

import com.example.android.moviesremake.retrofit.response.MoviesResponse;
import com.example.android.moviesremake.retrofit.response.ReviewResponse;
import com.example.android.moviesremake.retrofit.response.TrailerResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by PepovPC on 1/16/2018.
 */

public interface ApiInterface {
    @GET("movie/top_rated")
    Observable<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Observable<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Observable<ReviewResponse> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/trailers")
    Observable<TrailerResponse> getMovieTrailers(@Path("id") int id, @Query("api_key") String apiKey);
}
