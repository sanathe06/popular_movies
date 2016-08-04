package com.sanath.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sanathnandasiri on 8/4/16.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {
    public static final String TAG = MoviesDbHelper.class.getSimpleName();

    public static final int VERSION = 1;
    public static final String NAME = "movies.db";

    public MoviesDbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MovieContract.MovieEntry.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        sqLiteDatabase.execSQL(MovieContract.MovieEntry.TABLE_DROP);
        onCreate(sqLiteDatabase);
    }
}
