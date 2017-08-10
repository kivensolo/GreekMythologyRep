package com.nativeWidgets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kingz.customdemo.R;

/**
 * @author: KingZ
 * @Description:  原生seekBar
 */
@SuppressLint("Registered")
public class SeekBarNative extends Activity{

    private SeekBar mSeekBarDefault;
    private TextView tv_1;
    private TextView tv_2;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seek_bar_src);

		initView();
		mSeekBarDefault.setOnSeekBarChangeListener(mSeekBarChangeListener);
	}

	private void initView() {
		// TODO Auto-generated method stub
		mSeekBarDefault = (SeekBar) findViewById(R.id.seekBar_default);
		tv_1 = (TextView) findViewById(R.id.skbdefault_tv1);
		tv_2 = (TextView) findViewById(R.id.skbdefault_tv2);
	}

	private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			tv_1.setText("EndTouch");
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			tv_1.setText("StartTouch");
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			tv_1.setText("值改变中...");
			Message message = new Message();
			Bundle bundle = new Bundle();	// 锟斤拷锟斤拷锟斤拷锟?
			int currentPos = mSeekBarDefault.getProgress();
			bundle.putInt("key", currentPos);
			message.setData(bundle);
			message.what = 0;
			handler.sendMessage(message);
		}
	};

	 /**
     * 锟斤拷Handler锟斤拷锟斤拷锟斤拷UI
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tv_2.setText(msg.getData().getInt("key") + "/1000");
        }
    };
}
