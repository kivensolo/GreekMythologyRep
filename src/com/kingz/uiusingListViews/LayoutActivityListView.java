package com.kingz.uiusingListViews;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.kingz.uiusingLayout.FragmentPageFromCode;
import com.kingz.uiusingLayout.FragmentPageFromXml;
import com.kingz.uiusingLayout.LinearLayoutTest_Act;

public class LayoutActivityListView extends Activity implements OnItemClickListener{

	private ListView listView;
	private ArrayAdapter<ListBillData> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_list);

		listView = (ListView) findViewById(R.id.layout_list_view);
		mAdapter = new ArrayAdapter<ListBillData>(this, R.layout.list_bill);
		listView.setAdapter(mAdapter);//加载适配器

		addData();
		listView.setOnItemClickListener(this);
	}

	private void addData() {
		mAdapter.add(new ListBillData(this,"LinnerLayout",new Intent(this,LinearLayoutTest_Act.class)));
		mAdapter.add(new ListBillData(this,"FragmentByXml",new Intent(this,FragmentPageFromXml.class)));
		mAdapter.add(new ListBillData(this,"FragmentByCode",new Intent(this,FragmentPageFromCode.class)));
	}

	/**
	 * ListItem点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ActivityOptions opts = ActivityOptions.makeCustomAnimation(this,R.anim.zoom_enter,R.anim.zoom_enter);
		ListBillData data = mAdapter.getItem(position);
		data.startActivity(opts);
	}
}
