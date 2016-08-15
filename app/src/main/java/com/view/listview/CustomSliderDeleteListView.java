package com.view.listview;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * Created by KingZ on 2015/12/27.
 * Discription: 自定义的ListView 可左右滑动删除
 * 主要思路：
 *      1：先根据手指触摸的点来获取点击的是ListView的哪一个item
        2：手指在屏幕中滑动利用scrollBy()来使该item跟随手指一起滑动
        3: 手指放开的时候，判断手指拖动的距离来判断item到底是滑出屏幕还是回到开始位置
 */
public class CustomSliderDeleteListView extends ListView{

    private static String TAG = CustomSliderDeleteListView.class.getSimpleName();
    private static final int THRESHOLD_VELOCITY = 600; //速度阀值
    private VelocityTracker velocityTracker;   //速度追踪者
    private Scroller scroller;      //滑动类
    private View itemView;
    private int currentPosition;    //当前滑动的ListView的position
    private int startPoint_Y;       //手指按下Y的坐标
    private int startPoint_X;       //手指按下点的X坐标
    private int screenWidth;        //屏幕宽度
    private int mMinDis;              //滑动的最小距离
    private boolean isSlider = false; //是否滑动

    private RemovedListener mRemoveListener;  //移除item后的回调接口
    private RemoveDirection removeDirection;

    /**
     * 指示item滑出屏幕的方向
     */
    public enum RemoveDirection{LEFT,RIGHT}

     /**
     * ListView item滑出删除接口
     */
    public interface RemovedListener {
        void removeItem(RemoveDirection direction, int position);
    }

    public void setRemoveListener(RemovedListener lsr) {
        mRemoveListener = lsr;
    }

    //代码创建view时调用
    public CustomSliderDeleteListView(Context context) {
        this(context, null);
    }

    //xml创建view时调用
    public CustomSliderDeleteListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 比如xml里通过某种方式指定了view的style时，
     * 此构造函数在该view被inflate时调用，并将style传入给defStyle。
     * 那么在xml里指定style有几种方式呢？
     * 大概有两种，一种是在直接在布局文件该view标签里使用：style="@style/customstyle"来指定。
     * 另一种是采用指定theme的方式，在AndroidManifest.xml的application标签里使用：android:theme="@style/customstyle"
     * 这两种方式都需要在res/values/styles.xml里定义customstyle。
     * 详见：http://blog.sina.com.cn/s/blog_67d95f400100z5tr.html
     * @param context
     * @param attrs
     * @param defStyle 用来指定view的默认style的，如果是0，那么将不会应用任何默认（或者叫缺省）的style。
     *                 也可以是一个属性指定的style引用，也可以直接是一个显式的style资源。
     */
    public CustomSliderDeleteListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //获取频幕宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        //screenWidth = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        Log.i(TAG, "屏幕宽度为：" + screenWidth);
        scroller = new Scroller(context);
        // getScaledTouchSlop返回一个距离值，表示滑动的时候，手的移动要大于这个距离才开始移动控件。
        // 如果小于这个距离就不触发移动控件，如viewpager就是用这个距离来判断用户是否翻页
        mMinDis = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * 触摸分发事件处理
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                addVelocityTracker(ev);         //添加行动追踪记录
                //滚动还未结束,直接返回
                if(!scroller.isFinished()){
                    return super.dispatchTouchEvent(ev);
                }
                startPoint_X = (int) ev.getX();
                startPoint_Y = (int) ev.getY();
                //根据按下的点位置，确定点击的是哪个item
                currentPosition = pointToPosition(startPoint_X,startPoint_Y);

                //无效的位置时，不作任何处理
                if(currentPosition == AdapterView.INVALID_POSITION){
                    return super.dispatchTouchEvent(ev);
                }
                //根据index获取相应子View
                itemView = getChildAt(currentPosition - getFirstVisiblePosition());
                break;
            case MotionEvent.ACTION_MOVE:
                //滑动状态判定  速度超限制|移动距离超设定值
                if(Math.abs(getscrollerVelocity_X()) > THRESHOLD_VELOCITY
                        || (Math.abs(ev.getX()) - startPoint_X) > mMinDis &&
                        Math.abs(ev.getY() - startPoint_Y) < mMinDis){
                    isSlider =true;
                }
                break;
            case MotionEvent.ACTION_UP:
                clearVelocityTracker();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 向左滑动  左正右负
     */
    private void scrollToLeft() {
        removeDirection = RemoveDirection.LEFT;
        final int delta = (screenWidth - itemView.getScrollX());
        //调用startScroll方法来设置一些滚动的参数，在computeScroll()方法中调用scrollTo来滚动item
        //由(startX , startY)在duration时间内前进(dx,dy)个单位，即到达坐标为(startX+dx , startY+dy)出
        scroller.startScroll(itemView.getScrollX(), itemView.getScrollY(), delta, 0,Math.abs(delta));
        postInvalidate();  // 刷新itemView  会调用computeScroll()方法
    }
    /**
     * 往右滑动
     */
    private void scrollToRight() {
        removeDirection = RemoveDirection.RIGHT;        //方向向右
        final int delta = (screenWidth + itemView.getScrollX());
        // 调用startScroll方法来设置一些滚动的参数，在computeScroll()方法中调用scrollTo来滚动item
        scroller.startScroll(itemView.getScrollX(), 0, -delta, 0,
                Math.abs(delta));
        postInvalidate(); // 刷新itemView
    }

     /**
      * 慢速状态下————>判断滑动方向
     */
    private void scrollByDistanceX() {
        // 如果向左滚动的距离大于屏幕的二分之一，就让其删除
        if (itemView.getScrollX() >= screenWidth / 2) {
            scrollToLeft();
        } else if (itemView.getScrollX() <= -screenWidth / 2) {
            scrollToRight();
        } else {
            // 滚回到原始位置,为了偷下懒这里是直接调用scrollTo滚动
            itemView.scrollTo(0, 0);
        }
    }

    /**
     * 处理触摸事件
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isSlider && currentPosition != AdapterView.INVALID_POSITION){
            addVelocityTracker(ev);     //追踪运动轨迹
            final int action = ev.getAction();
            int current_X = (int) ev.getX();
            switch (action) {
                //按下
                case MotionEvent.ACTION_DOWN:
                    break;
                //移动
                 case MotionEvent.ACTION_MOVE:
                    int decX = startPoint_X - current_X;    //左正右负
                    startPoint_X = current_X;               //重置x的位置
                    // 手指拖动itemView滚动, deltaX大于0向左滚动，小于0向右滚
                    itemView.scrollBy(decX, 0);         //让Veiw水平滚动
                    break;                              //拖动的时候ListView不滚动
                case MotionEvent.ACTION_UP:
                    int velocityX = getscrollerVelocity_X();//获取X方向滑动 的速度（有正负）
                    //左右滑动:快速状态直接滑到头  慢速状态比较滑动地距离
                    if (velocityX > THRESHOLD_VELOCITY) {
                        scrollToRight();
                    } else if (velocityX < -THRESHOLD_VELOCITY) {
                        scrollToLeft();
                    } else {
                        scrollByDistanceX();
                    }
                    clearVelocityTracker(); //UP的时候  不再记录运动轨迹事件
                    isSlider = false;
                    break;
                }
            	return true; // 拖动的时候ListView不滚动
            }
        return super.onTouchEvent(ev);
    }
    /**
     * 计算滚动
     */
      @Override
    public void computeScroll() {
        // 调用startScroll后scroller.computeScrollOffset()返回true，
        if(scroller.computeScrollOffset()){
            // 让ListView item根据当前的滚动偏移量进行滚动
            itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
            Log.i(TAG,"getCurrent_X = " + scroller.getCurrX()+"; getCurrenmt_Y" +scroller.getCurrY());
            postInvalidate();

            //是否停止滚动
            if(scroller.isFinished()){
                if(mRemoveListener == null){
                    throw  new NullPointerException("RemovedListener is null, we should called setRemoveListener()");
                }
                itemView.scrollTo(0, 0);
                mRemoveListener.removeItem(removeDirection, currentPosition);
            }
        }else{
            Log.i(TAG,"computeScroll has done");
        }
    }

     /**
     * 获取X方向上的滑动速度
     * @return
     */
    private int getscrollerVelocity_X(){
        velocityTracker.computeCurrentVelocity(1000);//计算当前速度
        return (int) velocityTracker.getXVelocity();
    }
     /**
     * 为速度追踪器添加运动事件
     * @param event  轨迹事件
     */
    private void addVelocityTracker(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }

        //为速度追踪者添加一个用户的移动轨迹
        //本应该在actionDown的时候就调用，但是可以随意的在任何我想调用的时候调用
        velocityTracker.addMovement(event);
    }
    /**
     * 移除用户速度跟踪器 回收
     */
    private void clearVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    /**
     * 获取当前点击的位置
     * @return
     */
    public int getCurrentPosition(){
        return currentPosition;
    }

}
