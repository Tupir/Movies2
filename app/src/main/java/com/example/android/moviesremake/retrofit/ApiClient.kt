package com.example.android.moviesremake.retrofit

import android.content.Context
import android.support.v7.preference.PreferenceManager
import com.example.android.moviesremake.R
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by PepovPC on 1/16/2018.
 */

object ApiClient {


    val BASE_URL = "http://api.themoviedb.org/3/"
    private var retrofit: Retrofit? = null


    val client: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit
        }


    fun getSearchQuery(context: Context): String {
        val prefs = PreferenceManager
                .getDefaultSharedPreferences(context)
        val keyForUnits = context.getString(R.string.pref_units_key)
        val defaultUnits = context.getString(R.string.pref_units_metric)
        val preferredUnits = prefs.getString(keyForUnits, defaultUnits)
        val metric = context.getString(R.string.pref_units_metric)
        val imperial = context.getString(R.string.pref_units_imperial)
        val userPrefersMetric: String
        if (metric == preferredUnits) {
            userPrefersMetric = defaultUnits
        } else {
            userPrefersMetric = imperial
        }
        return userPrefersMetric
    }


}
