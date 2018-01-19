package com.example.android.moviesremake.retrofit.model

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

/**
* Created by PepovPC on 1/16/2018.
*/

class MovieRetrofit : Parcelable {

    @SerializedName("poster_path")
    private var image: String? = null

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("original_title")
    var title: String? = null

    @SerializedName("overview")
    var overview: String? = null

    @SerializedName("vote_average")
    var vote: String? = null

    @SerializedName("release_date")
    var release: String? = null


    fun getImage(): String {
        return "http://image.tmdb.org/t/p/w185/" + image!!
    }

    fun setImage(image: String) {
        this.image = "http://image.tmdb.org/t/p/w185/" + image
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(image)
        dest.writeInt(id)
        dest.writeString(title)
        dest.writeString(overview)
        dest.writeString(vote)
        dest.writeString(release)
    }

    constructor(parcel: Parcel) {
        image = parcel.readString()
        id = parcel.readInt()
        title = parcel.readString()
        overview = parcel.readString()
        vote = parcel.readString()
        release = parcel.readString()
    }

    companion object CREATOR : Parcelable.Creator<MovieRetrofit> {

        override fun createFromParcel(parcel: Parcel): MovieRetrofit {
            return MovieRetrofit(parcel)
        }

        override fun newArray(size: Int): Array<MovieRetrofit?> {
            return arrayOfNulls(size)
        }
    }
}
