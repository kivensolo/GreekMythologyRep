package com.kingz.uiusingLayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kingz.uiusingListViews.R;

/**
 * Created by KingZ.
 * Data: 2016 2016/1/25
 * Discription: 标题Fragment
 */
public class TitleFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //将这个视图填充到相应的类中
        return inflater.inflate(R.layout.title_layout,container,false);
    }
}
