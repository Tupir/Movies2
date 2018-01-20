package com.example.android.moviesremake.utils


import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.example.android.moviesremake.MainActivity
import com.example.android.moviesremake.details.DetailAdapter
import com.example.android.moviesremake.retrofit.ApiClient
import com.example.android.moviesremake.retrofit.ApiInterface
import com.example.android.moviesremake.retrofit.response.ReviewResponse
import com.example.android.moviesremake.retrofit.response.TrailerResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HelperRxClass(internal var id: Int, internal var context: Context, internal var mAdapter: DetailAdapter?, internal var recycler: RecyclerView,
                    internal var recycler2: RecyclerView, internal var dp: DetailAdapter.ForecastAdapterOnClickHandler) {

    fun setReviews() {

        val layoutManager2 = LinearLayoutManager(context)
        recycler.layoutManager = layoutManager2
        recycler.setHasFixedSize(true)

        mAdapter = DetailAdapter(context, dp)
        recycler.adapter = mAdapter

        if (MainActivity.API_KEY.isEmpty()) {
            Toast.makeText(context, "Wrong API key", Toast.LENGTH_LONG).show()
            return
        }

        val apiService = ApiClient.client!!.create(ApiInterface::class.java)
        val call: Observable<ReviewResponse>
        call = apiService.getMovieReviews(id, MainActivity.API_KEY)
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { weatherData ->
                    val movies = weatherData.resultsForReview
                    println("Review size is: " + movies!!.size)
                    mAdapter!!.setReviewData(movies)
                    setTrailers()
                }
    }


    fun setTrailers() {

        val layoutManager2 = LinearLayoutManager(context)
        recycler2.layoutManager = layoutManager2
        recycler2.setHasFixedSize(true)

        mAdapter = DetailAdapter(context, dp)
        recycler2.adapter = mAdapter

        val apiService = ApiClient.client!!.create(ApiInterface::class.java)
        val call: Observable<TrailerResponse>
        call = apiService.getMovieTrailers(id, MainActivity.API_KEY)
        call.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { weatherData ->
                    val movies = weatherData.resultsForTrailer
                    println("Trailer size is: " + movies!!.size)
                    mAdapter!!.setTrailerData(movies)
                }
    }


}
