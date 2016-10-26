package com.kingz.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.App;
import com.kingz.databean.PosterGroupInfo;
import com.squareup.okhttp.*;
import com.utils.BitMapUtils;
import com.utils.ZLog;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/10/26 17:43 <br>
 */
public class FilmListGridViewAdapter extends CommonAdapter {
    private ImageView i;
    private InputStream inputStream;
    private Bitmap bm;
    private List<PosterGroupInfo.Poster> posterList;


    private IGetBitmapLsr lsr;

    interface IGetBitmapLsr {
        View onSucess();
    }

    public void setOnSuceeeLsr(IGetBitmapLsr lsr) {
        this.lsr = lsr;
    }


    public FilmListGridViewAdapter(Context contex, List<PosterGroupInfo.Poster> datas) {
        super(contex, datas);
        posterList = datas;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        //
        //setOnSuceeeLsr(new IGetBitmapLsr() {
        //    @Override
        //    public View onSucess() {
        //        if (convertView != null) {
        //            i = (ImageView) convertView;
        //        } else {
        //            i = new ImageView(App.getAppContext());
        //            i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //            i.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        //            i.setImageBitmap(bm);
        //        }
        //        return i;
        //    }
        //});

        OkHttpClient mOKhOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(posterList.get(position).poster_ur)
                .build();
        //Call对象
        Call call = mOKhOkHttpClient.newCall(request);
        //异步的方式去执行请求
        //持阻塞的方式: call.execute()
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                ZLog.e("TAG", ">>>>>>>> 网络请求失败 request=" + request.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //onResponse执行的线程并不是UI线程
                ZLog.i("TAG", ">>>>>>>> 网络请求成功");
                //String result = response.body().toString();
                //byte[] bytes = response.body().bytes();
                inputStream = response.body().byteStream();
                bm = BitMapUtils.decodeStream(inputStream);
                //lsr.onSucess();
            }
        });
        if (convertView != null) {
            i = (ImageView) convertView;
        } else {
            i = new ImageView(App.getAppContext());
            i.setScaleType(ImageView.ScaleType.FIT_CENTER);
            i.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
            i.setImageBitmap(bm);
        }
        return i;
    }
}
