package com.ui.layoutmanager

import android.content.Context
import android.graphics.PointF
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearSmoothScroller

class CenterSmoothScroller(context: Context?) : LinearSmoothScroller(context) {

    override fun calculateDtToFit(
        viewStart: Int,
        viewEnd: Int,
        boxStart: Int,
        boxEnd: Int,
        snapPreference: Int): Int {
        return boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
    }

    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
        return 160f / displayMetrics.densityDpi
    }

    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        return super.computeScrollVectorForPosition(targetPosition)
    }

    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_END
//        return super.getVerticalSnapPreference()
    }
}