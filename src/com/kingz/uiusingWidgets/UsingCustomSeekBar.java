package com.kingz.uiusingWidgets;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kingz.uiusingListViews.R;

/**
 * @author: KingZ
 * @Data: 2015-10-4   11:16:07
 * @Description:自定义进度条
 */
public class UsingCustomSeekBar extends Activity{

    private SeekBar mSeekBarSelfPic;
    private SeekBar mSeekBarSelfColor;
    private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_seek_bar);

		initView();
		mSeekBarSelfPic.setOnSeekBarChangeListener(mSeekBarChangeListener);
	}

	private void initView() {
		tv = (TextView) findViewById(R.id.textView1);
		mSeekBarSelfPic = (SeekBar) findViewById(R.id.seekbar_self);
		mSeekBarSelfColor = (SeekBar) findViewById(R.id.seekbar_self_2);
	}

	/**
	 *  seekBar状态改变监听器
	 */
	private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

		}
	};
}
