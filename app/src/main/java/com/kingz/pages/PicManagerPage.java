package com.kingz.pages;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kingz.mode.ListBillData;
import com.kingz.customdemo.R;

/**
 * Created by KingZ on 2015/11/1.
 */
@SuppressLint("Registered")
public class PicManagerPage extends Activity implements AdapterView.OnItemClickListener {


    private ListView listView;
	private ArrayAdapter<ListBillData> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pic_manager_list);

		listView = (ListView) findViewById(R.id.picture_manager_liastview);
		mAdapter = new ArrayAdapter<ListBillData>(this, R.layout.list_bill);
		listView.setAdapter(mAdapter);

		addData();
		listView.setOnItemClickListener(this);
    }

    private void addData() {
		mAdapter.add(new ListBillData(this,"图片管理",new Intent(this,LayoutPage.class)));
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    ActivityOptions opts = ActivityOptions.makeCustomAnimation(this,R.anim.zoom_enter,R.anim.zoom_enter);
		ListBillData data = mAdapter.getItem(position);
		data.startActivity(opts);
    }
}
