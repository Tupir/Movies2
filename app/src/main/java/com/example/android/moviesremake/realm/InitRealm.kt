package com.example.android.moviesremake.realm

import android.app.Application

import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by PepovPC on 1/14/2018.
 */

class InitRealm : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
                .name("tasky.realm")
                .schemaVersion(0)
                .build()
        Realm.setDefaultConfiguration(realmConfig)
    }

}