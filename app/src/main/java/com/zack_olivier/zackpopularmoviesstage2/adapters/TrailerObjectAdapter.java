package com.zack_olivier.zackpopularmoviesstage2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zack_olivier.zackpopularmoviesstage2.R;
import com.zack_olivier.zackpopularmoviesstage2.fragments.FragmentTrailers;

/**
 * Created by pc on 01/03/2017.
 */

public class TrailerObjectAdapter extends CursorAdapter {

    public final String TAG = TrailerObjectAdapter.class.getSimpleName();



    public TrailerObjectAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.items_movie_trailers,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.textHolder.setText(cursor.getString(FragmentTrailers.COL_NAME));

        viewHolder.playIcon.setImageResource(R.drawable.ic_media_play);
    }

    private static class ViewHolder {
        TextView textHolder;
        ImageView playIcon;



        ViewHolder(View view){
            textHolder = (TextView) view.findViewById(R.id.movie_trailer_text);
            playIcon = (ImageView) view.findViewById(R.id.play_icon);
        }

    }
}
