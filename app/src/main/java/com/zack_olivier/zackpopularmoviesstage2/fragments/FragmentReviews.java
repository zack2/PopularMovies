package com.zack_olivier.zackpopularmoviesstage2.fragments;


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
import android.widget.ListView;
import android.widget.TextView;

import com.zack_olivier.zackpopularmoviesstage2.R;
import com.zack_olivier.zackpopularmoviesstage2.adapters.ReviewObjectAdapter;
import com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility;
import com.zack_olivier.zackpopularmoviesstage2.data.MoviesContract;

import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.DETAIL_URI;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentReviews extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private String TAG = FragmentReviews.class.getSimpleName();

    public static final int COL_AUTHOR = 3;
    public static final int COL_CONTENT = 4;
    private Uri mUri;
    private static final int DETAIL_LOADER_REVIEWS = 1;

    private static final String[] REVIEWS_COLUMNS = {
            MoviesContract.ReviewEntry._ID,
            MoviesContract.ReviewEntry.COLUMN_MOVIE_ID,
            MoviesContract.ReviewEntry.COLUMN_COMMENT_ID,
            MoviesContract.ReviewEntry.COLUMN_AUTHOR,
            MoviesContract.ReviewEntry.COLUMN_CONTENT,
            MoviesContract.ReviewEntry.COLUMN_URL,
    };

    String mMovieID;
    ReviewObjectAdapter reviewsAdapter;
    TextView textView;

    public FragmentReviews() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(2, null, this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);
        textView = view.findViewById(R.id.text_error_no_review);
        reviewsAdapter = new ReviewObjectAdapter(getActivity(), null, 0);
        ListView mReviewsList = view.findViewById(R.id.movies_reviews_container);
        mReviewsList.setAdapter(reviewsAdapter);

        Bundle arguments = new Bundle();
        arguments.putParcelable(DETAIL_URI, getActivity().getIntent().getData());
        if (arguments != null) {
            mUri = arguments.getParcelable(DETAIL_URI);
            mMovieID = mUri.getLastPathSegment();
        }


        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Log.d(TAG, "URI is " + mUri);
        if (mUri == null)
            return null;

        String sortPref = Utility.getSortOrder(getActivity());

        String[] COLUMNS;
        Uri uri;

        if (!sortPref.equals(getString(R.string.key_favoris))) {
            COLUMNS = REVIEWS_COLUMNS;
            uri = MoviesContract.ReviewEntry.CONTENT_URI;
        } else {
            uri = MoviesContract.ReviewEntry.CONTENT_URI;
            COLUMNS = REVIEWS_COLUMNS;
        }

        return new CursorLoader(
                getActivity(),
                uri,
                COLUMNS,
                MoviesContract.ReviewEntry.COLUMN_MOVIE_ID + " =" + mMovieID,
                null,
                null
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int loaderId = loader.getId();

        int da = data.getCount();
        Log.e(TAG, "Reviews Cursor count : " + String.valueOf(da));


        if (loaderId == DETAIL_LOADER_REVIEWS) {
            if (!data.moveToFirst()) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(getString(R.string.no_review));
            }

            if (data != null && data.getCount() > 0) {

                for (int i = 0; i < data.getColumnNames().length; i++) {
                    Log.e(TAG, "Reviews Cursor column name: " + data.getString(i));
                }
                textView.setVisibility(View.GONE);
                reviewsAdapter.swapCursor(data);
            }

        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case DETAIL_LOADER_REVIEWS:
                reviewsAdapter.swapCursor(null);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(DETAIL_LOADER_REVIEWS, null, this);
    }
}
