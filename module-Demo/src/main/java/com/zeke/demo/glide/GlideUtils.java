package com.zeke.demo.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * on 2020/8/3.
 */
public class GlideUtils {

    /**
     * 给指定View对象设置可带有圆角效果背景DrawableRes
     *
     * @param view           View对象
     * @param resourceId     Drawable
     * @param cornerDipValue corner size in Dip
     */
    public static void setRoundCorner(final View view, final Drawable resourceId, final float cornerDipValue) {
        if (cornerDipValue == 0) {
            if (view.getMeasuredWidth() == 0 && view.getMeasuredHeight() == 0) {
                //当前View未layout完毕，等待布局改变
                view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        loadBackgroundWithGlide(view, resourceId, new CenterCrop());
                    }
                });
            } else {
                loadBackgroundWithGlide(view, resourceId, new CenterCrop());
            }
        } else {
            if (view.getMeasuredWidth() == 0 && view.getMeasuredHeight() == 0) {
                view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        loadBackgroundWithGlide(view, resourceId,
                                new CenterCrop(), new RoundedCorners((int) cornerDipValue));
                    }
                });
            } else {
                loadBackgroundWithGlide(view, resourceId,
                        new CenterCrop(), new RoundedCorners((int) cornerDipValue));
            }
        }
    }

    public static void setCorners(final View view, final Drawable resourceId,
                                  final float leftTop_corner,
                                  final float leftBottom_corner,
                                  final float rightTop_corner,
                                  final float rightBottom_corner) {
        if (leftTop_corner == 0 && leftBottom_corner == 0 && rightTop_corner == 0 && rightBottom_corner == 0) {
            if (view.getMeasuredWidth() == 0 && view.getMeasuredHeight() == 0) {
                view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        Glide.with(view)
                                .load(resourceId)
                                .override(view.getMeasuredWidth(), view.getMeasuredHeight())
                                .into(new CustomTarget<Drawable>() {
                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                                            view.setBackgroundDrawable(resource);
                                        } else {
                                            view.setBackground(resource);
                                        }
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {

                                    }
                                });
                    }
                });
            } else {
                Glide.with(view)
                        .load(resourceId)
                        .override(view.getMeasuredWidth(), view.getMeasuredHeight())
                        .into(new CustomTarget<Drawable>() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                                    view.setBackgroundDrawable(resource);
                                } else {
                                    view.setBackground(resource);
                                }
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }

        } else {
            if (view.getMeasuredWidth() == 0 && view.getMeasuredHeight() == 0) {
                view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        GlideRoundTransform transform = new GlideRoundTransform(view.getContext(), leftTop_corner, leftBottom_corner, rightTop_corner, rightBottom_corner);
                        loadBackgroundWithGlide(view, resourceId, transform);
                    }
                });
            } else {

                GlideRoundTransform transform = new GlideRoundTransform(view.getContext(), leftTop_corner, leftBottom_corner, rightTop_corner, rightBottom_corner);
                loadBackgroundWithGlide(view, resourceId, transform);
            }
        }
    }

    private static void loadBackgroundWithGlide(final View view, final Drawable resourceId,
                                                @NonNull Transformation<Bitmap>... transformations) {
        Glide.with(view)
                .load(resourceId)
                .transform(transformations)
                .override(view.getMeasuredWidth(), view.getMeasuredHeight())
                .into(new CustomTarget<Drawable>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                            view.setBackgroundDrawable(resource);
                        } else {
                            view.setBackground(resource);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}

