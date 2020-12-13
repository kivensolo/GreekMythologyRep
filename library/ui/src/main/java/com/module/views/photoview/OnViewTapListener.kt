package com.module.views.photoview

import android.view.View

/**
 * Interface definition for a callback to be invoked when the target
 * is experiencing a tap event.
 */
interface OnViewTapListener {
    /**
     * A callback to receive where the user taps on a target view. You will receive a callback if
     * the user taps anywhere on the view, tapping on 'whitespace' will not be ignored.
     *
     * @param view - View the user tapped.
     * @param x    - where the user tapped from the left of the View.
     * @param y    - where the user tapped from the top of the View.
     */
    fun onViewTap(view: View?, x: Float, y: Float)
}