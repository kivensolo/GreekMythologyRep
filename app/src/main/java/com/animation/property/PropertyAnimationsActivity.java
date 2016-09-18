package com.animation.property;

import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.BaseActivity;
import com.adapter.BitmapPageAdapter;
import com.kingz.customdemo.R;
import com.utils.ZLog;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/9/17 20:59
 * description: 属性动画
 * http://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650237082&idx=1&sn=73d02232c981d1565bcb2dbe5b10a681&scene=23&srcid=0917SscEyH3EiTM2RASAGJis#rd
 * ----ValueAnimator：是整个属性动画机制当中最核心的一个类，属性动画的运行机制是通过不断地对值进行操作来实现的，
 * 而初始值和结束值之间的动画过渡就是由ValueAnimator这个类来负责计算的。它的内
 * 部使用一种时间循环的机制来计算值与值之间的动画过渡，我们只需要将初始值和结束
 * 值提供给ValueAnimator，并且告诉它动画所需运行的时长，那么ValueAnimator就会
 * 自动帮我们完成从初始值平滑地过渡到结束值这样的效果。除此之外，ValueAnimator
 * 还负责管理动画的播放次数、播放模式、以及对动画设置监听器等
 * ---ObjectAnimator：可以直接对任意对象的任意属性进行动画操作的，比如说View的alpha属性
 */
public class PropertyAnimationsActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static final String TAG = "PropertyAnimationsActivity";
    protected BitmapPageAdapter bitmapAdapter;
    protected ListView mListView;
    protected TextView mTextView;
    protected TextView mRightText;
    protected int backgroundId;
    List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_screen_layout);
        initViews();
    }

    private void initViews() {
        datas = Arrays.asList(strs);
        bitmapAdapter = new BitmapPageAdapter(this, datas);
        mListView = (ListView) findViewById(R.id.anmations_list_id);
        mListView.setAdapter(bitmapAdapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setOnItemClickListener(this);
        mRightText = (TextView) findViewById(R.id.animation_show_text_id);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        bitmapAdapter.notifyDataSetChanged();
        startAnimation(view, position);
    }


    String[] strs = {"透明度变换", "旋转", "X-平移", "Y-缩放", "Y轴拉伸", "Z轴拉伸", "翻转"};
    private void startAnimation(View view, int position) {
        mTextView = (TextView) view.findViewById(R.id.list_item);
        if (mListView.isItemChecked(position)) {
            backgroundId = R.color.deepskyblue;
            mTextView.setTextColor(getResources().getColor(R.color.suncolor));
        } else {
            backgroundId = R.drawable.listview_unchecked;
            mTextView.setTextColor(getResources().getColor(R.color.lightskyblue));
        }
        Drawable background = this.getResources().getDrawable(backgroundId);
        view.setBackground(background);

        String clickedtype = (String) bitmapAdapter.getItem(position);
        ZLog.i(TAG, "onItemClick： chooseTpye = " + clickedtype);

        ObjectAnimator animator = new ObjectAnimator();
        animator.setTarget(mRightText);
        animator.setDuration(5000);
        animator.setRepeatCount(-1);
        switch (position) {
            case 0:
                animator.setPropertyName("alpha");
                animator.setFloatValues(1f,0f,1f);
                break;
            case 1:
                animator.setPropertyName("rotation");
                animator.setFloatValues(0f,360f);
                break;
            case 2:
                float x = mRightText.getTranslationX();
                animator.setPropertyName("translationX");
                animator.setFloatValues(x,-500f,x);
                break;
            case 3:
                animator.setPropertyName("scaleY");
                animator.setFloatValues(1f,4f,1f);
                break;
            default:
                break;
        }
        if(!TextUtils.isEmpty(animator.getPropertyName())){
            animator.start();
        }
    }
}
