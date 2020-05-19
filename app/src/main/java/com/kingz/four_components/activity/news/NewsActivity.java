package com.kingz.four_components.activity.news;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kingz.customdemo.R;
import com.zeke.kangaroo.utils.ZLog;

/**
 * Created by KingZ.
 * Data: 2016 2016/1/25
 * Discription:
 */
public class NewsActivity extends FragmentActivity {

    public static final String TAG_TITLE = "tag_title";
    public static final String TAG_CONTENT = "tag_content";
    private TitleListFragment titltFragment;
    private ContentFragment contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ZLog.d("NewsActivity","onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_demo);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            titltFragment = new TitleListFragment();
            contentFragment = new ContentFragment();
            addExtrasData();
            fragmentTransaction.add(R.id.news_title_layout, titltFragment, TAG_TITLE);
            fragmentTransaction.add(R.id.news_content_layout, contentFragment, TAG_CONTENT);
        }else{
            if (savedInstanceState != null) {
                return;
            }
        }
        fragmentTransaction.commit();
    }

    private void addExtrasData() {
        Bundle extras = getIntent().getExtras();
        if(null != extras){
            //防止这些片段是被一个特殊的intent启动起来的
            titltFragment.setArguments(extras);
            contentFragment.setArguments(extras);
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

}
