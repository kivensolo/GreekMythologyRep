package com.kingz.pages;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.BaseActivity;
import com.kingz.widgets.CusSeekBarView;

/**
 * @author: KingZ
 * @Description:
 */
public class CustomCanvasSeekBarAct extends BaseActivity implements OnTouchListener {

	private static String TAG = CustomCanvasSeekBarAct.class.getSimpleName();

	private int progressBarWidth = 1000;
	private int progressPaddingLeft = 20;
	private int progressPaddingTop = 50;
	private static final int MSG_NUM = 1280;

	CusSeekBarView myProgressBar;		//香港项目的进度条


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.custom_canvas_seekbar);

		initView();

		RelativeLayout main_layout = new RelativeLayout(this);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(-1,-1);
		main_layout.setLayoutParams(lp);
		super.setContentView(main_layout);

		Button btn = new Button(this);
		btn.setText("点击显示进度条");
		btn.setLayoutParams(new ViewGroup.LayoutParams(-2,-2));
		btn.setX(300);
		btn.setY(200);
		main_layout.addView(btn);
		main_layout.addView(myProgressBar);

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				myProgressBar.startPlayerTimer();
			}
		});
	}

	private void initView() {
//		seekbar = (SeekBarView) findViewById(R.id.mplayer_seekbar);
//		RelativeLayout.LayoutParams lpSeekbar = new RelativeLayout.LayoutParams(1280, 150);
//        lpSeekbar.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        lpSeekbar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        seekbar.initUIPara(progressPaddingLeft, progressBarWidth, progressPaddingTop, progressPaddingTop + 25);
//        seekbar.setLayoutParams(lpSeekbar);

		myProgressBar = new CusSeekBarView(this);
//		RelativeLayout.LayoutParams lpSeekbar = new RelativeLayout.LayoutParams(1000, 100);
//		lpSeekbar.addRule(RelativeLayout.CENTER_HORIZONTAL);
//		myProgressBar.setLayoutParams(lpSeekbar);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(isShow()){
//			seekbar.setVisibility(View.INVISIBLE);
			myProgressBar.setVisibility(View.INVISIBLE);
		}else if(!isShow()){
//			seekbar.setVisibility(View.VISIBLE);
			myProgressBar.setVisibility(View.VISIBLE);
		}
		return false;
	}

	private boolean isShow() {
		return myProgressBar.isShown();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
