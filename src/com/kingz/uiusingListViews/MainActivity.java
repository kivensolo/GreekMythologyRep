package com.kingz.uiusingListViews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.AsuyncTaskJsonTest.AsynctaskBitmapMainActivity;
import com.baidudemo.BaiduMapActivity;
import com.kingz.controls.BasicControlsActivity;
import com.kingz.filemanager.FileManagerActivity;
import com.kingz.uiusingActivity.BitmapActivity;
import com.kingz.uiusingActivity.FileAndPicTestACT;
import com.mplayer.KingZMediaPlayer;
import com.kingz.uiusingWidgets.SeekBarActivity;
import com.kingz.uiusingWidgets.UsingProgressBarAct;

/**
 * @author: KingZ
 * @Data: 2015年10月4日下午11:40:26
 * @Description: Demo首页
 */
public class MainActivity extends Activity implements OnItemClickListener{

	private static String TAG = MainActivity.class.getSimpleName();

	private ListView listView;
	private ArrayAdapter<ListBillData> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.list_view);

		/****添加列表项***/
		//原生
		//mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		//自定义的
		mAdapter = new ArrayAdapter<>(this, R.layout.list_bill);
		listView.setAdapter(mAdapter);//设置数据适配器

		addData();
		listView.setOnItemClickListener(this);
	}

	private void addData() {
		mAdapter.add(new ListBillData(this,"SrcLayoutTest",new Intent(this,LayoutActivityListView.class)));
		mAdapter.add(new ListBillData(this,"BasicControls",new Intent(this,BasicControlsActivity.class)));
		//mAdapter.add(new ListBillData(this,"SeniorControls",new Intent(this,SeniorControls.class)));
		mAdapter.add(new ListBillData(this,"ProgressBar",new Intent(this,UsingProgressBarAct.class)));
		mAdapter.add(new ListBillData(this,"SeekBar",new Intent(this,SeekBarActivity.class)));
		mAdapter.add(new ListBillData(this,"Custom controls",new Intent(this,CustomWidgetsActivity.class)));
		mAdapter.add(new ListBillData(this,"Four major components",new Intent(this,FourComponentListView.class)));
		mAdapter.add(new ListBillData(this,"File Test",new Intent(this,FileAndPicTestACT.class)));
		mAdapter.add(new ListBillData(this,"Bitmap Test",new Intent(this,BitmapActivity.class)));
		mAdapter.add(new ListBillData(this,"JsonParseAndPicLru ",new Intent(this,AsynctaskBitmapMainActivity.class)));
		mAdapter.add(new ListBillData(this,"BaiduMapTest",new Intent(this,BaiduMapActivity.class)));
		mAdapter.add(new ListBillData(this,"Media",new Intent(this,KingZMediaPlayer.class)));
		mAdapter.add(new ListBillData(this,"FileManager ",new Intent(this,FileManagerActivity.class)));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		ActivityOptions opts = ActivityOptions.makeCustomAnimation(this,R.anim.fade,R.anim.hold);
//		ActivityOptions opts = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
		ListBillData data = mAdapter.getItem(position);
		data.startActivity(null);
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
