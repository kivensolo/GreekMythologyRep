package com.kingz.pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.base.BaseActivity;
import com.kingz.adapter.PageListAdapter;
import com.kingz.customdemo.R;
import com.kingz.four_components.activity.news.NewsActivity;
import com.kingz.mode.ListBillData;

public class LayoutPage extends BaseActivity{

	private PageListAdapter mAdapter;
	private FloatingActionButton fab;
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_list);
		final View mainContent = findViewById(R.id.main_content);

		toolbar = (Toolbar)findViewById(R.id.toolbar);
		toolbar.setTitle("FloatingButtonAndSnackSimple");
		setSupportActionBar(toolbar);

		mAdapter = new PageListAdapter(this);
		initData();
		RecyclerView recyclerView = findViewById(R.id.rvToDoList);
		recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(mAdapter);

//		mAdapter.setItemClickListener(new CommonRecyclerAdapter.IItemClickListener() {
//			@Override
//			public void onItemClick(CommonRecyclerAdapter.ViewHolder holder, View v) {
//				int position = holder.getLayoutPosition();
//				ActivityOptions opts = ActivityOptions.makeCustomAnimation(LayoutPage.this,R.anim.zoom_enter,R.anim.zoom_enter);
//				ListBillData data = mAdapter.getItem(position);
//				if (data != null) {
//					data.startActivity(opts);
//				}
//			}
//
//			@Override
//			public void onItemLongClick(CommonRecyclerAdapter.ViewHolder holder, View v) {
//				int position = holder.getLayoutPosition();
//				Snackbar.make(mainContent, mAdapter.getItem(position).getItemName(), Snackbar.LENGTH_SHORT)
//						.setAction("action", null)
//						.setActionTextColor(getResources().getColor(R.color.colorAccent))
//						.show();
//			}
//		});

		fab = findViewById(R.id.src_layout_fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Snackbar.make(mainContent, "功能暂未实现", Snackbar.LENGTH_SHORT)
						.setAction("action", null)
						.setActionTextColor(getResources().getColor(R.color.colorAccent))
						.show();
			}
		});
	}

	private void initData() {
		Intent newsIntent = new Intent(this, NewsActivity.class);
		mAdapter.addItem(new ListBillData(this,NewsActivity.class.getSimpleName(),newsIntent));
		mAdapter.addItem(new ListBillData(this,NewsActivity.class.getSimpleName(),newsIntent));
		mAdapter.addItem(new ListBillData(this,NewsActivity.class.getSimpleName(),newsIntent));
		mAdapter.addItem(new ListBillData(this,NewsActivity.class.getSimpleName(),newsIntent));
		mAdapter.addItem(new ListBillData(this,NewsActivity.class.getSimpleName(),newsIntent));
		mAdapter.addItem(new ListBillData(this,NewsActivity.class.getSimpleName(),newsIntent));
		mAdapter.addItem(new ListBillData(this,NewsActivity.class.getSimpleName(),newsIntent));
		mAdapter.addItem(new ListBillData(this,NewsActivity.class.getSimpleName(),newsIntent));
		mAdapter.addItem(new ListBillData(this,NewsActivity.class.getSimpleName(),newsIntent));
		mAdapter.addItem(new ListBillData(this,NewsActivity.class.getSimpleName(),newsIntent));
		mAdapter.addItem(new ListBillData(this,NewsActivity.class.getSimpleName(),newsIntent));
		mAdapter.addItem(new ListBillData(this,NewsActivity.class.getSimpleName(),newsIntent));
		mAdapter.addItem(new ListBillData(this,NewsActivity.class.getSimpleName(),newsIntent));
		mAdapter.addItem(new ListBillData(this,NewsActivity.class.getSimpleName(),newsIntent));
		mAdapter.addItem(new ListBillData(this,NewsActivity.class.getSimpleName(),newsIntent));
		mAdapter.addItem(new ListBillData(this,NewsActivity.class.getSimpleName(),newsIntent));
		mAdapter.addItem(new ListBillData(this,NewsActivity.class.getSimpleName(),newsIntent));
		mAdapter.addItem(new ListBillData(this,"2222",newsIntent));
	}
}
