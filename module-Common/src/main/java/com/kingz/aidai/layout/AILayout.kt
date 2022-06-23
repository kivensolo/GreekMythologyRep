package com.kapplication.aitest.aidai.layout

import android.content.Context
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.kapplication.aitest.aidai.Direction
import com.kapplication.aitest.aidai.component.*
import com.kingz.module.common.R
import com.zeke.kangaroo.zlog.ZLog

/**
 * author：ZekeWang
 * date：2022/05/06
 */
class AILayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var rectBound: RectF? = null

    private var eyesView: AiLayer1EyesView? = null
    private var headView: AILayer3HeadView? = null
    private var faceView: AiLayer2FaceView? = null
    private var bottomBlurView: AILayer5BottomBlurView? = null
    private var bottomLightView: AILayer4BottomLightView? = null
    private val MSG_CODE_START_FLOAT = 1
    private var mHandler = Handler(Looper.getMainLooper()){ msg ->
        if (msg.what == MSG_CODE_START_FLOAT) {
            enableFloat()
            true
        }
        false
    }
    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        // 初始化各组件尺寸范围
        initRects()
    }

    private fun initRects() {
        rectBound = RectF(0f, 0f, width.toFloat(), height.toFloat())
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        eyesView = findViewById(R.id.aiEyesView)
        headView = findViewById(R.id.aiHeadView)
        faceView = findViewById(R.id.aiFaceView)
        bottomBlurView = findViewById(R.id.aiBottomBlureView)
        bottomLightView = findViewById(R.id.aiBottomLightView)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var mWidth = 0
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize
        }

        var mHeight = 0
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize
        }

        if(mWidth != 0 && mHeight != 0){
            setMeasuredDimension(mWidth, mHeight)
        }

//        ZLog.d(TAG,"onMeasure.")
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(event.action == MotionEvent.ACTION_UP ){
            startBlink()
        }
        return super.onTouchEvent(event)
    }

    /**
     * 开启/关闭 飘荡效果
     * 关闭不是立刻的
     */
    fun enableFloat(enable:Boolean = true, immediately:Boolean = true) {
        ZLog.d("AILayout","enableFloat enable:$enable, immediately=$immediately ")
        if(immediately && !enable){
            mHandler.removeMessages(MSG_CODE_START_FLOAT)
        }
        eyesView?.enableFloat(enable)
        faceView?.enableFloat(enable)
        headView?.enableFloat(enable)
        bottomLightView?.enableFloat(enable)
        bottomBlurView?.enableFloat(enable)
    }

    fun startBlink(duration:Long = 1000, repeat:Int = 1){
        eyesView?.startBlinkEye(duration, repeat)
    }

    /**
     * 设置AI看向的问题
     */
    fun setAiSee(@Direction.DIRECTION direction:Int){
        //相同方向，则不进行转头动画
        if(direction == bottomLightView?.currentDirection){
            eyesView?.startBlinkEye(repeat = 0)
            return
        }
        var dicCode = direction
        if(dicCode != Direction.RIGHT){
            //目前除了正向和右侧，其他还未调试
            dicCode = Direction.FRONT

            //正向2s后，启动浮动
            mHandler.sendEmptyMessageDelayed(MSG_CODE_START_FLOAT,2000)
        }else{
            //其他情况，关闭悬浮效果
            enableFloat(false)
        }
        bottomLightView?.setAiSee(dicCode)
        bottomBlurView?.setAiSee(dicCode)
        headView?.setAiSee(dicCode)
        faceView?.setAiSee(dicCode)
        eyesView?.setAiSee(dicCode)
    }

}