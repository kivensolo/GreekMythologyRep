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

import static com.kingz.four_components.activity.news.NewsInfo.newsData;
import static com.kingz.four_components.activity.news.NewsInfo.titleData;

/**
 * Created by KingZ.
 * Data: 2016 2016/1/31
 * Discription:内容Fragment
 */
public class ContentFragmentDynamic extends Fragment {
    static String titleName = "";
    static String contentInfo = "";
    //Todo 需要优化
    public static ContentFragmentDynamic newInstance(int index) {
        ContentFragmentDynamic f = new ContentFragmentDynamic();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        titleName = titleData.get(index);
        contentInfo = newsData.get(titleName);
        return f;
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
        scroller.addView(text);
        text.setText(contentInfo);
        return scroller;
    }

}
