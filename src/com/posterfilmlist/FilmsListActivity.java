package com.posterfilmlist;

import android.app.Activity;
import android.os.Bundle;
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
 * description:
 */
public class FilmsListActivity extends Activity{

    public static final  String TAG = "FilmsListActivity";

    private GetPosterImageTask getPosterImageTask;
    private ArrayList<PosterGroupInfo> posterInfoList;
    private PosterGroupInfo posterGroupInfo = new PosterGroupInfo();
    private ArrayList<PosterGroupInfo.Poster> posterList = new ArrayList<>();
    private PosterGroupInfo.Poster posterInfo = new PosterGroupInfo().new Poster();;





    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO 页面初始化

        getLocalPosterData();

        //获取url的图片
        getPosterImageTask = new GetPosterImageTask();
        getPosterImageTask.execute(posterList.get(0).poster_ur);
    }

    /**
     * 获取本地海报xml数据
     */
    private void getLocalPosterData(){
        try {
            InputStream filmInfoStream = getResources().getAssets().open("posterimage.xml");

            XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
            pullParser.setInput(filmInfoStream,"utf-8");
            int evtType = pullParser.getEventType();
            posterInfoList = new ArrayList<>();
            posterGroupInfo = new PosterGroupInfo();

            while(evtType != XmlPullParser.END_DOCUMENT){
                switch (evtType){
                    case XmlPullParser.START_TAG:
                        String tagValue =pullParser.getName();
                        if("id".equals(tagValue)){
                            posterGroupInfo.id = tagValue;
                        }else if("title".equals(tagValue)){
                            posterGroupInfo.title = tagValue;
                        }else if("img".equals(tagValue)){
                           posterInfo.poster_ur = tagValue;
                        }else if("title".equals(tagValue)){
                           posterInfo.title = tagValue;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                         String endTag =pullParser.getName();
                        if ("group".equals(endTag)) {
                            posterInfoList.add(posterGroupInfo);
                            posterGroupInfo = null;
                        }else if("poster".equals(endTag)){
                            posterList.add(posterInfo);
                            posterInfo = null;
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
