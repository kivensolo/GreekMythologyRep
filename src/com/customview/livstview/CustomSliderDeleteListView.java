package com.customview.livstview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
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
    private static final int THRESHOLD_VELOCITY = 600; //速度阀值 超过这个速度  就直接滑出屏幕
    private VelocityTracker velocityTracker;   //速度追踪者

    private Scroller scroller ;      //滑动类
    private int currentPosition;    //当前滑动的ListView的position
    private int startPoint_Y;       //手指按下Y的坐标
    private int startPoint_X;       //手指按下点的X坐标
    private int screenWidth;        //屏幕宽度
    private View itemView;

    private int mMinDis;              //滑动的最小距离
    private boolean isSlider = false; //是否滑动

    private RemoveListener mRemoveListener;  //移除item后的回调接口
    private RemoveDirection removeDirection;

    /**
     * 指示item滑出屏幕的方向
     */
    public enum RemoveDirection{LEFT,RIGHT}

    public CustomSliderDeleteListView(Context context) {
        super(context);
    }

    public CustomSliderDeleteListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSliderDeleteListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

         //获取频幕宽度(最新用法)
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        screenWidth= size.x;
          screenWidth = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        Log.i(TAG,"屏幕宽度为："+screenWidth);
        scroller = new Scroller(context);
        // getScaledTouchSlop是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。
        // 如果小于这个距离就不触发移动控件，如viewpager就是用这个距离来判断用户是否翻页
        mMinDis = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

      /**
    * 设置监听器
    * @param lsr
    */
    public void setRemoveListener(RemoveListener lsr) {
        mRemoveListener = lsr;
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
                //映射个list位置  不是很懂（好像是更具x和y确定index）
                currentPosition = pointToPosition(startPoint_X,startPoint_Y);
                //无效的位置时，不作任何处理
                if(currentPosition == AdapterView.INVALID_POSITION){
                    return super.dispatchTouchEvent(ev);
                }
                //获取ViewGroup中的特定index的View  点击的item View
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
     * 往右滑动
     */
    private void scrollRight() {
        removeDirection = RemoveDirection.RIGHT;        //方向向右
        final int delta = (screenWidth + itemView.getScrollX());
        // 调用startScroll方法来设置一些滚动的参数，在computeScroll()方法中调用scrollTo来滚动item
        scroller.startScroll(itemView.getScrollX(), 0, -delta, 0,
                Math.abs(delta));
        postInvalidate(); // 刷新itemView
    }
     /**
     * 向左滑动  左正右负
     */
    private void scrollLeft() {
        removeDirection = RemoveDirection.LEFT;
        final int delta = (screenWidth - itemView.getScrollX());
        //调用startScroll方法来设置一些滚动的参数，在computeScroll()方法中调用scrollTo来滚动item
        //由(startX , startY)在duration时间内前进(dx,dy)个单位，即到达坐标为(startX+dx , startY+dy)出
        scroller.startScroll(itemView.getScrollX(), itemView.getScrollY(), delta, 0,Math.abs(delta));
        postInvalidate();  // 刷新itemView  会调用computeScroll()方法
    }
     /**
      * 慢速状态下————>判断滑动方向
     */
    private void scrollByDistanceX() {
        // 如果向左滚动的距离大于屏幕的二分之一，就让其删除
        if (itemView.getScrollX() >= screenWidth / 2) {
            scrollLeft();
        } else if (itemView.getScrollX() <= -screenWidth / 2) {
            scrollRight();
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
//            requestDisallowInterceptTouchEvent(true);  //?????
            addVelocityTracker(ev);     //追踪运动轨迹
            final int action = ev.getAction();
            int current_X = (int) ev.getX();
            switch (action) {
                //按下
                case MotionEvent.ACTION_DOWN:
                    break;
                //移动
                 case MotionEvent.ACTION_MOVE:
//                    MotionEvent cancelEvent = MotionEvent.obtain(ev);
//                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
//                               (ev.getActionIndex()<< MotionEvent.ACTION_POINTER_INDEX_SHIFT));
//                    onTouchEvent(cancelEvent);
                     //手动调用ListView的onTouchEvent()方法  action为ACTION_CANCEL。
                     //ListView在ACTION_CANCEL会取消item的点击事件、长按事件以及item的高亮

                    int decX = startPoint_X - current_X;    //左正右负
                    startPoint_X = current_X;               //重置x的位置
                    // 手指拖动itemView滚动, deltaX大于0向左滚动，小于0向右滚
                    itemView.scrollBy(decX, 0);         //让Veiw水平滚动
                    break;                       //拖动的时候ListView不滚动
                case MotionEvent.ACTION_UP:
                    int velocityX = getscrollerVelocity_X();//获取X方向滑动 的速度（有正负）
                    //左右滑动:快速状态直接滑到头  慢速状态比较滑动地距离
                    if (velocityX > THRESHOLD_VELOCITY) {
                        scrollRight();
                    } else if (velocityX < -THRESHOLD_VELOCITY) {
                        scrollLeft();
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
//        super.computeScroll();
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        //是否还在执行滚动偏移
        if(scroller.computeScrollOffset()){
            // 让ListView item根据当前的滚动偏移量进行滚动
            itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
            Log.i(TAG,"getCurrent_X = " + scroller.getCurrX()+"; getCurrenmt_Y" +scroller.getCurrY());
            postInvalidate();

            //是否停止滚动
            if(scroller.isFinished()){
                if(mRemoveListener == null){
                    throw  new NullPointerException("RemoveListener is null, we should called setRemoveListener()");
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
     * @param event  想要追踪的轨迹事件
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
     * ListView item滑出删除接口
     */
    public interface RemoveListener {
       void removeItem(RemoveDirection direction, int position);
    }

}
