package com.takt;

@SuppressWarnings("WeakerAccess")
public interface IAudience {
    void heartbeat(double fps);
    void heartstop(long times,long delayDiff);
}
