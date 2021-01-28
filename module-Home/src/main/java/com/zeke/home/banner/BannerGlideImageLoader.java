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
            .placeholder(R.color.gray)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .error(R.color.gray);
//            .error(R.mipmap.ic_launcher);

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */
        Glide.with(context)
                .load(path)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade(600))
                 .thumbnail(0.1f)
                 // 设置缩略图支持：先加载缩略图 然后在加载全图
                 // 传了一个 0.1f 作为参数，Glide 将会显示原始图像的10%的大小。
                .into(imageView);
    }

}