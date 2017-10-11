package com.zack_olivier.zackpopularmoviesstage2.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.zack_olivier.zackpopularmoviesstage2.R;
import com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility;
import com.zack_olivier.zackpopularmoviesstage2.fragments.MainActivityFragment;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private String mSortPref;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "MAIN ACTIVITY CREATED!");
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null){
            mSortPref = Utility.getSortOrder(this);
            MainActivityFragment mf = (MainActivityFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.main_fragment);
            if (mf != null) {
                mf.onSortChange();
            }

        } else {mSortPref = savedInstanceState.getString("SORT", null);}

//        toolbar.setTitle(getString(R.string.app_name) + "-" + mSortPref);


    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "onSaveState CREATED!");
        outState.putString("SORT", mSortPref);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onItemSelected(Uri contentUri) {

        Intent intent = new Intent(this, DetailActivity.class)
                .setData(contentUri);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "MAIN ACTIVITY RESUMED!");
        String sortPref = Utility.getSortOrder(this);
        Log.d(LOG_TAG, "Sort Order is " + sortPref);
        Log.d(LOG_TAG, "Sort Order is " + mSortPref);

        if (sortPref != null && !sortPref.equals(mSortPref)) {
            Log.d(LOG_TAG, "SORTING CHANGED!!");
            mSortPref = sortPref;
        }
       mSortPref = sortPref;
        MainActivityFragment mf = (MainActivityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_fragment);
        if (mf != null) {
            mf.onSortChange();
        }

    }



}
