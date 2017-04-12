package com.kingz.four_components.activity.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import com.kingz.mode.NewsData;

/**
 * Created by KingZ.
 * Data: 2016 2016/1/31
 * Discription:内容Fragment
 */
public class ContentFragmentFromCode extends Fragment {
    private NewsData newsData = new NewsData();

    public static ContentFragmentFromCode newInstance(int index) {
        ContentFragmentFromCode f = new ContentFragmentFromCode();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//         if (container == null) {
//            // We have different layouts, and in one of them this
//            // fragment's containing frame doesn't exist.  The fragment
//            // may still be created from its saved state, but there is
//            // no reason to try to create its view hierarchy because it
//            // won't be displayed.  Note this is not needed -- we could
//            // just run the code below, where we would create and return
//            // the view hierarchy; it would just never be used.
//            return null;
//        }
        //return inflater.inflate(R.layout.content_layout,container,false);
        ScrollView scroller = new ScrollView(getActivity());

        TextView text = new TextView(getActivity());
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                4, getActivity().getResources().getDisplayMetrics());
        text.setPadding(padding, padding, padding, padding);
        scroller.addView(text);
        text.setText(newsData.News[getShownIndex()]);
        return scroller;
    }

}
