package com;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.apiDemo.content.ExternalStorage;
import com.asynctask_jsontest.LruPicListViewActivity;
import com.iflytek.synthesizer.VoiceActivity;
import com.kingz.adapter.CommExpandableListAdapter;
import com.kingz.basic_api.CanvasApiActivity;
import com.kingz.communicat.HMLActuivity;
import com.kingz.customdemo.R;
import com.kingz.file.DownloadAPPActivity;
import com.kingz.file.FileAndPicTestACT;
import com.kingz.filemanager.FileManagerActivity;
import com.kingz.graphics.Arcs;
import com.kingz.graphics.PathEffects;
import com.kingz.mode.ListBillData;
import com.kingz.newfeatures.NewFeaturesMainActivity;
import com.kingz.pages.CustomViewsPage;
import com.kingz.pages.FourComponentPage;
import com.kingz.pages.LayoutPage;
import com.kingz.pages.photo.BitmapPhotosActivity;
import com.kingz.pages.photo.colormatrix.ColorMatrixDemo;
import com.kingz.pages.photo.filmlist.FilmsListActivity;
import com.kingz.pages.photo.memory.MemoryCheck;
import com.kingz.text.LableTextViewPage;
import com.kingz.text.langs.TextViewOfLanguages;
import com.kingz.text.metrics.FontMetricsDemoActivity;
import com.kingz.utils.ZLog;
import com.kingz.view.animation.CrossfadeActivity;
import com.kingz.view.animation.InterpolatorAnimation;
import com.kingz.view.animation.PropertyAnimationsActivity;
import com.kingz.view.animation.ShakeAnimation;
import com.kingz.view.animation.ViewFlipperAnimation;
import com.kingz.view.surface.DrawRectWithSurface;
import com.kingz.widgets.AnimatedExpandableListView;
import com.kingz.widgets.android_src.BasicControlsActivity;
import com.kingz.widgets.android_src.NativeProgressBar;
import com.kingz.widgets.android_src.OriginViewPager;
import com.kingz.widgets.android_src.SpansDemo;
import com.mplayer.ApolloMediaPlayer;
import com.mplayer.exo_player.DetailPageActivty;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * author: KingZ
 * Data: 2015年10月4日下午11:40:26
 * Description: Demo首页
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
        listView.setOnGroupClickListener(new ImpOnGroupClickListener());

        //new BmobQuery<BmobUser>()
        //    .getObject("vO4whhhJ", new QueryListener<BmobUser>() {
        //        @Override
        //        public void done(BmobUser bmobUser, BmobException e) {
        //            if(e == null){
        //                bmobToastShow(bmobUser);
        //            }
        //        }
        //    });
    }

    private void bmobToastShow(BmobUser bmobUser){
        String userinfo = bmobUser.toString();
        Toast.makeText(this,userinfo,Toast.LENGTH_LONG).show();
        ZLog.d(TAG,"bmob test = " + userinfo);
    }

    private class ImpOnGroupClickListener implements ExpandableListView.OnGroupClickListener {

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

        //AboutText
        billDataManager.addGroup("AboutText");
        addChildData("FontMetrics",FontMetricsDemoActivity.class);
        addChildData("TextViewOfLanguages",TextViewOfLanguages.class);
        addChildData("LableText",LableTextViewPage.class);
        addChildData("SpanLable", SpansDemo.class);
        billDataManager.pushChilds();

        //原生基本控件
        billDataManager.addGroup("Original");
        addChildData("BasicControls", BasicControlsActivity.class);
        addChildData("ProgressBar", NativeProgressBar.class);
        addChildData("SrcLayout", LayoutPage.class);
        addChildData("Four major components", FourComponentPage.class);
        addChildData("ViewPager", OriginViewPager.class);
        addChildData("SurfaceDraw", DrawRectWithSurface.class);
        billDataManager.pushChilds();

        //Bimap
        billDataManager.addGroup("Graphics");
        addChildData("BitMapOverall", BitmapPhotosActivity.class);
        addChildData("ColorMatrix", ColorMatrixDemo.class);
        addChildData("Arcs", Arcs.class);
        addChildData("PathEffects", PathEffects.class);
        addChildData("MemoryCheck", MemoryCheck.class);
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
        addChildData("FilmsList", FilmsListActivity.class);
        billDataManager.pushChilds();

        //Mediaplayer
        billDataManager.addGroup("Mediaplayer");
        addChildData("Media", ApolloMediaPlayer.class);
        addChildData("Exo-Player", DetailPageActivty.class);
        billDataManager.pushChilds();

        //Other
        billDataManager.addGroup("Other");
        addChildData("FileManager", FileManagerActivity.class);
        addChildData("Iflytek", VoiceActivity.class);
        billDataManager.pushChilds();

        //通信
        billDataManager.addGroup("Communication");
        addChildData("HML_Deep", HMLActuivity.class);
        billDataManager.pushChilds();

        //BaseApiUnderStand
        billDataManager.addGroup("BasicApi");
        addChildData("Canvas", CanvasApiActivity.class);
        billDataManager.pushChilds();
        //新特性
        billDataManager.addGroup("NewFeatures");
        addChildData("NewFeature of UI", NewFeaturesMainActivity.class);
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
        //FpsTools.finish();
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private class ListBillDataManager {
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
