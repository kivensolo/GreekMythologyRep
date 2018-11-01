package com.kingz.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kingz.customdemo.R;
import com.module.tools.ScreenTools;

import java.text.DecimalFormat;

/**
 * 频道列表的item
 */
public class ChannelItemView extends LinearLayout {
    /**
     * 频道编号
     */
    private TextView txtChannelNum;
    /**
     * 频道名称
     */
    private TextView txtChannelName;
    /**
     * 频道编号默认颜色
     */
    private static final int CHANNEL_NO_DEFAULT_COLOR = 0xFF939393;
    /**
     * 频道名称默认颜色
     */
    private static final int CHANNEL_NAME_DEFAULT_COLOR = 0xFF7E7E7E;
    /**
     * 频道选定后文字颜色
     */
    private static final int CHANNEL_NAME_CLICK_COLOR = 0xFFFFFFFF;
    private boolean click;
    private Context mContext;
    private int numberTextSize = 18;
    private int titleTextSize = 26;
    private String currentNum;

    private Drawable numBackgroundDrawable = null;
    private int type;

    public ChannelItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setGravity(Gravity.CENTER);
        initViews();
    }

    public void hideNumImage() {
        if (txtChannelNum != null) {
            txtChannelNum.setVisibility(View.INVISIBLE);
        }
    }

    public ChannelItemView(Context context) {
        super(context);
        mContext = context;
        setGravity(Gravity.CENTER);
        initViews();
    }

    private void initViews() {
        txtChannelNum = new TextView(mContext);
        txtChannelNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenTools.Operation(numberTextSize));
        txtChannelNum.setTextColor(CHANNEL_NO_DEFAULT_COLOR);
        txtChannelNum.setGravity(Gravity.CENTER);
        addView(txtChannelNum, ScreenTools.Operation(56), ScreenTools.Operation(44));

        txtChannelName = new TextView(mContext);
        txtChannelName.setSingleLine();
        txtChannelName.setEllipsize(TruncateAt.MARQUEE);
        txtChannelName.setDuplicateParentStateEnabled(true);
        txtChannelName.setShadowLayer(3, 1, 1, Color.BLACK);
        txtChannelName.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenTools.Operation(titleTextSize));
        txtChannelName.setTextColor(CHANNEL_NAME_DEFAULT_COLOR);
        txtChannelName.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        addView(txtChannelName, ScreenTools.Operation(160), ScreenTools.Operation(48));
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (type == 1 && selected) {
            txtChannelNum.setText("VIP");
        } else {
            txtChannelNum.setText(currentNum);
        }
        if (selected) {
            txtChannelName.setTextColor(Color.WHITE);
            setBackgroundResource(R.drawable.text_selected_bg_shape);
        } else if (click) {
            txtChannelName.setTextColor(CHANNEL_NAME_CLICK_COLOR);
            setBackgroundResource(R.drawable.text_focused_bg_shape);
        } else {
            txtChannelName.setTextColor(CHANNEL_NAME_DEFAULT_COLOR);
            setBackgroundDrawable(null);
        }
    }

    public void setClick(boolean selected) {
        click = selected;
        if (isSelected()) {
            txtChannelName.setTextColor(Color.WHITE);
            setBackgroundResource(R.drawable.text_selected_bg_shape);
        } else if (click) {
            txtChannelName.setTextColor(CHANNEL_NAME_CLICK_COLOR);
            setBackgroundResource(R.drawable.text_focused_bg_shape);
        } else {
            txtChannelName.setTextColor(CHANNEL_NAME_DEFAULT_COLOR);
            setBackgroundDrawable(null);
        }
    }

    public boolean isClick() {
        return click;
    }

    public void setItemFontSize(int numSize, int nameSize) {
        numberTextSize = numSize;
        titleTextSize = nameSize;
        txtChannelNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenTools.Operation(numberTextSize));
        txtChannelName.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenTools.Operation(titleTextSize));
    }

    public void setItemAttribute(int channelNum, String channelName) {
        DecimalFormat df = new DecimalFormat("000");
        currentNum = df.format(channelNum);
        txtChannelNum.setText(currentNum);
        txtChannelName.setText(channelName);

    }

    public void setItemNumBackground(Bitmap bit, int type) {
        this.type = type;
        this.numBackgroundDrawable = new BitmapDrawable(bit);
        txtChannelNum.setBackgroundDrawable(numBackgroundDrawable);
    }
}
