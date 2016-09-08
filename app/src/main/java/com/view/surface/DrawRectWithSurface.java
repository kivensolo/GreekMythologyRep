package com.view.surface;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import com.view.ViewsActivity;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/9/8 13:53 <br>
 * description: SurfaceView测试类 <br>
 */
public class DrawRectWithSurface extends ViewsActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = new LinearLayout(this);
        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(-1, -1);
        lps.setLayoutDirection(LinearLayout.VERTICAL);
        root.setLayoutParams(lps);
        //setContentView(new LinearLayout(this));
        setContentView(new SampleView(this));
    }

    private static class SampleView extends SurfaceView implements SurfaceHolder.Callback {

        private SurfaceHolder holder;
        private DrawThread thread;

        public SampleView(Context context) {
            super(context);
            setFocusable(true);
            holder = getHolder();
            holder.addCallback(this);
            thread = new DrawThread(holder);
        }

        public SampleView(Context context, AttributeSet attrs) {
            super(context, attrs);
            setFocusable(true);
        }

        public SampleView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            setFocusable(true);

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            thread.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            thread.isRun = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    static class DrawThread extends Thread {
        private final SurfaceHolder holder;
        public boolean isRun;

        public DrawThread(SurfaceHolder holder) {
            this.holder = holder;
        }

        @Override
        public void run() {
            super.run();
            isRun = true;
            int count = 0;
            while (isRun) {
                Canvas canvas;
                synchronized (holder) {
                    canvas = holder.lockCanvas();//锁定画布，锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了。
                    canvas.drawColor(0xFFA08BE8);
                    Paint p = new Paint();
                    p.setColor(0xFF5FCDDA);
                    Rect r = new Rect(100, 50, 500, 200);
                    canvas.drawRect(r, p);
                    p.setColor(0xFFf4a460);
                    p.setTextSize(32);
                    canvas.drawText("这是第" + (count++) + "秒", 100, 150, p);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        holder.unlockCanvasAndPost(canvas);//结束锁定画图，并提交改变
                    }
                }
                if(count==6){
                    isRun = false;
                }
            }
        }
    }

}
