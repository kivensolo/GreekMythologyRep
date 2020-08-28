package com.zeke.home.banner;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.kingz.module.home.R;
import com.youth.banner.loader.ImageLoader;

public class BannerGlideImageLoader extends ImageLoader {
    RequestOptions requestOptions = new RequestOptions()
            .placeholder(R.mipmap.ic_launcher)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .error(R.mipmap.ic_launcher);

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade(600))
                .into(imageView);
    }

}