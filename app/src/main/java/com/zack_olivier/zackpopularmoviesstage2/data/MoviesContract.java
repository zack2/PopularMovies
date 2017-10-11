package com.zack_olivier.zackpopularmoviesstage2.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by pc on 07/02/2017.
 */

public class MoviesContract {


    public static final String CONTENT_AUTHORITY = "com.zack_olivier.zackpopularmoviesstage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";
    public static final String PATH_FAVORITES = "favorite";
    public static final String PATH_REVIEWS = "review";
    public static final String PATH_TRAILERS = "trailer";

    // Les colonne de base de données  24/02/2017


 public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        // Table name
        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOV_ID = "mov_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE = "release";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_BACKDROP_POSTER = "backdrop_path";
        public static final String COLUMN_FAVORIS = "favoris";
        public static final String COLUMN_SORT_ORDER = "sort_order";




        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieWithMovId(String mdbId) {
            return CONTENT_URI.buildUpon().appendPath(mdbId).build();
        }

        public static String getMovIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }


    //add Favorite table with two columns and update MovieDBHelper
    public static final class ReviewEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        public static final String TABLE_NAME = "review";

        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_COMMENT_ID = "comment_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";


        // For inserting a movie in to the favorite table
        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getReviewIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }



    //add Trailers table with three columns and update MovieDBHelper
    public static final class TrailerEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        public static final String TABLE_NAME = "trailer";

        public static final String COLUMN_MOVIE_ID = "mov_id";
        public static final String COLUMN_YOUTUBE_ID = "id";
        public static final String COLUMN_ISO_639_1 = "iso_639_1";
        public static final String COLUMN_ISO_3166_1 = "iso_3166_1";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";



        // For inserting a movie in to the favorite table
        public static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildTrailerWithMovId(String mdbId) {
            return CONTENT_URI.buildUpon().appendPath(mdbId).build();
        }

        public static String getTrailerIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
