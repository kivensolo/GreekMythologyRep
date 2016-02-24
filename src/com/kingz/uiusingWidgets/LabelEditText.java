package com.kingz.uiusingWidgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kingz.uiusingListViews.R;

/**
 * 自定义EditText
 */
public class LabelEditText extends LinearLayout{

	private TextView textView;
	private String labelPosition;
	private String labelText;
	private int labelFontSize;


	public LabelEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		getAttr(context, attrs);
		initView();
	}

	private void initView() {
		textView = (TextView) findViewById(R.id.lable_textview);
		textView.setTextSize(labelFontSize);
		textView.setText(labelText);
	}


	private void getAttr(Context context, AttributeSet attrs) {
		int resourceId = attrs.getAttributeResourceValue(null, "labelText", 0);
		if (resourceId == 0) {
			labelText = attrs.getAttributeValue(null,"labelText");
		}else{
			labelText = getResources().getString(resourceId);
		}
		if (labelText == null) {
			throw new RuntimeException(" labelText is null ~!");
		}

		resourceId = attrs.getAttributeResourceValue(null, "labelFontSize",0);
		if (resourceId == 0) {
			labelFontSize = attrs.getAttributeIntValue(null,"labelFontSize",12);
		}else{
			labelFontSize = getResources().getInteger(resourceId);
		}

		resourceId = attrs.getAttributeResourceValue(null, "labelPosition", 0);
		if (resourceId == 0) {
			labelPosition = attrs.getAttributeValue(null,"labelPosition");
		}else{
			labelPosition = getResources().getString(resourceId);
		}

		if (labelPosition == null) {
			labelPosition = "left";
		}

		String infService = Context.LAYOUT_INFLATER_SERVICE; //layout_inflater
		LayoutInflater inflater;

		//获取到系统级别的layout_inflater
		inflater = (LayoutInflater) context.getSystemService(infService);

		LinearLayout linearLayout = null;
		if("left".equals(labelPosition)){
			linearLayout = (LinearLayout) inflater.inflate(R.layout.labeledittext_horizontal, this);
		}else if("top".equals(labelPosition)){
			linearLayout = (LinearLayout) inflater.inflate(R.layout.labeledittext_vertical, this);
		}else{
			throw new RuntimeException("position not left and top");
		}
	}

}
