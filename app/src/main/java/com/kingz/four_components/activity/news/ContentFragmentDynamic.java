package com.kingz.four_components.activity.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import com.kingz.customdemo.R;
import com.utils.ZLog;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import static com.kingz.four_components.activity.news.NewsInfo.newsData;
import static com.kingz.four_components.activity.news.NewsInfo.titleData;

/**
 * Created by KingZ.
 * Data: 2016 2016/1/31
 * Discription:内容Fragment
 */
public class ContentFragmentDynamic extends Fragment {

    static WeakHashMap<Integer,WeakReference<ContentFragmentDynamic>> _fragmentsCache = new WeakHashMap<>();
    public static ContentFragmentDynamic newInstance(int index) {
        ZLog.d("ContentFragmentDynamic","index = " + index);
        WeakReference<ContentFragmentDynamic> reference = _fragmentsCache.get(index);
        if(reference != null){
            ZLog.d("ContentFragmentDynamic","find fragment cache.");
            return reference.get();
        }
        ZLog.d("ContentFragmentDynamic","add new fragment");
        ContentFragmentDynamic fragment = new ContentFragmentDynamic();
        String titleName = titleData.get(index);
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putString("title",titleName);
        args.putString("content",newsData.get(titleName));
        fragment.setArguments(args);
        _fragmentsCache.put(index,new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        ScrollView scroller = new ScrollView(getActivity());

        TextView text = new TextView(getActivity());
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5, getActivity().getResources().getDisplayMetrics());
        text.setPadding(padding, padding, padding, padding);
        text.setTextSize(26f);
        text.setFocusable(true);
        text.setBackground(getResources().getDrawable(R.drawable.text_forcused_style_xjdx));
        text.setText(getArguments().getString("content", "Empty Data"));
        scroller.addView(text);
        return scroller;
    }

}
