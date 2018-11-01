package com.kingz.customviews;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.kingz.customdemo.R;
import com.kingz.utils.ZLog;
import com.module.tools.ScreenTools;

import java.util.ArrayList;

/**
 * author: King.Z <br>
 * date:  2017/9/28 19:32 <br>
 *     卡片式折叠效果
 */
public class FoldingCardView extends LinearLayout {
	public static final String TAG = "FoldingCardView";

    private ScrollView scrollView;
	private LinearLayout subLayout;
	private ArrayList<String> infoList = null;


    public FoldingCardView(Context context) {
        this(context,null);
    }

    public FoldingCardView(Context context, @Nullable AttributeSet attrs) {
        this(context,null,0);
    }

    public FoldingCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(LinearLayout.VERTICAL);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		this.setLayoutParams(params);


        //initViews
        scrollView = new ScrollView(context);
		subLayout = new LinearLayout(context);
		subLayout.setOrientation(LinearLayout.VERTICAL);
	    android.view.ViewGroup.LayoutParams lps = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		scrollView.addView(subLayout, lps);

    }


    	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		ZLog.i(TAG, "dispatchKeyEvent refresh");
		this.postInvalidate();
		return super.dispatchKeyEvent(event);
	}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    class CardItem extends LinearLayout{
        private  String tag = "CardItem";
        private Context context = null;
        private TextView caTextView = null;
        private ScrollView parentScrollView = null;

        public CardItem(Context context) {
            super(context);
            initCardView();
            initCardItem(context);
        }

        private void initCardView() {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            this.setLayoutParams(params);
            this.setOrientation(LinearLayout.VERTICAL);
        }

        private void initCardItem(Context context) {
            caTextView = new TextView(context);
            caTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenTools.Operation(24));
            caTextView.setTextColor(Color.WHITE);
            caTextView.getPaint().setFakeBoldText(true);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = ScreenTools.OperationHeight(140);
            layoutParams.rightMargin = ScreenTools.OperationHeight(137);
            this.addView(caTextView,layoutParams);

            ImageView lineImageView = new ImageView(context);
            lineImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            lineImageView.setImageResource(R.drawable.hou);
            LayoutParams lineLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,ScreenTools.OperationHeight(2));
            lineLayoutParams.topMargin = ScreenTools.OperationHeight(10);
            lineLayoutParams.leftMargin = ScreenTools.OperationHeight(140);
            lineLayoutParams.rightMargin = ScreenTools.OperationHeight(137);
            this.addView(lineImageView, lineLayoutParams);
        }

        public void setCardData(ArrayList<String> cardData){
            caTextView.setText("莫阿你搜的老师内地哦啊接哦");

        }
    }

    public void moveToTop() {
		//清空 OR 刷新数据
        //清空
        subLayout.removeAllViews();
        infoList.remove(infoList);

        //subLayout.addView(xxx);

        scrollView.scrollTo(0, 0);
	}

}
