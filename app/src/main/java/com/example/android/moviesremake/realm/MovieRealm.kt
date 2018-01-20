package com.example.android.moviesremake.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by PepovPC on 1/14/2018.
 */

open class MovieRealm : RealmObject() {


    @PrimaryKey
    var id: String? = null

    var image: String? = null
    var movieID: Int = 0
    var release: String? = null
    var overview: String? = null
    var vote: String? = null
}
