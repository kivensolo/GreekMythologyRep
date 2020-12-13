package com.module.views.gesture

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.VelocityTracker
import android.view.ViewConfiguration
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * 一个自定义的手册检测器，想做一个包含所有手势处理的类
 * 但目前就是CustomGestureDetector的kotlin版本
 */
class AppGestureManager(
    context: Context, listener: AppGestureEventListener
) {

    companion object {
        private const val INVALID_POINTER_ID = -1
    }

    /**
     * View scaled detector.
     */
    private var mDetector: ScaleGestureDetector? = null

    private var density = 0f

    /**
     * The identifier id of pointer.
     */
    private var mActivePointerId = INVALID_POINTER_ID
    /**
     * Active pointer identifier index.
     */
    private var mActivePointerIndex = 0

    /**
     * The speed of scrolling.
     * The faster the speed, the greater the value.
     * The default value is 1
     */
    private var mScrollRatio = 1
    private var mTouchSlop = 0f
    private var mMinimumVelocity = 0f

    private var mIsDragging = false
    private var mLastTouchX = 0f
    private var mLastTouchY = 0f

    private var mListener: AppGestureEventListener? = null
    private var viewConfiguration: ViewConfiguration = ViewConfiguration.get(context)
    private var mVelocityTracker: VelocityTracker? = null

    init {
        mMinimumVelocity = viewConfiguration.scaledMinimumFlingVelocity.toFloat()
        mTouchSlop = viewConfiguration.scaledTouchSlop.toFloat()
        mDetector = ScaleGestureDetector(context, ScaleGestureLsr())
        mListener = listener
        density = context.resources.displayMetrics.density
    }


    private fun getTouchSlop(): Int {
        return viewConfiguration.scaledTouchSlop
    }

    fun onTouchEvent(ev: MotionEvent): Boolean {
        return try {
            mDetector!!.onTouchEvent(ev)
            processTouchEvent(ev)
        } catch (e: IllegalArgumentException) { // Fix for support lib bug, happening when onDestroy is called
            true
        }
    }


    private fun getActiveX(ev: MotionEvent): Float {
        return try {
            ev.getX(mActivePointerIndex)
        } catch (e: Exception) {
            ev.x
        }
    }

    private fun getActiveY(ev: MotionEvent): Float {
        return try {
            ev.getY(mActivePointerIndex)
        } catch (e: Exception) {
            ev.y
        }
    }

    /**
     * Deal touch event.
     */
    private fun processTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                mActivePointerId = ev.getPointerId(0)
                mVelocityTracker = VelocityTracker.obtain()
                mVelocityTracker?.addMovement(ev)
                mLastTouchX = getActiveX(ev)
                mLastTouchY = getActiveY(ev)
                mIsDragging = false
            }
            MotionEvent.ACTION_MOVE -> {
                val x: Float = getActiveX(ev)
                val y: Float = getActiveY(ev)
                val dx: Float = x - mLastTouchX
                val dy: Float = y - mLastTouchY
                if (!mIsDragging) {
                    // Use Pythagoras to see if drag length is larger than touch slop
                    mIsDragging = sqrt(dx * dx + (dy * dy).toDouble()) >= mTouchSlop
                }
                if (mIsDragging) {
                    mListener?.onDrag(dx, dy)
                    mLastTouchX = x
                    mLastTouchY = y
                    mVelocityTracker?.addMovement(ev)
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                mActivePointerId = INVALID_POINTER_ID
                recyclerVelocityTracker()
            }
            MotionEvent.ACTION_UP -> {
                mActivePointerId = INVALID_POINTER_ID
                if (mIsDragging) {
                        mLastTouchX = getActiveX(ev)
                        mLastTouchY = getActiveY(ev)
                        // Compute velocity within the last 1000ms
                        mVelocityTracker?.addMovement(ev)
                        mVelocityTracker?.computeCurrentVelocity(1000)
                        val vX: Float = mVelocityTracker?.xVelocity ?:0F
                        val vY: Float = mVelocityTracker?.yVelocity ?:0F
                        // If the velocity is greater than minVelocity, call listener
                        if (Math.max(abs(vX), abs(vY)) >= mMinimumVelocity) {
                            mListener?.onFling(mLastTouchX, mLastTouchY, -vX,-vY)
                        }
                }
                recyclerVelocityTracker()
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = getPointerIndex(ev.action)
                val pointerId = ev.getPointerId(pointerIndex)
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up.
                    // Choose a new active pointer and adjust accordingly.
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    mActivePointerId = ev.getPointerId(newPointerIndex)
                    mLastTouchX = ev.getX(newPointerIndex)
                    mLastTouchY = ev.getY(newPointerIndex)
                }
            }
        }
        mActivePointerIndex = ev.findPointerIndex(
            if (mActivePointerId != INVALID_POINTER_ID) mActivePointerId else 0)
        return true
    }

    /**
     * Recycle Velocity Tracker
     */
    private fun recyclerVelocityTracker() {
        mVelocityTracker?.recycle()
        mVelocityTracker = null
    }

    private fun getPointerIndex(action: Int): Int {
        //(action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        return action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
    }

    inner class ScaleGestureLsr : ScaleGestureDetector.OnScaleGestureListener {

        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            val scaleFactor = detector!!.scaleFactor

            if (java.lang.Float.isNaN(scaleFactor) || java.lang.Float.isInfinite(scaleFactor)) return false

            if (scaleFactor >= 0) {
                mListener?.onScaleChange(scaleFactor, detector.focusX, detector.focusY)
            }
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            // NO-OP
        }
    }
}