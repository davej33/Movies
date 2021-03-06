package com.example.android.movieapp2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by dnj on 6/19/17.
 */

public final class MovieContentProvider extends ContentProvider {

    // vars
    private static final int MOVIE_TABLE_CODE = 100;
    private static final int MOVIE_ITEM_CODE = 101;
    private static UriMatcher sUriMatcher = getUriMatcher();
    private MovieDbHelper mDbHelper;
    private static final String LOG_TAG = MovieContentProvider.class.getSimpleName();


    public static UriMatcher getUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.MOVIE_PATH, MOVIE_TABLE_CODE);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.MOVIE_PATH + "/#", MOVIE_ITEM_CODE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int rowsInserted = 0;

        switch (sUriMatcher.match(uri)) {
            case MOVIE_TABLE_CODE:
                db.beginTransaction();
                long insertCheck;
                try {
                    for (ContentValues v : values) {
                        insertCheck = db.insert(MovieContract.MovieEntry.MOVIE_TABLE, null, v);
                        if (insertCheck != -1) rowsInserted++;
                    }
                    db.setTransactionSuccessful();
                    Log.i(LOG_TAG, "rows inserted %%%%%%% " + rowsInserted);
                } catch (SQLException e) {
                    Log.e(LOG_TAG, "Bulk insert error: " + e);
                } finally {
                    db.endTransaction();
                }
                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = null;

        switch (sUriMatcher.match(uri)) {
            case MOVIE_TABLE_CODE:
                cursor = db.query(MovieContract.MovieEntry.MOVIE_TABLE, null, null, null, null, null, null);
                break;
            case MOVIE_ITEM_CODE:
                selection = MovieContract.MovieEntry.MOVIE_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(MovieContract.MovieEntry.MOVIE_TABLE, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted = 0;

        switch (sUriMatcher.match(uri)) {
            case MOVIE_TABLE_CODE:
                rowsDeleted = db.delete(MovieContract.MovieEntry.MOVIE_TABLE, selection, selectionArgs);
                break;
            case MOVIE_ITEM_CODE:
                selection = MovieContract.MovieEntry.MOVIE_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(MovieContract.MovieEntry.MOVIE_TABLE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Delete not supported: ");
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowUpdated = 0;
        Log.i(LOG_TAG, "URI: " + uri);

        try {
            switch (sUriMatcher.match(uri)) {
                case MOVIE_ITEM_CODE:
                    String id = uri.getPathSegments().get(1);
                    Log.i(LOG_TAG, "ID: " + id);
                    rowUpdated = db.update(MovieContract.MovieEntry.MOVIE_TABLE, values, "_id=?", new String[]{id});
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Uri: " + uri);
            }
        } catch (Exception e) {
            Log.i(LOG_TAG, "Favorites update error: " + e);
        }

        return rowUpdated;
    }


}
