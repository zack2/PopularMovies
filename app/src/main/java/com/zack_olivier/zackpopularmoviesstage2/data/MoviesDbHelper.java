/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zack_olivier.zackpopularmoviesstage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by pc on 07/02/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 9;
    private final String LOG_TAG = MoviesDbHelper.class.getSimpleName();

    static final String DATABASE_NAME = "popularmovies.db";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY," +
                MoviesContract.MoviesEntry.COLUMN_MOV_ID + " INTEGER UNIQUE NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_RATING + " REAL NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_BACKDROP_POSTER + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_RELEASE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_FAVORIS + " INTEGER DEFAULT 0, " +
                MoviesContract.MoviesEntry.COLUMN_SORT_ORDER + " TEXT NOT NULL, " +
                " UNIQUE (" + MoviesContract.MoviesEntry.COLUMN_MOV_ID + ") ON CONFLICT IGNORE);";



        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + MoviesContract.ReviewEntry.TABLE_NAME + " (" +
                MoviesContract.ReviewEntry._ID + " TEXT PRIMARY KEY, " +
                MoviesContract.ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MoviesContract.ReviewEntry.COLUMN_AUTHOR + " TEXT , " +
                MoviesContract.ReviewEntry.COLUMN_CONTENT + " TEXT , " +
                MoviesContract.ReviewEntry.COLUMN_URL + " TEXT , " +
                MoviesContract.ReviewEntry.COLUMN_COMMENT_ID + " TEXT,  " +
                " UNIQUE (" + MoviesContract.ReviewEntry.COLUMN_MOVIE_ID + ") ON CONFLICT IGNORE);";


        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + MoviesContract.TrailerEntry.TABLE_NAME + " (" +
                MoviesContract.TrailerEntry._ID + " TEXT PRIMARY KEY, " +
                MoviesContract.TrailerEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MoviesContract.TrailerEntry.COLUMN_YOUTUBE_ID + " TEXT  , " +
                MoviesContract.TrailerEntry.COLUMN_ISO_639_1 + " TEXT , " +
                MoviesContract.TrailerEntry.COLUMN_ISO_3166_1 + " TEXT , " +
                MoviesContract.TrailerEntry.COLUMN_SITE + " TEXT , " +
                MoviesContract.TrailerEntry.COLUMN_SIZE + " TEXT , " +
                MoviesContract.TrailerEntry.COLUMN_KEY + " TEXT , " +
                MoviesContract.TrailerEntry.COLUMN_NAME + " TEXT , " +
                MoviesContract.TrailerEntry.COLUMN_TYPE + " TEXT ,  " +
                " UNIQUE (" + MoviesContract.TrailerEntry.COLUMN_MOVIE_ID + ") ON CONFLICT IGNORE);";


        Log.d(LOG_TAG, SQL_CREATE_MOVIES_TABLE);

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.ReviewEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.TrailerEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
