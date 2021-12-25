/*
 * Copyright 2012 Lars Werkman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("ReplaceJavaStaticMethodWithKotlinAnalog")

package com.module.views.colorpicker

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import com.module.views.R
import kotlin.math.roundToInt

class HueBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : BaseBar(context, attrs, defStyle) {

    /**
     * Colors to construct the color wheel using [SweepGradient].
     * 从红色开始按逆时针方向计算
     */
    private val HUE_COLORS = intArrayOf(
        0xFFFF0000.toInt(), //0° 红色
        0xFFFFFF00.toInt(), //60° 黄色(补)
        0xFF00FF00.toInt(), //120° 绿色
        0xFF00FFFF.toInt(), //180° 青色(补)
        0xFF0000FF.toInt(), //240° 蓝色
        0xFFFF00FF.toInt(), //300° 紫色(补)
        0xFFFF0000.toInt()  //360° 红色
    )

    /**
     * Factor used to calculate the position to the hue on the bar.
     */
    private var mPosToHueFactor = 0f

    /**
     * Factor used to calculate the hue to the postion on the bar.
     */
    private var mHueToPosFactor = 0f
    /**
     * Interface and listener so that changes in HueBar are sent
     * to the host activity/fragment
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var onHueChangedListener: OnHueChangedListener? = null

    /**
     * Hue of the latest entry of the onSaturationChangedListener.
     */
    private var oldChangedListenerHue = 0

    interface OnHueChangedListener {
        fun onHueChanged(hue: Int)
    }

    init {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.ColorBars, defStyle, 0
        )
        val b = context.resources
        mBarThickness = a.getDimensionPixelSize(
            R.styleable.ColorBars_bar_thickness,
            b.getDimensionPixelSize(R.dimen.bar_thickness)
        )
        mBarLength = a.getDimensionPixelSize(
            R.styleable.ColorBars_bar_length,
            b.getDimensionPixelSize(R.dimen.bar_length)
        )
        mPreferredBarLength = mBarLength
        mBarPointerRadius = a.getDimensionPixelSize(
            R.styleable.ColorBars_bar_pointer_radius,
            b.getDimensionPixelSize(R.dimen.bar_pointer_radius)
        )
        mBarPointerHaloRadius = a.getDimensionPixelSize(
            R.styleable.ColorBars_bar_pointer_halo_radius,
            b.getDimensionPixelSize(R.dimen.bar_pointer_halo_radius)
        )
        mOrientation = a.getBoolean(
            R.styleable.ColorBars_bar_orientation_horizontal, ORIENTATION_DEFAULT
        )
        a.recycle()
        mBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBarPaint.shader = shader
        mBarPointerPosition = mBarLength + mBarPointerHaloRadius
        mBarPointerHaloPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBarPointerHaloPaint.color = Color.BLACK
        mBarPointerHaloPaint.alpha = 0x50
        mBarPointerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBarPointerPaint.color = 0xff81ff00.toInt()
        updateHPFactory()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val intrinsicSize = (mPreferredBarLength
                + mBarPointerHaloRadius * 2)

        // Variable orientation
        val measureSpec: Int = if (mOrientation == ORIENTATION_HORIZONTAL) {
            widthMeasureSpec
        } else {
            heightMeasureSpec
        }
        val lengthMode = MeasureSpec.getMode(measureSpec)
        val lengthSize = MeasureSpec.getSize(measureSpec)
        val length: Int
        length = if (lengthMode == MeasureSpec.EXACTLY) {
            lengthSize
        } else if (lengthMode == MeasureSpec.AT_MOST) {
            Math.min(intrinsicSize, lengthSize)
        } else {
            intrinsicSize
        }
        val barPointerHaloRadiusx2 = mBarPointerHaloRadius * 2
        mBarLength = length - barPointerHaloRadiusx2
        if (mOrientation == ORIENTATION_VERTICAL) {
            setMeasuredDimension(
                barPointerHaloRadiusx2,
                mBarLength + barPointerHaloRadiusx2
            )
        } else {
            setMeasuredDimension(
                mBarLength + barPointerHaloRadiusx2,
                barPointerHaloRadiusx2
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Fill the rectangle instance based on orientation
        val x1: Int
        val y1: Int
        if (mOrientation == ORIENTATION_HORIZONTAL) {
            x1 = mBarLength + mBarPointerHaloRadius
            y1 = mBarThickness
            mBarLength = w - mBarPointerHaloRadius * 2
            mBarRect[mBarPointerHaloRadius.toFloat(), (mBarPointerHaloRadius - mBarThickness / 2).toFloat(), (mBarLength + mBarPointerHaloRadius).toFloat()] =
                (mBarPointerHaloRadius + mBarThickness / 2).toFloat()
        } else {
            x1 = mBarThickness
            y1 = mBarLength + mBarPointerHaloRadius
            mBarLength = h - mBarPointerHaloRadius * 2
            mBarRect[(mBarPointerHaloRadius - mBarThickness / 2).toFloat(), mBarPointerHaloRadius.toFloat(), (mBarPointerHaloRadius + mBarThickness / 2).toFloat()] =
                (mBarLength + mBarPointerHaloRadius).toFloat()
        }

        // Update variables that depend of mBarLength.
        if (!isInEditMode) {
            shader = LinearGradient(
                mBarPointerHaloRadius.toFloat(), 0f,
                x1.toFloat(), y1.toFloat(),
                HUE_COLORS, null,
                Shader.TileMode.CLAMP
            )
        } else {
            shader = LinearGradient(
                mBarPointerHaloRadius.toFloat(), 0f,
                x1.toFloat(), y1.toFloat(),
                HUE_COLORS, null,
                Shader.TileMode.CLAMP
            )
            Color.colorToHSV(0x81ff00, mHSVColorArray)
        }
        mBarPaint.shader = shader
        updateHPFactory()
        val hsvColor = FloatArray(3)
        Color.colorToHSV(mColor, hsvColor)
        mBarPointerPosition = if (!isInEditMode) {
            Math.round(mHueToPosFactor * hsvColor[0] + mBarPointerHaloRadius)
        } else {
            mBarLength + mBarPointerHaloRadius
        }
    }

    /**
     * 更新UI和值转化的变化因子
     */
    private fun updateHPFactory(){
        mPosToHueFactor = 360 / mBarLength.toFloat()
        mHueToPosFactor = mBarLength.toFloat() / 360
    }

    override fun onDraw(canvas: Canvas) {
        // Draw the bar.
        canvas.drawRect(mBarRect, mBarPaint)

        // Calculate the center of the pointer.
        val cX: Int
        val cY: Int
        if (mOrientation) {
            cX = mBarPointerPosition
            cY = mBarPointerHaloRadius
        } else {
            cX = mBarPointerHaloRadius
            cY = mBarPointerPosition
        }

        // Draw the pointer halo.
        canvas.drawCircle(
            cX.toFloat(),
            cY.toFloat(),
            mBarPointerHaloRadius.toFloat(),
            mBarPointerHaloPaint
        )
        // Draw the pointer.
        canvas.drawCircle(cX.toFloat(), cY.toFloat(), mBarPointerRadius.toFloat(), mBarPointerPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)

        // Convert coordinates to our internal coordinate system
        val dimen: Float = if (ORIENTATION_HORIZONTAL) {
            event.x
        } else {
            event.y
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mIsMovingPointer = true
                // Check whether the user pressed on (or near) the pointer
                if (dimen >= mBarPointerHaloRadius
                    && dimen <= mBarPointerHaloRadius + mBarLength
                ) {
                    mBarPointerPosition = Math.round(dimen)
                    calculateColor(Math.round(dimen))
                    mBarPointerPaint.color = mColor
                    invalidate()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (mIsMovingPointer) {
                    // Move the the pointer on the bar.
                    if (dimen >= mBarPointerHaloRadius
                        && dimen <= mBarPointerHaloRadius + mBarLength
                    ) {
                        mBarPointerPosition = Math.round(dimen)
                        calculateColor(Math.round(dimen))
                        mBarPointerPaint.color = mColor
                        setPickerColor()
                        invalidate()
                    } else if (dimen < mBarPointerHaloRadius) {
                        mBarPointerPosition = mBarPointerHaloRadius
                        mColor = Color.RED
                        mBarPointerPaint.color = mColor
                        setPickerColor()
                        invalidate()
                    } else if (dimen > mBarPointerHaloRadius + mBarLength) {
                        mBarPointerPosition = mBarPointerHaloRadius + mBarLength
                        mColor = Color.HSVToColor(mHSVColorArray)
                        mBarPointerPaint.color = mColor
                        setPickerColor()
                        invalidate()
                    }
                }
                if (onHueChangedListener != null && oldChangedListenerHue != mColor) {
                    onHueChangedListener!!.onHueChanged(mColor)
                    oldChangedListenerHue = mColor
                }
            }
            MotionEvent.ACTION_UP -> mIsMovingPointer = false
        }
        return true
    }

    /**
     * Set the pointer on the bar. With the opacity value.
     *
     * @param hue float between 0 and 360
     */
    fun setHue(hue: Float) {
        mHSVColorArray[0] = hue
        mBarPointerPosition = ((mHueToPosFactor * hue).roundToInt() + mBarPointerHaloRadius)
        calculateColor(mBarPointerPosition)
        mBarPointerPaint.color = mColor
        invalidate()
    }

    /**
     * Calculate the color selected by the pointer on the bar.
     *
     * @param coordinate Coordinate of the pointer.
     */
    private fun calculateColor(coordinate: Int) {
        var coord = coordinate
        coord -= mBarPointerHaloRadius
        if (coord < 0) {
            coord = 0
        } else if (coord > mBarLength) {
            coord = mBarLength
        }
        mColor = Color.HSVToColor(
            floatArrayOf(
                mPosToHueFactor * coord,
                mHSVColorArray[1],
                mHSVColorArray[2]
            )
        )
    }
    /**
     * Get the currently selected color.
     *
     * @return The ARGB value of the currently selected color.
     *
     * Set the bar color. <br></br>
     * <br></br>
     * Its discouraged to use this method.
     *
     * @param color The RGB value of color.
     */
    var color: Int
        get() = mColor
        set(color) {
            val x1: Int
            val y1: Int
            if (mOrientation == ORIENTATION_HORIZONTAL) {
                x1 = mBarLength + mBarPointerHaloRadius
                y1 = mBarThickness
            } else {
                x1 = mBarThickness
                y1 = mBarLength + mBarPointerHaloRadius
            }
            Color.colorToHSV(color, mHSVColorArray)
            shader = LinearGradient(
                mBarPointerHaloRadius.toFloat(), 0f,
                x1.toFloat(), y1.toFloat(), HUE_COLORS, null,
                Shader.TileMode.CLAMP
            )
            mBarPaint.shader = shader
            calculateColor(mBarPointerPosition)
            mBarPointerPaint.color = mColor
            setPickerColor()
            invalidate()
        }

    /**
     * 更新Picker颜色
     */
    private fun setPickerColor() {
        mPicker?.setColor(mColor,true)
    }

    /**
     * Adds a `ColorPicker` instance to the bar. <br></br>
     * <br></br>
     * WARNING: Don't change the color picker. it is done already when the bar
     * is added to the ColorPicker
     *
     * @see
     * @param picker
     */
    fun setColorPicker(picker: ColorPicker) {
        mPicker = picker
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val state = Bundle()
        state.putParcelable(STATE_PARENT, superState)
        state.putFloatArray(STATE_COLOR, mHSVColorArray)
        val hsvColor = FloatArray(3)
        Color.colorToHSV(mColor, hsvColor)
        state.putFloat(STATE_HUE, hsvColor[0])
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as Bundle
        val superState = savedState.getParcelable<Parcelable>(STATE_PARENT)
        super.onRestoreInstanceState(superState)
        color = Color.HSVToColor(savedState.getFloatArray(STATE_COLOR))
        setHue(savedState.getFloat(STATE_HUE))
    }
}