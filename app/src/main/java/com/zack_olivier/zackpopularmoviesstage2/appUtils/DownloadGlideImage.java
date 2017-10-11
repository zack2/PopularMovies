package com.zack_olivier.zackpopularmoviesstage2.appUtils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zack_olivier.zackpopularmoviesstage2.R;


/**
 * Created by pc on 13/01/2017.
 */
//this method allow you to download image
public class DownloadGlideImage {
    public static void downloadImage(Context c, String url, ImageView imageView){
        Glide.with(c).load(url).thumbnail(0.5f)
                .placeholder(R.drawable.no_network)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        ;
    }
}
