package com.kingz.controls;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.kingz.uiusingListViews.R;

import java.util.List;

/**
 * Created by KingZ.
 * Data: 2016 2016/2/7
 * Discription:
 *      用GridView显示系统Lanuher中icon图标
 */
public class MyGridView extends Activity implements View.OnClickListener {

    private GridView mGridView;
    private List<ResolveInfo> mApps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadApps(); // do this in onresume?
        setContentView(R.layout.basic_control_gridview);
        mGridView = (GridView) findViewById(R.id.page_gridView);
        mGridView.setAdapter(new GridViewAdapter(this));
    }

      private void loadApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        mApps = getPackageManager().queryIntentActivities(mainIntent, 0);
    }

    @Override
    public void onClick(View v) {

    }

    class GridViewAdapter extends BaseAdapter {
        private Context context;

        private int[] imagesArray = {
                R.mipmap.sample_1,R.mipmap.sample_2,R.mipmap.sample_3,
                R.mipmap.sample_4,R.mipmap.sample_5,R.mipmap.sample_6,
                R.mipmap.sample_7,R.mipmap.sample_thumb_0,R.mipmap.sample_thumb_1,
                R.mipmap.sample_thumb_2,R.mipmap.sample_thumb_3,R.mipmap.sample_thumb_4,
        };


        public GridViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return mApps.size();
        }

        @Override
        public Object getItem(int position) {
            return mApps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i;
            if(convertView != null){
                 i = (ImageView)convertView;//获取缓存视图
            }else{
                i  = new ImageView(MyGridView.this);
                i.setScaleType(ImageView.ScaleType.FIT_CENTER);
                i.setLayoutParams(new GridView.LayoutParams(50, 50));
            }
             ResolveInfo info = mApps.get(position);
            i.setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));
            return i;
        }
    }

}

