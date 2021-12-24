package com.zeke.demo.menu.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zeke.demo.R;
import com.zeke.demo.menu.adapter.SelectionQuickAdapter;
import com.zeke.demo.menu.bean.MyItemDetailsLookup;

import java.util.ArrayList;
import java.util.List;

public class ContextMenu3Activity extends AppCompatActivity {

    private static final String TAG = "ContextMenu3Activity";

    private RecyclerView recyclerView;
//    private SelectionStringAdapter mAdapter;
    private SelectionQuickAdapter mAdapter;

    private SelectionTracker mSelectionTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_menu3);

        initView();
        initData();
        initEvent();

        if (savedInstanceState != null)
            mSelectionTracker.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState != null && mSelectionTracker != null){
            mSelectionTracker.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onBackPressed() {
        //返回时先监听是否选择状态中，如果选择状态先取消选择
        if (mSelectionTracker.hasSelection()){
            mSelectionTracker.clearSelection();
            return;
        }
        super.onBackPressed();
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,ContextMenu3Activity.class);
        context.startActivity(intent);
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        //尺寸大小确定，可以设置该参数，避免重新计算大小
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
//        mAdapter = new SelectionStringAdapter(android.R.layout.simple_list_item_multiple_choice,createDataList());
//        mAdapter = new SelectionQuickAdapter(R.layout.item_recyclerview,createDataList());
        mAdapter = new SelectionQuickAdapter(android.R.layout.simple_list_item_multiple_choice,createDataList());
        recyclerView.setAdapter(mAdapter);

    }

    private void initData() {
        //创建选择跟踪器
        mSelectionTracker = new SelectionTracker.Builder(
                "mySelection",
                recyclerView,
//                new StringItemKeyProvider(1,createDataList()),      //密钥提供者
                new StableIdKeyProvider(recyclerView),      //密钥提供者
//                new MyStringItemDetailsLookup(recyclerView),
                new MyItemDetailsLookup(recyclerView),
//                StorageStrategy.createStringStorage())
                StorageStrategy.createLongStorage())
                .withSelectionPredicate(SelectionPredicates.<Long>createSelectAnything())
                .build();
        mAdapter.setSelectionTracker(mSelectionTracker);

    }

    private void initEvent() {
        //创建选择观察器
        mSelectionTracker.addObserver(new SelectionTracker.SelectionObserver() {
            @Override
            public void onItemStateChanged(@NonNull Object key, boolean selected) {
                super.onItemStateChanged(key, selected);
                Log.i(TAG, "onItemStateChanged: "+key+" to "+selected);
                setSelectionTitle();
            }

            @Override
            public void onSelectionRefresh() {
                super.onSelectionRefresh();
            }

            @Override
            public void onSelectionChanged() {
                super.onSelectionChanged();
                Log.i(TAG, "onSelectionChanged: ");
                setSelectionTitle();
            }

            @Override
            public void onSelectionRestored() {
                super.onSelectionRestored();
            }
        });
    }

    private void setSelectionTitle(){
        int nItems = mSelectionTracker.getSelection().size();
        String title;
        if(nItems > 0){
            title = "%1$d items selected";
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ef6c00")));
        }else {
            title = getResources().getString(R.string.app_name);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        }
        getSupportActionBar().setTitle(String.format(title,nItems));
    }

    //生成测试数据List
    private List<String> createDataList(){
        List<String> list=new ArrayList<>();
        for(int i=0;i<20;i++){
            list.add("测试条目"+i);
        }
        return list;
    }
}
