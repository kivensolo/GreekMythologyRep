package com.nioserver;

/**
 * Handler operation interface for the optional channel
 *
 * @author ZekeWang
 * date 2021/4/26
 */
public interface ISelectableChannelHandler {
    /**
     * shutdown channel handler
     */
    void terminate();

    /**
     * notify write operation
     * @param clock timeStamp of notify.  reserved
     */
    void notifyWritable(long clock);

    /**
     * notify read operation.
     * @param clock timeStamp of notify.  reserved
     */
    void notifyReadable(long clock);
}
