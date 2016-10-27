
package com.photo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.GridView;
import com.kingz.adapter.FilmListGridViewAdapter;
import com.base.BaseActivity;
import com.kingz.databean.PosterGroupInfo;
import com.posterfilmlist.GetPosterImageTask;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date: 2016 2016/4/2 16:49
 * description: 影片列表页  用GridView和本地的图片url去显示图片
 * 网络方式使用OKHttp
 */
public class FilmsListActivity extends BaseActivity {

    public static final String TAG = "FilmsListActivity";

    private GetPosterImageTask getPosterImageTask;
    private ArrayList<PosterGroupInfo> posterInfoList;
    private PosterGroupInfo posterGroupInfo;
    private ArrayList<PosterGroupInfo.Poster> posterList = new ArrayList<>();
    private PosterGroupInfo.Poster posterInfo;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLocalPosterData();

        ViewGroup.LayoutParams lps = new ViewGroup.LayoutParams(-1, -1);
        GridView rootView = new GridView(this);
        rootView.setColumnWidth(200);
        rootView.setVerticalSpacing(20);
        rootView.setHorizontalSpacing(20);
        rootView.setNumColumns(GridView.AUTO_FIT);
        rootView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        rootView.setLayoutParams(lps);
        rootView.setBackgroundColor(Color.WHITE);
        rootView.setAdapter(new FilmListGridViewAdapter(this, posterList));
        setContentView(rootView);
        //获取url的图片
        //getPosterImageTask = new GetPosterImageTask();
        //getPosterImageTask.execute(posterList.get(0).poster_ur);
    }

    /**
     * 获取本地海报xml数据
     */
    private void getLocalPosterData() {
        try {
            InputStream filmInfoStream = getResources().getAssets().open("posterimage.xml");
            XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
            pullParser.setInput(filmInfoStream, "utf-8");
            int evtType = pullParser.getEventType();

            boolean isInposter = false;
            posterInfoList = new ArrayList<>();

            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                    case XmlPullParser.START_TAG:
                        String tagValue = pullParser.getName();
                        if("poster".equals(tagValue)){
                            posterInfo = new PosterGroupInfo().new Poster();
                            isInposter = true;
                        }else if("group".equals(tagValue)){
                            posterGroupInfo = new PosterGroupInfo();
                        }
                        else if ("id".equals(tagValue)) {
                            posterGroupInfo.id = pullParser.nextText();
                        }else if ("img".equals(tagValue)) {
                            posterInfo.poster_ur = pullParser.nextText();
                        }else if ("title".equals(tagValue)) {
                            if(isInposter){
                                posterInfo.title = pullParser.nextText();
                            }else{
                                posterGroupInfo.title = pullParser.nextText();
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = pullParser.getName();
                        if ("group".equals(endTag)) {
                            posterInfoList.add(posterGroupInfo);
                            posterGroupInfo = null;
                        } else if ("poster".equals(endTag)) {
                            posterList.add(posterInfo);
                            posterInfo = null;
                            isInposter = false;
                        }
                        break;
                    default:
                        break;
                }
                evtType = pullParser.next();
            }
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }

}
