package com.zeke.demo.gaussian_blur;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingz.module.common.router.RouterConfig;
import com.zeke.demo.R;
import com.zeke.kangaroo.blur.BlurringView;

import java.util.Random;

/**
 * Demonstrates the use of the blurring view.
 */
@Route(path = RouterConfig.PAGE_500px_BLURRING)
public class FiveHundredBlurActivity extends AppCompatActivity {

    private final ImageView[] mImageViews = new ImageView[9];
    private int mStartIndex;
    private final Random mRandom = new Random();
    private boolean mShifted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_500px_blur);
        mBlurringView = findViewById(R.id.blurring_view);
        View blurredView = findViewById(R.id.blurred_view);

        // Give the blurring view a reference to the blurred view.
        mBlurringView.setBlurredView(blurredView);

        mImageViews[0] = findViewById(R.id.image0);
        mImageViews[1] = findViewById(R.id.image1);
        mImageViews[2] = findViewById(R.id.image2);
        mImageViews[3] = findViewById(R.id.image3);
        mImageViews[4] = findViewById(R.id.image4);
        mImageViews[5] = findViewById(R.id.image5);
        mImageViews[6] = findViewById(R.id.image6);
        mImageViews[7] = findViewById(R.id.image7);
        mImageViews[8] = findViewById(R.id.image8);
    }

    public void shuffle(View view) {

        // Randomly pick a different start in the array of available images.
        int newStartIndex;
        do {
            newStartIndex = IMAGE_IDS[mRandom.nextInt(IMAGE_IDS.length)];
        } while (newStartIndex == mStartIndex);
        mStartIndex = newStartIndex;

        // Update the images for the image views contained in the blurred view.
        for (int i = 0; i < mImageViews.length; i++) {
            int drawableId = IMAGE_IDS[(mStartIndex + i) % IMAGE_IDS.length];
            mImageViews[i].setImageDrawable(getResources().getDrawable(drawableId));
        }

        // Invalidates the blurring view when the content of the blurred view changes.
        mBlurringView.invalidate();
    }

    private ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mBlurringView.invalidate();
        }
    };

    public void shift(View view) {
        if (!mShifted) {
            for (ImageView imageView : mImageViews) {
                ObjectAnimator tx = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, (mRandom.nextFloat() - 0.5f) * 500);
                tx.addUpdateListener(listener);
                ObjectAnimator ty = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_Y, (mRandom.nextFloat() - 0.5f) * 500);
                ty.addUpdateListener(listener);
                AnimatorSet set = new AnimatorSet();
                set.playTogether(tx, ty);
                set.setDuration(3000);
                set.setInterpolator(new OvershootInterpolator());
                set.addListener(new AnimationEndListener(imageView));
                set.start();
            }
            mShifted = true;
        } else {
            for (ImageView imageView : mImageViews) {
                ObjectAnimator tx = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, 0);
                tx.addUpdateListener(listener);
                ObjectAnimator ty = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_Y, 0);
                ty.addUpdateListener(listener);
                AnimatorSet set = new AnimatorSet();
                set.playTogether(tx, ty);
                set.setDuration(3000);
                set.setInterpolator(new OvershootInterpolator());
                set.addListener(new AnimationEndListener(imageView));
                set.start();
            }
            mShifted = false;
        }
    }

    private BlurringView mBlurringView;

    private static final int[] IMAGE_IDS = {
            R.drawable.p0, R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4,
            R.drawable.p5, R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9
    };

    private static class AnimationEndListener implements Animator.AnimatorListener {

        View mView;

        public AnimationEndListener(View v) {
            mView = v;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            mView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mView.setLayerType(View.LAYER_TYPE_NONE, null);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            mView.setLayerType(View.LAYER_TYPE_NONE, null);
        }

        @Override
        public void onAnimationRepeat(Animator animation) {}
    }
}
