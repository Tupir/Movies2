package com.example.android.moviesremake.retrofit.model

import com.google.gson.annotations.SerializedName

/**
* Created by PepovPC on 1/16/2018.
*/

class MovieRetrofitReview(
        @field:SerializedName("author") var autorName: String?,
        @field:SerializedName("content") private var autorComment: String?) {

    fun getAutorComment(): String? {
        return autorComment
    }

    fun setAutorComment(autorName: String) {
        this.autorComment = autorComment
    }


}
