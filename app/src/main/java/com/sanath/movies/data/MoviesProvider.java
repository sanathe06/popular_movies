package com.sanath.movies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MoviesProvider extends ContentProvider {
    private static final String TAG = MoviesProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private MoviesDbHelper mMoviesDbHelper;

    public static final int MOVIE = 100;
    public static final int MOVIES = 200;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI(authority, MovieContract.MovieEntry.TABLE_MOVIES, MOVIES);
        matcher.addURI(authority, MovieContract.MovieEntry.TABLE_MOVIES + "/#", MOVIE);

        return matcher;
    }

    public MoviesProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numOfDeleted;
        switch (match) {
            case MOVIES:
                numOfDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_MOVIES, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.MovieEntry.TABLE_MOVIES + "'");
                break;
            case MOVIE:
                numOfDeleted = db.delete(MovieContract.MovieEntry.TABLE_MOVIES,
                        MovieContract.MovieEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.MovieEntry.TABLE_MOVIES + "'");

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numOfDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE: {
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            }
            case MOVIES: {
                return MovieContract.MovieEntry.CONTENT_DIR_TYPE;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri : " + uri);
            }
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase database = mMoviesDbHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {

            case MOVIES: {
                long id = database.insert(MovieContract.MovieEntry.TABLE_MOVIES, null, values);
                if (id > 0) {
                    returnUri = MovieContract.MovieEntry.buildMovieUri(id);
                } else {
                    throw new SQLException("Failed to insert row into : " + uri);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri : " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        mMoviesDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIES: {
                cursor = mMoviesDbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_MOVIES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                return cursor;
            }
            case MOVIE: {
                cursor = mMoviesDbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_MOVIES,
                        projection,
                        MovieContract.MovieEntry._ID + " =?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                return cursor;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri : " + uri);
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase database = mMoviesDbHelper.getWritableDatabase();
        int numberOfUpdated;
        if (values == null) {
            throw new IllegalArgumentException("Can not have content values");
        }

        switch (sUriMatcher.match(uri)) {
            case MOVIES: {
                numberOfUpdated = database.update(MovieContract.MovieEntry.TABLE_MOVIES,
                        values,
                        MovieContract.MovieEntry._ID + "=?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))}
                );
                break;
            }
            case MOVIE: {
                numberOfUpdated = database.update(MovieContract.MovieEntry.TABLE_MOVIES,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        if (numberOfUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numberOfUpdated;
    }
}
