package com.kingz.play.view.controller;

import android.view.View;
import android.widget.TextView;

import com.kingz.customdemo.R;
import com.kingz.utils.TimeUtils;

/**
 * author：KingZ
 * date：2019/9/29
 * description：手势滑动seek过程预览试图的Controller
 */

public class SeekTimePreViewController extends BaseController{
    // 准备seek的时间显示
    private TextView seekTimeView;
    private long durationOffSet = 0L;

    public SeekTimePreViewController(View view) {
        this.rootView = view.findViewById(R.id.gesture_seek_layout);
        this.seekTimeView = view.findViewById(R.id.seek_time_preview);
    }

    /**
     * 设置时长预览
     * @param duration  拖动时长
     */
    public void setDuration(long duration){
        durationOffSet = duration;
        String timeDuration = TimeUtils.formatSeekingPreviewTime(duration);
        seekTimeView.setText(timeDuration);
    }

    @Override
    public void show() {
        rootView.setVisibility(View.VISIBLE);
        seekTimeView.setVisibility(View.VISIBLE);
    }

    public long getDurationOffset(){
        return durationOffSet;
    }
}
