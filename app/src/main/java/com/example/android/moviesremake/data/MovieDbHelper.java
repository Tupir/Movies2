package com.example.android.moviesremake.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.moviesremake.data.MovieTableContents.MovieEntry;

import static com.example.android.moviesremake.data.MovieTableContents.MovieEntry.COLUMN_IMAGE;
import static com.example.android.moviesremake.data.MovieTableContents.MovieEntry.TABLE_NAME;

/**
 * Created by PepovPC on 8/6/2017.
 */



public class MovieDbHelper extends SQLiteOpenHelper{


    private static MovieDbHelper sInstance;
    public static final String DATABASE_NAME = "movies.db";


    //  singleton ensures that only one MovieHelper will exists at any given time
    public static synchronized MovieDbHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new MovieDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private static final int DATABASE_VERSION = 7;     // always increment when changes come

    private MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//  COMPLETED (15) Override onCreate and create the movie table from within it
    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our weather data.
         */
        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + TABLE_NAME + " (" +

                /*
                 * WeatherEntry did not explicitly declare a column called "_ID". However,
                 * WeatherEntry implements the interface, "BaseColumns", which does have a field
                 * named "_ID". We use that here to designate our table's primary key.
                 */
                        MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COLUMN_IMAGE + " VARCHAR (255), " +

                        MovieEntry.COLUMN_TITLE + " VARCHAR (255), " +

                        MovieEntry.COLUMN_OVERVIEW + " VARCHAR (255), " +

                        MovieEntry.COLUMN_VOTE + " REAL," +

                        MovieEntry.COLUMN_ID + " INTEGER," +

                        MovieEntry.COLUMN_RELEASE + " VARCHAR (255)" + ");";

        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }


    private static final String DATABASE_ALTER = "ALTER TABLE "
            + TABLE_NAME + " ADD COLUMN " + COLUMN_IMAGE + " string;";


    /**
     * This database is only a cache for online data, so its upgrade policy is simply to discard
     * the data and call through to onCreate to recreate the table. Note that this only fires if
     * you change the version number for your database (in our case, DATABASE_VERSION). It does NOT
     * depend on the version number for your application found in your app/build.gradle file. If
     * you want to update the schema without wiping data, commenting out the current body of this
     * method should be your top priority before modifying this method.
     *
     * @param sqLiteDatabase Database that is being upgraded
     * @param oldVersion     The old database version
     * @param newVersion     The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//      Within onUpgrade, drop the weather table if it exists
        System.out.println(oldVersion +"\n\n"+newVersion);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(sqLiteDatabase);
    }


}
