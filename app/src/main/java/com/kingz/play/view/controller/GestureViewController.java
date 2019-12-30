package com.kingz.play.view.controller;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.App;
import com.kingz.customdemo.R;
import com.zeke.kangaroo.utils.TimeUtils;

/**
 * author：KingZ
 * date：2019/9/29
 * description：手势视图的Controller
 */

public class GestureViewController extends BaseController{
    // 手势文本提示显示 音量百分比/屏幕亮度百分比/快进快退进度值
    private TextView textView;
    private ImageView imageView;
    private Drawable brightnessDrawable = null;
    private Drawable volumeDrable = null;
    private long durationOffSet = 0L;

    public GestureViewController(View view) {
        rootView = view.findViewById(R.id.gesture_seek_layout);
        imageView = rootView.findViewById(R.id.gesture_tip_img);
        textView = rootView.findViewById(R.id.seek_time_preview);

        Resources resources = App.getAppInstance().getResources();
        brightnessDrawable = resources.getDrawable(R.drawable.brightness_icon);
        volumeDrable = resources.getDrawable(R.drawable.volume_icon);
    }

    /**
     * 设置亮度程度
     * @param ratio 亮度比例0~1.0f
     */
    void setBrightness(float ratio){
        float value = ratio * 100;
        if(imageView.getVisibility() == View.GONE){
            imageView.setVisibility(View.VISIBLE);
        }
        imageView.setImageDrawable(brightnessDrawable);
        textView.setText(String.format("%.0f %%",value));
    }

    /**
     * 设置音量程度
     * @param ratio 亮度比例0~1.0f
     */
    void setVolume(float ratio){
        float value = ratio * 100;
        if(imageView.getVisibility() == View.GONE){
            imageView.setVisibility(View.VISIBLE);
        }
        imageView.setImageDrawable(volumeDrable);
        textView.setText(String.format("%.0f %%",value));
    }

    /**
     * 设置时长预览
     * @param duration  拖动时长
     */
    public void setDuration(long duration){
        if(imageView.getVisibility() == View.VISIBLE){
            imageView.setVisibility(View.GONE);
        }
        durationOffSet = duration;
        String timeDuration = TimeUtils.formatSeekingPreviewTime(duration);
        textView.setText(timeDuration);
    }

    @Override
    public void show() {
        rootView.setVisibility(View.VISIBLE);
    }

    @Override
    public void close() {
        textView.setText("");
        imageView.setVisibility(View.GONE);
        super.close();
    }

    /**
     * 是否处于seeking模式  暂时用imageView的显示来判断
     */
    public boolean isInSeekingControl(){
        return imageView.getVisibility() == View.GONE;
    }

    public long getDurationOffset(){
        return durationOffSet;
    }
}
