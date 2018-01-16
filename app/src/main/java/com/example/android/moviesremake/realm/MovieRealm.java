package com.example.android.moviesremake.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by PepovPC on 1/14/2018.
 */

public class MovieRealm extends RealmObject {



    @PrimaryKey
    private String id;

    private String image;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
