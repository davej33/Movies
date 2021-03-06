package com.example.android.movieapp2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.movieapp2.data.MovieContract.MovieEntry;

/**
 * Created by dnj on 6/19/17.
 */

public final class MovieDbHelper extends SQLiteOpenHelper {

    // vars
    public static final String MOVIE_DB_NAME = "movies.db";
    public static final int DB_VERSION = 1;


    public MovieDbHelper(Context context) {
        super(context, MOVIE_DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_DB = "CREATE TABLE " + MovieEntry.MOVIE_TABLE + " (" +
                MovieEntry.MOVIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.MOVIE_TMDB_ID + " INTEGER UNIQUE NOT NULL, " +
                MovieEntry.MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieEntry.MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.MOVIE_PLOT + " TEXT NOT NULL, " +
                MovieEntry.MOVIE_POPULARITY + " FLOAT NOT NULL, " +
                MovieEntry.MOVIE_RATING + " FLOAT NOT NULL, " +
                MovieEntry.MOVIE_FAVORITE + " INTEGER DEFAULT 0, " +
                MovieEntry.MOVIE_POSTER + " BLOB);";

        db.execSQL(CREATE_DB);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.MOVIE_TABLE);
        onCreate(db);
    }
}
