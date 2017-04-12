package com;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import com.AsuyncTaskJsonTest.LruPicListViewActivity;
import com.content.ExternalStorage;
import com.iflytek.synthesizer.VoiceActivity;
import com.kingz.adapter.CommExpandableListAdapter;
import com.kingz.customdemo.R;
import com.kingz.file.DownloadAPPActivity;
import com.kingz.file.FileAndPicTestACT;
import com.kingz.filemanager.FileManagerActivity;
import com.kingz.graphics.Arcs;
import com.kingz.graphics.PathEffects;
import com.kingz.mode.ListBillData;
import com.kingz.pages.CustomViewsPage;
import com.kingz.pages.FourComponentPage;
import com.kingz.pages.LayoutPage;
import com.lbs.BaiduMapActivity;
import com.mplayer.ApolloMediaPlayer;
import com.nativeWidgets.BasicControlsActivity;
import com.nativeWidgets.NativeProgressBar;
import com.nativeWidgets.OriginViewPager;
import com.nativeWidgets.SpansDemo;
import com.kingz.pages.photo.BitmapPhotosActivity;
import com.kingz.pages.photo.FilmsListActivity;
import com.view.animation.*;
import com.view.surface.DrawRectWithSurface;
import com.kingz.widgets.AnimatedExpandableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: KingZ
 * @Data: 2015年10月4日下午11:40:26
 * @Description: Demo首页
 */
public class MainActivity extends ExpandableListActivity implements OnItemClickListener {

    private static String TAG = MainActivity.class.getSimpleName();
    private CommExpandableListAdapter comExpandAdapter;
    private AnimatedExpandableListView listView;
    private ListBillDataManager billDataManager = new ListBillDataManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (AnimatedExpandableListView) findViewById(android.R.id.list);
//		getExpandableListView().setIndicatorBounds(UITools.SCREEN_WIDTH - 60, UITools.SCREEN_WIDTH - 27);
        initAdapterData();
        comExpandAdapter = new CommExpandableListAdapter(this, getGroupData(), getSubTitleData());
        listView.setAdapter(comExpandAdapter);
        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ImpOnGroupClickListener());

    }

    public class ImpOnGroupClickListener implements ExpandableListView.OnGroupClickListener {

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
    }

    private void initAdapterData() {
        initData();
    }

    private void initData() {
        //ViewAimation
        billDataManager.addGroup("Animation");
        addChildData("PropertyAnimation", PropertyAnimationsActivity.class);
        addChildData("ShakeAnimation", ShakeAnimation.class);
        addChildData("ViewFlipperAnimation", ViewFlipperAnimation.class);
        addChildData("InterpolatorAnimation", InterpolatorAnimation.class);
        addChildData("Crossfading Two Views", CrossfadeActivity.class);
        billDataManager.pushChilds();

        //原生基本控件
        billDataManager.addGroup("Original");
        addChildData("BasicControls", BasicControlsActivity.class);
        addChildData("ProgressBar", NativeProgressBar.class);
        addChildData("SrcLayout", LayoutPage.class);
        addChildData("Four major components", FourComponentPage.class);
        addChildData("ViewPager", OriginViewPager.class);
        addChildData("SurfaceDraw", DrawRectWithSurface.class);
        addChildData("SpanLable", SpansDemo.class);
        billDataManager.pushChilds();

        //Bimap
        billDataManager.addGroup("Graphics");
        addChildData("BitMapOverall", BitmapPhotosActivity.class);
        addChildData("Arcs", Arcs.class);
        addChildData("PathEffects", PathEffects.class);
        billDataManager.pushChilds();

        //自定义控件
        billDataManager.addGroup("Custom controls");
        addChildData("Custom controls", CustomViewsPage.class);
        billDataManager.pushChilds();

        //File
        billDataManager.addGroup("File");
        addChildData("File Test", FileAndPicTestACT.class);
        addChildData("DownloadFile", DownloadAPPActivity.class);
        addChildData("ExternalStorage", ExternalStorage.class);
        billDataManager.pushChilds();


        //Net
        billDataManager.addGroup("Net");
        addChildData("JsonParseAndPicLru", LruPicListViewActivity.class);
        addChildData("BaiduMapTest", BaiduMapActivity.class);
        addChildData("FilmsList", FilmsListActivity.class);
        billDataManager.pushChilds();

        //Mediaplayer
        billDataManager.addGroup("Mediaplayer");
        addChildData("Media", ApolloMediaPlayer.class);
        billDataManager.pushChilds();

        //Other
        billDataManager.addGroup("Other");
        addChildData("FileManager", FileManagerActivity.class);
        addChildData("Iflytek", VoiceActivity.class);
        billDataManager.pushChilds();
    }

    private void addChildData(String subTitle, Class<?> pageClass) {
        billDataManager.buildChilds(this, subTitle, pageClass);
    }

    private List<String> getGroupData() {
        return billDataManager.getGroups();
    }

    private List<List<ListBillData>> getSubTitleData() {
        return billDataManager.getChildrens();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//		ActivityOptions opts = ActivityOptions.makeCustomAnimation(this,R.anim.fade,R.anim.hold);
//		ActivityOptions opts = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
        ListBillData data = (ListBillData) comExpandAdapter.getChild(groupPosition, childPosition);
        data.startActivity(null);
		//overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    class ListBillDataManager {
        private List<String> gruops = new ArrayList<>();
        private List<List<ListBillData>> childs = new ArrayList<>();
        private ArrayList<ListBillData> item = new ArrayList<>();

        private void addGroup(String parentTitle) {
            gruops.add(parentTitle);
        }

        private void buildChilds(Context ctx, String subTitle, Class<?> pageClass) {
            if (item == null) {
                item = new ArrayList<>();
            }
            item.add(new ListBillData(ctx, subTitle, new Intent(ctx, pageClass)));
        }

        private void pushChilds() {
            if (item != null) {
                childs.add(item);
                item = null;
            } else {
                throw new NullPointerException("pushChilds() 失败,参数为空");
            }
        }

        private List<String> getGroups() {
            return gruops;
        }

        private List<List<ListBillData>> getChildrens() {
            return childs;
        }
    }
}
