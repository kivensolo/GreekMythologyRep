package com.module.views.colorpicker

import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View

/**
 * author：ZekeWang
 * date：2021/12/24
 * description：与Picker强关联的Bar抽象类
 */
abstract class BaseBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object{
        /**
         * Constants used to save/restore the instance state.
         */
        const val STATE_PARENT = "parent"
        const val STATE_COLOR = "color"
        const val STATE_OPACITY = "opacity"
        const val STATE_HUE = "hue"
        const val STATE_VALUE = "value"
        const val STATE_SATURATION = "saturation"
        const val STATE_ORIENTATION = "orientation"

        /**
         * Constants used to identify orientation.
         */
        const val ORIENTATION_HORIZONTAL = true
        const val ORIENTATION_VERTICAL = false

        /**
         * Default orientation of the bar.
         */
        const val ORIENTATION_DEFAULT = ORIENTATION_HORIZONTAL
    }

    /**
     * Bar的厚度
     */
    @JvmField
    protected var mBarThickness = 0

    /**
     * Bar的长度
     */
    @JvmField
    protected var mBarLength = 0
    @JvmField
    protected var mPreferredBarLength = 0

    /**
     * Pointer半径尺寸.
     */
    @JvmField
    protected var mBarPointerRadius = 0

    /**
     * Pointer光晕的半径。
     */
    @JvmField
    protected var mBarPointerHaloRadius = 0

    /**
     * The position of the pointer on the bar.
     */
    @JvmField
    protected var mBarPointerPosition = 0

    /**
     * `Paint` instance used to draw the bar.
     */
    protected lateinit var mBarPaint: Paint

    /**
     * `Paint` instance used to draw the pointer.
     */
    protected lateinit var  mBarPointerPaint: Paint

    /**
     * `Paint` instance used to draw the halo of the pointer.
     */
    protected lateinit var  mBarPointerHaloPaint: Paint

    /**
     * The rectangle enclosing the bar.
     */
    @JvmField
    protected var mBarRect = RectF()

    /**
     * `Shader` instance used to fill the shader of the paint.
     */
    @JvmField
    protected var shader: Shader? = null


    /**
     * `true` if the user clicked on the pointer to start the move mode. <br></br>
     * `false` once the user stops touching the screen.
     *
     * @see .onTouchEvent
     */
    @JvmField
    protected var mIsMovingPointer = false


    /**
     * The ARGB value of the currently selected color.
     */
    @JvmField
    protected var mColor = 0

    /**
     * An array of floats that can be build into a `Color` <br></br>
     * Where we can extract the color from.
     */
    @JvmField
    protected var mHSVColorArray = FloatArray(3)

    /**
     * `ColorPicker` instance used to control the ColorPicker.
     */
    @JvmField
    protected var mPicker: ColorPicker? = null

    /**
     * Used to toggle orientation between vertical and horizontal.
     */
    @JvmField
    protected var mOrientation = ORIENTATION_DEFAULT
}