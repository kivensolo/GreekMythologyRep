package com.customview.listview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.kingz.uiusingListViews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KingZ on 2015/12/30.
 * Discription: 展示自定义滑动和删除的listView的页面
 */
public class SliderListViewActivity extends Activity implements CustomSliderDeleteListView.RemovedListener {

    private CustomSliderDeleteListView sliderListView;
    private ArrayAdapter<String> adapter;
    private List<String> dataSourceList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.slider_list);
        iniView();
    }

    private void iniView() {
        sliderListView = (CustomSliderDeleteListView) findViewById(R.id.sliderCustomListView);
        sliderListView.setRemoveListener(this);

        for(int i=0; i<20; i++){
            dataSourceList.add("第" + i + "个Item");
        }

        adapter = new ArrayAdapter<String>(this, R.layout.simplelist_every_item, R.id.list_item, dataSourceList);
        sliderListView.setAdapter(adapter);

        sliderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Toast.makeText(SliderListViewActivity.this, dataSourceList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void removeItem(CustomSliderDeleteListView.RemoveDirection direction, int position) {
        adapter.remove(adapter.getItem(position)); //移除掉指定的一个

       switch (direction){
           case RIGHT:
               Toast.makeText(this, "向右删除  "+ position, Toast.LENGTH_SHORT).show();
               break;
           case  LEFT:
                Toast.makeText(this, "向左删除  "+ position, Toast.LENGTH_SHORT).show();
               break;
            default:
            break;
       }
    }
}
