package com.kingz.pages.photo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.kingz.adapter.BitmapListAdapter;
import com.kingz.customdemo.R;
import com.kingz.module.common.BaseActivity;

import java.util.List;

/**
 * author: King.Z <br>
 * date:  2016/9/2 17:21 <br>
 * description: 图片展示基类页面 <br>
 *     Left: ListView  Right: Imageview
 */
public abstract class PhotosActivity extends BaseActivity
        implements AdapterView.OnItemClickListener {

    protected BitmapListAdapter bitmapAdapter;
    protected ListView mListView;
    protected ImageView picView;
    List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_activity);
        mListView = findViewById(R.id.type_change_id);
        picView = findViewById(R.id.normal_pic);
        initViews();
    }

    private void initViews() {
        bitmapAdapter = new BitmapListAdapter(datas);
        mListView.setAdapter(bitmapAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        bitmapAdapter.setCurrentItemPostion(position);
        bitmapAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(datas != null){
            datas = null;
        }
    }
}
