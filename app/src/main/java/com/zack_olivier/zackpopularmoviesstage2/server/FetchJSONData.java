package com.zack_olivier.zackpopularmoviesstage2.server;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.zack_olivier.zackpopularmoviesstage2.data.MoviesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by pc on 28/02/2017.
 */

public class FetchJSONData {

    final static String LOG_TAG = FetchJSONData.class.getSimpleName();

    public static void getMovieDataFromJson(Context mContext, String moviesJsonStr, String sortOrder) throws JSONException {
        final String RESULTS = "results";
        final String POSTER_PATH = "poster_path";
        final String BACKDROP_PATH = "backdrop_path";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String ORIGINAL_TITLE = "original_title";
        final String ID = "id";
        final String VOTE_AVERAGE = "vote_average";
        final String POPULARITY = "popularity";


        try {
            JSONObject jsonObject = new JSONObject(moviesJsonStr);
            JSONArray movieArray = jsonObject.getJSONArray(RESULTS);
            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());

            for (int i = 0; i < movieArray.length(); i++) {

                String posterPath;
                String backDropPath;
                String overview;
                String releaseDate;
                String originalTitle;
                String movieID;
                String voteAverage;
                String popularity;


                JSONObject result = movieArray.getJSONObject(i);

                posterPath = result.getString(POSTER_PATH);
                backDropPath = result.getString(BACKDROP_PATH);
                overview = result.getString(OVERVIEW);
                releaseDate = result.getString(RELEASE_DATE);
                originalTitle = result.getString(ORIGINAL_TITLE);
                movieID = result.getString(ID);
                voteAverage = result.getString(VOTE_AVERAGE);
                popularity = result.getString(POPULARITY);


                ContentValues movieValues = new ContentValues();

                movieValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER, posterPath);
                movieValues.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP_POSTER, backDropPath);
                movieValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, overview);
                movieValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE, releaseDate);
                movieValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE, originalTitle);
                movieValues.put(MoviesContract.MoviesEntry.COLUMN_MOV_ID, movieID);
                movieValues.put(MoviesContract.MoviesEntry.COLUMN_RATING, voteAverage);
                movieValues.put(MoviesContract.MoviesEntry.COLUMN_POPULARITY, popularity);


                if (sortOrder.contains("popular")) {
                    Log.e(LOG_TAG, "sort_order " + sortOrder);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_SORT_ORDER, sortOrder);
                }

                if (sortOrder.contains("top_rated")) {
                    Log.e(LOG_TAG, "sort_order " + sortOrder);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_SORT_ORDER, sortOrder);
                }
                if (sortOrder.contains("latest")) {
                    Log.e(LOG_TAG, "latest " + sortOrder);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_SORT_ORDER, sortOrder);
                }
                if (sortOrder.contains("now_playing")) {
                    Log.e(LOG_TAG, "now_playing " + sortOrder);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_SORT_ORDER, sortOrder);
                }
                if (sortOrder.contains("upcoming")) {
                    Log.e(LOG_TAG, "upcoming " + sortOrder);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_SORT_ORDER, sortOrder);
                }

                cVVector.add(movieValues);
            }


            int inserted = 0;
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, cvArray);
            }

            Log.d(LOG_TAG, "FetchMovieTask Complete. " + inserted + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }


    public static void getReviewDataFromJson(Context mContext, String moviesJsonStr) throws JSONException {

        final String RESULTS = "results";
        final String COMMENT_ID = "id";
        final String MOVIE_ID = "id";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String URL = "url";

        try {
            JSONObject jsonObject = new JSONObject(moviesJsonStr);
            String idMovie = jsonObject.getString(MOVIE_ID);
            JSONArray movieArray = jsonObject.getJSONArray(RESULTS);

            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());

            for (int i = 0; i < movieArray.length(); i++) {

                String author;
                String content;
                String commentID;
                String url;

                JSONObject result = movieArray.getJSONObject(i);

                author = result.getString(AUTHOR);
                content = result.getString(CONTENT);
                commentID = result.getString(COMMENT_ID);
                url = result.getString(URL);

                ContentValues movieValues = new ContentValues();

                movieValues.put(MoviesContract.ReviewEntry.COLUMN_MOVIE_ID, idMovie);
                movieValues.put(MoviesContract.ReviewEntry.COLUMN_COMMENT_ID, commentID);
                movieValues.put(MoviesContract.ReviewEntry.COLUMN_AUTHOR, author);
                movieValues.put(MoviesContract.ReviewEntry.COLUMN_CONTENT, content);
                movieValues.put(MoviesContract.ReviewEntry.COLUMN_URL, url);

                Log.e(LOG_TAG + " id: ", idMovie);
                Log.e(LOG_TAG + " commentId: ", commentID);
                Log.e(LOG_TAG + " Auteur: ", author);
                Log.e(LOG_TAG + " Content: ", content);
                Log.e(LOG_TAG + " URL: ", url);

                cVVector.add(movieValues);

            }

            int inserted = 0;


            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);

                inserted = mContext.getContentResolver().bulkInsert(MoviesContract.ReviewEntry.CONTENT_URI, cvArray);
            }

            Log.e(LOG_TAG, "FetchReviewTask Complete. " + inserted + " Inserted");
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }


    public static void getTrailerDataFromJson(Context mContext, String moviesJsonStr) throws JSONException {

        final String MOVIE_ID = "id";
        final String YOUTUBE_ID = "id";
        final String ISO_639_1 = "iso_639_1";
        final String ISO_3166_1 = "iso_3166_1";
        final String SITE = "site";
        final String SIZE = "size";
        final String KEY = "key";
        final String NAME = "name";
        final String TYPE = "type";
        final String RESULT = "results";

        try {
            JSONObject jsonObject = new JSONObject(moviesJsonStr);
            String id = jsonObject.getString(MOVIE_ID);
            JSONArray resultArray = jsonObject.getJSONArray(RESULT);

            Vector<ContentValues> cVVector = new Vector<ContentValues>(resultArray.length());

            for (int i = 0; i < resultArray.length(); i++) {
                String idYoutube;
                String iso_639_1;
                String iso_3166_1;
                String key;
                String name;
                String site;
                String size;
                String type;

                JSONObject result = resultArray.getJSONObject(i);

                idYoutube = result.getString(YOUTUBE_ID);
                iso_639_1 = result.getString(ISO_639_1);
                iso_3166_1 = result.getString(ISO_3166_1);
                key = result.getString(KEY);
                name = result.getString(NAME);
                site = result.getString(SITE);
                size = result.getString(SIZE);
                type = result.getString(TYPE);

                ContentValues trailerValues = new ContentValues();

                trailerValues.put(MoviesContract.TrailerEntry.COLUMN_YOUTUBE_ID, idYoutube);
                trailerValues.put(MoviesContract.TrailerEntry.COLUMN_ISO_639_1, iso_639_1);
                trailerValues.put(MoviesContract.TrailerEntry.COLUMN_ISO_3166_1, iso_3166_1);
                trailerValues.put(MoviesContract.TrailerEntry.COLUMN_KEY, key);
                trailerValues.put(MoviesContract.TrailerEntry.COLUMN_NAME, name);
                trailerValues.put(MoviesContract.TrailerEntry.COLUMN_SITE, site);
                trailerValues.put(MoviesContract.TrailerEntry.COLUMN_SIZE, size);
                trailerValues.put(MoviesContract.TrailerEntry.COLUMN_TYPE, type);
                trailerValues.put(MoviesContract.TrailerEntry.COLUMN_MOVIE_ID, id);


                cVVector.add(trailerValues);
            }

            int inserted = 0;

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(MoviesContract.TrailerEntry.CONTENT_URI, cvArray);
            }

            Log.e(LOG_TAG, "FetchTrailerTask Complete. " + inserted + " Inserted");
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }


}
