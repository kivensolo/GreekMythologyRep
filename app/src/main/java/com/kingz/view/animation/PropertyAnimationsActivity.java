package com.kingz.view.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.kingz.adapter.PropertyAdapter;
import com.kingz.customdemo.R;
import com.kingz.module.common.base.BaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author: King.Z
 * date:  2016/9/17 20:59
 * description: 属性动画展示
 */
public class PropertyAnimationsActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    public static final String TAG = "PropertyAnimationsActivity";
    protected PropertyAdapter propertyAdapter;
    protected ListView mListView;
    protected TextView mRightText;
    List<String> datas;

    ObjectAnimator animatorAlpha = new ObjectAnimator();
    ObjectAnimator animatorRotation = new ObjectAnimator();
    ObjectAnimator animatorRotationX = new ObjectAnimator();
    ObjectAnimator animatorRotationY = new ObjectAnimator();
    ObjectAnimator animatorTranslationX = new ObjectAnimator();
    ObjectAnimator animatorScaleY = new ObjectAnimator();
    ObjectAnimator animatorColor = new ObjectAnimator();
    ObjectAnimator animatorZ = new ObjectAnimator();

    AnimatorSet animatorSet;
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
        mListView = findViewById(R.id.anmations_list_id);
        mRightText = findViewById(R.id.animation_show_text_id);
        initViews();
    }

    private void initViews() {
        datas = Arrays.asList(strs);
        propertyAdapter = new PropertyAdapter(datas);
        mListView.setAdapter(propertyAdapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setOnItemClickListener(this);
        initAnimator();
    }

    //strs与values顺序对应
    String[] strs = {"alpha", "rotation", "rotationX", "rotationY",
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
        if (mListView.isItemChecked(position)) {
            propertyAdapter.addClickedPos(position);
        }else{
            propertyAdapter.removeClickedPos(position);
        }
        propertyAdapter.notifyDataSetChanged();
//        changeListItemStyle(view, position);
        changeAnimation(position);
    }

    private void changeAnimation(int position) {
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
