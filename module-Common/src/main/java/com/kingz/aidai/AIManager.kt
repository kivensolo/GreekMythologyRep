package com.kingz.aidai

import android.content.Context
import android.graphics.Point
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.FloatRange
import com.kapplication.aitest.aidai.Direction
import com.kapplication.aitest.aidai.layout.AILayout
import com.kingz.module.common.CommonApp
import com.kingz.module.common.R
import com.yhao.floatwindow.FloatWindow
import com.yhao.floatwindow.MoveType
import com.zeke.kangaroo.utils.ScreenDisplayUtils
import com.zeke.kangaroo.utils.UIUtils
import com.zeke.kangaroo.zlog.ZLog

/**
 * author：ZekeWang
 * date：2022/5/31
 * description：
 */
object AIManager {
    private val screenWidth = ScreenDisplayUtils.getScreenWidth(CommonApp.getInstance().applicationContext)
    public const val TAG_AI_TOAST = "ai_toast"
    private const val CODE_CHANGE_IDLE = 0x110
    private const val IDLE_TIME_MS = 8 * 1000L

    private var idleHandler = Handler(Looper.getMainLooper()) { msg ->
        when (msg.what) {
            CODE_CHANGE_IDLE -> {
                setAIMagnetRatio(visibleRatio= 0.5f)
                true
            }
            else -> false
        }
    }

    /**
     * 初始化AI 并设置初始位置
     */
    fun initAI(ctx: Context, position: Point = Point()) {
        if (!isEnableAI()) {
            return
        }
        val inflateView = View.inflate(ctx, R.layout.ai_root_view, null) as AILayout
        FloatWindow
            .with(CommonApp.getInstance().applicationContext)
            .setView(inflateView)
            .setXY(position.x, position.y)
            .setMoveType(MoveType.inactive)
            .build()
    }

    /**
     * 初始化AI的气泡Toast
     */
    fun initAIToast(ctx: Context){
        if (!isEnableAI()) {
            return
        }
        val inflateView = View.inflate(ctx, R.layout.view_ai_toast_down, null)
        inflateView.findViewById<TextView>(R.id.ai_toast_text).text = "null"
        inflateView.findViewById<TextView>(R.id.ok_btn).visibility = View.GONE
        inflateView.measure(0,0) //触发初始宽高计算
        FloatWindow
            .with(CommonApp.getInstance().applicationContext)
            .setTag(TAG_AI_TOAST)
            .setView(inflateView, true)
            .build()
//        setAIToastAlpha(0f)
    }

    /**
     * 显示AI
     */
    fun showAi(position: Point? = null) {
        if (!isEnableAI()) {
            return
        }
        position?.apply {
            FloatWindow.get().updateXY(x, y, false)
        }
        FloatWindow.get().show()
    }

    /**
     * 显示AIToast
     * @param msg 文本内容
     * @param dx  相对于AI.X的偏移量
     * @param offsetY  相对于AI 组件高度的正向偏移比例  0.0为顶部，1.0为底部
     * @param msg 文本内容
     * @param delayDismiss 消失时间  -1 不消失
     */
    fun showAiToast(msg:String, dx:Int = 0, offsetY:Float = 0.6f, delayDismiss:Long = 3000L){
        if (!isEnableAI()) {
            return
        }
        val aiWindow = FloatWindow.get() ?: return
        val floatWindow = FloatWindow.get(TAG_AI_TOAST)
        ZLog.d("AIManager","ai_toast show.")
        if(floatWindow == null){
            val inflateView = View.inflate(CommonApp.getInstance().applicationContext
                , R.layout.view_ai_toast_down, null)
            inflateView.findViewById<TextView>(R.id.ai_toast_text).text = msg
            inflateView.findViewById<TextView>(R.id.ok_btn).visibility = View.GONE
            inflateView.measure(0,0) //触发初始宽高计算
            val x = aiWindow.x - inflateView.measuredWidth + FloatWindow.get().view.height/3
            val y: Int = ((aiWindow.y + aiWindow.view.height * offsetY).toInt())
            FloatWindow
                .with(CommonApp.getInstance().applicationContext)
                .setMoveType(MoveType.inactive)
                .setTag(TAG_AI_TOAST)
                .setView(inflateView, true)
                .setXY(x, y)
                .build()
            FloatWindow.get(TAG_AI_TOAST).show()
        }else{
            floatWindow.view.findViewById<TextView>(R.id.ok_btn).visibility = View.GONE
            floatWindow.view.findViewById<TextView>(R.id.ai_toast_text).text = msg
            floatWindow.view.measure(0,0)
            val x = aiWindow.x -  floatWindow.view.measuredWidth + FloatWindow.get().view.height/3
            val y: Int = ((aiWindow.y + aiWindow.view.height * offsetY).toInt())

            floatWindow.updateXY(x, y, false)
            floatWindow.show()

        }
        if(delayDismiss != -1L){
            FloatWindow.get(TAG_AI_TOAST).view.postDelayed({
                hideAIToast()
            }, delayDismiss)
        }
        startIdleDetect()
    }

    /**
     * 显示AIToast
     * @param ctx acitiviy的context 非第一次使用可以不传
     * @param msg 文本内容
     *
     * FIXME 修改AIToast初始化调用,
     * 测试场景:
     * 1. 在没有弹出一次toast时,直接弹出有按钮的toast
     */
    fun showAiToastWithButton(msg:String){
        if (!isEnableAI()) {
            return
        }
        val aiWindow = FloatWindow.get() ?: return
        val floatWindow = FloatWindow.get(TAG_AI_TOAST)
//            ?: throw IllegalStateException("AI toast is not initialized, Please call initAIToast() first!")
        ZLog.d("AIManager","ai_toast show with button.")
        if(floatWindow == null){
            val inflateView = View.inflate(CommonApp.getInstance().applicationContext
                , R.layout.view_ai_toast_down, null)
            inflateView.findViewById<TextView>(R.id.ai_toast_text).text = msg
            inflateView.findViewById<TextView>(R.id.ok_btn).apply {
                visibility = View.VISIBLE
                setOnClickListener { hideAIToast() }
            }
            inflateView.measure(0,0) //触发初始宽高计算
            val x = aiWindow.x - inflateView.measuredWidth + FloatWindow.get().view.height/3
            val y: Int = ((aiWindow.y + aiWindow.view.height * 0.6f).toInt())
            FloatWindow
                .with(CommonApp.getInstance().applicationContext)
                .setMoveType(MoveType.inactive)
                .setTag(TAG_AI_TOAST)
                .setView(inflateView, true)
                .setXY(x, y)
                .build()
            FloatWindow.get(TAG_AI_TOAST).show()
        }else{
            floatWindow.view.findViewById<TextView>(R.id.ok_btn).apply {
                visibility = View.VISIBLE
                setOnClickListener { hideAIToast() }
            }
            floatWindow.view.findViewById<TextView>(R.id.ai_toast_text).text = msg
            floatWindow.view.measure(0,0)
            val x = aiWindow.x -  floatWindow.view.measuredWidth + FloatWindow.get().view.width/3
            val y: Int = (aiWindow.y + aiWindow.view.height * 0.6).toInt()
            floatWindow.updateXY(x, y, false)
            floatWindow.show()
        }
    }

    fun hideAI() {
        if (!isEnableAI()) {
            return
        }
        FloatWindow.get()?.hide()
    }

    fun hideAIToast(){
        if (!isEnableAI()) {
            return
        }
        FloatWindow.get(TAG_AI_TOAST)?.hide()
    }

    /**
     * 根据坐标设置AI形象的位置
     * @param x viewLeft in px
     * @param y viewTop in px
     */
    fun updateAIXY(x: Int, y: Int, animation:Boolean = true) {
        if (!isEnableAI()) {
            return
        }
        idleHandler.removeCallbacksAndMessages(null)
        FloatWindow.get()?.updateXY(x, y, animation)
    }

    fun getAIRawX() = FloatWindow.get()?.x ?: 0
    fun getAIRawY() = FloatWindow.get()?.y ?: 0
    fun getAIW() = FloatWindow.get()?.view?.width ?: 0
    fun getAIH() = FloatWindow.get()?.view?.height ?: 0

    fun scale(scale: Float) {
        if (!isEnableAI()) {
            return
        }
        FloatWindow.get()?.view?.apply {
            if (scale > 1f) {
                this.layoutParams.width = (UIUtils.dip2px(326f) * scale).toInt()
                this.layoutParams.height = (UIUtils.dip2px(326f) * scale).toInt()
            } else {
                this.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                this.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            scaleX = scale
            scaleY = scale
        }
    }

    fun setAIAlpha(@FloatRange(from = 0.0, to= 1.0) alpha: Float){
        FloatWindow.get()?.view?.alpha = alpha
    }

    private fun setAIToastAlpha(@FloatRange(from = 0.0, to= 1.0) alpha: Float){
        FloatWindow.get(TAG_AI_TOAST)?.view?.alpha = alpha
    }

    /**
     * 设置AI形象在右侧的限制效果, 自动缩放其为0.75f
     * @param visibleRatio 可见尺寸的比例
     * @param y  垂直方向的像素值
     * @param animation  移动是否具有动画
     */
    fun setAIMagnetRatio(visibleRatio:Float = 0.65f,
                         y:Int = -999,
                         animation:Boolean = true){
        if (!isEnableAI()) {
            return
        }
        if(FloatWindow.get() == null){
            return
        }
        //缩小一定比例
        val view = FloatWindow.get().view
        view.scaleX = 0.75f
        view.scaleY = 0.75f

        //右侧设置为显示大部分的时候，AI必定探头出来
        if(visibleRatio == 0.65f){
            view.rotation = 315f
            setAiSee(Direction.FRONT)
        }else{
            view.rotation = 0f
        }
        //右侧吸附时，停掉悬浮
        stopFloat()

        val newY = if(y == -999){ FloatWindow.get().y }else{ y }
        updateAIXY((screenWidth - view.width * visibleRatio * view.scaleX).toInt(),
            newY,
            animation
        )

        // 内置逻辑：移动吸附后， 自动眨眼
        if(visibleRatio >= 0.65f){
            view.postDelayed({
                (view as AILayout).startBlink(duration = 400, repeat = 0)
            },800)
            startIdleDetect()
        }
    }

    private fun startIdleDetect(){
        idleHandler.removeMessages(CODE_CHANGE_IDLE)
        idleHandler.sendMessageDelayed(Message.obtain().apply {
            what = CODE_CHANGE_IDLE
        }, IDLE_TIME_MS)
    }

    /**
     * 设置AI移动模式
     */
    fun changeAIMoveMode(moveType: Int, slideLeftMargin: Int = 0, slideRightMargin: Int = 0) {
        if (!isEnableAI()) {
            return
        }
        FloatWindow.get()?.changeMoveType(moveType, slideLeftMargin, slideRightMargin)
    }

    fun attchAIToast() {}

    fun setAiSee(@Direction.DIRECTION direction: Int) {
        if (!isEnableAI()) {
            return
        }
        if(FloatWindow.get() == null){
            return
        }
        (FloatWindow.get().view as AILayout).setAiSee(direction)
    }

    /**
     * 设置AI整体旋转角度
     */
    fun setAIRotation(rotation:Float){
        if(FloatWindow.get() == null){
            return
        }
        val aiView = FloatWindow.get().view as AILayout
        if (rotation != aiView.rotation) {
            aiView.rotation = rotation
        }
    }

    /**
     * 重置AI效果，将其恢复至以下状态：
     * - 无旋转
     * - 无缩放
     * - 无透明度状态
     * - 不可垂直拖动
     */
    fun resetAI(){
        setAIRotation(0f)
        setAIAlpha(1f)
        scale(1f)
        changeAIMoveMode(MoveType.inactive)
    }

    fun startFloat(){
        (FloatWindow.get().view as AILayout).enableFloat()
    }

    fun stopFloat(){
        (FloatWindow.get().view as AILayout).enableFloat(false)
    }

    private fun isEnableAI():Boolean{
        return true
    }
}