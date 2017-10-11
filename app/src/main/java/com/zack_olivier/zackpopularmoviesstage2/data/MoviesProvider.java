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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by pc on 07/02/2017.
 */

public class MoviesProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mOpenHelper;
    private final String LOG_TAG = MoviesProvider.class.getSimpleName();

    static final int MOVIES = 100;
    static final int MOVIES_WITH_ID = 101;
    static final int MOVIES_SORT_ORDER = 102;

    static final int REVIEW = 300;
    static final int REVIEW_DETAIL = 301;
    static final int TRAILER = 400;
    static final int TRAILER_WITH_ID = 401;


    private static final String sMoviesIDSelection =
            MoviesContract.MoviesEntry.TABLE_NAME +
                    "." + MoviesContract.MoviesEntry.COLUMN_MOV_ID + " = ? ";


    private static final String sReviewIDSelection =
            MoviesContract.ReviewEntry.TABLE_NAME +
                    "." + MoviesContract.ReviewEntry.COLUMN_MOVIE_ID + " = ? ";

    private static final String sTrailerIDSelection =
            MoviesContract.TrailerEntry.TABLE_NAME +
                    "." + MoviesContract.TrailerEntry.COLUMN_MOVIE_ID + " = ? ";




    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MoviesContract.PATH_MOVIES, MOVIES);

        matcher.addURI(authority, MoviesContract.PATH_REVIEWS, REVIEW);
        matcher.addURI(authority, MoviesContract.PATH_TRAILERS, TRAILER);

        matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/*", MOVIES_SORT_ORDER);


        matcher.addURI(authority, MoviesContract.PATH_TRAILERS + "/*", TRAILER_WITH_ID);
        matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);
        matcher.addURI(authority, MoviesContract.PATH_REVIEWS + "/*", REVIEW_DETAIL);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return MoviesContract.MoviesEntry.CONTENT_TYPE;
            case MOVIES_WITH_ID:
                return MoviesContract.MoviesEntry.CONTENT_ITEM_TYPE;

            case MOVIES_SORT_ORDER:
                return MoviesContract.MoviesEntry.CONTENT_ITEM_TYPE;

            case REVIEW:
                return MoviesContract.ReviewEntry.CONTENT_TYPE;
            case REVIEW_DETAIL:
                return MoviesContract.ReviewEntry.CONTENT_ITEM_TYPE;

            case TRAILER:
                return MoviesContract.TrailerEntry.CONTENT_TYPE;

            case TRAILER_WITH_ID:
                return MoviesContract.TrailerEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        Log.d(LOG_TAG, "URI is " + uri);

        switch (sUriMatcher.match(uri)) {


            // "movies"
            case MOVIES_SORT_ORDER: {
               // String sortOrder1 = MoviesContract.MoviesEntry.COLUMN_SORT_ORDER;

//                if(selection == null){
//                  selection = MoviesContract.MoviesEntry.TABLE_NAME +
//                          "." + MoviesContract.MoviesEntry.COLUMN_SORT_ORDER + "=?";
//                }

                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            // "movies"
            case MOVIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "movies/*"
            case MOVIES_WITH_ID: {
                String mov_id = MoviesContract.MoviesEntry.getMovIdFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        sMoviesIDSelection,
                        new String[]{mov_id},
                        null,
                        null,
                        sortOrder
                );
                break;
            }


            case REVIEW: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;
            }

            case REVIEW_DETAIL: {
                String mov_id = MoviesContract.ReviewEntry.getReviewIdFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.ReviewEntry.TABLE_NAME,
                        projection,
                        sReviewIDSelection,
                        new String[]{mov_id},
                        null,
                        null,
                        null
                );
                break;
            }

            case TRAILER: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.TrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;
            }

            case TRAILER_WITH_ID: {
                String mov_id = MoviesContract.TrailerEntry.getTrailerIdFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.TrailerEntry.TABLE_NAME,
                        projection,
                        sTrailerIDSelection,
                        new String[]{mov_id},
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        Log.d(LOG_TAG, String.valueOf(match));

        switch (match) {
            case MOVIES: {
                long _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesContract.MoviesEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case REVIEW: {
                long _id = db.insert(MoviesContract.ReviewEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesContract.ReviewEntry.buildReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW_DETAIL: {
                long _id = db.insert(MoviesContract.ReviewEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesContract.ReviewEntry.buildReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILER: {
                long _id = db.insert(MoviesContract.TrailerEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesContract.TrailerEntry.buildTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case MOVIES:
                rowsDeleted = db.delete(
                        MoviesContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case REVIEW:
                rowsDeleted = db.delete(
                        MoviesContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case TRAILER:
                rowsDeleted = db.delete(
                        MoviesContract.TrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case REVIEW_DETAIL:
                rowsDeleted = db.delete(
                        MoviesContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        Log.d(LOG_TAG, "delete is " + rowsDeleted);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        if(selection == null){
            selection = MoviesContract.MoviesEntry.COLUMN_MOV_ID + "=?";
        }

        if(selectionArgs == null){
            String movieID = uri.getLastPathSegment();
            selectionArgs = new String[]{movieID};
        }

        switch (match) {
            case MOVIES:
                rowsUpdated = db.update(MoviesContract.MoviesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            case MOVIES_SORT_ORDER:
                rowsUpdated = db.update(MoviesContract.MoviesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            case MOVIES_WITH_ID:
                rowsUpdated = db.update(MoviesContract.MoviesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            case REVIEW:
                rowsUpdated = db.update(MoviesContract.ReviewEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case TRAILER:
                rowsUpdated = db.update(MoviesContract.TrailerEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Log.e(LOG_TAG, "Matched int is " + match);

        //delete(uri, null, null);
        switch (match) {
            case MOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = 0;
                        try {
                           _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, value);
                        }catch (Exception ex){
                            Log.d(LOG_TAG, ex.toString());
                        }

                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case REVIEW_DETAIL:
                Log.d(LOG_TAG, "Review Bulk insert");
                db.beginTransaction();
                returnCount = 0;

                    for (ContentValues value : values) {

                        long _id =0;

                        try {
                            _id = db.insert(MoviesContract.ReviewEntry.TABLE_NAME, null, value);
                        }catch (Exception ex){
                             ex.printStackTrace();
                            Log.e(LOG_TAG+ "echec  REVIEWDETAIL", ex.toString());
                        }

                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                    db.endTransaction();


                if (returnCount > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return returnCount;

            case REVIEW:
                Log.d(LOG_TAG, "Review Bulk insert");
                db.beginTransaction();
                returnCount = 0;

                    for (ContentValues value : values) {

                        long _id =0;

                        try {
                            _id = db.insert(MoviesContract.ReviewEntry.TABLE_NAME, null, value);
                        }catch (Exception ex){
                             ex.printStackTrace();
                            Log.e(LOG_TAG+ "echec insertion review ", ex.toString());
                        }

                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();

                    db.endTransaction();


                if (returnCount > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return returnCount;

            case TRAILER:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(MoviesContract.TrailerEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (returnCount > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return returnCount;


            case TRAILER_WITH_ID:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(MoviesContract.TrailerEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (returnCount > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return returnCount;


            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }


}