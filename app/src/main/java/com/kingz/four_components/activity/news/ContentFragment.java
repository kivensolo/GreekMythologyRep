package com.kingz.four_components.activity.news;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kingz.customdemo.R;
import com.zeke.kangaroo.utils.ZLog;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * Created by KingZ.
 * Data: 2016 2016/1/31
 * Discription:内容Fragment
 */
public class ContentFragment extends Fragment {
    public static final String TAG = "ContentFragment";

    public static final String ARG_INDEX = "index";
    public static final String ARG_TITLE = "title";
    public static final String ARGS_CONTENT = "content";
    static WeakHashMap<Integer,WeakReference<ContentFragment>> _fragmentsCache = new WeakHashMap<>();

    public static ContentFragment newInstance(int index) {
        WeakReference<ContentFragment> reference = _fragmentsCache.get(index);
        if(reference != null){
            ZLog.d(TAG,"find fragment cache.");
            return reference.get();
        }

        ZLog.d(TAG,"add new fragment");
        ContentFragment fragment = new ContentFragment();
        String titleName = NewsInfo.titleData.get(index);
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        args.putString(ARG_TITLE,titleName);
        args.putString(ARGS_CONTENT,NewsInfo.newsData.get(titleName));
        // 传递数据到Fragment中
        fragment.setArguments(args);
        _fragmentsCache.put(index,new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public int getShownIndex() {
        Bundle arguments = getArguments();
        if(arguments == null){
            return -1;
        }
        return arguments.getInt(ARG_INDEX, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         if (container == null) {
            return null;
        }

        ScrollView scroller = new ScrollView(getActivity());
        TextView text = new TextView(getActivity());
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5, getActivity().getResources().getDisplayMetrics());
        text.setPadding(padding, padding, padding, padding);
        text.setTextSize(26f);
        text.setFocusable(true);
        text.setBackground(getResources().getDrawable(R.drawable.news_content_bkg,null));
        text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Bundle args = getArguments();
        if(args == null){
            text.setText("Empty Data");
        }else{
            text.setText(getArguments().getString(ARGS_CONTENT, "Empty Data"));
        }
        scroller.addView(text);
        return scroller;
    }

}
