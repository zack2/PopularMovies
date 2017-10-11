package com.zack_olivier.zackpopularmoviesstage2.activities;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Network;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.zack_olivier.zackpopularmoviesstage2.R;
import com.zack_olivier.zackpopularmoviesstage2.api.CustomStringRequest;
import com.zack_olivier.zackpopularmoviesstage2.api.VolleyController;
import com.zack_olivier.zackpopularmoviesstage2.appUtils.ConnectivityReceiver;
import com.zack_olivier.zackpopularmoviesstage2.appUtils.DownloadGlideImage;
import com.zack_olivier.zackpopularmoviesstage2.appUtils.PopularmoviesApplication;
import com.zack_olivier.zackpopularmoviesstage2.appUtils.SessionManager;
import com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility;
import com.zack_olivier.zackpopularmoviesstage2.data.MoviesContract;
import com.zack_olivier.zackpopularmoviesstage2.fragments.FragmentMovies;
import com.zack_olivier.zackpopularmoviesstage2.fragments.FragmentReviews;
import com.zack_olivier.zackpopularmoviesstage2.fragments.FragmentTrailers;
import com.zack_olivier.zackpopularmoviesstage2.model.Movie;
import com.zack_olivier.zackpopularmoviesstage2.server.FetchJSONData;
import com.zack_olivier.zackpopularmoviesstage2.viewpager.ViewPagerAdapter;

import org.json.JSONException;

import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.COL_BACKDROP_PATH;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.COL_FAVORIS;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.COL_MOV_ID;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.COL_OVERVIEW;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.COL_POPULARITY;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.COL_POSTER;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.COL_RATING;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.COL_RELEASE;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.COL_TITLE;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.DEFAULT_NUM_STARS;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.DETAIL_URI;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.MAX_API_RATING;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.MOVIE_COLUMNS;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.PARCELABLE_KEY;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.THE_MOVIE_DB_API_KEY;


public class DetailActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    String TAG = DetailActivity.class.getSimpleName();

    private Uri mUri;
    private String mMovieID;

    BottomNavigationView mBottomNav;
    BottomBar mBottombar;
    ViewPager viewPager;
    MenuItem prevMenuItem;
    FragmentMovies fragmentMovies;
    FragmentReviews fragmentReviews;
    FragmentTrailers fragmentTrailers;

    Bundle args;

    private String mSortPref;

    ImageView imageViewBackdrop;

    SimpleRatingBar mSimpleRatingBar;


    private static String originalTitle;
    private static String movieID;
    Movie movie;
    MaterialFavoriteButton toolbarFavorite;
    SessionManager sessionManager;
    Toolbar toolbar;
    String sortPref;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().getTitle();
        toolbar.setTitle(originalTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(this);

        if (savedInstanceState == null || !savedInstanceState.containsKey(DETAIL_URI)) {
            movie = new Movie();
            restoreActivityItem();
        } else {
            movie = savedInstanceState.getParcelable(PARCELABLE_KEY);
            mUri = savedInstanceState.getParcelable(DETAIL_URI);
            mSortPref = savedInstanceState.getString("SORT");
            restoreActivityItem();
        }
    }

    private void restoreActivityItem() {
        mSimpleRatingBar = findViewById(R.id.rating_bar);
        KenBurnsView mImagePoster = findViewById(R.id.img_etabl);
        imageViewBackdrop = findViewById(R.id.poster_image);
        TextView mTitleMovie = findViewById(R.id.title_movie);
        TextView mPopularity = findViewById(R.id.title_popularity);
        TextView mReleaseDate = findViewById(R.id.release_date);

        toolbarFavorite = new MaterialFavoriteButton.Builder(this) //
                .favorite(false)
                .color(android.R.color.holo_red_light)
                .type(MaterialFavoriteButton.STYLE_HEART)
                .rotationDuration(400)
                .create();
        toolbar.addView(toolbarFavorite);

        toolbarFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean favorite = toolbarFavorite.isFavorite();
                toggleFavorite(favorite);
            }
        });


        TextView mRatingUser = findViewById(R.id.rating_user);

        args = new Bundle();
        args.putParcelable(DETAIL_URI, getIntent().getData());

        if (args != null) {
            mUri = args.getParcelable(DETAIL_URI);
            mMovieID = mUri.getLastPathSegment();
            Log.e(TAG + "mMovieID", mMovieID);

            updateMovieReviewAndTrailer();
        }

        initView();

        Log.e(TAG, "" + movieID);

        mUri = MoviesContract.MoviesEntry.CONTENT_URI;

        Cursor movieCursor = getApplicationContext().getContentResolver().query(
                mUri,
                MOVIE_COLUMNS,
                MoviesContract.MoviesEntry.COLUMN_MOV_ID + "=" + mMovieID,
                null,
                null
        );

        sortPref = Utility.getSortOrder(this);


        if (movieCursor.moveToFirst()) {

            Log.e(TAG + " sortPref movieCursor", movieCursor.getString(COL_TITLE));

            String posterPath = movieCursor.getString(COL_POSTER);
            String backdropPath = movieCursor.getString(COL_BACKDROP_PATH);
            originalTitle = movieCursor.getString(COL_TITLE);
            String releaseDate = movieCursor.getString(COL_RELEASE);
            String overview = movieCursor.getString(COL_OVERVIEW);
            movieID = movieCursor.getString(COL_MOV_ID);
            String voteAverage = movieCursor.getString(COL_RATING);
            String popularity = movieCursor.getString(COL_POPULARITY);
            int favoris = movieCursor.getInt(COL_FAVORIS);

            Log.e(TAG + " sortPref movieCursor", String.valueOf(favoris));
            Log.e(TAG + " sortPref overview", String.valueOf(overview));

            movie.setId(movieID);
            movie.setBackdropPath(backdropPath);
            movie.setTitle(originalTitle);
            movie.setReleaseDate(releaseDate);
            movie.setOverview(overview);
            movie.setVote(voteAverage);
            movie.setPopularity(popularity);
            movie.setFavoris(favoris);


            ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setTitle(originalTitle);

            String imgUrl = Utility.BASE_IMG_URL + Utility.IMG_SIZE + posterPath;
            String imgUrlBackdrop = Utility.BASE_IMG_URL + Utility.IMG_SIZE + backdropPath;
            DownloadGlideImage.downloadImage(this, imgUrlBackdrop, mImagePoster);
            DownloadGlideImage.downloadImage(this, imgUrl, imageViewBackdrop);

            mTitleMovie.setText(originalTitle);
            mReleaseDate.setText(getString(R.string.string_release_date) + " " + releaseDate);
            mPopularity.setText(getString(R.string.title_popularity) + " " + popularity);
            mRatingUser.setText(String.format("%s/10", voteAverage));

            mSimpleRatingBar.setFocusable(false);
            mSimpleRatingBar.setNumberOfStars(DEFAULT_NUM_STARS);
            float rate = Float.parseFloat(voteAverage);
            float mStarLight = ((rate * DEFAULT_NUM_STARS) / MAX_API_RATING);
            mSimpleRatingBar.setRating(mStarLight);
        }


        if (movie.getFavoris() == 1) {

            Log.e(TAG + "movie is favoris", String.valueOf(movie.getFavoris()));
            toolbarFavorite.setFavorite(true);
        } else {
            Log.e(TAG + "movie isn't favoris", String.valueOf(movie.getFavoris()));
            toolbarFavorite.setFavorite(false);
        }


    }


    /***
     * this methode add movie to favorite */
    private void toggleFavorite(boolean favorite) {


        Log.e(TAG + " enter " + favorite, "toggleFavorite");
        Uri uriFavorite = MoviesContract.MoviesEntry.buildMovieUri(Long.parseLong(movieID));

        ContentValues cv2 = new ContentValues();
        int valueUpdate;

        if(!favorite){

            // cv2.put(MoviesContract.MoviesEntry.COLUMN_FAVORIS,  (favorite ? 1 : 0));
            cv2.put(MoviesContract.MoviesEntry.COLUMN_FAVORIS,  1);

            valueUpdate =  getApplicationContext().getContentResolver().update(
                    uriFavorite,
                    cv2,
                    null,
                    null

            );

            Log.e(TAG, "id movie " + movieID);
            Log.e(TAG, "favoris movie " + favorite);

            if(valueUpdate > 0){

                Log.e(TAG, "UPDATE SUCCESS");

                toolbarFavorite.setFavorite(true, true);
                Toast.makeText(getApplicationContext(), R.string.favoris_message, Toast.LENGTH_SHORT).show();
            }else {
                Log.e(TAG, "UPDATE Failed");

            }

        }else {

            cv2.put(MoviesContract.MoviesEntry.COLUMN_FAVORIS,  0);

            valueUpdate =  getApplicationContext().getContentResolver().update(
                    uriFavorite,
                    cv2,
                    null,
                    null

            );

            if(valueUpdate == 0){

                Log.e(TAG, "UPDATE NOT FAVORIS SUCCESS");

                toolbarFavorite.setFavorite(false, true);
                Toast.makeText(getApplicationContext(), R.string.no_favoris, Toast.LENGTH_SHORT).show();
            }else {
                Log.e(TAG, "UPDATE NOT FAVORIS Failed");

            }

        }




    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("SORT", mSortPref);
        outState.putParcelable(PARCELABLE_KEY, movie);
        outState.putParcelable(DETAIL_URI, mUri);

        Log.e(TAG, "onSaveInstanceState");

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movie = savedInstanceState.getParcelable(PARCELABLE_KEY);
        mUri = savedInstanceState.getParcelable(DETAIL_URI);
        mSortPref = savedInstanceState.getString("SORT");

        Log.e(TAG, String.valueOf(movie));
        Log.e(TAG, String.valueOf(mUri));
        Log.e(TAG, " onRestoreInstanceState");

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "DETAIL ACTIVITY RESUMED!");
        String sortPref = Utility.getSortOrder(this);
        Log.d(TAG, "Sort Order is " + sortPref);
        Log.d(TAG, "Sort Order is " + mSortPref);

        if (sortPref != null && !sortPref.equals(mSortPref)) {
            Log.d(TAG, "SORTING CHANGED!!");
            mSortPref = sortPref;
            initView();
        }
    }


    private void initView(){

        viewPager = findViewById(R.id.viewpager);
       // mBottomNav = findViewById(R.id.navigation);
        mBottombar = findViewById(R.id.bottomBar);

        mBottombar.setOnTabSelectListener(new OnTabSelectListener() {


            @Override
            public void onTabSelected(@IdRes int tabId) {

                Log.e(TAG +"onTabSelected", String.valueOf(tabId));

                if (tabId == R.id.tab_home) {
                    viewPager.setCurrentItem(0);

                }
                if (tabId == R.id.tab_review) {
                    viewPager.setCurrentItem(1);

                }
                if (tabId == R.id.tab_trailer) {
                    viewPager.setCurrentItem(2);

                }
            }
        });
       /* mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int tabId = item.getItemId();
                if (tabId == R.id.tab_home) {
                    viewPager.setCurrentItem(0);

                }
                if (tabId == R.id.tab_review) {
                    viewPager.setCurrentItem(1);

                }
                if (tabId == R.id.tab_trailer) {
                    viewPager.setCurrentItem(2);

                }
                return true;
            }

        });*/


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               /* if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    mBottomNav.getMenu().getItem(0).setChecked(false);
                }

                mBottomNav.getMenu().getItem(position).setChecked(true);
                prevMenuItem = mBottomNav.getMenu().getItem(position);

            */
               mBottombar.getTabAtPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);

    }

    // intial viewpager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentMovies = new FragmentMovies();
        fragmentReviews = new FragmentReviews();
        fragmentTrailers = new FragmentTrailers();
        adapter.addFragment(fragmentMovies);
        adapter.addFragment(fragmentReviews);
        adapter.addFragment(fragmentTrailers);
        viewPager.setAdapter(adapter);
    }

    //start the AsyncTack of Movies
    private void updateMovieReviewAndTrailer() {

        final String BASE_URL_REVIEWS = "http://api.themoviedb.org/3/movie";
        final String API_KEY = "api_key";
        final String REVIEWS = "reviews";
        String lang = "en-US";
        final String LANG_PARAM = "language";
        final String TRAILERS = "videos";

        Uri builtUri = Uri.parse(BASE_URL_REVIEWS).buildUpon()
                .appendPath(mMovieID)
                .appendPath(REVIEWS)
                .appendQueryParameter(API_KEY, THE_MOVIE_DB_API_KEY)
                .appendQueryParameter(LANG_PARAM, lang)
                .build();

        //Instantiate Request..

        String url = builtUri.toString();

        CustomStringRequest request = new CustomStringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    try {
                        FetchJSONData.getReviewDataFromJson(getApplication(), response);
                    } catch (JSONException e) {
                        Toast.makeText(getApplication().getApplicationContext(), R.string.volley_message,
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplication().getApplicationContext(), R.string.volley_message2
                            , Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplication(), error.toString(),Toast.LENGTH_LONG).show();
                Log.e(TAG, error.toString());

                if (error instanceof Network) {
                    showSnack();
                } else {
                    int color = Color.RED;
                    snackbarMessage(getString(R.string.error), color);
                }
            }
        });

        VolleyController.getInstance(getApplication().getApplicationContext()).addToRequestQueue(request);

        //Go for the trailers...
        Uri builtUri1 = Uri.parse(BASE_URL_REVIEWS).buildUpon()
                .appendPath(mMovieID)
                .appendPath(TRAILERS)
                .appendQueryParameter(API_KEY, THE_MOVIE_DB_API_KEY)
                .appendQueryParameter(LANG_PARAM, lang)
                .build();

        String url1 = builtUri1.toString();

        CustomStringRequest request1 = new CustomStringRequest(url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    try {
                        FetchJSONData.getTrailerDataFromJson(getApplication().getApplicationContext(), response);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), R.string.volley_message,
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplication().getApplicationContext(), R.string.volley_message2
                            , Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());


            }
        });

        VolleyController.getInstance(getApplication().getApplicationContext()).addToRequestQueue(request1);


    }

    private boolean checkConnection() {
        return ConnectivityReceiver.isConnected();
    }

    private void showSnack() {
        String message;
        int color;
        if (checkConnection()) {
            message = getString(R.string.internet_available);
            color = Color.WHITE;
        } else {
            message = getString(R.string.string_no_connection);
            color = Color.RED;
        }
        snackbarMessage(message, color);

    }

    private void snackbarMessage(String message, int color) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.navigation), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }


    @Override
    protected void onStart() {
        //initial loader
        PopularmoviesApplication.getInstance().setConnectivityListener(this);
//        getLoaderManager().getLoader(MOVIE_LOADER_POPULAR);
//        getLoaderManager().getLoader(MOVIE_LOADER_TOP_RATED);
//        getLoaderManager().getLoader(FAVORIS_LOADER);
        initView();
        super.onStart();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack();
    }
}
