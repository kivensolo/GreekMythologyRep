package com;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import com.AsuyncTaskJsonTest.AsynctaskBitmapMainActivity;
import com.adapter.CommExpandableListAdapter;
import com.iflytek.synthesizer.VoiceActivity;
import com.kingz.customDemo.R;
import com.kingz.filemanager.FileManagerActivity;
import com.kingz.pages.*;
import com.lbs.BaiduMapActivity;
import com.mplayer.KingZMediaPlayer;
import com.nativeWidgets.BasicControlsActivity;
import com.nativeWidgets.NativeProgressBar;
import com.photo.PhotosActivity;
import com.utils.UITools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: KingZ
 * @Data: 2015年10月4日下午11:40:26
 * @Description: Demo首页
 */
public class MainActivity extends ExpandableListActivity implements OnItemClickListener{

	private static String TAG = MainActivity.class.getSimpleName();

	private ListView listView;
	private ExpandableListView expandableListView;
	private ArrayAdapter<ListBillData> mAdapter;
	private List<Map<String, String>> gruops  = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> childs  = new ArrayList<Map<String, String>>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//图标设置在右边
		getExpandableListView().setIndicatorBounds(UITools.SCREEN_WIDTH - 60,UITools.SCREEN_WIDTH - 30);
		setListAdapter(new CommExpandableListAdapter(this));
		//listView = (ListView) findViewById(R.id.list_view);
		/****添加列表项***/
		//原生
		//mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		//自定义的
		//mAdapter = new ArrayAdapter<>(this, R.layout.list_bill);
		//listView.setAdapter(mAdapter);//设置数据适配器
		//addData();
		//listView.setOnItemClickListener(this);
	}

	private void addData() {
		mAdapter.add(new ListBillData(this,"SrcLayoutTest",new Intent(this,LayoutPage.class)));
		mAdapter.add(new ListBillData(this,"BitMapOverall",new Intent(this,PhotosActivity.class)));
		mAdapter.add(new ListBillData(this,"BasicControls",new Intent(this,BasicControlsActivity.class)));
		//mAdapter.add(new ListBillData(this,"SeniorControls",new Intent(this,SeniorControls.class)));
		mAdapter.add(new ListBillData(this,"ProgressBar",new Intent(this,NativeProgressBar.class)));
		mAdapter.add(new ListBillData(this,"Custom controls",new Intent(this,CustomViewsPage.class)));
		mAdapter.add(new ListBillData(this,"Four major components",new Intent(this,FourComponentPage.class)));
		mAdapter.add(new ListBillData(this,"File Test",new Intent(this,FileAndPicTestACT.class)));
		mAdapter.add(new ListBillData(this,"DownloadFile",new Intent(this,DownloadAPPActivity.class)));
		mAdapter.add(new ListBillData(this,"JsonParseAndPicLru ",new Intent(this,AsynctaskBitmapMainActivity.class)));
		mAdapter.add(new ListBillData(this,"BaiduMapTest",new Intent(this,BaiduMapActivity.class)));
		mAdapter.add(new ListBillData(this,"Media",new Intent(this,KingZMediaPlayer.class)));
		mAdapter.add(new ListBillData(this,"FileManager ",new Intent(this,FileManagerActivity.class)));
		mAdapter.add(new ListBillData(this,"Iflytek",new Intent(this,VoiceActivity.class)));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//ActivityOptions opts = ActivityOptions.makeCustomAnimation(this,R.anim.fade,R.anim.hold);
//		ActivityOptions opts = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
		ListBillData data = mAdapter.getItem(position);
		data.startActivity(null);
		//TODO Activity切换的动画
		//overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
