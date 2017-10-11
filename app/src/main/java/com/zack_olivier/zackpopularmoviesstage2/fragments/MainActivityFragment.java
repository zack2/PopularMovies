package com.zack_olivier.zackpopularmoviesstage2.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.zack_olivier.zackpopularmoviesstage2.R;
import com.zack_olivier.zackpopularmoviesstage2.activities.MainActivity;
import com.zack_olivier.zackpopularmoviesstage2.activities.SettingsActivity;
import com.zack_olivier.zackpopularmoviesstage2.adapters.ItemClickSupport;
import com.zack_olivier.zackpopularmoviesstage2.adapters.MoviesAdapter;
import com.zack_olivier.zackpopularmoviesstage2.api.CustomStringRequest;
import com.zack_olivier.zackpopularmoviesstage2.api.VolleyController;
import com.zack_olivier.zackpopularmoviesstage2.appUtils.ConnectivityReceiver;
import com.zack_olivier.zackpopularmoviesstage2.appUtils.PopularmoviesApplication;
import com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility;
import com.zack_olivier.zackpopularmoviesstage2.data.MoviesContract;
import com.zack_olivier.zackpopularmoviesstage2.server.FetchJSONData;

import org.json.JSONException;

import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.COL_MOV_ID;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.FAVORIS_LOADER;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.MOVIE_COLUMNS;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.MOVIE_LOADER_LATEST;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.MOVIE_LOADER_NOW_PLAYING;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.MOVIE_LOADER_POPULAR;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.MOVIE_LOADER_TOP_RATED;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.MOVIE_LOADER_UPCOMING;
import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.THE_MOVIE_DB_API_KEY;


public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ConnectivityReceiver.ConnectivityReceiverListener {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private int mPosition = GridView.INVALID_POSITION;


    private MoviesAdapter mMoviesAdapter;
    private static final String SELECTED_KEY = "selected_position";
    String mMovieID;
    @SuppressLint("StaticFieldLeak")
    static LinearLayout linearLayout;
    LinearLayout linearLayoutNetwork;
    LinearLayout linearLayoutFavoris;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    GridLayoutManager gridLayoutManager;

    MainActivity mainActivity = (MainActivity) getActivity();
    int index;
    int mPositionScroll = 0;
    int mOffset = 0;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int SPAN_COUNT = 2;
    public static String sortPref;

    public MainActivityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mainActivity = (MainActivity) context;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack();
    }

    public interface Callback {
        /**
         * Callback used when an item has been selected.
         */
        void onItemSelected(Uri dateUri);
        //void onItemSelected(Intent intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        PopularmoviesApplication.getInstance().setConnectivityListener(this);
        if (checkConnection()) {
            if (mMoviesAdapter.getItemCount() == 0) {
                String sortPref = Utility.getSortOrder(getActivity());

                if (sortPref.equals(getString(R.string.pref_sort_default))) {
                    updateMovies(sortPref);
                } else if (sortPref.equals(getString(R.string.sort_top_rated))) {
                    updateMovies(sortPref);
                }else if (sortPref.equals(getString(R.string.sort_latest))) {
                    updateMovies(sortPref);
                }else if (sortPref.equals(getString(R.string.sort_now_playing))) {
                    updateMovies(sortPref);
                }else if (sortPref.equals(getString(R.string.sort_upcoming))) {
                    updateMovies(sortPref);
                } else {
                    getLoaderManager().restartLoader(FAVORIS_LOADER, null, this);
                }

            }
        } else {
            linearLayoutNetwork.setVisibility(View.VISIBLE);
            showSnack();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            Log.e(LOG_TAG + " onCreate position", String.valueOf(mPositionScroll));
            mPositionScroll = savedInstanceState.getInt(SELECTED_KEY);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        linearLayout = rootView.findViewById(R.id.layout_info_data);
        linearLayoutNetwork = rootView.findViewById(R.id.layout_info_network);
        linearLayoutFavoris = rootView.findViewById(R.id.layout_info_favoris);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        recyclerView = rootView.findViewById(R.id.recyclerview_movies);
      /*  staggeredGridLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
 */
        gridLayoutManager = new GridLayoutManager(getActivity(),SPAN_COUNT);
        recyclerView.setLayoutManager(gridLayoutManager);

       // recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        mMoviesAdapter = new MoviesAdapter(null,getContext());


        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = gridLayoutManager.getItemCount();
                firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached

                    Log.i("...", "end called");

                    // Do something

                    loading = true;
                }
            }
        });



        if (savedInstanceState != null) {
            Log.e(LOG_TAG + " onCreateView position", String.valueOf(mPositionScroll));
            mPositionScroll = savedInstanceState.getInt(SELECTED_KEY);
            recyclerView.smoothScrollToPosition(mPositionScroll);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            Log.e(LOG_TAG + " onViewCreated position", String.valueOf(mPositionScroll));
            mPositionScroll = savedInstanceState.getInt(SELECTED_KEY);
            recyclerView.smoothScrollToPosition(mPositionScroll);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_menu, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        sortPref = Utility.getSortOrder(getActivity());

        if (sortPref.equals(getString(R.string.pref_sort_default))) {
            getLoaderManager().initLoader(MOVIE_LOADER_POPULAR, null, this);
        } else if (sortPref.equals(getString(R.string.sort_top_rated))) {
            getLoaderManager().initLoader(MOVIE_LOADER_TOP_RATED, null, this);
        }  else if (sortPref.equals(getString(R.string.sort_latest))) {
            getLoaderManager().initLoader(MOVIE_LOADER_LATEST, null, this);
        } else if (sortPref.equals(getString(R.string.sort_now_playing))) {
            getLoaderManager().initLoader(MOVIE_LOADER_NOW_PLAYING, null, this);
        } else if (sortPref.equals(getString(R.string.sort_upcoming))) {
            getLoaderManager().initLoader(MOVIE_LOADER_UPCOMING, null, this);
        } else {
            getLoaderManager().initLoader(FAVORIS_LOADER, null, this);
        }


        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_refresh) {
            String sortPref = Utility.getSortOrder(getActivity());

            if (sortPref.equals(getString(R.string.pref_sort_default))) {
                updateMovies(sortPref);
            } else if (sortPref.equals(getString(R.string.sort_top_rated))) {
                updateMovies(sortPref);
            } else {
                getLoaderManager().restartLoader(FAVORIS_LOADER, null, this);
            }


        } else {
            Toast.makeText(getActivity(), R.string.no_refresh, Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(LOG_TAG + " savedInstanceState po", String.valueOf(mPositionScroll));
        outState.putInt(SELECTED_KEY, mPositionScroll);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!= null) mPosition = savedInstanceState.getInt(SELECTED_KEY);
        Log.e(LOG_TAG + " onViewStateRestored po", String.valueOf(mPositionScroll));
    }

    public void onSortChange() {
        Log.d(LOG_TAG, "OnSortChange!!");
        mPosition = GridView.INVALID_POSITION;
        String sortPref = Utility.getSortOrder(mainActivity);
        if (sortPref.equals(getString(R.string.pref_sort_default))) {
            getLoaderManager().restartLoader(MOVIE_LOADER_POPULAR, null, this);
        } else if (sortPref.equals(getString(R.string.sort_top_rated))) {
            getLoaderManager().restartLoader(MOVIE_LOADER_TOP_RATED, null, this);
        }else if (sortPref.equals(getString(R.string.sort_latest))) {
            getLoaderManager().initLoader(MOVIE_LOADER_LATEST, null, this);
        }else if (sortPref.equals(getString(R.string.sort_now_playing))) {
            getLoaderManager().restartLoader(MOVIE_LOADER_NOW_PLAYING, null, this);
        }else if (sortPref.equals(getString(R.string.sort_upcoming))) {
            getLoaderManager().restartLoader(MOVIE_LOADER_UPCOMING, null, this);
        } else if (sortPref.equals(getString(R.string.sort_favorites))) {
            getLoaderManager().restartLoader(FAVORIS_LOADER, null, this);
        }
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri uri;
        CursorLoader cursorLoader = null;

        sortPref = Utility.getSortOrder(getActivity());


        if (id == MOVIE_LOADER_POPULAR) {

            Log.e(LOG_TAG + " loader popular", String.valueOf(id));


            uri = MoviesContract.MoviesEntry.buildMovieWithMovId(sortPref);
            cursorLoader = new CursorLoader(
                    getActivity(),
                    uri,
                    MOVIE_COLUMNS,
                    MoviesContract.MoviesEntry.COLUMN_SORT_ORDER + "=?",
                    new String[]{sortPref},
                    null);

        } else if (id == MOVIE_LOADER_TOP_RATED) {

            Log.e(LOG_TAG + " loader top_rated", String.valueOf(id));

            uri = MoviesContract.MoviesEntry.buildMovieWithMovId(sortPref);
            cursorLoader = new CursorLoader(
                    getActivity(),
                    uri,
                    MOVIE_COLUMNS,
                    MoviesContract.MoviesEntry.COLUMN_SORT_ORDER + "=?",
                    new String[]{sortPref},
                    null);


        }else if (id == MOVIE_LOADER_NOW_PLAYING) {

            Log.e(LOG_TAG + " loader now playing", String.valueOf(id));

            uri = MoviesContract.MoviesEntry.buildMovieWithMovId(sortPref);
            cursorLoader = new CursorLoader(
                    getActivity(),
                    uri,
                    MOVIE_COLUMNS,
                    MoviesContract.MoviesEntry.COLUMN_SORT_ORDER + "=?",
                    new String[]{sortPref},
                    null);


        }else if (id == MOVIE_LOADER_UPCOMING) {

            Log.e(LOG_TAG + " loader upcoming", String.valueOf(id));

            uri = MoviesContract.MoviesEntry.buildMovieWithMovId(sortPref);
            cursorLoader = new CursorLoader(
                    getActivity(),
                    uri,
                    MOVIE_COLUMNS,
                    MoviesContract.MoviesEntry.COLUMN_SORT_ORDER + "=?",
                    new String[]{sortPref},
                    null);


        } else if (id == FAVORIS_LOADER) {

            Log.e(LOG_TAG + "loader favorite", String.valueOf(id));

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

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {

        int loaderId = loader.getId();
        sortPref = Utility.getSortOrder(getActivity());

        if (loaderId == MOVIE_LOADER_POPULAR) {

            Log.e(LOG_TAG + " onLoadFinished", String.valueOf(loaderId));

            if (!data.moveToFirst()) {
                linearLayout.setVisibility(View.VISIBLE);
                mMoviesAdapter.swapCursor(null);
                mMoviesAdapter.notifyDataSetChanged();
            }

            if (data.moveToFirst()) {

                linearLayoutFavoris.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                linearLayoutNetwork.setVisibility(View.GONE);
                mMoviesAdapter.swapCursor(data);
                recyclerView.setAdapter(mMoviesAdapter);
            }


        }
        if (loaderId == MOVIE_LOADER_TOP_RATED) {

            Log.e(LOG_TAG + " onLoadFinished", String.valueOf(loaderId));

            if (!data.moveToFirst()) {
                linearLayout.setVisibility(View.VISIBLE);
                mMoviesAdapter.swapCursor(null);
                mMoviesAdapter.notifyDataSetChanged();
            }

            if (data.moveToFirst()) {

                linearLayoutFavoris.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                linearLayoutNetwork.setVisibility(View.GONE);
                mMoviesAdapter.swapCursor(data);
                recyclerView.setAdapter(mMoviesAdapter);
            }

        }
        if (loaderId == MOVIE_LOADER_LATEST) {

            Log.e(LOG_TAG + " onLoadFinished", String.valueOf(loaderId));
            Log.e(LOG_TAG + " MOVIE_LOADER_LATEST", String.valueOf(loaderId));

            if (!data.moveToFirst()) {
                linearLayout.setVisibility(View.VISIBLE);
                mMoviesAdapter.swapCursor(null);
                mMoviesAdapter.notifyDataSetChanged();
            }

            if (data.moveToFirst()) {

                linearLayoutFavoris.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                linearLayoutNetwork.setVisibility(View.GONE);
                mMoviesAdapter.swapCursor(data);
                recyclerView.setAdapter(mMoviesAdapter);
            }

        }
        if (loaderId == MOVIE_LOADER_NOW_PLAYING) {

            Log.e(LOG_TAG + " onLoadFinished", String.valueOf(loaderId));
            Log.e(LOG_TAG + "MOVIE_LOADE_NOW_PLAYING", String.valueOf(loaderId));

            if (!data.moveToFirst()) {
                linearLayout.setVisibility(View.VISIBLE);
                mMoviesAdapter.swapCursor(null);
                mMoviesAdapter.notifyDataSetChanged();
            }

            if (data.moveToFirst()) {

                linearLayoutFavoris.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                linearLayoutNetwork.setVisibility(View.GONE);
                mMoviesAdapter.swapCursor(data);
                recyclerView.setAdapter(mMoviesAdapter);
            }

        }
        if (loaderId == MOVIE_LOADER_UPCOMING) {

            Log.e(LOG_TAG + " onLoadFinished", String.valueOf(loaderId));
            Log.e(LOG_TAG + " MOVIE_LOADER_UPCOMING", String.valueOf(loaderId));

            if (!data.moveToFirst()) {
                linearLayout.setVisibility(View.VISIBLE);
                mMoviesAdapter.swapCursor(null);
                mMoviesAdapter.notifyDataSetChanged();
            }

            if (data.moveToFirst()) {

                linearLayoutFavoris.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                linearLayoutNetwork.setVisibility(View.GONE);
                mMoviesAdapter.swapCursor(data);
                recyclerView.setAdapter(mMoviesAdapter);
            }

        }
        if (loaderId == FAVORIS_LOADER) {
            Log.e(LOG_TAG + " onLoadFinished", String.valueOf(loaderId));
            Log.e(LOG_TAG + " FAVORIS_LOADER", String.valueOf(loaderId));

            if (!data.moveToFirst()) {
                linearLayout.setVisibility(View.VISIBLE);
                mMoviesAdapter.swapCursor(null);
                mMoviesAdapter.notifyDataSetChanged();
            }

            if (data.moveToFirst()) {

                linearLayoutFavoris.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                linearLayoutNetwork.setVisibility(View.GONE);
                mMoviesAdapter.swapCursor(data);
                recyclerView.setAdapter(mMoviesAdapter);
            }
        }

        if(mPosition > 0){
            recyclerView.smoothScrollToPosition(mPosition);
        }

        ItemClickSupport.addTo(recyclerView)

                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Log.d("ITEM CLICK", "Item single clicked " + recyclerView.getChildItemId(v));

                        long i = mMoviesAdapter.getItemId(position);

                        if (i != 0) {
                            String sortPref = Utility.getSortOrder(getActivity());

                            Uri itemUri;
                            if (!sortPref.equals(getString(R.string.sort_favorites))) {
                                itemUri = MoviesContract.MoviesEntry.buildMovieWithMovId(
                                        Integer.toString(data.getInt(COL_MOV_ID)));
                            } else {
                                itemUri = MoviesContract.MoviesEntry.buildMovieWithMovId(
                                        Integer.toString(data.getInt(COL_MOV_ID)));
                            }

                            mMovieID = itemUri.getLastPathSegment();
                            ((Callback) getActivity()).onItemSelected(itemUri);
                        }

                        mPosition = position;
                    }

                    @Override
                    public void onItemDoubleClicked(RecyclerView recyclerView, int position, View v) {
                        // Log.d("ITEM CLICK", "Item double clicked " + recyclerView.getItemAt(position).getProductId());
                    }
                });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }

    private void updateMovies(final String sortOrder) {
        Toast.makeText(getActivity(), R.string.fecth_movies, Toast.LENGTH_SHORT).show();

        final String BASE_URL = "http://api.themoviedb.org/3/movie";

        String API_KEY_WORD = "api_key";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(API_KEY_WORD, THE_MOVIE_DB_API_KEY)
                .build();

        //Instantiate Request..

        String url = builtUri.toString();

        Log.e(LOG_TAG, url);

        CustomStringRequest request = new CustomStringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    try {
                        FetchJSONData.getMovieDataFromJson(getActivity(), response, sortOrder);
                    } catch (JSONException e) {
                        Toast.makeText(getActivity().getApplicationContext(), R.string.volley_message,
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.volley_message2
                            , Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, error.toString());

            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyController.getInstance(getActivity()).addToRequestQueue(request);

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

        Snackbar snackbar = Snackbar
                .make(getView().findViewById(R.id.recyclerview_movies), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    public void onResume() {
        onSortChange();
        super.onResume();
    }
}