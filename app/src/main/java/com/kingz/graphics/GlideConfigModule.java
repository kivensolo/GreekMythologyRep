package com.kingz.graphics;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

/**
 * 一个项目（包含主项目与依赖库）中只能存在一个继承AppGlideModule的自定义模块
 * 但是允许有多个继承LibraryGlideModule的自定义模块
 */
@GlideModule
public class GlideConfigModule extends AppGlideModule {
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        //修改默认配置，如缓存配置
        //磁盘缓存配置（默认缓存大小250M，默认保存在内部存储中）

        //设置磁盘缓存保存在外部存储，且指定缓存大小
        // FIXME 加了这个，请求网络图片的时候，就会在AssetFileDescriptorLocalUriFetcher.loadResource.java:22 处报错
//        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, 20 * 1024));

        //设置内存缓存大小
//        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        //设置Bitmap池大小
//        builder.setBitmapPool(new LruBitmapPool(bitmapPoolSize));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        //替换组件，如网络请求组件
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
}
