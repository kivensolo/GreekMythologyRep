package com.module.views.gesture

import com.module.views.photoview.*

/**
 * 供App使用的手势事件监听器
 */
abstract class AppGestureEventListener :
    OnViewDragListener,
    OnViewTapListener,
    OnSingleFlingListener,
    OnScaleChangedListener,
    OnMatrixChangedListener {

}