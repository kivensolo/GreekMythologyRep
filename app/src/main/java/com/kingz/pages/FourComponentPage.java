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
import com.kingz.four_components.ObtainProviderDataPage;
import com.kingz.four_components.ServiceComponentsACT;
import com.kingz.four_components.activity.ChangeAppIconActivity;
import com.kingz.ipcdemo.AIDLActivity;
import com.kingz.mode.ListBillData;
import com.kingz.text.LabelTextViewPage;

public class FourComponentPage extends Activity implements OnItemClickListener{

	private ListView listView;
	private ArrayAdapter<ListBillData> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_four_component_list);
		listView = (ListView) findViewById(R.id.four_component_list_view);
		mAdapter = new ArrayAdapter<ListBillData>(this, R.layout.list_bill);
		listView.setAdapter(mAdapter);
		addData();
		listView.setOnItemClickListener(this);
	}

	private void addData() {
		addItem("服务",ServiceComponentsACT.class);
		addItem("LableTextViewPage", LabelTextViewPage.class);
		addItem("ContentProvider",ObtainProviderDataPage.class);
		addItem("AIDL",AIDLActivity.class);
		addItem("Activity-Alias", ChangeAppIconActivity.class);
	}

	private void addItem(String  name,Class<?> pageName) {
		mAdapter.add(new ListBillData(this,name, new Intent(this, pageName)));
	}

	/**
	 * ListItem被点击的事件处理
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ActivityOptions opts = ActivityOptions.makeCustomAnimation(this,R.anim.zoom_enter,R.anim.zoom_enter);
		ListBillData data = mAdapter.getItem(position);
		data.startActivity(opts);
	}

}
