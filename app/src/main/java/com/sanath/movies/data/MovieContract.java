package com.sanath.movies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

import com.sanath.movies.models.Movie;

/**
 * Created by sanathnandasiri on 8/4/16.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.sanath.movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class MovieEntry implements BaseColumns {
        //table name
        public static final String TABLE_MOVIES = "movies";

        //column names
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_BACKDROP_PATH = "backdropPath";
        public static final String COLUMN_VOTE_AVERAGE = "voteCount";

        //create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_MOVIES).build();

        //create base type for multiple entries
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + TABLE_MOVIES;

        //create base type for single item
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + TABLE_MOVIES;

        //uri for movie
        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_MOVIES +
                " ( " +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                "UNIQUE ( " + _ID + " ) ON CONFLICT REPLACE" +
                " ) ";

        public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_MOVIES;

        public static ContentValues getContentValues(Movie movie) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(_ID, Integer.valueOf(movie.id));
            contentValues.put(COLUMN_TITLE, movie.title);
            contentValues.put(COLUMN_RELEASE_DATE, movie.releaseDate);
            contentValues.put(COLUMN_OVERVIEW, movie.overview);
            contentValues.put(COLUMN_POSTER_PATH, movie.posterPath);
            contentValues.put(COLUMN_BACKDROP_PATH, movie.backdropPath);
            contentValues.put(COLUMN_VOTE_AVERAGE, movie.voteAverage);
            return contentValues;
        }
    }
}
