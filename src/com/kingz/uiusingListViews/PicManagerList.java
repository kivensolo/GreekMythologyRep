package com.kingz.uiusingListViews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by KingZ on 2015/11/1.
 * Discription:图片管理的一些Demo
 */
public class PicManagerList extends Activity implements AdapterView.OnItemClickListener {


    private ListView listView;
	private ArrayAdapter<ListBillData> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pic_manager_list);

		listView = (ListView) findViewById(R.id.picture_manager_liastview);
		mAdapter = new ArrayAdapter<ListBillData>(this, R.layout.list_bill);
		listView.setAdapter(mAdapter);//设置数据适配器

		addData();
		listView.setOnItemClickListener(this);
    }

    private void addData() {
		mAdapter.add(new ListBillData(this,"网络下载图片-保存-删除",new Intent(this,LayoutActivityListView.class)));
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    ListBillData data = mAdapter.getItem(position);
		data.startActivity();
    }
}
