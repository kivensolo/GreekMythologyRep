package com.module.views.recycler


import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.Scroller
import androidx.recyclerview.widget.LinearLayoutManager
import kotlin.math.abs


/**
 * 侧滑菜单栏RecyclerView，模仿QQ聊天列表交互，
 * 即触碰已打开菜单栏以外的itemView时关闭菜单栏。
 *
 * 功能特点：
 * 快速左滑或者将itemView侧滑至菜单栏显示过半则打开菜单栏；
 * 快速右滑或者将itemView侧滑至菜单栏显示过未半则关闭菜单栏；
 * 点击菜单栏按钮或点击其他itemView，关闭菜单栏；
 * 竖直滑动RecyclerView，关闭菜单栏；
 * 打开其他itemView的菜单栏，关闭之前itemView的菜单栏；
 * 松手后的菜单栏滑动平缓
 * 不影响原先RecyclerView的功能
 */
open class TxSlideRecyclerView: BoundaryAwareNetScrollableRecyclerView {

    companion object {
        /**最小速度 */
        private const val MINIMUM_VELOCITY = 500
        /**ItemView的子View个数，默认2个，第2个为菜单View*/
        private const val ITEM_VIEW_CHILD_COUNTS = 2
    }

    /**滑动的itemView */
    private var mMoveView: ViewGroup? = null

    /**itemView中菜单控件宽度 */
    private var mMenuWidth = 0
    private var mVelocity: VelocityTracker? = null

    /**触碰时的首个横坐标 */
    private var mFirstX = 0

    /**触碰时的首个纵坐标 */
    private var mFirstY = 0

    /**触碰末次的横坐标 */
    private var mLastX = 0

    /**最小滑动距离 */
    private var mTouchSlop = 0

    private lateinit var mScroller: Scroller

    /**是否正在水平滑动 */
    private var mMoving = false

    /**是否由onInterceptTouchEvent（）方法拦截 */
    private var mIntercepted = false


    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!, attrs, defStyle
    ) {
        init()
    }

    private fun init() {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mScroller = Scroller(context)
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        val x = e.x.toInt()
        val y = e.y.toInt()
        addVelocityEvent(e)
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                //若Scroller处于动画中，则终止动画
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
                mFirstX = x
                mFirstY = y
                mLastX = x
                //获取点击区域所在的itemView
                val view = findChildViewUnder(x.toFloat(), y.toFloat()) as ViewGroup?
                //在点击区域以外的itemView开着菜单，则关闭菜单并拦截该次触碰事件
                if (mMoveView != null && view != mMoveView && mMoveView!!.scrollX != 0) {
                    closeMenu()
                    mIntercepted = true
                    return true
                }
                mMoveView = view
                mMoveView?.apply {
                    //获取itemView中菜单的宽度（规定itemView中为两个子View）
                    mMenuWidth = if(childCount == ITEM_VIEW_CHILD_COUNTS){
                        getChildAt(1).width
                    }else{
                        -1
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                mVelocity?.computeCurrentVelocity(1000)
                val velocityX = abs(mVelocity!!.xVelocity.toDouble()).toInt()
                val velocityY = abs(mVelocity!!.yVelocity.toDouble()).toInt()
                val moveX = abs((x - mFirstX).toDouble()).toInt()
                val moveY = abs((y - mFirstY).toDouble()).toInt()
                //满足如下条件其一则判定为水平滑动：
                //1、水平速度大于竖直速度,且水平速度大于最小速度
                //2、水平位移大于竖直位移,且大于最小移动距离
                //必需条件：itemView菜单栏宽度大于0，且recyclerView处于静止状态（即并不在竖直滑动）
                val isHorizontalMove = (abs(
                    velocityX.toDouble()
                ) >= MINIMUM_VELOCITY && velocityX > velocityY || moveX > moveY && moveX > mTouchSlop) && mMenuWidth > 0 && scrollState == 0
                if (isHorizontalMove) {
                    mIntercepted = true
                    return true
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                releaseVelocity()
                //itemView以及其子view触发点击事件，菜单未关闭则直接关闭
                closeMenuNow()
            }

            else -> {}
        }
        return super.onInterceptTouchEvent(e)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val x = e.x.toInt()
        val y = e.y.toInt()
        addVelocityEvent(e)
        when (e.action) {
            MotionEvent.ACTION_DOWN ->
                if (mIntercepted) {
                    mIntercepted = false
//                    //若是通过onInterceptTouchEvent（）方法ACTION_DOWN拦截而来的，则丢弃此次事件
//                    return false
                    // [修改]: 不丢弃，防止滑动冲突引发无法滚动的问题
                    return super.onTouchEvent(e)
                }

            MotionEvent.ACTION_MOVE -> {
                mVelocity?.computeCurrentVelocity(1000)
                val velocityX = abs(mVelocity!!.xVelocity.toDouble()).toInt()
                val velocityY = abs(mVelocity!!.yVelocity.toDouble()).toInt()
                val moveX = abs((x - mFirstX).toDouble()).toInt()
                val moveY = abs((y - mFirstY).toDouble()).toInt()
                //若为onInterceptTouchEvent()方法拦截而来或者已处于水平滑动状态，则让itemView跟随手指滑动
                //或根据水平滑动条件判断，是否让itemView跟随手指滑动
                // （这里重新判断是避免itemView中不拦截ACTION_DOWN事件，则后续ACTION_MOVE并不会走若为onInterceptTouchEvent（）方法）
                val isHorizontalMove = mIntercepted || mMoving || (abs(
                    velocityX.toDouble()
                ) >= MINIMUM_VELOCITY && velocityX > velocityY || moveX > moveY && moveX > mTouchSlop) && mMenuWidth > 0 && scrollState == 0
                if (isHorizontalMove) {
                    val dx = mLastX - x
                    //让itemView在规定区域随手指移动
                    if (mMoveView!!.scrollX + dx >= 0 && mMoveView!!.scrollX + dx <= mMenuWidth) {
                        mMoveView!!.scrollBy(dx, 0)
                    }
                    mLastX = x
                    //设置正处于水平滑动状态
                    mMoving = true
                    mIntercepted = false
                    return true
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (mMoving) {
                    mMoving = false
                    mVelocity?.computeCurrentVelocity(1000)
                    val scrollX = mMoveView!!.scrollX
                    //若速度大于正方向最小速度，则关闭菜单栏；若速度小于反方向最小速度，则打开菜单栏
                    //若速度没到判断条件，则对菜单显示的宽度进行判断打开/关闭菜单
                    if (mVelocity!!.xVelocity >= MINIMUM_VELOCITY) {
                        mScroller.startScroll(
                            scrollX, 0, -scrollX, 0, abs(scrollX.toDouble()).toInt()
                        )
                    } else if (mVelocity!!.xVelocity < -MINIMUM_VELOCITY) {
                        val dx = mMenuWidth - scrollX
                        mScroller.startScroll(
                            scrollX, 0, dx, 0, abs(dx.toDouble()).toInt()
                        )
                    } else if (scrollX > mMenuWidth / ITEM_VIEW_CHILD_COUNTS) {
                        val dx = mMenuWidth - scrollX
                        mScroller.startScroll(
                            scrollX, 0, dx, 0, abs(dx.toDouble()).toInt()
                        )
                    } else {
                        mScroller.startScroll(
                            scrollX, 0, -scrollX, 0, abs(scrollX.toDouble()).toInt()
                        )
                    }
                    invalidate()
                } else if (mMoveView != null && mMoveView!!.scrollX != 0) {
                    //若不是水平滑动状态，菜单栏开着则关闭
                    closeMenu()
                }
                releaseVelocity()
            }

            else -> {}
        }
        return super.onTouchEvent(e)
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (isInWindow(mMoveView)) {
                mMoveView!!.scrollTo(mScroller.currX, 0)
                invalidate()
            } else {
                //若处于动画的itemView滑出屏幕，则终止动画，并让其到达结束点位置
                mScroller.abortAnimation()
                mMoveView!!.scrollTo(mScroller.finalX, 0)
            }
        }
    }

    /**
     * 使用Scroller关闭菜单栏
     */
    private fun closeMenu() {
        mScroller.startScroll(mMoveView!!.scrollX, 0, -mMoveView!!.scrollX, 0, 300)
        invalidate()
    }

    /**
     * 即刻关闭菜单栏
     */
    private fun closeMenuNow() {
        if (mMoveView != null && mMoveView!!.scrollX != 0) {
            mMoveView!!.scrollTo(0, 0)
        }
    }

    /**
     * 获取VelocityTracker实例，并为其添加事件
     * @param e 触碰事件
     */
    private fun addVelocityEvent(e: MotionEvent) {
        if (mVelocity == null) {
            mVelocity = VelocityTracker.obtain()
        }
        mVelocity?.addMovement(e)
    }

    /**
     * 释放VelocityTracker
     */
    private fun releaseVelocity() {
        mVelocity?.apply {
            clear()
            recycle()
        }
        mVelocity = null
    }

    /**
     * 判断该itemView是否显示在屏幕内
     * @param view itemView
     * @return isInWindow
     */
    private fun isInWindow(view: View?): Boolean {
        if (layoutManager is LinearLayoutManager) {
            val manager = layoutManager as LinearLayoutManager?
            val firstPosition = manager!!.findFirstVisibleItemPosition()
            val lastPosition = manager.findLastVisibleItemPosition()
            val currentPosition = manager.getPosition(view!!)
            return currentPosition in firstPosition..lastPosition
        }
        return true
    }
}