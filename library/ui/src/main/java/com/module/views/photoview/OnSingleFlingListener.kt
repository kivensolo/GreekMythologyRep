package com.module.views.photoview

import android.view.MotionEvent

/**
 * A callback to be invoked when the target is flung with a single
 * touch.
 */
interface OnSingleFlingListener {
    /**
     * A callback to receive where the user flings on a targetView. You will receive a callback if
     * the user flings anywhere on the view.
     *
     * @param e1        MotionEvent the user first touch.
     * @param e2        MotionEvent the user last touch.
     * @param velocityX distance of user's horizontal fling.
     * @param velocityY distance of user's vertical fling.
     */
    fun onFling(e1: MotionEvent, e2: MotionEvent,
                velocityX: Float, velocityY: Float): Boolean

    /**
     * A callback to receive where the user flings on a targetView. You will receive a callback if
     * the user flings anywhere on the view.
     *
     * @param startX    x position of start touch.
     * @param startY    y position of start touch.
     * @param velocityX distance of user's horizontal fling.
     * @param velocityY distance of user's vertical fling.
     */
    fun onFling(startX: Float, startY: Float,
                velocityX: Float,velocityY: Float)
}