package com;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;

import com.AsuyncTaskJsonTest.AsynctaskBitmapMainActivity;
import com.adapter.CommExpandableListAdapter;
import com.iflytek.synthesizer.VoiceActivity;
import com.kingz.customDemo.R;
import com.kingz.filemanager.FileManagerActivity;
import com.kingz.pages.CustomViewsPage;
import com.kingz.pages.DownloadAPPActivity;
import com.kingz.pages.FileAndPicTestACT;
import com.kingz.pages.FourComponentPage;
import com.kingz.pages.LayoutPage;
import com.lbs.BaiduMapActivity;
import com.mplayer.KingZMediaPlayer;
import com.nativeWidgets.BasicControlsActivity;
import com.nativeWidgets.NativeProgressBar;
import com.photo.PhotosActivity;
import com.view.slider.wzviewpager.OriginViewPagerActivity;
import com.widgets.AnimatedExpandableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: KingZ
 * @Data: 2015年10月4日下午11:40:26
 * @Description: Demo首页
 */
public class MainActivity extends ExpandableListActivity implements OnItemClickListener{

	private static String TAG = MainActivity.class.getSimpleName();

	private CommExpandableListAdapter comExpandAdapter;
	private List<String> gruops  = new ArrayList<>();
	private List<List<ListBillData>> childs  = new ArrayList<>();
	private AnimatedExpandableListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (AnimatedExpandableListView) findViewById(android.R.id.list);
//		getExpandableListView().setIndicatorBounds(UITools.SCREEN_WIDTH - 60,UITools.SCREEN_WIDTH - 27);
		initAdapterData();
		comExpandAdapter = new CommExpandableListAdapter(this,gruops,childs);
		listView.setAdapter(comExpandAdapter);

		// In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
		listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int)/expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });

	}

	private void initAdapterData() {
		initGroupData();
		initChlidData();
	}

	private void initGroupData(){
		gruops.add("Original");
		gruops.add("Bimap");
		gruops.add("Custom controls");
		gruops.add("File");
		gruops.add("Net");
		gruops.add("Mediaplayer");
		gruops.add("Other");
	}
	private void initChlidData(){
		//原生基本控件
		ArrayList<ListBillData> item1 = new ArrayList<>();
		item1.add(new ListBillData(this,"BasicControls",new Intent(this,BasicControlsActivity.class)));
		item1.add(new ListBillData(this,"ProgressBar",new Intent(this,NativeProgressBar.class)));
		item1.add(new ListBillData(this,"SrcLayoutTest",new Intent(this,LayoutPage.class)));
		item1.add(new ListBillData(this,"Four major components",new Intent(this,FourComponentPage.class)));
		item1.add(new ListBillData(this,"ViewPager",new Intent(this,OriginViewPagerActivity.class)));
		childs.add(item1);

		//Bimap
		ArrayList<ListBillData> item2 = new ArrayList<>();
		item2.add(new ListBillData(this,"BitMapOverall",new Intent(this,PhotosActivity.class)));
		childs.add(item2);

		//自定义控件
		ArrayList<ListBillData> item3 = new ArrayList<>();
		item3.add(new ListBillData(this,"Custom controls",new Intent(this,CustomViewsPage.class)));
		childs.add(item3);

		//File
		ArrayList<ListBillData> item4 = new ArrayList<>();
		item4.add(new ListBillData(this,"File Test",new Intent(this,FileAndPicTestACT.class)));
		item4.add(new ListBillData(this,"DownloadFile",new Intent(this,DownloadAPPActivity.class)));
		childs.add(item4);

		//Net
		ArrayList<ListBillData> item5 = new ArrayList<>();
		item5.add(new ListBillData(this,"JsonParseAndPicLru ",new Intent(this,AsynctaskBitmapMainActivity.class)));
		item5.add(new ListBillData(this,"BaiduMapTest",new Intent(this,BaiduMapActivity.class)));
		childs.add(item5);

		//Mediaplayer
		ArrayList<ListBillData> item6 = new ArrayList<>();
		item6.add(new ListBillData(this,"Media",new Intent(this,KingZMediaPlayer.class)));
		childs.add(item6);

		//Other
		ArrayList<ListBillData> item7 = new ArrayList<>();
		item7.add(new ListBillData(this,"FileManager ",new Intent(this,FileManagerActivity.class)));
		item7.add(new ListBillData(this,"Iflytek",new Intent(this,VoiceActivity.class)));
		childs.add(item7);
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//		ActivityOptions opts = ActivityOptions.makeCustomAnimation(this,R.anim.fade,R.anim.hold);
//		ActivityOptions opts = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
		ListBillData data = (ListBillData) comExpandAdapter.getChild(groupPosition,childPosition);
		data.startActivity(null);
//		TODO Activity切换的动画
//		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}
}
