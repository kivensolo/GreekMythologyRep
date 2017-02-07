package com.kingz.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.kingz.customdemo.R;
import com.kingz.databean.PosterGroupInfo;
import com.utils.OkHttpClientManager;

import java.util.List;

@Deprecated
public class FilmListGridViewAdapter extends CommonAdapter {
    private ImageView i;
    private List<PosterGroupInfo.Poster> posterList;

    public FilmListGridViewAdapter(Context contex, List<PosterGroupInfo.Poster> datas) {
        super(contex, datas);
        posterList = datas;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        if (convertView != null) {
            i = (ImageView) convertView;
        } else {
            i = new ImageView(mContex);
            //i.setBackground(mContex.getResources().getDrawable(R.drawable.ic_launcher));
            i.setBackgroundColor(mContex.getResources().getColor(R.color.forestgreen));
            i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            i.setMaxHeight(250);
        }
        OkHttpClientManager.displayImage(i,posterList.get(position).poster_ur);
        return i;
    }
}
