package com.takt;

import android.view.Choreographer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * author: King.Z <br>
 * date:  2017/11/25 16:51 <br>
 * description:
 *      To update fps when a new display frame is being rendered
 *  and determine the number of frames dropped.
 */
@SuppressWarnings("WeakerAccess")
public class GrapherFramesCallback implements Choreographer.FrameCallback {
    private Choreographer choreographer = Choreographer.getInstance();
    private List<IAudience> listeners = new ArrayList<>();
    private long mFrameUpdateStartTime_ms = 0L;
    private long mFrameLastTime_ns = 0L;
    private long mFrameCurrentTime = 0L;
    private int mFramesRendered = 0;
    private int interval = 500;

    public GrapherFramesCallback() {
    }

    public void start() {
        this.choreographer.postFrameCallback(this);
    }

    public void stop() {
        this.mFrameUpdateStartTime_ms = 0L;
        this.mFrameLastTime_ns = 0L;
        this.mFrameCurrentTime = 0L;
        this.mFramesRendered = 0;
        this.choreographer.removeFrameCallback(this);
    }

    public void addListener(IAudience l) {
        this.listeners.add(l);
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        mFrameCurrentTime = frameTimeNanos;
        //long diffMillis = TimeUnit.MILLISECONDS.convert(mFrameCurrentTime - mFrameLastTime_ns, TimeUnit.NANOSECONDS);
        long currentTimeMillis = TimeUnit.NANOSECONDS.toMillis(frameTimeNanos);
        if(mFrameUpdateStartTime_ms == 0){
             mFrameUpdateStartTime_ms = currentTimeMillis;
        }
        if(mFrameLastTime_ns == 0){
            mFrameLastTime_ns = frameTimeNanos;
        }
        if (mFrameUpdateStartTime_ms > 0L) {
            long timeSpan = currentTimeMillis - mFrameUpdateStartTime_ms;
            ++this.mFramesRendered;
            if (timeSpan > (long) this.interval) {
                double fps = (double) (this.mFramesRendered * 1000) / (double) timeSpan;
                this.mFrameUpdateStartTime_ms = currentTimeMillis;
                this.mFramesRendered = 0;
                for (IAudience audience : this.listeners) {
                    audience.heartbeat(fps);
                }
            }
        }
        long diffMillis = TimeUnit.NANOSECONDS.toMillis(mFrameCurrentTime - mFrameLastTime_ns);
        if((float)diffMillis > 16.6f * 2){
            long droppedCount = (int) (diffMillis / 16.6);
            //丢帧值
            notifyHeartStop(droppedCount,diffMillis);
        }else {
            //瞬时值
            notifyHeartStop(0,diffMillis);
        }
        mFrameLastTime_ns = mFrameCurrentTime;
        this.choreographer.postFrameCallback(this);
    }

    private void notifyHeartStop(long times,long diffMillis) {
        for (IAudience audience : this.listeners) {
            audience.heartstop(times,diffMillis);
        }
    }
}
