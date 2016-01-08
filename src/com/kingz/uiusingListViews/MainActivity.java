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
import com.kingz.uiusingActivity.BitmapActivity;
import com.kingz.uiusingActivity.FileAndPicTestACT;
import com.kingz.uiusingWidgets.UsingProgressBarAct;
import com.kingz.uiusingWidgets.UsingSeekBarScrAct;

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
//		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		//自定义的
		mAdapter = new ArrayAdapter<ListBillData>(this, R.layout.list_bill);
		listView.setAdapter(mAdapter);//设置数据适配器

		addData();
		listView.setOnItemClickListener(this);
	}

	private void addData() {
		mAdapter.add(new ListBillData(this,"LayoutTest",new Intent(this,LayoutActivityListView.class)));
		mAdapter.add(new ListBillData(this,"ProgressBar",new Intent(this,UsingProgressBarAct.class)));
		mAdapter.add(new ListBillData(this,"SeekBar Src",new Intent(this,UsingSeekBarScrAct.class)));
		mAdapter.add(new ListBillData(this,"Custom controls",new Intent(this,CustomWidgetsActivity.class)));
		mAdapter.add(new ListBillData(this,"Four major components",new Intent(this,FourComponentListView.class)));
		mAdapter.add(new ListBillData(this,"File test",new Intent(this,FileAndPicTestACT.class)));
		mAdapter.add(new ListBillData(this,"Bitmap Test",new Intent(this,BitmapActivity.class)));
		mAdapter.add(new ListBillData(this,"JsonParseAndPicLru ",new Intent(this,AsynctaskBitmapMainActivity.class)));
	}

	/**
	 * ListItem被点击的事件处理
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ListBillData data = mAdapter.getItem(position);
		data.startActivity();
	}

}
