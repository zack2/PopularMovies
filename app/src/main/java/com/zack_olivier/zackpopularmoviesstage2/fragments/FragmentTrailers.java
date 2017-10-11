package com.zack_olivier.zackpopularmoviesstage2.fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zack_olivier.zackpopularmoviesstage2.R;
import com.zack_olivier.zackpopularmoviesstage2.adapters.TrailerObjectAdapter;
import com.zack_olivier.zackpopularmoviesstage2.data.MoviesContract;

import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.DETAIL_URI;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTrailers extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private String TAG = FragmentTrailers.class.getSimpleName();
    private Uri mUri;
    private String mSite;

    private static final String[] TRAILER_COLUMNS = {
            MoviesContract.MoviesEntry._ID,
            MoviesContract.MoviesEntry.COLUMN_MOV_ID,
            MoviesContract.TrailerEntry.COLUMN_YOUTUBE_ID,
            MoviesContract.TrailerEntry.COLUMN_ISO_639_1,
            MoviesContract.TrailerEntry.COLUMN_ISO_3166_1,
            MoviesContract.TrailerEntry.COLUMN_KEY,
            MoviesContract.TrailerEntry.COLUMN_NAME,
            MoviesContract.TrailerEntry.COLUMN_SITE,
            MoviesContract.TrailerEntry.COLUMN_SIZE,
            MoviesContract.TrailerEntry.COLUMN_TYPE
    };
    private static final int COL_KEY = 5;
    public static final int COL_NAME = 6;


    private static final int DETAIL_LOADER_TRAILERS = 2;
    private static final String SAVE_KEY = "FragmentTrailers";

    private String mMovieID;
    TrailerObjectAdapter trailersAdapter;

    String youtubeUrl = "http://www.youtube.com/watch?v=";
    String completeYoutubeUrl;

    public FragmentTrailers() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVE_KEY, getActivity().getIntent().getData());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mUri = savedInstanceState.getParcelable(SAVE_KEY);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DETAIL_LOADER_TRAILERS, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment3, container, false);

        Bundle arguments = new Bundle();
        arguments.putParcelable(DETAIL_URI, getActivity().getIntent().getData());
        if (arguments != null) {
            mUri = arguments.getParcelable(DETAIL_URI);
            mMovieID = mUri.getLastPathSegment();
        }

        ListView mTrailersList = view.findViewById(R.id.movies_trailer_container);
        trailersAdapter = new TrailerObjectAdapter(getActivity().getApplicationContext(), null, 0);
        mTrailersList.setAdapter(trailersAdapter);


        mTrailersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                completeYoutubeUrl = youtubeUrl + cursor.getString(COL_KEY);

                if (cursor != null) {
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + cursor.getString(COL_KEY)));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(completeYoutubeUrl));

                    Log.e(TAG + " setOnItemClickListener",  completeYoutubeUrl);

                    try {
                        startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        startActivity(webIntent);
                    }
                }

            }
        });

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "Ok URI is " + mUri);

        switch (id) {
            case DETAIL_LOADER_TRAILERS:
                Log.v(TAG, "In onCreateLoader case DETAIL_LOADER_TRAILERS");
                return new CursorLoader(
                        getActivity(),
                        MoviesContract.TrailerEntry.CONTENT_URI,
                        TRAILER_COLUMNS,
                        MoviesContract.TrailerEntry.COLUMN_MOVIE_ID + "=" + mMovieID,
                        null,
                        null
                );
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        TextView textViewNoTrailer =  getView().findViewById(R.id.text_error_no_trailer);
        if (!data.moveToFirst()) {
            textViewNoTrailer.setVisibility(View.VISIBLE);
            textViewNoTrailer.setText(getString(R.string.no_trailers));
        }

        if (data != null && data.getCount() > 0) {
            for (int i = 0; i < data.getColumnNames().length; i++) {
                Log.e(TAG, "Trailer Cursor column names: " + data.getString(i));
            }
            textViewNoTrailer.setVisibility(View.GONE);
            trailersAdapter.swapCursor(data);

            Log.e(TAG, "Trailer Cursor count: " + data.getCount());
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case DETAIL_LOADER_TRAILERS:
                trailersAdapter.swapCursor(null);
                break;
        }
    }


   /* @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
    }*/


}
