package com.zack_olivier.zackpopularmoviesstage2.appUtils;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.zack_olivier.zackpopularmoviesstage2.R;
import com.zack_olivier.zackpopularmoviesstage2.data.MoviesContract;

import static android.content.Context.MODE_PRIVATE;

/***
 *
 * @param **/

public class Utility {

    public static final String BASE_IMG_URL = "http://image.tmdb.org/t/p/";
    public static final String STATE_MOVIES = "state_movies";
    public static String BASE_MOVIES_URL = "http://api.themoviedb.org/3/discover/movie";
    public static final String IMG_SIZE = "w500";

    public final static String API_KEY_WORD = "api_key";
    public final static String SORT_ORDER = "sort_by";
    //the API key provided by the movieDB.org
    //TODO replace your API Key here
   public static final String THE_MOVIE_DB_API_KEY = "d42bdd7d55b0315acc1b88203fdff03b";
    //  public static final String THE_MOVIE_DB_API_KEY = "";
    // this is about ratingbar
    public static final int DEFAULT_NUM_STARS = 5;
    public static final float MAX_API_RATING = 10;

    //About review
    public static final String RESULTS = "results";
    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";

    //About Trailers Youtube
    public static final String YOUTUBE_URL = "http://www.youtube.com";
    public static final String YOUTUBE_PATH = "watch";
    public static final String KEY = "key";
    public static final String NAME = "name";

    public static final String YOUTUBE_API_KEY = "YOUR API KEY";

    public static final String DETAIL_URI = "URI";
    public static final String PARCELABLE_KEY = "DetailActivity";

    public static final int COL_ID = 0;
    public static final int COL_MOV_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_RELEASE = 3;
    public static final int COL_POPULARITY = 4;
    public static final int COL_RATING = 5;
    public static final int COL_OVERVIEW = 6;
    public static final int COL_POSTER = 7;
    public static final int COL_BACKDROP_PATH = 8;
    public static final int COL_FAVORIS = 9;
    public static final int COL_SORT_ORDER = 10;


    public static final int MOVIE_LOADER_POPULAR = 0;
    public static final int MOVIE_LOADER_TOP_RATED = 1;
    public static final int FAVORIS_LOADER = 2;
    public static final int MOVIE_LOADER_LATEST = 3;
    public static final int MOVIE_LOADER_NOW_PLAYING= 4;
    public static final int MOVIE_LOADER_UPCOMING = 5;

    public static final String[] MOVIE_COLUMNS = {
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry._ID,
            MoviesContract.MoviesEntry.COLUMN_MOV_ID,
            MoviesContract.MoviesEntry.COLUMN_TITLE,
            MoviesContract.MoviesEntry.COLUMN_RELEASE,
            MoviesContract.MoviesEntry.COLUMN_POPULARITY,
            MoviesContract.MoviesEntry.COLUMN_RATING,
            MoviesContract.MoviesEntry.COLUMN_OVERVIEW,
            MoviesContract.MoviesEntry.COLUMN_POSTER,
            MoviesContract.MoviesEntry.COLUMN_BACKDROP_POSTER,
            MoviesContract.MoviesEntry.COLUMN_FAVORIS,
            MoviesContract.MoviesEntry.COLUMN_SORT_ORDER

    };



    public static String getSortOrder(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_default));
    }

    public  static void setPreferFavoris(Activity activity, Boolean value){
        SharedPreferences.Editor editor = activity.
                getSharedPreferences("com.zack_olivier.zackpopularmoviesstage2.activities.DetailActivity", MODE_PRIVATE).edit();
        editor.putBoolean("Favorite Added", value);
        editor.apply();
    }


    public static boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }


}
