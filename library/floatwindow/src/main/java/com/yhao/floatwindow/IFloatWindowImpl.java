package com.yhao.floatwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

/**
 * Created by yhao on 2017/12/22.
 * https://github.com/yhaolpz
 */

public class IFloatWindowImpl extends IFloatWindow {


    private FloatWindow.B mB;
    private FloatView mFloatView;
    private FloatLifecycle mFloatLifecycle;
    private boolean isShow;
    private boolean once = true;
    private ValueAnimator mAnimator;
    private TimeInterpolator mDecelerateInterpolator;
    private float downX;
    private float downY;
    private float upX;
    private float upY;
    private boolean mClick = false;
    private int mSlop;
    private int screenWidth = 0;
    private int screenHeight = 0;

    private IFloatWindowImpl() {

    }

    IFloatWindowImpl(FloatWindow.B b) {
        mB = b;
        screenWidth = Util.getScreenWidth(mB.mApplicationContext);
        screenHeight = Util.getScreenHeight(mB.mApplicationContext);
        if(mB.isPopup){
            mFloatView = new PopupFloatPhone(b.mApplicationContext, mB.mPermissionListener);
            initTouchEvent();
        } else if (mB.mMoveType == MoveType.fixed) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                mFloatView = new FloatPhone(b.mApplicationContext, mB.mPermissionListener);
            } else {
                mFloatView = new FloatToast(b.mApplicationContext);
            }
        } else {
            mFloatView = new FloatPhone(b.mApplicationContext, mB.mPermissionListener);
            initTouchEvent();
        }
        mFloatView.setSize(mB.mWidth, mB.mHeight);
        mFloatView.setGravity(mB.gravity, mB.xOffset, mB.yOffset);
        mFloatView.setView(mB.mView);
        mFloatLifecycle = new FloatLifecycle(mB.mApplicationContext, mB.mShow, mB.mActivities, new LifecycleListener() {

            @Override
            public void onResumeShow() {
                resumeShow();
            }

            @Override
            public void onHide() {
                hide();
            }

            @Override
            public void onBackToDesktop() {
                if (!mB.mDesktopShow) {
                    hide();
                }
                if (mB.mViewStateListener != null) {
                    mB.mViewStateListener.onBackToDesktop();
                }
            }
        });
    }

    @Override
    public void changeMoveType(int moveType, int slideLeftMargin, int slideRightMargin) {
        mB.setMoveType(moveType,slideLeftMargin,slideRightMargin);
    }

    private void resumeShow(){
        if (once) {
            mFloatView.init();
            once = false;
        } else {
            if (isShow) {
                return;
            }
            //resume时, popup的就不再show了
            if(mB.isPopup){
                return;
            }
        }
        isShow = true;
        checkPopupShow();
        if (mB.mViewStateListener != null) {
            mB.mViewStateListener.onShow();
        }
    }

    @Override
    public void show() {
        if (once) {
            mFloatView.init();
            once = false;
        } else {
            if (isShow) {
                return;
            }
        }
        isShow = true;
        checkPopupShow();
        if (mB.mViewStateListener != null) {
            mB.mViewStateListener.onShow();
        }
    }

    @Override
    public void hide() {
        if (once || !isShow) {
            return;
        }
        checkPopupHide();
        isShow = false;
        if (mB.mViewStateListener != null) {
            mB.mViewStateListener.onHide();
        }
    }

    private void checkPopupShow(){
        View innerView = getView();
        if(mB.isPopup){
            //from rop-right
            innerView.setScaleX(mB.popStartScale);
            innerView.setScaleY(mB.popStartScale);
            innerView.setAlpha(0f);
            innerView.setVisibility(View.VISIBLE);
            innerView.post(new Runnable() {
                @Override
                public void run() {
                    innerView.setPivotX(innerView.getMeasuredWidth());
                    innerView.setPivotY(0f);
                    innerView.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .alpha(1f)
                            .setDuration(600)
                            .setInterpolator(new OvershootInterpolator(1f))
                            .start();
                }
            });
        }else{
            innerView.setVisibility(View.VISIBLE);
        }
    }

    private void checkPopupHide(){
        if(mB.isPopup){
            View innerView = getView();
            innerView.post(new Runnable() {
                @Override
                public void run() {
                    ViewPropertyAnimator animator = innerView.animate()
                            .alpha(0f)
                            .setDuration(600)
                            .setInterpolator(new FastOutSlowInInterpolator());
                    animator.setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {}

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            getView().setVisibility(View.INVISIBLE);
                            animator.setListener(null);
                        }
                    });
                    animator.start();
                }
            });
        }else{
            getView().setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean isShowing() {
        return isShow;
    }

    @Override
    void dismiss() {
        mFloatView.dismiss();
        isShow = false;
        if (mB.mViewStateListener != null) {
            mB.mViewStateListener.onDismiss();
        }
    }

    @Override
    public void updateX(int x) {
        checkMoveType();
        mB.xOffset = x;
        mFloatView.updateX(x);
    }

    @Override
    public void updateY(int y) {
        checkMoveType();
        mB.yOffset = y;
        mFloatView.updateY(y);
    }
    @Override
    public void updateXY(int x, int y, boolean animation) {
        checkMoveType();
        mB.yOffset = y;
        mB.xOffset = x;
        if(animation){
            scrollTo(x, y);
        }else{
            mFloatView.updateXY(x, y);
        }
    }

    @Override
    public void setTouchable(boolean enable) {
        mB.isTouchable = enable;
    }

    private void scrollTo(int x, int y) {
        int lastX = mFloatView.getX();
        int lastY = mFloatView.getY();
        int mDeltaX = x - lastX;
        int mDeltaY = y - lastY;
        mAnimator = ObjectAnimator.ofFloat(0f,1f);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float precent = (float) animation.getAnimatedValue();
                int newx = (int) (lastX + mDeltaX*precent);
                int newy = (int) (lastY + mDeltaY*precent);
                mFloatView.updateXY(newx, newy);
                if (mB.mViewStateListener != null) {
                    mB.mViewStateListener.onPositionUpdate(newx, newy);
                }
            }
        });
        startAnimator();
    }

    @Override
    public void updateX(int screenType, float ratio) {
        checkMoveType();
        mB.xOffset = (int) ((screenType == Screen.width ?
                Util.getScreenWidth(mB.mApplicationContext) :
                Util.getScreenHeight(mB.mApplicationContext)) * ratio);
        mFloatView.updateX(mB.xOffset);

    }

    @Override
    public void updateY(int screenType, float ratio) {
        checkMoveType();
        mB.yOffset = (int) ((screenType == Screen.width ?
                Util.getScreenWidth(mB.mApplicationContext) :
                Util.getScreenHeight(mB.mApplicationContext)) * ratio);
        mFloatView.updateY(mB.yOffset);

    }

    @Override
    public int getX() {
        return mFloatView.getX();
    }

    @Override
    public int getY() {
        return mFloatView.getY();
    }


    @Override
    public View getView() {
        mSlop = ViewConfiguration.get(mB.mApplicationContext).getScaledTouchSlop();
        return mB.mView;
    }


    private void checkMoveType() {
        if (mB.mMoveType == MoveType.fixed) {
            throw new IllegalArgumentException("MoveType.fixed is not allowed to move!");
        }
    }


    private void initTouchEvent() {
        getView().setOnTouchListener(new View.OnTouchListener() {
            float lastX, lastY, changeX, changeY;
            int newX, newY;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mB.mMoveType == MoveType.inactive){
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getRawX();
                        downY = event.getRawY();
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        cancelAnimator();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        changeX = event.getRawX() - lastX;
                        changeY = event.getRawY() - lastY;
                        newX = (int) (mFloatView.getX() + changeX);
                        newY = (int) (mFloatView.getY() + changeY);
                        if(mB.mMoveType == MoveType.slide_vertical){
                            newX = mFloatView.getX();
                            int detalY =  Util.dp2px(mB.mApplicationContext, 70f);
                            if(newY < -detalY){
                                newY = -detalY;
                            }
                            if(newY >= screenHeight + detalY - v.getHeight()){
                                newY = screenHeight + detalY - v.getHeight();
                            }
                        }
                         mFloatView.updateXY(newX, newY);
                        if (mB.mViewStateListener != null) {
                            mB.mViewStateListener.onPositionUpdate(newX, newY);
                        }
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        upX = event.getRawX();
                        upY = event.getRawY();
                        mClick = (Math.abs(upX - downX) > mSlop) || (Math.abs(upY - downY) > mSlop);
                        switch (mB.mMoveType) {
                            case MoveType.slide_force:
                                dealForceSlide(v);
                                break;
                            case MoveType.slide_magnet:
                                dealAutoMagnet(v);
                                break;
                            case MoveType.back:
                                PropertyValuesHolder pvhX = PropertyValuesHolder.ofInt("x", mFloatView.getX(), mB.xOffset);
                                PropertyValuesHolder pvhY = PropertyValuesHolder.ofInt("y", mFloatView.getY(), mB.yOffset);
                                mAnimator = ObjectAnimator.ofPropertyValuesHolder(pvhX, pvhY);
                                mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        int x = (int) animation.getAnimatedValue("x");
                                        int y = (int) animation.getAnimatedValue("y");
                                        mFloatView.updateXY(x, y);
                                        if (mB.mViewStateListener != null) {
                                            mB.mViewStateListener.onPositionUpdate(x, y);
                                        }
                                    }
                                });
                                startAnimator();
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                return mClick;
            }

            /**
             * 进行强制吸附判断, 松手要么吸附左侧，要么吸附至右侧
             */
            private void dealForceSlide(View innerView) {
                int screenWidth = Util.getScreenWidth(mB.mApplicationContext);
                int attachedX = screenWidth - innerView.getWidth() - mB.mSlideRightMargin;
                int startX = mFloatView.getX();
                int endX = (startX * 2 + innerView.getWidth() > screenWidth) ? attachedX :
                        mB.mSlideLeftMargin;
                mAnimator = ObjectAnimator.ofInt(startX, endX);
                mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        notifyAnimatorUpdate(animation);
                    }
                });
                startAnimator();
            }

            private void notifyAnimatorUpdate(ValueAnimator animation) {
                int x = (int) animation.getAnimatedValue();
                mFloatView.updateX(x);
                if (mB.mViewStateListener != null) {
                    mB.mViewStateListener.onPositionUpdate(x, (int) upY);
                }
            }

            /**
             * 进行按照磁吸范围进行自动磁吸的处理
             */
            private void dealAutoMagnet(View innerView) {
                int screenWidth = Util.getScreenWidth(mB.mApplicationContext);
                int waringLine =  screenWidth - mB.magnetMargin;
                int attachedX = screenWidth - innerView.getWidth() - mB.mSlideRightMargin;
                int startX = mFloatView.getX();
                int endX = startX + innerView.getWidth() > waringLine ? attachedX : -999;
                if(endX == -999){
                    return;
                }
                mAnimator = ObjectAnimator.ofInt(startX, endX);
                mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        notifyAnimatorUpdate(animation);
                    }
                });
                startAnimator();
            }
        });
    }


    private void startAnimator() {
        if (mB.mInterpolator == null) {
            if (mDecelerateInterpolator == null) {
                mDecelerateInterpolator = new DecelerateInterpolator();
            }
            mB.mInterpolator = mDecelerateInterpolator;
        }
        mAnimator.setInterpolator(mB.mInterpolator);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimator.removeAllUpdateListeners();
                mAnimator.removeAllListeners();
                mAnimator = null;
                if (mB.mViewStateListener != null) {
                    mB.mViewStateListener.onMoveAnimEnd();
                }
            }
        });
        mAnimator.setDuration(mB.mDuration).start();
        if (mB.mViewStateListener != null) {
            mB.mViewStateListener.onMoveAnimStart();
        }
    }

    private void cancelAnimator() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

}
