package com.kingz.view.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.base.BaseActivity;
import com.kingz.customdemo.R;
import com.kingz.pages.photo.adapter.PropertyAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/9/17 20:59
 * description: 属性动画
 *
 * http://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650237082&idx=1&sn=73d02232c981d1565bcb2dbe5b10a681&scene=23&srcid=0917SscEyH3EiTM2RASAGJis#rd
 *
 * ----【ValueAnimator】：属性动画机制当中最核心的一个类;
 * 属性动画的运行机制是通过不断地对值进行操作来实现的，
 * 初始值和结束值之间的动画过渡就是由ValueAnimator这个类来负责计算的。
 * 内部使用一种时间循环的机制来计算值与值之间的动画过渡。
 * 负责管理动画的播放次数、播放模式、以及对动画设置监听器等
 *
 * ----【ObjectAnimator】：可以直接对任意对象的任意属性进行动画操作的，
 * 比如说View的alpha属性
 * <p/>
 * ----【AnimatorSet】 这个类提供了一个play()方法，如果我们向这个方法中传入一个Animator对象
 * (ValueAnimator或ObjectAnimator)将会返回一个AnimatorSet.Builder的实例，
 * AnimatorSet.Builder中包括以下四个方法：
 * after(Animator anim) 将现有动画插入到传入的动画之后执行
 * after(long delay) 将现有动画延迟指定毫秒后执行
 * before(Animator anim) 将现有动画插入到传入的动画之前执行
 * with(Animator anim) 将现有动画和传入的动画同时执行
 *
 *  除了set，还可以以下方式：
 * 用ValuesHolder  ------->
 *         PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("rotationX", 0f, 360f);
 *         PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f);
 *         PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f);
 *         ObjectAnimator holderAnim = {@link ObjectAnimator}ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ);
 *         holderAnim.setDuration(5000);
 *         holderAnim.setRepeatCount(100);
 *         holderAnim.start();
 *
 * addUpdateListener ------->
 *         ObjectAnimator anim = ObjectAnimator.ofFloat(mTextView, "kingz", 1.0F, 0.0F);//自定义属性名
 *         anim.setDuration(5000);
 *         anim.setRepeatCount(100);
 *         anim.start();
 *         anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
 *             public void onAnimationUpdate(ValueAnimator animation) {
 *                 float cVal = (Float) animation.getAnimatedValue();
 *                mTextView.setAlpha(cVal);
 *                mTextView.setScaleX(cVal);
 *                mTextView.setScaleY(cVal);
 *            }
 *         });
 * <p/>
 * ----【AnimatorListener】：监听动画的各种事件，比如动画何时开始，何时结束，
 * Animator类当中提供了一个addListener()方法，
 * 这个方法接收一个AnimatorListener，
 * 只需要去实现这个AnimatorListener就可以监听动画的各种事件了。
 */
public class PropertyAnimationsActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    public static final String TAG = "PropertyAnimationsActivity";
    protected PropertyAdapter propertyAdapter;
    @BindView(R.id.anmations_list_id)
    protected ListView mListView;
    @BindView(R.id.animation_show_text_id)
    protected TextView mRightText;
    List<String> datas;

    AnimatorSet animatorSet;
    ObjectAnimator animatorAlpha = new ObjectAnimator();
    ObjectAnimator animatorRotation = new ObjectAnimator();
    ObjectAnimator animatorRotationX = new ObjectAnimator();
    ObjectAnimator animatorRotationY = new ObjectAnimator();
    ObjectAnimator animatorTranslationX = new ObjectAnimator();
    ObjectAnimator animatorScaleY = new ObjectAnimator();
    ObjectAnimator animatorColor = new ObjectAnimator();
    ObjectAnimator animatorZ = new ObjectAnimator();

    List<Animator> animatorList = new ArrayList<>();
    List<ObjectAnimator> allAnimator = new ArrayList<>();

    private static final int RED = 0xffFF8080;
    private static final int BLUE = 0xff8080FF;
    private static final int CYAN = 0xff80ffff;
    private static final int GREEN = 0xff80ff80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_screen_layout);
        initViews();
    }

    private void initViews() {
        datas = Arrays.asList(strs);
        propertyAdapter = new PropertyAdapter(this, datas);
        mListView.setAdapter(propertyAdapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setOnItemClickListener(this);
        initAnimator();
    }

    //strs与values顺序对应
    String[] strs = {"alpha", "rotation", "rotationX","rotationY",
            "scaleY", "TranslationX", "BackgroundColor"};
    float[][] values = {{1f, 0f, 1f},
                        {0f, 360f},
                        {0f, 360f},
                        {0f, 360f},
                        {1f, 5f, 1f}};

    private void initAnimator() {
        animatorSet = new AnimatorSet();
        allAnimator.add(animatorAlpha);
        allAnimator.add(animatorRotation);
        allAnimator.add(animatorRotationX);
        allAnimator.add(animatorRotationY);
        allAnimator.add(animatorScaleY);
        allAnimator.add(animatorTranslationX);
        allAnimator.add(animatorColor);
//        allAnimator.add(animatorZ);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        propertyAdapter.notifyDataSetChanged();
        changeAnimation(view, position);
    }

    private void changeAnimation(View view, int position) {
        TextView textView = (TextView) view.findViewById(R.id.list_item);
        changeListItemStyle(view, textView, position);

        //String clickedtype = (String) propertyAdapter.getItem(position);
        //ZLog.i(TAG, "onItemClick： chooseTpye = " + clickedtype);
        if (animatorSet.isRunning()) {
            animatorSet.end();
        }
        animatorSet = new AnimatorSet();//清空set
        if (position < 5) {
            detalFloatAction(position, allAnimator.get(position), strs[position], values[position]);
        } else {
            switch (position) {
                case 5:
                    float x = mRightText.getTranslationX();
                    detalFloatAction(position, allAnimator.get(position), "translationX", x, 500f, x);
                    break;
                case 6:
                    detalIntAction(position, allAnimator.get(position), "backgroundColor", CYAN, BLUE, RED);
                    break;
                default:
                    break;
            }
        }
        startOrEndAnimation();
    }

    private void startOrEndAnimation() {
        if (animatorList.size() > 0) {
            animatorSet.playTogether(animatorList);
            animatorSet.start();
        } else {
            animatorSet.end();
        }
    }

    private void changeListItemStyle(View view, TextView textView, int position) {
        int textColor;
        int bkgColor;
        if (mListView.isItemChecked(position)) {
            bkgColor = R.color.deepskyblue;
            textColor = R.color.suncolor;
        } else {
            bkgColor = R.drawable.listview_unchecked;
            textColor = R.color.lightskyblue;
        }
        Drawable background = this.getResources().getDrawable(bkgColor);
        view.setBackground(background);
        textView.setTextColor(getResources().getColor(textColor));
    }

    public void setFloatAnimation(ObjectAnimator animator, String name, int setRepeatCount, float... values) {
        animatorList.add(animator);
        animator.setTarget(mRightText);
        animator.setPropertyName(name);
        animator.setDuration(5000);
        animator.setRepeatCount(setRepeatCount);
        animator.setFloatValues(values);
    }

    public void setIntAnimation(ObjectAnimator animator, String name, int setRepeatCount, int... values) {
        animatorList.add(animator);
        animator.setTarget(mRightText);
        animator.setPropertyName(name);
        animator.setDuration(5000);
        animator.setRepeatCount(setRepeatCount);
        animator.setIntValues(values);
    }

    public void removeAnimation(ObjectAnimator animator) {
//        ZLog.i(TAG, "removeAnimation() ---> " + animator.getPropertyName());
        animator.cancel();
        animatorList.remove(animator);
    }

    public void detalFloatAction(int position, ObjectAnimator animator, String animatorName, float... values) {
        if (!mListView.isItemChecked(position)) {
            removeAnimation(animator);
        } else {
            setFloatAnimation(animator, animatorName, 100, values);
        }
    }

    public void detalIntAction(int position, ObjectAnimator animator, String animatorName, int... values) {
        if (!mListView.isItemChecked(position)) {
            removeAnimation(animator);
        } else {
            if (animator == animatorColor) {
                animator.setEvaluator(new ArgbEvaluator());
            }
            setIntAnimation(animator, animatorName, 100, values);
        }
    }
}
