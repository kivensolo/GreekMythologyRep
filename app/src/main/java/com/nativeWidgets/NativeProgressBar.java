package com.nativeWidgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.kingz.customdemo.R;
import com.mplayer.ApolloSeekBar;


public class NativeProgressBar extends Activity implements OnClickListener{

	private ProgressBar progress;
	private TextView text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progressbar_test);
		initlayout();

		int first = progress.getProgress();//得到A进度
		int second = progress.getSecondaryProgress();//得到B进度
		int max = progress.getMax();//获取seekBar最大值
		text.setText("fiorstBar:"+(int)(first/(float)max*100)+"% ;Secondbar:"+(int)(second/(float)max*100)+"%");
	}

	/**
	 * 初始化layout
	 */
	private void  initlayout() {
		progress = (ProgressBar) findViewById(R.id.horiz);
		//ApolloSeekBar seekBar = (ApolloSeekBar) findViewById(R.id.mediaplayer_seekbar);
		findViewById(R.id.add).setOnClickListener(this);
		findViewById(R.id.reduce).setOnClickListener(this);
		findViewById(R.id.reset).setOnClickListener(this);
		findViewById(R.id.clear).setOnClickListener(this);
		findViewById(R.id.alertdialog).setOnClickListener(this);
		text = (TextView)findViewById(R.id.text);
		ApolloSeekBar seekBar = new ApolloSeekBar(this);
		addContentView(seekBar,new ViewGroup.LayoutParams(-1,-1));
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.add:{
				progress.incrementProgressBy(15);
				progress.incrementSecondaryProgressBy(15);
				break;
				}
			case R.id.reduce:{
				progress.incrementProgressBy(-15);
				progress.incrementSecondaryProgressBy(-15);
				break;
			}
			case R.id.reset:{
				progress.setProgress(50);
				progress.setSecondaryProgress(80);
				break;
			}
			case R.id.clear:{
				progress.setProgress(0);
				progress.setSecondaryProgress(0);
				break;
			}
			case R.id.alertdialog:{
				AlertDialog.Builder dialog = new AlertDialog.Builder(NativeProgressBar.this);
				dialog.setTitle("提示：");
				dialog.setMessage("原生seekBar");
				dialog.setCancelable(false);
				dialog.setPositiveButton("确定",new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which){
							finish();
						}
					});
				dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
				dialog.show();
				break;
			}
			default:
				break;
		}
		text.setText("第一条:"+(int)(progress.getProgress()/(float)progress.getMax()*100)
					+"%   第二条:"+(int)(progress.getSecondaryProgress()/(float)progress.getMax()*100)+"%");
	}


}
