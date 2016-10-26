package com.posterfilmlist;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date: 2016 2016/4/2 16:50
 * description:
 */
public class GetPosterImageTask extends AsyncTask<String,Void,List<PosterInfo>> {

    @Override
    protected List<PosterInfo> doInBackground(String... params) {
        return getLocalUrl(params[0]);
    }

    @Override
    protected void onPostExecute(List<PosterInfo> posterInfos) {
        super.onPostExecute(posterInfos);
        //TODO 为GridView设置图片
    }

    private List<PosterInfo> getLocalUrl(String url) {
        List<PosterInfo> posteUrlList = new ArrayList<>();
        PosterInfo posterInfo;

        //posterInfo =

        return posteUrlList;
    }
}

class PosterInfo{
    public String poster_url;
}
