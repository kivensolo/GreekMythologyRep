package com.kingz.pages.photo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.BaseActivity;
import com.kingz.adapter.BitmapPageAdapter;
import com.kingz.customdemo.R;

import java.util.List;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/9/2 17:21 <br>
 * description: PhotosActivity <br>
 */
@SuppressLint("Registered")
public class PhotosActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    protected BitmapPageAdapter bitmapAdapter;
    protected ListView mListView;
    protected TextView mTextView;
    protected Bitmap srcBitmap;
    protected Bitmap waterMark;
    protected int backgroundId;
    protected ImageView img1;
    List<BitmapPhotosActivity.ItemInfo> datas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_activity);
        initViews();
    }

    private void initViews() {
        bitmapAdapter = new BitmapPageAdapter(this, datas);
        mListView = (ListView) findViewById(R.id.type_change_id);
        mListView.setAdapter(bitmapAdapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setOnItemClickListener(this);
        img1 = (ImageView) findViewById(R.id.normal_pic);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        bitmapAdapter.notifyDataSetChanged();
        setItemState(view, position);
    }

    private void setItemState(View view, int position) {
        mTextView = (TextView) view.findViewById(R.id.list_item);
        if (mListView.isItemChecked(position)) {
            backgroundId = R.color.deepskyblue;
            mTextView.setTextColor(getResources().getColor(R.color.suncolor));
        } else {
            backgroundId = R.drawable.listview_unchecked;
            mTextView.setTextColor(getResources().getColor(R.color.lightskyblue));
        }
        Drawable background = this.getResources().getDrawable(backgroundId);
        view.setBackground(background);
    }

}
