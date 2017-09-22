package com.kingz.four_components.activity.news;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import com.kingz.customdemo.R;
import com.kingz.utils.ZLog;

/**
 * Created by KingZ.
 * Data: 2016 2016/1/25
 * Discription:
 * 向Activity 添加片段方法之二:通过动态写代码将片段添加到某个现有 ViewGroup
 * 在 Activity 运行期间随时将片段添加到 Activity 布局中。
 * 只需指定要将片段放入哪个 ViewGroup。
 * 要想在Activity 中执行片段事务（如添加、删除或替换片段），必须使用 FragmentTransaction 中的 API。
 */
public class NewsActivity extends FragmentActivity{

    //private FragmentManager mManager;
    //private TitleFragmentDynamic titltFragment;
    //private ContentFragmentDynamic contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ZLog.d("NewsActivity","onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.srclayout_frame_layout);

        //以下代码是布局中未指定明确的Fragment时，动态添加
        //mManager = getSupportFragmentManager();
        //Fragment fragment = mManager.findFragmentById(R.id.fragment_container);
        //if (fragment.getChildFragmentManager().findFragmentById(R.id.news_content_fragment) != null) {
        //    //横屏
        //    //如果之前已经储存了状态，直接返回，否则覆盖之前的Fragment
        //    if (savedInstanceState != null) {
        //        return;
        //    }
        //
        //    titltFragment = new TitleFragmentDynamic();
        //    contentFragment = new ContentFragmentDynamic();
        //    addExtrasData();
        //    //add(ViewGroup 放置片段的位置, Fragment 要添加的片段, @Nullable String var3)
        //    FragmentTransaction fragmentTransaction = mManager.beginTransaction();
        //    fragmentTransaction.add(R.id.title_fragment, titltFragment,"tag_title");
        //    fragmentTransaction.add(R.id.details, contentFragment,"tag_content");
        //    fragmentTransaction.commit();
        //
        //    /*********** 对于碎片管理  start*************/
        //    //通过 findFragmentById()（对于在 Activity 布局中提供 UI 的片段）或 findFragmentByTag()
        //    //     （对于提供或不提供 UI 的片段）获取 Activity 中存在的片段。
        //    //通过 popBackStack()（模拟用户发出的 Back 命令）将片段从返回栈中弹出。
        //    //通过 addOnBackStackChangedListener() 注册一个侦听返回栈变化的侦听器。
        //    //通过  FragmentManager 打开一个 FragmentTransaction，通过它来执行某些事务，如添加和删除片段。
        //    /*********** 对于碎片管理  end*************/
        //}else{
        //    ZLog.e("NewsActivity","getChildFragmentManager() error");
        //}
    }

    //private void addExtrasData() {
    //    Bundle extras = getIntent().getExtras();
    //    if(null != extras){
    //        //防止这些片段是被一个特殊的intent启动起来的
    //        titltFragment.setArguments(extras);
    //        contentFragment.setArguments(extras);
    //    }
    //}

    /**
     * 系统会在片段首次绘制其用户界面时调用此方法。 要想为您的片段绘制 UI，
     * 您从此方法中返回的 View 必须是片段布局的根视图。如果片段未提供 UI，您可以返回 null。
     */
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    /**
     * 系统将此方法作为用户离开片段的第一个信号（但并不总是意味着此片段会被销毁）进行调用。
     * 您通常应该在此方法内确认在当前用户会话结束后仍然有效的任何更改（因为用户可能不会返回）。
     */
    @Override
    protected void onPause() {
        super.onPause();
    }
}
