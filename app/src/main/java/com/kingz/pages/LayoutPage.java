package com.kingz.pages;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kingz.customdemo.R;
import com.kingz.four_components.activity.news.NewsActivity;
import com.kingz.mode.ListBillData;

public class LayoutPage extends Activity implements OnItemClickListener{

	private ListView listView;
	private ArrayAdapter<ListBillData> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_list);

		listView = findViewById(R.id.layout_list_view);
		mAdapter = new ArrayAdapter<>(this, R.layout.list_bill);
		listView.setAdapter(mAdapter);//加载适配器

		addData();
		listView.setOnItemClickListener(this);
	}

	private void addData() {
		mAdapter.add(new ListBillData(this,"NewsPage",new Intent(this,NewsActivity.class)));
	}

	/**
	 * ListItem点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ActivityOptions opts = ActivityOptions.makeCustomAnimation(this,R.anim.zoom_enter,R.anim.zoom_enter);
		ListBillData data = mAdapter.getItem(position);
		if (data != null) {
			data.startActivity(opts);
		}
	}
}
