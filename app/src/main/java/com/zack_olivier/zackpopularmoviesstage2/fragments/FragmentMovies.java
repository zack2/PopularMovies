package com.zack_olivier.zackpopularmoviesstage2.fragments;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zack_olivier.zackpopularmoviesstage2.R;
import com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility;
import com.zack_olivier.zackpopularmoviesstage2.data.MoviesContract;
import com.zack_olivier.zackpopularmoviesstage2.model.Movie;

import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.COL_OVERVIEW;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.DETAIL_URI;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.FAVORIS_LOADER;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.MOVIE_COLUMNS;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.MOVIE_LOADER_LATEST;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.MOVIE_LOADER_NOW_PLAYING;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.MOVIE_LOADER_POPULAR;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.MOVIE_LOADER_TOP_RATED;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.MOVIE_LOADER_UPCOMING;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMovies extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private String TAG = FragmentMovies.class.getSimpleName();


    private TextView mOverview;

    private Uri mUri;
    private static final String SELECTED_KEY = "selected_position";
    Movie movie;
    String mMovieID;

    public FragmentMovies() {
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SELECTED_KEY, movie);
        outState.putParcelable(DETAIL_URI, mUri);
        Log.e(TAG + " URI", String.valueOf(mUri));
        Log.e(TAG + " overView", String.valueOf(movie.getOverview()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIE_LOADER_POPULAR, null, this);
        getLoaderManager().initLoader(MOVIE_LOADER_TOP_RATED, null, this);
        getLoaderManager().initLoader(FAVORIS_LOADER, null, this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey(SELECTED_KEY)) {
            movie = new Movie();
        } else {
            movie = savedInstanceState.getParcelable(SELECTED_KEY);
            mUri = savedInstanceState.getParcelable(DETAIL_URI);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);
        mOverview = view.findViewById(R.id.text_overview);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DETAIL_URI, getActivity().getIntent().getData());

            mUri = arguments.getParcelable(DETAIL_URI);
            mMovieID = mUri.getLastPathSegment();
            Log.e(TAG + "arguments mMovieID", String.valueOf(mMovieID));

        } else {
            movie = savedInstanceState.getParcelable(SELECTED_KEY);
            mUri = savedInstanceState.getParcelable(DETAIL_URI);
            Log.e(TAG + " URI", String.valueOf(mUri));
            Log.e(TAG + " overView", String.valueOf(movie.getOverview()));
        }


        return view;
    }


    String sortPref;

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Uri uri;
        CursorLoader cursorLoader = null;

        sortPref = Utility.getSortOrder(getActivity());
        if (id == MOVIE_LOADER_POPULAR) {

            Log.e(TAG + " loader popular", String.valueOf(id));

            uri = MoviesContract.MoviesEntry.buildMovieWithMovId(sortPref);
            cursorLoader = new CursorLoader(
                    getActivity(),
                    uri,
                    MOVIE_COLUMNS,
                    MoviesContract.MoviesEntry.COLUMN_SORT_ORDER + "=?",
                    new String[]{sortPref},
                    null);

        } else if (id == MOVIE_LOADER_TOP_RATED) {

            Log.e(TAG + " loader top_rated", String.valueOf(id));

            uri = MoviesContract.MoviesEntry.buildMovieWithMovId(sortPref);
            cursorLoader = new CursorLoader(
                    getActivity(),
                    uri,
                    MOVIE_COLUMNS,
                    MoviesContract.MoviesEntry.COLUMN_SORT_ORDER + "=?",
                    new String[]{sortPref},
                    null);


        } else if (id == MOVIE_LOADER_LATEST) {

            Log.e(TAG + " loader latest", String.valueOf(id));

            uri = MoviesContract.MoviesEntry.buildMovieWithMovId(sortPref);
            cursorLoader = new CursorLoader(
                    getActivity(),
                    uri,
                    MOVIE_COLUMNS,
                    MoviesContract.MoviesEntry.COLUMN_SORT_ORDER + "=?",
                    new String[]{sortPref},
                    null);


        } else if (id == MOVIE_LOADER_NOW_PLAYING) {

            Log.e(TAG + " loader now playing", String.valueOf(id));

            uri = MoviesContract.MoviesEntry.buildMovieWithMovId(sortPref);
            cursorLoader = new CursorLoader(
                    getActivity(),
                    uri,
                    MOVIE_COLUMNS,
                    MoviesContract.MoviesEntry.COLUMN_SORT_ORDER + "=?",
                    new String[]{sortPref},
                    null);


        }else if (id == MOVIE_LOADER_UPCOMING) {

            Log.e(TAG + " loader now playing", String.valueOf(id));

            uri = MoviesContract.MoviesEntry.buildMovieWithMovId(sortPref);
            cursorLoader = new CursorLoader(
                    getActivity(),
                    uri,
                    MOVIE_COLUMNS,
                    MoviesContract.MoviesEntry.COLUMN_SORT_ORDER + "=?",
                    new String[]{sortPref},
                    null);


        } else if (id == FAVORIS_LOADER) {

            Log.e(TAG + "loader favorite", String.valueOf(id));

            uri = MoviesContract.MoviesEntry.buildMovieWithMovId(sortPref);
            cursorLoader = new CursorLoader(
                    getActivity(),
                    uri,
                    MOVIE_COLUMNS,
                    MoviesContract.MoviesEntry.COLUMN_FAVORIS + "=?",
                    new String[]{String.valueOf(1)},
                    null);
        }
        return cursorLoader;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        int loaderID = loader.getId();
        int da = data.getCount();

        Log.e(TAG + " onLoadFinished", String.valueOf(da));
        String overview;

        if (loaderID == MOVIE_LOADER_POPULAR) {

            Log.e(TAG + "onLoadFinished POPU", String.valueOf(loaderID));
            if (!data.moveToFirst()) {
                mOverview.setText("No overView");
            }

            if (data.moveToFirst()) {
                overview = data.getString(COL_OVERVIEW);
                movie.setOverview(overview);
                mOverview.setText(overview);
                Log.e(TAG + " overView", String.valueOf(overview));
            }

        } else if (loaderID == MOVIE_LOADER_TOP_RATED) {

            Log.e(TAG + "onLoadFinished top", String.valueOf(loaderID));
            if (!data.moveToFirst()) {
                mOverview.setText("No overView");
            }

            if (data.moveToFirst()) {
                overview = data.getString(COL_OVERVIEW);
                movie.setOverview(overview);
                mOverview.setText(overview);
                Log.e(TAG + " overView", String.valueOf(overview));
            }

        }else if(loaderID == FAVORIS_LOADER){

            Log.e(TAG + "onLoadFinished favoris", String.valueOf(loaderID));
            if (!data.moveToFirst()) {
                mOverview.setText("No overView");
            }

            if (data.moveToFirst()) {
                overview = data.getString(COL_OVERVIEW);
                movie.setOverview(overview);
                mOverview.setText(overview);
                Log.e(TAG + " overView", String.valueOf(overview));
            }
        }


    }


    @Override
    public void onLoaderReset(Loader loader) {
        loader.startLoading();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(MOVIE_LOADER_POPULAR, null, this);
        getLoaderManager().initLoader(MOVIE_LOADER_TOP_RATED, null, this);
        getLoaderManager().initLoader(FAVORIS_LOADER, null, this);
    }

}
