package com.kingz.uiusingLayout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import com.kingz.customDemo.R;

/**
 * Created by KingZ.
 * Data: 2016 2016/1/25
 * Discription: 向 Activity 添加片段方法之二:通过编程方式将片段添加到某个现有 ViewGroup
 * 您可以在 Activity 运行期间随时将片段添加到 Activity 布局中。您只需指定要将片段放入哪个 ViewGroup。
 * 要想在您的 Activity 中执行片段事务（如添加、删除或替换片段），您必须使用 FragmentTransaction 中的 API。
 *
 *  ※※※  同一个FragmentTransaction进行多次fragment事务。
 *         当完成这些变化操作的时候，必须调用commit()方法。
 */
public class FragmentPageFromCode extends FragmentActivity implements TitleFragment.OnArticleSelectedListener {

    // Create a new Fragment to be placed in the activity layout
    TitleFragment titltFragment;
    ContentFragment contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.srclayout_frame_layout);

//        if (findViewById(R.id.fragment_container) != null) {
//            //如果之前已经储存了状态，直接返回，否则覆盖之前的Fragment
//            if (savedInstanceState != null) {
//                return;
//            }
//
//            titltFragment = new TitleFragment2();
//            contentFragment = new ContentFragment2();
//            //防止这些片段是被一个特殊的intent启动起来的   设置参数先'
//            titltFragment.setArguments(getIntent().getExtras());
//            contentFragment.setArguments(getIntent().getExtras());
//
//            //您可以像下面这样从 Activity 获取一个 FragmentTransaction 实例
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            //然后，您可以使用 add() 方法添加一个片段，指定要添加的片段以及将其插入哪个视图。例如：
//            fragmentTransaction.add(R.id.fragment_container, titltFragment,"tag_title");
//            fragmentTransaction.add(R.id.fragment_container, contentFragment,"tag_content");
//            fragmentTransaction.commit();
//            //------------传递到 add() 的第一个参数是 ViewGroup，即应该放置片段的位置，由资源 ID 指定，
//            //       ----|第二个参数是要添加的片段。一旦您通过 FragmentTransaction 做出了更改，就必须调用 commit() 以使更改生效。
//
//            /*********** 对于碎片管理  start*************/
//            //通过 findFragmentById()（对于在 Activity 布局中提供 UI 的片段）或 findFragmentByTag()
//            //     （对于提供或不提供 UI 的片段）获取 Activity 中存在的片段。
//            //通过 popBackStack()（模拟用户发出的 Back 命令）将片段从返回栈中弹出。
//            //通过 addOnBackStackChangedListener() 注册一个侦听返回栈变化的侦听器。
//            //通过  FragmentManager 打开一个 FragmentTransaction，通过它来执行某些事务，如添加和删除片段。
//            /*********** 对于碎片管理  end*************/
//        }
    }

    /**
     * 系统会在片段首次绘制其用户界面时调用此方法。 要想为您的片段绘制 UI，
     * 您从此方法中返回的 View 必须是片段布局的根视图。如果片段未提供 UI，您可以返回 null。
     * @param name
     * @param context
     * @param attrs
     * @return
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


    //when title view clicked
    @Override
    public void onArticleSelected(Uri articleUri) {

    }
}
