package com.kingz.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingz.customdemo.R;

/**
 * 自定义EditText
 */
public class CustomEditText extends LinearLayout{

	private TextView textView;
	private String labelPosition;
	private String labelText;
	private int labelFontSize;
	private int labelFontColor;
	private	int resourceId = 0;


	public static final int FONT_DEFAULT_VALUE = 12;
	public static final int FONT_DEFAULT_COLOR = R.color.bright_green;

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		getAttr(context, attrs);
		initView();
	}

	private void initView() {
		View editView = findViewById(R.id.edittext);
		textView = (TextView) findViewById(R.id.lable_textview);
		textView.setTextSize(labelFontSize);
		textView.setTextColor(labelFontColor);
		textView.setText(labelText);
	}


	private void getAttr(Context context, AttributeSet attrs) {
		resolveLabelText(attrs);
		resolveLabelFontSize(attrs);
		resolveLabelFontColor(attrs);
		resolveLabelPosition(attrs);

		String infService = Context.LAYOUT_INFLATER_SERVICE; //layout_inflater
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(infService);//获取到系统级别的layout_inflater
		if("left".equals(labelPosition)){
			inflater.inflate(R.layout.labeledittext_horizontal, this);
		}else if("top".equals(labelPosition)){
			inflater.inflate(R.layout.labeledittext_vertical, this);
		}else{
			throw new RuntimeException("position not left and top");
		}
	}

	private void resolveLabelPosition(AttributeSet attrs) {
		resourceId = attrs.getAttributeResourceValue(null, "labelPosition", 0);
		if (resourceId == 0) {
			labelPosition = attrs.getAttributeValue(null,"labelPosition");
		}else{
			labelPosition = getResources().getString(resourceId);
		}
		if (labelPosition == null) {
			labelPosition = "left";
		}
	}

	private void resolveLabelFontColor(AttributeSet attrs) {
		resourceId = attrs.getAttributeResourceValue(null, "labelFontColor",0);
		if (resourceId == 0) {
			labelFontColor = attrs.getAttributeIntValue(null,"labelFontColor", FONT_DEFAULT_COLOR);
		}else{
			labelFontColor = getResources().getInteger(resourceId);
		}
	}

	private void resolveLabelFontSize(AttributeSet attrs) {
		resourceId = attrs.getAttributeResourceValue(null, "labelFontSize",0);
		if (resourceId == 0) {
			labelFontSize = attrs.getAttributeIntValue(null,"labelFontSize", FONT_DEFAULT_VALUE);
		}else{
			labelFontSize = getResources().getInteger(resourceId);
		}
	}

	private void resolveLabelText(AttributeSet attrs) {
		resourceId = attrs.getAttributeResourceValue(null, "labelText", 0);
		if (resourceId == 0) {
			labelText = attrs.getAttributeValue(null,"labelText");
		}else{
			labelText = getResources().getString(resourceId);
		}
		if (labelText == null) {
			throw new RuntimeException(" labelText is null ~!");
		}
	}

}
