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
    private long frameUpdateStartTime = 0L;
    private long frameLastTime = 0L;
    private long frameCurrentTime = 0L;
    private int framesRendered = 0;
    private int interval = 500;

    public GrapherFramesCallback() {
    }

    public void start() {
        this.choreographer.postFrameCallback(this);
    }

    public void stop() {
        this.frameUpdateStartTime = 0L;
        this.frameLastTime = 0L;
        this.frameCurrentTime = 0L;
        this.framesRendered = 0;
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
        frameCurrentTime = frameTimeNanos;
        //long diffMillis = TimeUnit.MILLISECONDS.convert(frameCurrentTime - frameLastTime, TimeUnit.NANOSECONDS);
        long currentTimeMillis = TimeUnit.NANOSECONDS.toMillis(frameTimeNanos);
        if (this.frameUpdateStartTime > 0L) {
            long timeSpan = currentTimeMillis - this.frameUpdateStartTime;
            ++this.framesRendered;
            if (timeSpan > (long) this.interval) {
                double fps = (double) (this.framesRendered * 1000) / (double) timeSpan;
                this.frameUpdateStartTime = currentTimeMillis;
                this.framesRendered = 0;
                for (IAudience audience : this.listeners) {
                    audience.heartbeat(fps);
                }
            }
        } else {
            this.frameLastTime = this.frameUpdateStartTime = currentTimeMillis;
        }

        long diffMillis = TimeUnit.NANOSECONDS.toMillis(frameCurrentTime - frameLastTime);
        if(diffMillis > 16.6f * 3){
            long droppedCount = (long) (diffMillis / 16.6);
            for (IAudience audience : this.listeners) {
                audience.heartstop(droppedCount);
            }
        }
        frameLastTime = frameCurrentTime;
        this.choreographer.postFrameCallback(this);
    }
}
