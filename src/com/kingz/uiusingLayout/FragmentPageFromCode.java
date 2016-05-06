package com.kingz.uiusingLayout;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.kingz.uiusingListViews.R;

/**
 * Created by KingZ.
 * Data: 2016 2016/1/25
 * Discription: Fragment的创建以及使用
 *  |----创建Fragment类
 *       创建必须在onCreateView()回调方法中定义布局
 *
 *  |----添加Fragment
 *   ——1：静态加载，需要布局中存在fragment组件
 *       （每个组件绑定了各自使用的类,而类又绑定了相应的布局）
 *   ——2：动态添加Fragment
 *
 *  ※※※  同一个FragmentTransaction进行多次fragment事务。
 *         当完成这些变化操作的时候，必须调用commit()方法。
 */
public class FragmentPageFromCode extends FragmentActivity{

    // Create a new Fragment to be placed in the activity layout
    TitleFragment titltFragment;
    ContentFragment contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.srclayout_headline_layout);

        if (findViewById(R.id.fragment_container) != null) {
            //如果之前已经储存了状态，直接返回，否则覆盖之前的Fragment
            if (savedInstanceState != null) {
                return;
            }
            //通过FragmentManager来获取到相应的Fragment
            //titltFragment = (TitleFragment) getFragmentManager().findFragmentById(R.id.title_fragment);
            //contentFragment = (ContentFragment) getFragmentManager().findFragmentById(R.id.content_fragment);
            ////防止这个activity是被一个特殊的intent启动起来的   设置参数先
            //titltFragment.setArguments(getIntent().getExtras());
            //contentFragment.setArguments(getIntent().getExtras());
            // 把Fragment添加到容器FrameLayout中
            //getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,titltFragment).commit();

            titltFragment = new TitleFragment();
            titltFragment.setArguments(getIntent().getExtras());
            //动态添加一个Fragment到FragmentLayout中，（替换在Activity布局文件中用<fragment>元素定义的）
            //Activity可以移除它，并用另一个Fragment替换它。
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, titltFragment).commit();//添加、删除、替换等等操作
        }
    }
}
