package com.example.android.moviesremake.retrofit

import com.example.android.moviesremake.retrofit.response.MoviesResponse
import com.example.android.moviesremake.retrofit.response.ReviewResponse
import com.example.android.moviesremake.retrofit.response.TrailerResponse

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by PepovPC on 1/16/2018.
 */

interface ApiInterface {
    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") apiKey: String): Observable<MoviesResponse>

    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String): Observable<MoviesResponse>

    @GET("movie/{id}/reviews")
    fun getMovieReviews(@Path("id") id: Int, @Query("api_key") apiKey: String): Observable<ReviewResponse>

    @GET("movie/{id}/trailers")
    fun getMovieTrailers(@Path("id") id: Int, @Query("api_key") apiKey: String): Observable<TrailerResponse>
}
