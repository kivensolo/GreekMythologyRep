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
public class MetronomeCallback implements Choreographer.FrameCallback {
    private Choreographer choreographer;
    private List<IAudience> listeners = new ArrayList<>();

    private long frameStartTime = 0;
    private int framesRendered = 0;

    /**
     * FPS ui update period(ms)
     */
    private int interval = 500;

    /**
     * frame detecting edges
     * The default standard limit is 5 times
     */
    private int upperLimit = 5;
    private double dropLimit = 16.6 * upperLimit;

    public MetronomeCallback() {
        choreographer = Choreographer.getInstance();
    }

    public void start() {
        choreographer.postFrameCallback(this);
    }

    public void stop() {
        frameStartTime = 0;
        framesRendered = 0;
        choreographer.removeFrameCallback(this);
    }

    public void addListener(IAudience l) {
        listeners.add(l);
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @Override public void doFrame(long frameTimeNanos) {
        long currentTimeMillis = TimeUnit.NANOSECONDS.toMillis(frameTimeNanos);
        if(frameStartTime > 0){
            // take the span in milliseconds
            final long timeSpan = currentTimeMillis - frameStartTime;
            framesRendered++;
            calculateFPS(currentTimeMillis, timeSpan);
            calculateFrameDrop(timeSpan);

        }else{
            frameStartTime = currentTimeMillis;
        }

        //set next frame callback listener.
        choreographer.postFrameCallback(this);
    }

    private void calculateFrameDrop(long timeSpan) {
        if((float)timeSpan > dropLimit){
            long droppedCount = (int) (timeSpan / upperLimit);
            notifyHeartStop(droppedCount,timeSpan);
        }else {
            notifyHeartStop(0,timeSpan);
        }
    }

    /**
     * Calculate fps
     */
    private void calculateFPS(long currentTimeMillis, long timeSpan) {
        if (timeSpan > interval) {
            final double fps = framesRendered * 1000 / (double) timeSpan;
            frameStartTime = currentTimeMillis;
            framesRendered = 0;
            for (IAudience audience : listeners) {
                audience.heartbeat(fps);
            }
        }
    }

    private void notifyHeartStop(final long times, final long diffMillis) {
        for (IAudience audience : listeners) {
            audience.heartstop(times,diffMillis);
        }
    }

}
