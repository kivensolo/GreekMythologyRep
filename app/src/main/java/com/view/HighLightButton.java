package com.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import com.kingz.customDemo.R;

/**
 * 自定义高亮按钮
 */
public class HighLightButton extends Button{
	public static final int TEXT_SIZE = 26;
	// 选定标记
	private boolean selectedFalg = false;

	public boolean isSelectedFalg(){
		return selectedFalg;
	}

	/**
	 * 设置选中样式
	 * @param selectedFlag
     */
	public void setSelectedFalg(boolean selectedFlag){
		this.selectedFalg = selectedFlag;
		if (selectedFlag){
			setTextColor(Color.WHITE);
			setBackgroundResource(R.drawable.text_selected_bg_shape);
		} else{
			init();
			this.setBackground(null);
		}
	}

	@SuppressWarnings("unused")
	private static final int SELECTED_COLOR = 0xFF3892FF;

	public HighLightButton(Context context)
	{
		super(context);
		init();
	}

	// public void setCallBack(BtnStatusCallBack callBack)
	// {
	// this.callBack = callBack;
	// }

	/**
	 * 初始化视图
	 */
	private void init(){
		this.setTextColor(0xFF646464);
		this.setBackgroundColor(Color.TRANSPARENT);
		this.setGravity(Gravity.CENTER);
		this.setTextSize(TypedValue.COMPLEX_UNIT_PX, TEXT_SIZE);
		getPaint().setFakeBoldText(false);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		if (focused) {
			setTextColor(Color.WHITE);
			setTextSize(TypedValue.COMPLEX_UNIT_PX,TEXT_SIZE);
			getPaint().setFakeBoldText(true);// 加粗
			getPaint().setShadowLayer(3, 1, 1, Color.BLACK);
			this.setBackgroundResource(R.drawable.text_selected_bg_shape);
		} else if (selectedFalg) {
			setTextColor(Color.WHITE);
			setTextSize(TypedValue.COMPLEX_UNIT_PX,TEXT_SIZE);
			getPaint().setFakeBoldText(false);// 加粗
			getPaint().setShadowLayer(3, 1, 1, Color.BLACK);
			this.setBackgroundResource(R.drawable.text_focused_bg_shape);
		} else {
			setTextColor(0xFF666666);
			setBackgroundColor(Color.TRANSPARENT);
			setTextSize(TypedValue.COMPLEX_UNIT_PX,TEXT_SIZE);
			getPaint().setFakeBoldText(false);
			// getPaint().setShadowLayer(0, 0, 0, Color.TRANSPARENT);
			this.setBackground(null);
		}
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

}
