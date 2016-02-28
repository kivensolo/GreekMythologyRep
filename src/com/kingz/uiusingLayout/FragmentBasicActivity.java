package com.kingz.uiusingLayout;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.kingz.uiusingListViews.R;

/**
 * Created by KingZ.
 * Data: 2016 2016/1/25
 * Discription: Fragment的创建以及使用
 * <!---- 添加Fragment ------->
 *  1：静态的从xml文件中创建，需要布局中存在fragment组件（每个组件绑定了各自使用的类,
 *          而类又绑定了相应的布局）
 *  2：通过编码方式添加Fragment
 *
 *  同一个 FragmentTransaction进行多次fragment事务。当完成这些变化操作的时候，必须调
 *  用commit()方法。
 */
public class FragmentBasicActivity extends FragmentActivity{

    // Create a new Fragment to be placed in the activity layout
    TitleFragment titltFragment;
    ContentFragment contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.srclayout_headline_layout);

        if(findViewById(R.id.headlines_fragment) != null){
            //如果之前已经储存了状态，我们就不需要做任何事
            if(savedInstanceState != null){
                return;
            }
        }
        //通过FragmentManager来获取到相应的Fragment
        titltFragment = (TitleFragment) getFragmentManager().findFragmentById(R.id.title_fragment);
        contentFragment = (ContentFragment) getFragmentManager().findFragmentById(R.id.content_fragment);
        //防止这个activity是是被一个特殊的intent启动起来的   设置参数先
        titltFragment.setArguments(getIntent().getExtras());
        contentFragment.setArguments(getIntent().getExtras());

        //动态添加一个Fragment（在FragmentLayout中）
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();//开始一个事务
//        ft.add(R.id.fragment_container, 相应的fragment对象).commit();//添加、删除、替换等等操作
    }
}
