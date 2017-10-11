package com.zack_olivier.zackpopularmoviesstage2.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zack_olivier.zackpopularmoviesstage2.Interace.OnItemClickListener;
import com.zack_olivier.zackpopularmoviesstage2.R;
import com.zack_olivier.zackpopularmoviesstage2.appUtils.DownloadGlideImage;
import com.zack_olivier.zackpopularmoviesstage2.data.MoviesContract;

import static com.zack_olivier.zackpopularmoviesstage2.appUtils.Utility.COL_TITLE;


public class MoviesAdapter extends RecyclerViewCursorAdapter<MoviesAdapter.ViewHolder> {

    private final String TAG = MoviesAdapter.class.getSimpleName();
    private static final String BASE_IMG_URL = "http://image.tmdb.org/t/p/";
    private static final String IMG_SIZE = "w342";
    private static final String IMG_SIZE_SW600 = "w500";
    private static final String IMG_SIZE_SW720 = "w720";
    Context mContext;
    private View view;

    private static OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        MoviesAdapter.onItemClickListener = onItemClickListener;
    }


    public MoviesAdapter(Cursor c, Context context) {
        super(c);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        int mIdposter = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER);
        String mTitle = cursor.getString(COL_TITLE);

        //return layouyt sw600
        if (view.findViewById(R.id.recyclerview_movies_sw600) != null) {

            String poster_path_sw600 = BASE_IMG_URL + IMG_SIZE_SW600 + cursor.getString(mIdposter);
            DownloadGlideImage.downloadImage(mContext, poster_path_sw600, holder.mImageView_sw600);
            holder.textView_sw600.setText(mTitle);
            //return layouyt sw720
        } else if (view.findViewById(R.id.recyclerview_movies_sw720) != null) {

            String poster_path_sw720 = BASE_IMG_URL + IMG_SIZE_SW720 + cursor.getString(mIdposter);
            DownloadGlideImage.downloadImage(mContext, poster_path_sw720, holder.mImageView_sw720);
            holder.textView_sw720.setText(mTitle);
        } else {

            String poster_path = BASE_IMG_URL + IMG_SIZE + cursor.getString(mIdposter);
            DownloadGlideImage.downloadImage(mContext, poster_path, holder.imageHolder);
            holder.textView.setText(mTitle);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageHolder;
        ImageView mImageView_sw600;
        ImageView mImageView_sw720;
        TextView textView;
        TextView textView_sw600;
        TextView textView_sw720;

        @SuppressLint("CutPasteId")
        ViewHolder(View view) {
            super(view);
            imageHolder = view.findViewById(R.id.gridview_item_imageview);
            mImageView_sw600 = view.findViewById(R.id.gridview_item_imageview_sw600);
            mImageView_sw720 = view.findViewById(R.id.gridview_item_imageview_sw720);
            textView = view.findViewById(R.id.txt_movie_title);
            textView_sw600 = view.findViewById(R.id.txt_movie_title_sw600);
            textView_sw720 = view.findViewById(R.id.txt_movie_title_sw720);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

}