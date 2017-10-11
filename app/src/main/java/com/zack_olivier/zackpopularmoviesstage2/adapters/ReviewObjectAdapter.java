package com.zack_olivier.zackpopularmoviesstage2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.zack_olivier.zackpopularmoviesstage2.R;
import com.zack_olivier.zackpopularmoviesstage2.fragments.FragmentReviews;

/**
 * Created by pc on 01/03/2017.
 */

public class ReviewObjectAdapter extends CursorAdapter {

    public final String TAG = ReviewObjectAdapter.class.getSimpleName();


    public ReviewObjectAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.items_movie_reviews, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.textAuthor.setText(cursor.getString(FragmentReviews.COL_AUTHOR));
        viewHolder.textReviewContent.setText(cursor.getString(FragmentReviews.COL_CONTENT));

        Log.e(TAG + " item Review author", cursor.getString(FragmentReviews.COL_AUTHOR));
        Log.e(TAG + " item Review Content", cursor.getString(FragmentReviews.COL_CONTENT));

    }

    private static class ViewHolder {

        TextView textReviewContent;
        TextView textAuthor;

        ViewHolder(View view) {
            textReviewContent = (TextView) view.findViewById(R.id.movie_review_content);
            textAuthor = (TextView) view.findViewById(R.id.movie_review_author);
        }

    }
}
