package com.kingz.uiusingWidgets;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;
import android.widget.TextView;
import com.kingz.uiusingListViews.R;

/**
 * @author: KingZ
 * @Description:  原生seekBar
 */
public class SeekBarActivity extends Activity{

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
		mSeekBarDefault = (SeekBar) findViewById(R.id.seekBar_default);
		tv_1 = (TextView) findViewById(R.id.skbdefault_tv1);
		tv_2 = (TextView) findViewById(R.id.skbdefault_tv2);
	}

	private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			tv_1.setText("EndTouch");
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			tv_1.setText("StartTouch");
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			tv_1.setText("值改变中...");
			Message message = new Message();
			Bundle bundle = new Bundle();
			int currentPos = mSeekBarDefault.getProgress();
			bundle.putInt("key", currentPos);
			message.setData(bundle);
			message.what = 0;
			handler.sendMessage(message);
		}
	};

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tv_2.setText(msg.getData().getInt("key") + "/1000");
        }
    };
}
