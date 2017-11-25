package com.takt;

import android.view.Choreographer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * author: King.Z <br>
 * date:  2017/11/25 16:51 <br>
 * description: To update fps when a new display frame is being rendered.
 */
@SuppressWarnings("WeakerAccess")
public class GrapherFramesCallback implements Choreographer.FrameCallback {
    private Choreographer choreographer = Choreographer.getInstance();
    private List<IAudience> listeners = new ArrayList<>();
    private long frameStartTime = 0L;
    private int framesRendered = 0;
    private int interval = 500;

    public GrapherFramesCallback() {
    }

    public void start() {
        this.choreographer.postFrameCallback(this);
    }

    public void stop() {
        this.frameStartTime = 0L;
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
        long currentTimeMillis = TimeUnit.NANOSECONDS.toMillis(frameTimeNanos);
        if (this.frameStartTime > 0L) {
            long timeSpan = currentTimeMillis - this.frameStartTime;
            ++this.framesRendered;
            if (timeSpan > (long) this.interval) {
                double fps = (double) (this.framesRendered * 1000) / (double) timeSpan;
                this.frameStartTime = currentTimeMillis;
                this.framesRendered = 0;
                for (Object audience : this.listeners) {
                    ((IAudience)audience).heartbeat(fps);
                }
            }
        } else {
            this.frameStartTime = currentTimeMillis;
        }
        this.choreographer.postFrameCallback(this);
    }
}
