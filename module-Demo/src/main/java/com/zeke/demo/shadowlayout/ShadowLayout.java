package com.zeke.demo.shadowlayout;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.zeke.demo.R;
import com.zeke.demo.glide.GlideUtils;
import com.zeke.kangaroo.utils.RectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leo
 * on 2019/7/9.
 * https://github.com/lihangleo2/ShadowLayout
 * <p>
 * * 嵌套后即可享受阴影，比CardView更好用、更方便也更强大
 * <p>
 * 支持定制化阴影：
 * 1. 随意修改阴影颜色值
 * 2. 阴影圆角：可统一设置圆角，也可对某几个角单独设置
 * 3. x,y轴阴影偏移
 * 4. 随意修改阴影扩散程度
 * 5. 隐藏阴影：隐藏某边或多边阴影，或完全隐藏
 * <p>
 * 不止于阴影；系统shape功能：项目中shape、selector、ripple统统拥有。
 * 解放你的双手，清空项目drawable文件夹
 * 1. shape样式：pressed（按钮点击）、selected（按钮选择）、ripple（点击水波纹）
 * 2. 背景色设置
 * 3. stroke边框设置
 * 4. 渐变色背景色值
 * 5. 按钮是否可被点击
 * 6. 可绑定textView后，可伴随文案变化，可伴随文案颜色变化
 * 7. 支持设置图片背景，支持图片selector
 *
 *
 * 此版本已经经过轻微重构调整;
 */
public class ShadowLayout extends FrameLayout {
    private Drawable clickAbleFalseDrawable;
    private static final int DEFAULT_CODE = -101;
    private int clickAbleFalseColor = DEFAULT_CODE;

    // <editor-fold defaultstate="collapsed" desc="变量 --- 当前Layout相关变量">
    // 目标View,期望是第一个childView，若没有，则为自身
    private View targetView;
    private Paint paint;
    //当前Layout的clipRect
    private RectF clipRectf = new RectF();
    private boolean isClickable;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="变量 --- 背景色">
    private Drawable backgroundDrawable;
    private Drawable backgroundTrueDrawable;
    @ColorInt
    private int backgroundColor;
    private int backgroundColorTrue = DEFAULT_CODE;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="变量 --- 内边距">
    private int leftPadding;
    private int topPadding;
    private int rightPadding;
    private int bottomPadding;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="变量 --- 阴影 Virable of shadow">
    private Paint shadowPaint;
    //是否显示阴影,默认显示
    private boolean isShowShadow = true;
    //控件区域是否对称，默认是对称。不对称的话，那么控件区域随着阴影区域走
    private boolean isShadowSymmetry;
    @ColorInt
    private int mShadowColor;
    //四周阴影单独的控制开关 TODO 换成位运算
    private boolean leftShow;
    private boolean rightShow;
    private boolean topShow;
    private boolean bottomShow;
    //阴影的扩散范围
    private float mShadowLimit;
    //X|Y方向的偏移量
    private float mShadowOffsetX;
    private float mShadowOffsetY;
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="变量 --- 边框效果">
    private Paint paint_stroke; //边框画笔
    private RectF strokeRect = new RectF();   //边框Rect
    private float strokeSize;   //边框大小（最大不超过7dp）
    @ColorInt
    private int _strokeColor;   //边框颜色(普通状态)
    @ColorInt
    private int _strokeColorTrue; //边框颜色(选中状态)
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="变量 --- 圆角">
    //各个圆角(包括了阴影，shape、背景图、stroke边框圆角)的属性
    private float mCornerRadius; //统一圆角大小
    // 单独设置指定圆角大小,设置后会忽略cornerRadius的值
    private float mCornerRadius_leftTop;
    private float mCornerRadius_rightTop;
    private float mCornerRadius_leftBottom;
    private float mCornerRadius_rightBottom;
    // 无效值
    private static final float RADIUS_INVALID_VALUE = -1f;
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="变量 --- 填充渐变色">
    private int startColor;
    private int centerColor;
    private int endColor;
    private int angle;
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="变量 --- 绑定Child文字">
    //在普遍情况，在点击或选中按钮时，很可能伴随着textView的字体颜色变化
    private int mTextViewResId = -1;
    private TextView mTextView;
    private int textColor;
    private int textColor_true;
    private String text;
    private String text_true;
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="变量 --- Select效果">
    //ShadowLayout的样式，默认为pressed.
    private SelectorType selectorType = SelectorType.PRESS;

    // 选中类型效果
    private enum SelectorType {
        PRESS(1), SELECTED(2), RIPPLE(3);
        private int type;
        private static final Map<Integer, SelectorType> intToEnum = new HashMap<>();

        static {
            for (SelectorType value : values()) {
                intToEnum.put(value.getType(), value);
            }
        }

        SelectorType(int type) {
            this.type = type;
        }

        public static SelectorType fromType(int type) {
            return intToEnum.get(type);
        }

        public int getType() {
            return type;
        }
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="构造函数">
    public ShadowLayout(Context context) {
        this(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="工具API">


    private boolean isDimensionValid() {
        return getWidth() != 0 && getHeight() != 0;
    }

     //将画笔附上 渐变色
    private void attatchGradient(Paint paint) {
        if (!isClickable) {
            paint.setShader(null);
            return;
        }

        //左上 x,y   leftPadding, topPadding,
        //右下 x,y   getWidth() - rightPadding, getHeight() - bottomPadding
        int[] colors;
        if (centerColor == DEFAULT_CODE) {
            colors = new int[]{startColor, endColor};
        } else {
            colors = new int[]{startColor, centerColor, endColor};
        }

        if (angle < 0) {
            int trueAngle = angle % 360;
            angle = trueAngle + 360;
        }

        //当设置的角度大于0的时候
        //这个要算出每隔45度
        int trueAngle = angle % 360;
        int angleFlag = trueAngle / 45;
        LinearGradient linearGradient;
        switch (angleFlag) {
            case 0://0°
                linearGradient = new LinearGradient(leftPadding, topPadding, getWidth() - rightPadding, topPadding, colors, null, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                break;

            case 1://45°
                linearGradient = new LinearGradient(leftPadding, getHeight() - bottomPadding, getWidth() - rightPadding, topPadding, colors, null, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                break;

            case 2://90°
                int x_ = (getWidth() - rightPadding - leftPadding) / 2 + leftPadding;
                linearGradient = new LinearGradient(x_, getHeight() - bottomPadding, x_, topPadding, colors, null, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                break;

            case 3://135°
                linearGradient = new LinearGradient(getWidth() - rightPadding, getHeight() - bottomPadding, leftPadding, topPadding, colors, null, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                break;
            case 4://180°
                linearGradient = new LinearGradient(getWidth() - rightPadding, topPadding, leftPadding, topPadding, colors, null, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                break;

            case 5://225°
                linearGradient = new LinearGradient(getWidth() - rightPadding, topPadding, leftPadding, getHeight() - bottomPadding, colors, null, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                break;

            case 6://270°
                int x = (getWidth() - rightPadding - leftPadding) / 2 + leftPadding;
                linearGradient = new LinearGradient(x, topPadding, x, getHeight() - bottomPadding, colors, null, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                break;

            case 7://315°
                linearGradient = new LinearGradient(leftPadding, topPadding, getWidth() - rightPadding, getHeight() - bottomPadding, colors, null, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                break;
        }
    }

    public void isAddAlpha(@ColorInt int color) {
        //获取单签颜色值的透明度，如果没有设置透明度，默认加上#2a
        if (Color.alpha(color) == 255) {
            String red = Integer.toHexString(Color.red(color));
            String green = Integer.toHexString(Color.green(color));
            String blue = Integer.toHexString(Color.blue(color));

            if (red.length() == 1) {
                red = "0" + red;
            }

            if (green.length() == 1) {
                green = "0" + green;
            }

            if (blue.length() == 1) {
                blue = "0" + blue;
            }
            String endColor = "#2a" + red + green + blue;
            mShadowColor = convertToColorInt(endColor);
        }
    }


    public static int convertToColorInt(String argb)
            throws IllegalArgumentException {

        if (!argb.startsWith("#")) {
            argb = "#" + argb;
        }

        return Color.parseColor(argb);
    }

    public int dip2px(float dipValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private boolean hasChild() {
        return getChildCount() > 0;
    }

    /**
     * 是否没有指定单独的圆角大小
     * @return true|false
     */
    private boolean isNoSpecialCornerRadius(){
        return mCornerRadius_leftTop == RADIUS_INVALID_VALUE &&
                mCornerRadius_leftBottom == RADIUS_INVALID_VALUE &&
                mCornerRadius_rightTop == RADIUS_INVALID_VALUE &&
                mCornerRadius_rightBottom == RADIUS_INVALID_VALUE;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="对外提供的设置APi">
    public void setBackGround(Drawable drawable) {
        if (targetView != null && drawable != null) {
            if (isNoSpecialCornerRadius()) {
                GlideUtils.setRoundCorner(targetView, drawable, mCornerRadius);
            } else {
                int leftTop = (int) (mCornerRadius_leftTop == RADIUS_INVALID_VALUE ?
                        mCornerRadius : mCornerRadius_leftTop);
                int leftBottom = (int) (mCornerRadius_leftBottom == RADIUS_INVALID_VALUE ?
                        mCornerRadius : mCornerRadius_leftBottom);
                int rightTop = (int) (mCornerRadius_rightTop == RADIUS_INVALID_VALUE ?
                        mCornerRadius : mCornerRadius_rightTop);
                int rightBottom = (int) (mCornerRadius_rightBottom == RADIUS_INVALID_VALUE ?
                        mCornerRadius : mCornerRadius_rightBottom);

                GlideUtils.setCorners(targetView, drawable, leftTop, leftBottom, rightTop, rightBottom);
            }
        }
    }

    public void setClickable(boolean clickable) {
        super.setClickable(clickable);
        this.isClickable = clickable;
        changeSwitchClickable();
        if (isClickable) {
            super.setOnClickListener(onClickListener);
        }

        if (paint != null) {
            if (startColor != DEFAULT_CODE && endColor != DEFAULT_CODE) {
                attatchGradient(paint);
            }
        }
    }


    //解决xml设置clickable = false时。代码设置true时，点击事件无效的bug
    private OnClickListener onClickListener;

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.onClickListener = l;
        if (isClickable) {
            super.setOnClickListener(l);
        }
    }

    public void setPadding() {
        if (isShowShadow && mShadowLimit > 0) {
            //控件区域是否对称，默认是对称。不对称的话，那么控件区域随着阴影区域走
            if (isShadowSymmetry) {
                int xPadding = (int) (mShadowLimit + Math.abs(mShadowOffsetX));
                int yPadding = (int) (mShadowLimit + Math.abs(mShadowOffsetY));

                if (leftShow) {
                    leftPadding = xPadding;
                } else {
                    leftPadding = 0;
                }

                if (topShow) {
                    topPadding = yPadding;
                } else {
                    topPadding = 0;
                }


                if (rightShow) {
                    rightPadding = xPadding;
                } else {
                    rightPadding = 0;
                }

                if (bottomShow) {
                    bottomPadding = yPadding;
                } else {
                    bottomPadding = 0;
                }
            } else {
                if (Math.abs(mShadowOffsetY) > mShadowLimit) {
                    if (mShadowOffsetY > 0) {
                        mShadowOffsetY = mShadowLimit;
                    } else {
                        mShadowOffsetY = 0 - mShadowLimit;
                    }
                }


                if (Math.abs(mShadowOffsetX) > mShadowLimit) {
                    if (mShadowOffsetX > 0) {
                        mShadowOffsetX = mShadowLimit;
                    } else {
                        mShadowOffsetX = 0 - mShadowLimit;
                    }
                }

                if (topShow) {
                    topPadding = (int) (mShadowLimit - mShadowOffsetY);
                } else {
                    topPadding = 0;
                }

                if (bottomShow) {
                    bottomPadding = (int) (mShadowLimit + mShadowOffsetY);
                } else {
                    bottomPadding = 0;
                }


                if (rightShow) {
                    rightPadding = (int) (mShadowLimit - mShadowOffsetX);
                } else {
                    rightPadding = 0;
                }


                if (leftShow) {
                    leftPadding = (int) (mShadowLimit + mShadowOffsetX);
                } else {
                    leftPadding = 0;
                }
            }
            setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        }
    }

    public void changeSwitchClickable() {
        if(targetView == null){
            return;
        }
        if(selectorType != SelectorType.PRESS){
            //不可点击的状态只在press mode的模式下生效
            return;
        }

        //press mode
        if (!isClickable) {
            //不可点击的状态。
            if (clickAbleFalseColor != DEFAULT_CODE) {
                //说明设置了颜色
                if (backgroundDrawable != null) {
                    //说明此时是设置了图片的模式
                    targetView.getBackground().setAlpha(0);
                }
                paint.setColor(clickAbleFalseColor);
                postInvalidate();


            } else if (clickAbleFalseDrawable != null) {
                //说明设置了背景图
                setBackGround(clickAbleFalseDrawable);
                paint.setColor(Color.parseColor("#00000000"));
                postInvalidate();
            }
        } else {
            //可点击的状态
            if (backgroundDrawable != null) {
                setBackGround(backgroundDrawable);
            } else {
                if (targetView.getBackground() != null) {
                    targetView.getBackground().setAlpha(0);
                }
            }
            paint.setColor(backgroundColor);
            postInvalidate();
        }
    }

    //增加selector样式
    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (getWidth() != 0) {
            if (selectorType == SelectorType.SELECTED) {
                if (selected) {
                    if (backgroundColorTrue != DEFAULT_CODE) {
                        paint.setColor(backgroundColorTrue);
                    }

                    paint.setShader(null);
                    if (_strokeColorTrue != DEFAULT_CODE) {
                        paint_stroke.setColor(_strokeColorTrue);
                    }
                    if (backgroundTrueDrawable != null) {
                        setBackGround(backgroundTrueDrawable);
                    }

                    if (mTextView != null) {
                        mTextView.setTextColor(textColor_true);
                        if (!TextUtils.isEmpty(text_true)) {
                            mTextView.setText(text_true);
                        }
                    }

                } else {
                    paint.setColor(backgroundColor);
                    if (startColor != DEFAULT_CODE) {
                        attatchGradient(paint);
                    }

                    if (_strokeColor != DEFAULT_CODE) {
                        paint_stroke.setColor(_strokeColor);
                    }

                    if (backgroundDrawable != null) {
                        setBackGround(backgroundDrawable);
                    }

                    if (mTextView != null) {
                        mTextView.setTextColor(textColor);
                        if (!TextUtils.isEmpty(text)) {
                            mTextView.setText(text);
                        }
                    }

                }
                postInvalidate();
            }
        } else {
            addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    removeOnLayoutChangeListener(this);
                    setSelected(isSelected());
                }
            });
        }
    }

    //设置x轴偏移量
    public void setShadowOffsetX(float mDx) {
        if (isShowShadow) {
            if (Math.abs(mDx) > mShadowLimit) {
                if (mDx > 0) {
                    this.mShadowOffsetX = mShadowLimit;
                } else {
                    this.mShadowOffsetX = -mShadowLimit;
                }
            } else {
                this.mShadowOffsetX = mDx;
            }
            setPadding();
        }
    }

    //动态设置y轴偏移量
    public void setShadowOffsetY(float mDy) {
        if (isShowShadow) {
            if (Math.abs(mDy) > mShadowLimit) {
                if (mDy > 0) {
                    this.mShadowOffsetY = mShadowLimit;
                } else {
                    this.mShadowOffsetY = -mShadowLimit;
                }
            } else {
                this.mShadowOffsetY = mDy;
            }
            setPadding();
        }
    }


    //动态设置 圆角属性
    public void setCornerRadius(int mCornerRadius) {
        this.mCornerRadius = mCornerRadius;
        if (isDimensionValid()) {
            setBackgroundCompat(getWidth(), getHeight());
        }
    }

    public float getShadowLimit() {
        return mShadowLimit;
    }

    //动态设置阴影扩散区域
    public void setShadowLimit(int mShadowLimit) {
        if (isShowShadow) {
            int dip5 = (int) getContext().getResources().getDimension(R.dimen.dp_5);
            if (mShadowLimit >= dip5) {
                this.mShadowLimit = mShadowLimit;
            } else {
                this.mShadowLimit = dip5;
            }
            setPadding();
        }
    }

    //动态设置阴影颜色值
    public void setShadowColor(int mShadowColor) {
        this.mShadowColor = mShadowColor;
        if (isDimensionValid()) {
            setBackgroundCompat(getWidth(), getHeight());
        }
    }


    public void setSpecialCorner(int leftTop, int rightTop, int leftBottom, int rightBottom) {
        mCornerRadius_leftTop = leftTop;
        mCornerRadius_rightTop = rightTop;
        mCornerRadius_leftBottom = leftBottom;
        mCornerRadius_rightBottom = rightBottom;
        if (isDimensionValid()) {
            setBackgroundCompat(getWidth(), getHeight());
        }
    }

    //是否隐藏阴影
    public void setShadowHidden(boolean hide) {
        isShowShadow = !hide;
        if (isDimensionValid()) {
            setBackgroundCompat(getWidth(), getHeight());
        }
    }

    //是否隐藏阴影的上边部分
    public void setShadowHiddenTop(boolean hide) {
        this.topShow = !hide;
        setPadding();
    }

    public void setShadowHiddenBottom(boolean hide) {
        this.bottomShow = !hide;
        setPadding();
    }

    public void setShadowHiddenRight(boolean hide) {
        this.rightShow = !hide;
        setPadding();
    }

    public void setShadowHiddenLeft(boolean hide) {
        this.leftShow = !hide;
        setPadding();
    }


    public void setBackgroundDrawable(int color) {
        if (backgroundTrueDrawable != null) {
            throw new UnsupportedOperationException("使用了ShadowLayout_layoutBackground_true属性，要与ShadowLayout_layoutBackground属性统一为颜色");
        }
        backgroundColor = color;
        if (selectorType == SelectorType.SELECTED) {
            //select模式
            if (!this.isSelected()) {
                paint.setColor(backgroundColor);
            }
        } else {
            paint.setColor(backgroundColor);
        }
        postInvalidate();
    }


    public void setLayoutBackgroundTrue(int color) {
        if (backgroundDrawable != null) {
            throw new UnsupportedOperationException("使用了ShadowLayout_layoutBackground属性，要与ShadowLayout_layoutBackground_true属性统一为颜色");
        }
        backgroundColorTrue = color;
        if (selectorType == SelectorType.SELECTED) {
            //select模式
            if (this.isSelected()) {
                paint.setColor(backgroundColorTrue);
            }
        }
        postInvalidate();
    }


    public void setStrokeColor(int color) {
        _strokeColor = color;
        if (selectorType == SelectorType.SELECTED) {
            //select模式
            if (!this.isSelected()) {
                paint_stroke.setColor(_strokeColor);
            }
        } else {
            paint_stroke.setColor(_strokeColor);
        }
        postInvalidate();
    }


    public void setStrokeColorTrue(int color) {

        _strokeColorTrue = color;
        if (selectorType == SelectorType.SELECTED) {
            //select模式
            if (this.isSelected()) {
                paint_stroke.setColor(_strokeColorTrue);
            }
        }
        postInvalidate();
    }

    public void setStrokeWidth(int stokeWidth) {
        this.strokeSize = stokeWidth;
        if (strokeSize > dip2px(7)) {
            strokeSize = dip2px(5);
        }
        paint_stroke.setStrokeWidth(strokeSize);
        postInvalidate();
    }
    // </editor-fold>

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mTextViewResId != -1) {
            mTextView = findViewById(mTextViewResId);
            if (mTextView == null) {
                throw new NullPointerException("ShadowLayout找不到hl_bindTextView，请确保绑定的资源id在ShadowLayout内");
            } else {
                if (textColor == DEFAULT_CODE) {
                    textColor = mTextView.getCurrentTextColor();
                }


                if (textColor_true == DEFAULT_CODE) {
                    textColor_true = mTextView.getCurrentTextColor();
                }

                mTextView.setTextColor(textColor);
                if (!TextUtils.isEmpty(text)) {
                    mTextView.setText(text);
                }
            }
        }

        targetView = getChildAt(0);
        if (targetView == null) {
            //当子View都没有的时候。默认不使用阴影
            targetView = ShadowLayout.this;
            isShowShadow = false;
        }

        //selector样式不受clickable的影响
        if (selectorType == SelectorType.SELECTED) {
            //if (this.isSelected()) {
            //这个方法内已经判断了是否为空
            //  setmBackGround(layoutBackground_true);
            //} else {
            //  setmBackGround(layoutBackground);
            //  }
        } else {
            if (isClickable) {
                setBackGround(backgroundDrawable);
            } else {
                setBackGround(clickAbleFalseDrawable);
                if (clickAbleFalseColor != DEFAULT_CODE) {
                    paint.setColor(clickAbleFalseColor);
                }
            }
        }

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            setBackgroundCompat(w, h);
            if (startColor != DEFAULT_CODE) {
                attatchGradient(paint);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="控件初始化  Init ui">
    private void initView(Context context, AttributeSet attrs) {
        initAttributes(attrs);
        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.FILL);


        paint_stroke = new Paint();
        paint_stroke.setAntiAlias(true);
        paint_stroke.setStyle(Paint.Style.STROKE);
        paint_stroke.setStrokeWidth(strokeSize);
        if (_strokeColor != DEFAULT_CODE) {
            paint_stroke.setColor(_strokeColor);
        }


        //矩形画笔
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        //打个标记
        paint.setColor(backgroundColor);

        setPadding();
    }
    // </editor-fold>

// <editor-fold defaultstate="collapsed" desc="背景效果设置">

    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings("deprecation")
    private void setBackgroundCompat(int w, int h) {
        if (isShowShadow) {
            //判断传入的颜色值是否有透明度
            isAddAlpha(mShadowColor);
            Bitmap bitmap = createShadowBitmap(w, h, mCornerRadius, mShadowLimit, mShadowOffsetX,
                    mShadowOffsetY, mShadowColor, Color.TRANSPARENT);
            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                setBackgroundDrawable(drawable);
            } else {
                setBackground(drawable);
            }
        } else {
            if (!hasChild()) {
                if (backgroundDrawable != null) {
                    targetView = ShadowLayout.this;
                    if (isClickable) {
                        setBackGround(backgroundDrawable);
                    } else {
                        changeSwitchClickable();
                    }
                } else {
                    //解决不执行onDraw方法的bug就是给其设置一个透明色
                    this.setBackgroundColor(Color.parseColor("#00000000"));
                }
            } else {
                this.setBackgroundColor(Color.parseColor("#00000000"));
            }
        }
    }
// </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="自定义属性解析  Attributes parse">
    private void initAttributes(AttributeSet attrs) {
        TypedArray attr = getContext().obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        if (attr == null) {
            return;
        }
//        ShadowPropParser parser = new ShadowPropParser(attr);

        try {
//            ShadowPropParser.ParsedTyped_Border border = parser.getParsedValue(R.styleable.ShadowLayout_border);
//            if (border != null) {
//                ZLog.d(border.toString());
//            }
            initShadowAttr(attr);
            initConerRadiusAttr(attr);
            initBackgroundAttr(attr);
            initStrokeAttr(attr);
            initSelectTypeAttr(attr);
            initBindTextViewAttr(attr);
            initClickableAttr(attr);
        } finally {
            attr.recycle();
        }
    }

      /**
     * 初始化阴影属性
     */
    private void initShadowAttr(TypedArray attr) {
        //阴影显示规则
        isShowShadow = !attr.getBoolean(R.styleable.ShadowLayout_shadowHidden, false);
        leftShow = !attr.getBoolean(R.styleable.ShadowLayout_shadowHiddenLeft, false);
        rightShow = !attr.getBoolean(R.styleable.ShadowLayout_shadowHiddenRight, false);
        bottomShow = !attr.getBoolean(R.styleable.ShadowLayout_shadowHiddenBottom, false);
        topShow = !attr.getBoolean(R.styleable.ShadowLayout_shadowHiddenTop, false);

        //初始化扩散区域 如果没有设置阴影扩散区域，则默认隐藏阴影 最低有效值为5dp
        mShadowLimit = attr.getDimension(R.styleable.ShadowLayout_shadowLimit, 0);
        if (mShadowLimit == 0) {
            isShowShadow = false;
        } else {
            int dip5 = (int) getContext().getResources().getDimension(R.dimen.dp_5);
            if (mShadowLimit < dip5) {
                mShadowLimit = dip5;
            }
        }

        //初始化阴影偏移量
        mShadowOffsetX = attr.getDimension(R.styleable.ShadowLayout_shadowOffsetX, 0);
        mShadowOffsetY = attr.getDimension(R.styleable.ShadowLayout_shadowOffsetY, 0);

        mShadowColor = attr.getColor(R.styleable.ShadowLayout_shadowColor,
                getResources().getColor(R.color.default_shadow_color));

        // 阴影是否对称
        isShadowSymmetry = attr.getBoolean(R.styleable.ShadowLayout_shadowSymmetry, true);

        // 阴影渐变
        startColor = attr.getColor(R.styleable.ShadowLayout_startColor, DEFAULT_CODE);
        centerColor = attr.getColor(R.styleable.ShadowLayout_centerColor, DEFAULT_CODE);
        endColor = attr.getColor(R.styleable.ShadowLayout_endColor, DEFAULT_CODE);
        if (startColor != DEFAULT_CODE  && endColor == DEFAULT_CODE) {
            throw new IllegalArgumentException("使用了ShadowLayout_startColor渐变起始色，必须搭配终止色ShadowLayout_endColor");
        }

        // 阴影角度
        angle = attr.getInt(R.styleable.ShadowLayout_angle, 0);
        if (angle % 45 != 0) {
            throw new IllegalArgumentException("Linear gradient requires 'angle' attribute to be a multiple of 45");
        }
    }

    void initClickableAttr(TypedArray attr) {
        Drawable clickAbleFalseBackground = attr.getDrawable(R.styleable.ShadowLayout_layoutBackground_clickFalse);
        if (clickAbleFalseBackground != null) {
            if (clickAbleFalseBackground instanceof ColorDrawable) {
                ColorDrawable colorDrawableClickableFalse = (ColorDrawable) clickAbleFalseBackground;
                clickAbleFalseColor = colorDrawableClickableFalse.getColor();
            } else {
                clickAbleFalseDrawable = clickAbleFalseBackground;
            }
        }
        isClickable = attr.getBoolean(R.styleable.ShadowLayout_clickable, true);
        setClickable(isClickable);
    }

    private void initConerRadiusAttr(TypedArray attr) {
        mCornerRadius = attr.getDimension(R.styleable.ShadowLayout_cornerRadius, getResources().getDimension(R.dimen.size_0dp));
        mCornerRadius_leftTop = attr.getDimension(R.styleable.ShadowLayout_cornerRadius_leftTop, RADIUS_INVALID_VALUE);
        mCornerRadius_leftBottom = attr.getDimension(R.styleable.ShadowLayout_cornerRadius_leftBottom, RADIUS_INVALID_VALUE);
        mCornerRadius_rightTop = attr.getDimension(R.styleable.ShadowLayout_cornerRadius_rightTop, RADIUS_INVALID_VALUE);
        mCornerRadius_rightBottom = attr.getDimension(R.styleable.ShadowLayout_cornerRadius_rightBottom, RADIUS_INVALID_VALUE);
    }

    /**
     * 功能扩展: 可绑定一个TextView类型的子元素，用于点击时进行效果切换
     */
    private void initBindTextViewAttr(TypedArray attr) {
        mTextViewResId = attr.getResourceId(R.styleable.ShadowLayout_bindTextView, -1);
        textColor = attr.getColor(R.styleable.ShadowLayout_textColor, DEFAULT_CODE);
        textColor_true = attr.getColor(R.styleable.ShadowLayout_textColor_true, DEFAULT_CODE);
        text = attr.getString(R.styleable.ShadowLayout_text);
        text_true = attr.getString(R.styleable.ShadowLayout_text_true);
    }

    private void initSelectTypeAttr(TypedArray attr) {
        selectorType = SelectorType.fromType(attr.getInt(R.styleable.ShadowLayout_shapeMode, SelectorType.PRESS.getType()));
        if (selectorType == SelectorType.RIPPLE) {
            //如果是ripple的话
            if (backgroundColor == DEFAULT_CODE || backgroundColorTrue == DEFAULT_CODE) {
                throw new IllegalStateException("使用了ShadowLayout的水波纹，必须设置使用了ShadowLayout_layoutBackground和使用了ShadowLayout_layoutBackground_true属性，且为颜色值");
            }

            //如果是设置了图片的话，则也不支持水波纹
            if (backgroundDrawable != null) {
                selectorType = SelectorType.PRESS;
            }
        }
    }

    private void initBackgroundAttr(TypedArray attr) {
        //背景颜色的点击(默认颜色为白色)
        backgroundColor = getResources().getColor(R.color.default_shadowback_color);

        Drawable background = attr.getDrawable(R.styleable.ShadowLayout_layoutBackground);
        if (background != null) {
            if (background instanceof ColorDrawable) {
                ColorDrawable colorDrawable = (ColorDrawable) background;
                backgroundColor = colorDrawable.getColor();
            } else {
                backgroundDrawable = background;
            }
        }

        Drawable trueBackground = attr.getDrawable(R.styleable.ShadowLayout_layoutBackground_true);
        if (trueBackground != null) {
            if (trueBackground instanceof ColorDrawable) {
                ColorDrawable colorDrawableTrue = (ColorDrawable) trueBackground;
                backgroundColorTrue = colorDrawableTrue.getColor();

            } else {
                backgroundTrueDrawable = trueBackground;
            }
        }

        if (backgroundColorTrue != DEFAULT_CODE && backgroundDrawable != null) {
            throw new UnsupportedOperationException("使用了ShadowLayout_layoutBackground_true属性，必须先设置ShadowLayout_layoutBackground属性。且设置颜色时，必须保持都为颜色");
        }

        if (backgroundDrawable == null && backgroundTrueDrawable != null) {
            throw new UnsupportedOperationException("使用了ShadowLayout_layoutBackground_true属性，必须先设置ShadowLayout_layoutBackground属性。且设置图片时，必须保持都为图片");
        }
    }

    private void initStrokeAttr(TypedArray attr) {
        _strokeColor = attr.getColor(R.styleable.ShadowLayout_strokeColor, DEFAULT_CODE);
        _strokeColorTrue = attr.getColor(R.styleable.ShadowLayout_strokeColor_true, DEFAULT_CODE);
        if (_strokeColor == DEFAULT_CODE && _strokeColorTrue != DEFAULT_CODE) {
            throw new UnsupportedOperationException("使用了ShadowLayout_strokeColor_true属性，必须先设置ShadowLayout_strokeColor属性");
        }

        strokeSize = attr.getDimension(R.styleable.ShadowLayout_strokeWith, dip2px(1));
        if (strokeSize > dip2px(7)) {
            strokeSize = dip2px(5);
        }
    }

    // </editor-fold>


    /**
     * 创建阴影的Bitmap
     *
     * @param shadowWidth  阴影宽度
     * @param shadowHeight 阴影高度
     * @param cornerRadius 阴影圆角大小
     * @param shadowRadius 阴影模糊半径
     * @param dx           阴影x轴偏移量
     * @param dy           阴影Y轴偏移量
     * @param shadowColor  阴影颜色
     * @param fillColor    填充色
     * @return Bitmap图像
     */
    private Bitmap createShadowBitmap(int shadowWidth, int shadowHeight, float cornerRadius, float shadowRadius,
                                      float dx, float dy, int shadowColor, int fillColor) {
        //优化阴影bitmap大小,将尺寸缩小至原来的1/4。
        dx = dx / 4;
        dy = dy / 4;
        shadowWidth = shadowWidth / 4 == 0 ? 1 : shadowWidth / 4;
        shadowHeight = shadowHeight / 4 == 0 ? 1 : shadowHeight / 4;
        cornerRadius = cornerRadius / 4;
        shadowRadius = shadowRadius / 4;

        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);

        //这里缩小limit的是因为，setShadowLayer后会将bitmap扩散到shadowWidth，shadowHeight
        //同时也要根据某边的隐藏情况去改变

        float rect_left = 0;
        float rect_right = 0;
        float rect_top = 0;
        float rect_bottom = 0;
        if (leftShow) {
            rect_left = shadowRadius;
        } else {
//            rect_left = 0;
            float maxLeftTop = Math.max(cornerRadius, mCornerRadius_leftTop);
            float maxLeftBottom = Math.max(cornerRadius, mCornerRadius_leftBottom);
            float maxLeft = Math.max(maxLeftTop, maxLeftBottom);
            rect_left = maxLeft;
        }

        if (topShow) {
            rect_top = shadowRadius;
        } else {
//            rect_top = 0;
            float maxLeftTop = Math.max(cornerRadius, mCornerRadius_leftTop);
            float maxRightTop = Math.max(cornerRadius, mCornerRadius_rightTop);
            float maxTop = Math.max(maxLeftTop, maxRightTop);
            rect_top = maxTop;
        }

        if (rightShow) {
            rect_right = shadowWidth - shadowRadius;
        } else {
//            rect_right = shadowWidth;
            float maxRightTop = Math.max(cornerRadius, mCornerRadius_rightTop);
            float maxRightBottom = Math.max(cornerRadius, mCornerRadius_rightBottom);
            float maxRight = Math.max(maxRightTop, maxRightBottom);
            rect_right = shadowWidth - maxRight;
        }

        if (bottomShow) {
            rect_bottom = shadowHeight - shadowRadius;
        } else {
//            rect_bottom = shadowHeight;
            float maxLeftBottom = Math.max(cornerRadius, mCornerRadius_leftBottom);
            float maxRightBottom = Math.max(cornerRadius, mCornerRadius_rightBottom);
            float maxBottom = Math.max(maxLeftBottom, maxRightBottom);
            rect_bottom = shadowHeight - maxBottom;
        }


        RectF shadowRect = new RectF(
                rect_left,
                rect_top,
                rect_right,
                rect_bottom);

        if (isShadowSymmetry) {
            if (dy > 0) {
                shadowRect.top += dy;
                shadowRect.bottom -= dy;
            } else if (dy < 0) {
                shadowRect.top += Math.abs(dy);
                shadowRect.bottom -= Math.abs(dy);
            }

            if (dx > 0) {
                shadowRect.left += dx;
                shadowRect.right -= dx;
            } else if (dx < 0) {

                shadowRect.left += Math.abs(dx);
                shadowRect.right -= Math.abs(dx);
            }
        } else {
            shadowRect.top -= dy;
            shadowRect.bottom -= dy;
            shadowRect.right -= dx;
            shadowRect.left -= dx;
        }


        shadowPaint.setColor(fillColor);
        if (!isInEditMode()) {//dx  dy
            shadowPaint.setShadowLayer(shadowRadius / 2, dx, dy, shadowColor);
        }

        if (isNoSpecialCornerRadius()) {
            canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint);
        } else {
            //目前最佳的解决方案
            clipRectf.left = leftPadding;
            clipRectf.top = topPadding;
            clipRectf.right = getWidth() - rightPadding;
            clipRectf.bottom = getHeight() - bottomPadding;


            shadowPaint.setAntiAlias(true);
            int leftTop;
            if (mCornerRadius_leftTop == -1) {
                leftTop = (int) mCornerRadius / 4;
            } else {
                leftTop = (int) mCornerRadius_leftTop / 4;
            }
            int leftBottom;
            if (mCornerRadius_leftBottom == -1) {
                leftBottom = (int) mCornerRadius / 4;
            } else {
                leftBottom = (int) mCornerRadius_leftBottom / 4;
            }

            int rightTop;
            if (mCornerRadius_rightTop == -1) {
                rightTop = (int) mCornerRadius / 4;
            } else {
                rightTop = (int) mCornerRadius_rightTop / 4;
            }

            int rightBottom;
            if (mCornerRadius_rightBottom == -1) {
                rightBottom = (int) mCornerRadius / 4;
            } else {
                rightBottom = (int) mCornerRadius_rightBottom / 4;
            }

            float[] outerR = new float[]{leftTop, leftTop, rightTop, rightTop, rightBottom, rightBottom, leftBottom, leftBottom};//左上，右上，右下，左下
            Path path = new Path();
            path.addRoundRect(shadowRect, outerR, Path.Direction.CW);
            canvas.drawPath(path, shadowPaint);
        }

        return output;
    }


    // <editor-fold defaultstate="collapsed" desc="绘制部分">
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (hasChild()) {
            //TODO 这是绘制啥？
            int trueHeight = (int) RectUtils.calRectHeight(clipRectf);
            Path path = new Path();
            if (isNoSpecialCornerRadius()) {
                float radius = Math.min(mCornerRadius,trueHeight / 2);
                path.addRoundRect(clipRectf, radius, radius, Path.Direction.CW);
            } else {
                float[] outerR = getRippleCornerValue(trueHeight);
                path.addRoundRect(leftPadding, topPadding, getWidth() - rightPadding,
                        getHeight() - bottomPadding, outerR, Path.Direction.CW);
            }

            canvas.clipPath(path);
        }
        super.dispatchDraw(canvas);

    }

    private boolean isInvisible() {
        return getVisibility() != View.VISIBLE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        if (isInvisible()) {
//            return;
//        }

//        if (!hasChild()) {
//            return;
//        }

        clipRectf.left = leftPadding;
        clipRectf.top = topPadding;
        clipRectf.right = getWidth() - rightPadding;
        clipRectf.bottom = getHeight() - bottomPadding;

        int clipHeight = (int) RectUtils.calRectHeight(clipRectf); //可见高度
        if (isNoSpecialCornerRadius()) {
            //没有设置特定圆角，正常绘制
            if (selectorType != SelectorType.RIPPLE) {
                drawBorder(canvas, clipRectf);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    float[] outerR = getRippleCornerValue(clipHeight);
                    setRippleStyle(outerR);
                }
            }
        } else {
            //设置特定圆角
            if (backgroundDrawable == null && backgroundTrueDrawable == null) {
                setSpaceCorner(canvas, clipHeight);
            }
        }
    }

    /**
     * 绘制边框效果
     * @param canvas 画布对象
     * @param rect   layout的可见Rect
     */
    private void drawBorder(Canvas canvas,RectF rect){
        int clipH = (int) RectUtils.calRectHeight(rect);
        int maxCornerRadius = clipH / 2;
        if (backgroundDrawable == null && backgroundTrueDrawable == null) {
            float radius = Math.min(mCornerRadius,maxCornerRadius);
            if(strokeSize > 0.1f && (_strokeColor & 0xFF000000) != 0){
                if (radius > 0.5f) {
                    canvas.drawRoundRect(rect, radius, radius, paint);
                } else {
                    canvas.drawRect(rect, paint);
                }

                //解决边框线太细时，四角的width偏大和其他边不同
                if (_strokeColor != DEFAULT_CODE) {
                    int the_height = (int) RectUtils.calRectHeight(rect);
                    int the_height_stoke = (int) (the_height - strokeSize);
                    int trueCorner = 0;
                    if (mCornerRadius > maxCornerRadius) {
                        //没看懂如何计算的....
                        trueCorner = clipH * the_height_stoke / 2 / the_height;
                    } else {
                        trueCorner = (int) (mCornerRadius * the_height_stoke / the_height);
                    }

                    float halfStrokeSize = strokeSize / 2;
                    strokeRect.set(rect.left + halfStrokeSize,
                            rect.top + halfStrokeSize,
                            rect.right - halfStrokeSize,
                            rect.bottom - halfStrokeSize);
                    canvas.drawRoundRect(strokeRect, trueCorner, trueCorner, paint_stroke);

                }
            }
        }
    }

    // </editor-fold>
// <editor-fold defaultstate="collapsed" desc="对外提供的Get方法">


    public float getCornerRadius() {
        return mCornerRadius;
    }

    /**
     * 获取水波纹效果需要的Corner数组数据
     * @param clipHeight Layout的有效Rect高度
     * @return Ripple需要的数组
     */
    public float[] getRippleCornerValue(int clipHeight) {
        int leftTop;
        int rightTop;
        int rightBottom;
        int leftBottom;
        int maxRadius = clipHeight / 2;
        leftTop = (int) (mCornerRadius_leftTop == RADIUS_INVALID_VALUE ?
                mCornerRadius : mCornerRadius_leftTop);
        leftTop = Math.min(leftTop, maxRadius);

        rightTop = (int) (mCornerRadius_rightTop == RADIUS_INVALID_VALUE ?
                mCornerRadius : mCornerRadius_rightTop);
        rightTop = Math.min(rightTop, maxRadius);

        rightBottom = (int) (mCornerRadius_rightBottom == RADIUS_INVALID_VALUE ?
                mCornerRadius : mCornerRadius_rightBottom);
        rightBottom = Math.min(rightBottom, maxRadius);

        leftBottom = (int) (mCornerRadius_leftBottom == RADIUS_INVALID_VALUE ?
                mCornerRadius : mCornerRadius_leftBottom);
        leftBottom = Math.min(leftBottom, maxRadius);

        return new float[]{leftTop, leftTop, rightTop, rightTop, rightBottom, rightBottom, leftBottom, leftBottom};
    }


    //这里是为了解决stokeWith很大时，圆角盖不住底部四个角的bug(issues #86)
    public float[] getCornerValueOther(int trueHeight, int stokeWith) {
        trueHeight = trueHeight - stokeWith * 2;
        int leftTop;
        int rightTop;
        int rightBottom;
        int leftBottom;
        if (mCornerRadius_leftTop == -1) {
            leftTop = (int) mCornerRadius;
        } else {
            leftTop = (int) mCornerRadius_leftTop;
        }

        if (leftTop > trueHeight / 2) {
            leftTop = trueHeight / 2;
        }

        if (mCornerRadius_rightTop == -1) {
            rightTop = (int) mCornerRadius;
        } else {
            rightTop = (int) mCornerRadius_rightTop;
        }

        if (rightTop > trueHeight / 2) {
            rightTop = trueHeight / 2;
        }

        if (mCornerRadius_rightBottom == -1) {
            rightBottom = (int) mCornerRadius;
        } else {
            rightBottom = (int) mCornerRadius_rightBottom;
        }

        if (rightBottom > trueHeight / 2) {
            rightBottom = trueHeight / 2;
        }


        if (mCornerRadius_leftBottom == -1) {
            leftBottom = (int) mCornerRadius;
        } else {
            leftBottom = (int) mCornerRadius_leftBottom;
        }

        if (leftBottom > trueHeight / 2) {
            leftBottom = trueHeight / 2;
        }
        leftTop = leftTop - stokeWith;
        rightTop = rightTop - stokeWith;
        leftBottom = leftBottom - stokeWith;
        rightBottom = rightBottom - stokeWith;

        return new float[]{leftTop, leftTop, rightTop, rightTop, rightBottom, rightBottom, leftBottom, leftBottom};
    }

// </editor-fold>

    //这是自定义四个角的方法。
    private void setSpaceCorner(Canvas canvas, int trueHeight) {

        float[] outerR = getRippleCornerValue(trueHeight);

        if (_strokeColor != DEFAULT_CODE) {
            if (selectorType != SelectorType.RIPPLE) {
                ShapeDrawable mDrawables = new ShapeDrawable(new RoundRectShape(outerR, null, null));
                if (paint.getShader() != null) {
                    mDrawables.getPaint().setShader(paint.getShader());
                } else {
                    mDrawables.getPaint().setColor(paint.getColor());
                }
                mDrawables.setBounds(leftPadding, topPadding, getWidth() - rightPadding, getHeight() - bottomPadding);
                mDrawables.draw(canvas);

                float[] outerR_stroke = getCornerValueOther(trueHeight, (int) strokeSize);
                ShapeDrawable mDrawablesStroke = new ShapeDrawable(new RoundRectShape(outerR_stroke, null, null));
                mDrawablesStroke.getPaint().setColor(paint_stroke.getColor());
                mDrawablesStroke.getPaint().setStyle(Paint.Style.STROKE);
                mDrawablesStroke.getPaint().setStrokeWidth(strokeSize);
                mDrawablesStroke.setBounds((int) (leftPadding + strokeSize / 2), (int) (topPadding + strokeSize / 2), (int) (getWidth() - rightPadding - strokeSize / 2), (int) (getHeight() - bottomPadding - strokeSize / 2));
                mDrawablesStroke.draw(canvas);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setRippleStyle(outerR);
                }
            }

        } else {
            if (selectorType != SelectorType.RIPPLE) {
                ShapeDrawable mDrawables = new ShapeDrawable(new RoundRectShape(outerR, null, null));
                if (paint.getShader() != null) {
                    mDrawables.getPaint().setShader(paint.getShader());
                } else {
                    mDrawables.getPaint().setColor(paint.getColor());
                }
                mDrawables.setBounds(leftPadding, topPadding, getWidth() - rightPadding, getHeight() - bottomPadding);
                mDrawables.draw(canvas);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setRippleStyle(outerR);
                }
            }
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setRippleStyle(float[] outRadius) {

        // Ripple states
        int[][] stateList = new int[][]{
                new int[]{android.R.attr.state_pressed},
                new int[]{android.R.attr.state_focused},
                new int[]{android.R.attr.state_activated},
                new int[]{}
        };

        // Ripple states color
        int normalColor = backgroundColor;
        int pressedColor = backgroundColorTrue;
        int[] stateColorList = new int[]{
                pressedColor,
                pressedColor,
                pressedColor,
                normalColor
        };
        //The ripple color
        ColorStateList colorStateList = new ColorStateList(stateList, stateColorList);

        RoundRectShape roundRectShape = new RoundRectShape(outRadius, null, null);
        ShapeDrawable maskDrawable = new ShapeDrawable();
        maskDrawable.setShape(roundRectShape);
        maskDrawable.getPaint().setStyle(Paint.Style.FILL);
        if (startColor != DEFAULT_CODE) {
            attatchGradient(maskDrawable.getPaint());
        } else {
            maskDrawable.getPaint().setColor(normalColor);
        }

        ShapeDrawable contentDrawable = new ShapeDrawable();
        contentDrawable.setShape(roundRectShape);
        contentDrawable.getPaint().setStyle(Paint.Style.FILL);

        if (startColor != DEFAULT_CODE) {
            attatchGradient(contentDrawable.getPaint());
        } else {
            contentDrawable.getPaint().setColor(normalColor);
        }

        //contentDrawable实际是默认初始化时展示的；maskDrawable 控制了rippleDrawable的范围
        RippleDrawable rippleDrawable = new RippleDrawable(colorStateList, contentDrawable, maskDrawable);
        targetView.setBackground(rippleDrawable);
    }


    // <editor-fold defaultstate="collapsed" desc="触摸事件处理">
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (selectorType == SelectorType.RIPPLE) {
            //如果是水波纹模式，那么不需要进行下面的渲染，采用系统ripper即可
            if (isClickable) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (selectorType == SelectorType.RIPPLE) {
                            if (mTextView != null) {
                                mTextView.setTextColor(textColor_true);
                                if (!TextUtils.isEmpty(text_true)) {
                                    mTextView.setText(text_true);
                                }
                            }
                        }
                        break;

                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (selectorType == SelectorType.RIPPLE) {
                            if (mTextView != null) {
                                mTextView.setTextColor(textColor);
                                if (!TextUtils.isEmpty(text)) {
                                    mTextView.setText(text);
                                }
                            }
                        }
                        break;


                }
            }
            return super.onTouchEvent(event);
        }

        if (backgroundColorTrue != DEFAULT_CODE || _strokeColorTrue != DEFAULT_CODE || backgroundTrueDrawable != null) {
            if (isClickable) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (selectorType == SelectorType.PRESS) {
                            if (backgroundColorTrue != DEFAULT_CODE) {
                                paint.setColor(backgroundColorTrue);
                                //打个标记
                                paint.setShader(null);
                            }
                            if (_strokeColorTrue != DEFAULT_CODE) {
                                paint_stroke.setColor(_strokeColorTrue);
                            }

                            if (backgroundTrueDrawable != null) {
                                setBackGround(backgroundTrueDrawable);
                            }
                            postInvalidate();

                            if (mTextView != null) {
                                mTextView.setTextColor(textColor_true);
                                if (!TextUtils.isEmpty(text_true)) {
                                    mTextView.setText(text_true);
                                }
                            }
                        }
                        break;

                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (selectorType == SelectorType.PRESS) {
                            //打个标记
                            paint.setColor(backgroundColor);
                            if (startColor != DEFAULT_CODE) {
                                attatchGradient(paint);
                            }
                            if (_strokeColor != DEFAULT_CODE) {
                                paint_stroke.setColor(_strokeColor);
                            }

                            if (backgroundDrawable != null) {
                                setBackGround(backgroundDrawable);
                            }
                            postInvalidate();

                            if (mTextView != null) {
                                mTextView.setTextColor(textColor);
                                if (!TextUtils.isEmpty(text)) {
                                    mTextView.setText(text);
                                }
                            }
                        }
                        break;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    // </editor-fold>
}