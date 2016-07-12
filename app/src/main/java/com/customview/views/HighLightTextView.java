package com.customview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;
import com.kingz.uiusingListViews.R;

public class HighLightTextView extends TextView {
	@SuppressWarnings("unused")
	private static final int SELECTED_BACKGOURD_COLOR = 0xFF3892FF;
	private boolean isSelected = false;
	private boolean bgSelectedStyle = false;
	private int DEFAULT_COLOR = 0XFFd6d6d6;

	public boolean isBgSelectedStyle() {
		return bgSelectedStyle;
	}

	public void setBgSelectedStyle(boolean bgSelectedStyle) {
		this.bgSelectedStyle = bgSelectedStyle;
	}

	private String focusText = null;

	/**
	 * 设置焦点内容
	 *
	 * @param str
	 *            焦点内容
	 */
	public void setFocusText(String str) {
		focusText = str;
		lightFocus();
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		if (focused) {
			setSelected(true);
		} else {
			setSelected(false);
		}
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		if (isSelected == selected)
			return;
		isSelected = selected;
		if (isSelected) {
			setBackgroundResource(R.drawable.text_selected_bg_shape);
			// setBackgroundColor(SELECTED_BACKGOURD_COLOR);
		} else {
			// setBackgroundColor(Color.TRANSPARENT);
			setBackgroundDrawable(null);
		}
		if (isSelected || hightLight) {
			this.setTextColor(Color.WHITE);
			if (!isSelected) {
				this.setBackgroundResource(R.drawable.text_focused_bg_shape);
			}
		} else {

			this.setTextColor(DEFAULT_COLOR);
		}
	}

	public void setDefaultColor(int color) {

		this.DEFAULT_COLOR = color;
	}

	private boolean hightLight = false;

	public void setHightLight(boolean value) {
		if (hightLight == value) {
			return;
		}
		hightLight = value;
		if (hightLight || isSelected) {
			this.setTextColor(Color.WHITE);
			if (!isSelected) {
				this.setBackgroundResource(R.drawable.text_focused_bg_shape);
			}
		} else {

			this.setTextColor(DEFAULT_COLOR);
			this.setBackgroundDrawable(null);
		}
	}

	private void lightFocus() {
		String scrText = getText().toString();
		if (focusText != null && scrText != null) {
			SpannableStringBuilder style = new SpannableStringBuilder(scrText);
			int start = scrText.indexOf(focusText);
			if (start != -1) {
				int end = start + focusText.length();
				style.setSpan(new ForegroundColorSpan(Color.BLUE), start, end,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

				setText(style);
			}
		}
	}

	public HighLightTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setTextColor(DEFAULT_COLOR);
		setTextSize(TypedValue.COMPLEX_UNIT_PX, 24);
	}

	public HighLightTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public HighLightTextView(Context context) {
		this(context, null);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (hightLight) {
			//setTextSize(TypedValue.COMPLEX_UNIT_PX, App.OperationHeight(24));
			getPaint().setShadowLayer(3, 1, 1, Color.BLACK);
		} else {
			//setTextSize(TypedValue.COMPLEX_UNIT_PX, App.OperationHeight(24));
			// getPaint().setFakeBoldText(false);//加粗
			getPaint().setShadowLayer(0, 0, 0, Color.TRANSPARENT);
		}
		getPaint().setFakeBoldText(isSelected);// 加粗
		super.onDraw(canvas);
	}
}
