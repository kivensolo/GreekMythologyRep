package com.kingz.uiusingLayout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewParent;
import android.widget.LinearLayout;
import com.kingz.customDemo.R;

/**
 * @author: KingZ
 */
public class LinearLayoutTest_Act extends Activity{

	public static final String TAG = "LinearLayoutTest_Act";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.linearlayout_test);
		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.layout_main_id);
		ViewParent parentView = mainLayout.getParent();
		//如郭神所说，在setContentView()方法中，Android会自动在布局文件的最外层再嵌套一个FrameLayout
		//所以控件的layout_width等属性才是有效的  并且id叫做android:id/content 所以才叫setContentView()而不是setView()
		Log.d(TAG, "根布局的父类" + parentView);
	}
}
