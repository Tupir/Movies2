package com.example.android.moviesremake.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by PepovPC on 8/6/2017.
 */

public class MovieTableContents {

    /*
 * The "Content authority" is a name for the entire content provider, similar to the
 * relationship between a domain name and its website. A convenient string to use for the
 * content authority is the package name for the app, which is guaranteed to be unique on the
 * Play Store.
 */
    public static final String CONTENT_AUTHORITY = "com.example.android.moviesremake";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider for Sunshine.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {


        /* The base CONTENT_URI used to query the Movie table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();


        /* Used internally as the name of our movies table. */
        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_IMAGE = "image";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_VOTE = "vote";

        public static final String COLUMN_RELEASE = "release";


        public static Uri buildOneMovieUri(String image) {
            return CONTENT_URI.buildUpon()
                    .appendPath(image)
                    .build();
        }
        


    }



}
