package com.mplayer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import com.kingz.uiusingListViews.R;

/**
 * Created by KingZ on 2016/1/24.
 * Discription:
 */
public class SeekBarView extends View implements View.OnTouchListener{

    private static String TAG = "SeekBarView";

    private int progressBarWidth = 0;   //进度条宽度
    private int progressBarHeight = 0;  //进度条高度
    private int playProgressWidth = 0;  //播放进度宽度

    /** 播放状态图标 Start **/
    private Bitmap playStatus;
    private Bitmap pauseStatus;
    private Bitmap fforwordStatus;
    private Bitmap rewindStatus;

    private Rect playStatusRect;
    /** 播放状态图标 End **/

    private RectF durationRect;
    private RectF bgcSeekBar;
//    private Rect durationRect;
//    private Rect durationRect;

    private Canvas canvas;
    private Paint seekPaint;
    private Paint borderPaint;
    private Paint textPaint;
    private Context context;


    public SeekBarView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public SeekBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView() {

        playStatus = decodeResource(getResources(), R.drawable.mplayerv_state_play);
        pauseStatus = decodeResource(getResources(), R.drawable.mplayerv_state_pause);
        fforwordStatus = decodeResource(getResources(), R.drawable.mplayerv_state_fforward);
        rewindStatus = decodeResource(getResources(), R.drawable.mplayerv_state_rewind);
        playStatusRect = new Rect(0,0,0,0);

        seekPaint = new Paint();
        seekPaint.setColor(getResources().getColor(android.R.color.holo_blue_light));
        seekPaint.setStrokeWidth(4);
        seekPaint.setStyle(Paint.Style.FILL);
        seekPaint.setAntiAlias(true);

        /** 总progress */
        borderPaint = new Paint();
        borderPaint.setColor(getResources().getColor(android.R.color.black));
        borderPaint.setStrokeWidth(4);
        borderPaint.setStyle(Paint.Style.FILL);
        borderPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(getResources().getColor(android.R.color.white));
        borderPaint.setStrokeWidth(4);
    }

    public void show(){
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        durationRect = new RectF(0,200,1180,250);
        canvas.save();
        canvas.drawRoundRect(durationRect,15,15,seekPaint);
        canvas.restore();

        bgcSeekBar = new RectF(90,180,1190,270);
        canvas.save();
        canvas.drawRoundRect(bgcSeekBar,15,15,seekPaint);
        canvas.restore();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
//            case :
//            break;
        }
        return false;
    }

    /**
     * 获取Resource 图片资源
     * @param resources
     * @param id
     * @return
     */
     private Bitmap decodeResource(Resources resources, int id) {
    	TypedValue value = new TypedValue();
    	resources.openRawResource(id, value);
    	BitmapFactory.Options opts = new BitmapFactory.Options();
    	opts.inTargetDensity = value.density;
    	return BitmapFactory.decodeResource(resources, id, opts);
    }
}
