package com.kingz.pages;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.bling.pages.ProgressViewsActivity;
import com.bling.pages.UsingCustomSeekBar;
import com.bling.pages.WaveLoadingActivity;
import com.kingz.customdemo.R;
import com.kingz.mode.ListBillData;
import com.kingz.scroll.SliderListViewActivity;
import com.kingz.scroll.SoapFlingActivity;
import com.kingz.scroll.view_pager.WZViewPagerActicity;
import com.kingz.view.listview.CustomListViewActivity;
import com.kingz.view.listview.RecyclerviewDemo;
import com.kingz.view.shape.ShapeDrawOval;
import com.kingz.view.webview.WebViewActivity;
import com.kingz.widgets.android_src.popwindow.CommonPopupWindow;

/**
 * 自定义View汇总主界面
 */
public class CustomViewsPage extends  Activity implements OnItemClickListener{

	private ArrayAdapter<ListBillData> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("CustomViewsPage","CustomViewsPage onCreate");

 		//1:加载ListView布局
		setContentView(R.layout.customview_main);
		ListView listView = (ListView) findViewById(R.id.widgetsListView_id);
		//2:初始化适配器
		mAdapter = new ArrayAdapter<ListBillData>(this, R.layout.list_bill);
		//3：为LisView设置数据适配器
		listView.setAdapter(mAdapter);
		addData();
		listView.setOnItemClickListener(this);
	}

	private void addData() {
		mAdapter.add(new ListBillData(this,"ShapeDrawOval",new Intent(this,ShapeDrawOval.class)));
		mAdapter.add(new ListBillData(this,"ProgressViews",new Intent(this,ProgressViewsActivity.class)));
		mAdapter.add(new ListBillData(this,"Customer SeekBar",new Intent(this,UsingCustomSeekBar.class)));
		mAdapter.add(new ListBillData(this,"画布绘制进度条",new Intent(this,CustomCanvasSeekBarAct.class)));
		mAdapter.add(new ListBillData(this,"自定义ListView",new Intent(this,CustomListViewActivity.class)));
		mAdapter.add(new ListBillData(this,"Recyclerview例子",new Intent(this,RecyclerviewDemo.class)));
		mAdapter.add(new ListBillData(this,"ListView-----左右滑动删除",new Intent(this,SliderListViewActivity.class)));
		mAdapter.add(new ListBillData(this,"View 的Drag & Fling",new Intent(this,SoapFlingActivity.class)));
		mAdapter.add(new ListBillData(this,"PopupWindow",new Intent(this,CommonPopupWindow.class)));
		mAdapter.add(new ListBillData(this,"WebAPP测试",new Intent(this,WebViewActivity.class)));
		mAdapter.add(new ListBillData(this,"WZViewPager",new Intent(this,WZViewPagerActicity.class)));
		mAdapter.add(new ListBillData(this,"贝塞尔水波曲线",new Intent(this,WaveLoadingActivity.class)));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		/** 窗口切换自定义动画设置 */
		ActivityOptions opts = ActivityOptions.makeCustomAnimation(this,R.anim.zoom_enter,R.anim.zoom_enter);
		ListBillData data = mAdapter.getItem(position);
		data.startActivity(opts);
	}

}
