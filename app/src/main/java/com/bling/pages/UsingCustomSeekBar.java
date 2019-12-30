package com.bling.pages;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.kingz.customdemo.R;

/**
 * @author: KingZ
 * @Data: 2015-10-4   11:16:07
 * @Description:自定义SeekBar
 */
public class UsingCustomSeekBar extends Activity{

    private SeekBar mSeekBarSelfPic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_seek_bar);
		initView();
	}

	private void initView() {
		mSeekBarSelfPic = (SeekBar) findViewById(R.id.seekbar_self);
		mSeekBarSelfPic.setOnSeekBarChangeListener(mSeekBarChangeListener);
	}

	/**
	 *  seekBar状态改变监听器
	 */
	private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

		}
	};
}
