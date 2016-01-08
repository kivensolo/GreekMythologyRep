package com.kingz.uiusingListViews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.customview.FingerBallActivity;
import com.customview.livstview.CustomListViewActivity;
import com.customview.livstview.SliderListViewActivity;
import com.kingz.basic_controls.SpansActivity;
import com.kingz.uiusingActivity.LableTextView_Act;
import com.kingz.uiusingMediaTest.CustomCanvasSeekBarAct;
import com.kingz.uiusingWidgets.UsingCustomSeekBar;

/**
 * 自定义View汇总主界面
 */
public class CustomWidgetsActivity extends  Activity implements OnItemClickListener{

	private ArrayAdapter<ListBillData> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

 		//1:加载ListView布局
		setContentView(R.layout.customWidgets_MainPage);
		ListView listView = (ListView) findViewById(R.id.widgetsListView_id);
		//2:初始化适配器
		mAdapter = new ArrayAdapter<ListBillData>(this, R.layout.list_bill);
		//3：为LisView设置数据适配器
		listView.setAdapter(mAdapter);
		addData();
		listView.setOnItemClickListener(this);
	}

	private void addData() {
		mAdapter.add(new ListBillData(this,"Customer SeekBar",new Intent(this,UsingCustomSeekBar.class)));
		mAdapter.add(new ListBillData(this,"Canvas SeekBar",new Intent(this,CustomCanvasSeekBarAct.class)));
		mAdapter.add(new ListBillData(this,"LableText",new Intent(this,LableTextView_Act.class)));
		mAdapter.add(new ListBillData(this,"TrackBall",new Intent(this,FingerBallActivity.class)));
		mAdapter.add(new ListBillData(this,"Custom ListView",new Intent(this,CustomListViewActivity.class)));
		mAdapter.add(new ListBillData(this,"自定义左右滑动删除的ListView()存在bug",new Intent(this,SliderListViewActivity.class)));
		mAdapter.add(new ListBillData(this,"SpanLable",new Intent(this,SpansActivity.class)));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		ListBillData data = mAdapter.getItem(position);
		data.startActivity();
	}

}
