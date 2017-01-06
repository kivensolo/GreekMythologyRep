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

import com.ListBillData;
import com.kingz.customdemo.R;
import com.kingz.four_components.ObtainConnectPeopleActivity;
import com.kingz.four_components.ServiceComponentsACT;
import com.kingz.four_components.activity.AttrOfAliasAct;
import com.kingz.ipcdemo.AIDLActivity;
import com.kingz.uiusingCanvas.CustomCanvasSeekBarAct;

public class FourComponentPage extends  Activity implements OnItemClickListener{

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
		mAdapter.add(new ListBillData(this,"LableTextViewPage",new Intent(this,LableTextViewPage.class)));
		mAdapter.add(new ListBillData(this,"ContentProvider",new Intent(this,ObtainConnectPeopleActivity.class)));
		mAdapter.add(new ListBillData(this,"AIDL",new Intent(this,AIDLActivity.class)));
		mAdapter.add(new ListBillData(this,"Activity-Alias",new Intent(this,AttrOfAliasAct.class)));
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
