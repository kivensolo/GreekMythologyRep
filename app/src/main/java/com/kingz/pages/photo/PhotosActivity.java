package com.kingz.pages.photo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.kingz.adapter.BitmapListAdapter;
import com.kingz.customdemo.R;
import com.zeke.ktx.base.BaseActivity;

import java.util.List;

import butterknife.BindView;

/**
 * author: King.Z <br>
 * date:  2016/9/2 17:21 <br>
 * description: 图片展示基类页面 <br>
 *     Left: ListView  Right: Imageview
 */
public abstract class PhotosActivity extends BaseActivity
        implements AdapterView.OnItemClickListener {

    protected BitmapListAdapter bitmapAdapter;
    @BindView(R.id.type_change_id)
    protected ListView mListView;
    @BindView(R.id.normal_pic)
    protected ImageView picView;
    List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_activity);
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
