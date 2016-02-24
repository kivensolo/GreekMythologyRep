package com.kingz.uiusingListViews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.kingz.four_components.ServiceComponentsACT;
import com.kingz.uiusingActivity.LableTextView_Act;
import com.kingz.uiusingMediaTest.CustomCanvasSeekBarAct;

public class FourComponentListView extends  Activity implements OnItemClickListener{

	private ListView listView;
	private ArrayAdapter<ListBillData> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_four_component_list);

		listView = (ListView) findViewById(R.id.four_component_list_view);
		mAdapter = new ArrayAdapter<ListBillData>(this, R.layout.list_bill);
		listView.setAdapter(mAdapter);//设置数据适配器

		addData();
		listView.setOnItemClickListener(this);
	}

	private void addData() {
		mAdapter.add(new ListBillData(this,"服务",new Intent(this,ServiceComponentsACT.class)));
		mAdapter.add(new ListBillData(this,"自定义seekBar",new Intent(this,CustomCanvasSeekBarAct.class)));
		mAdapter.add(new ListBillData(this,"LableTextView_Act",new Intent(this,LableTextView_Act.class)));
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
