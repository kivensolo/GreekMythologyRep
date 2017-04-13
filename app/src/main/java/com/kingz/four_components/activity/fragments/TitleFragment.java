package com.kingz.four_components.activity.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kingz.customdemo.R;
import com.utils.ZLog;

import static android.view.View.Z;

/**
 * Created by KingZ.
 * Data: 2016 2016/1/25
 * Discription:【标题Fragment】创建提供布局的片段
 * 添加用户界面 要想为片段提供布局，必须实现 onCreateView() 回调方法，
 * Android 系统会在片段需要绘制其布局时调用该方法。
 * 此方法的实现返回的 View 必须是片段布局的根视图。
 * 注：如果片段是 ListFragment 的子类，则默认实现会从 onCreateView() 返回一个 ListView，因此无需实现它。
 */
public class TitleFragment extends ListFragment {
    private static final String TAG = TitleFragment.class.getSimpleName();
    boolean mDualPane;
    int mCurCheckPosition = 0;
    private static String[] TITLES = {"体育新闻","娱乐新闻","科技新闻","内涵段子","娱乐新闻","社会新闻","我的收藏"};
    OnArticleSelectedListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnArticleSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.title_layout,container,false);
    }
    /************ inflate() 方法的三个参数 *********************
        1.您想要扩展的布局的资源 ID；
        2.将作为扩展布局父项的 ViewGroup 。传递 container 对系统向扩展布局的根视图（由其所属的父视图指定）应用布局参数具有重要意义；
        3.指示是否应该在扩展期间将扩展布局附加至 ViewGroup（第二个参数）的布尔值。
          （在本例中，其值为 false，因为系统已经将扩展布局插入container—传递true值会在最终布局中创建一个多余的视图组。）
     ***********************************************************/

    /**
     * 在 Activity 的 onCreate() 方法已返回时调用
     * @param savedInstanceState  异常下保存的数据
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_activated_1, TITLES));

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
            Log.i(TAG,"mCurCheckPosition = "+mCurCheckPosition);
        }

        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showDetails(mCurCheckPosition);
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
       showDetails(position);
    }

    private void showDetails(int index) {
        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);
            // Check what fragment is currently shown, replace if needed.
            ContentFragmentFromCode details = (ContentFragmentFromCode)getFragmentManager().findFragmentById(R.id.details);
            ZLog.d(TAG,"showDetails() details=" + details);
            if (details == null || details.getShownIndex() != index) {
                details = ContentFragmentFromCode.newInstance(index);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.details, details);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
            }
        } else {
            //TODO 解决单屏幕的显示问题
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            //Intent intent = new Intent();
            //intent.setClass(getActivity(), DetailsActivity.class);
            //intent.putExtra("index", index);
            //startActivity(intent);
        }
    }
    interface OnArticleSelectedListener {
        public void onArticleSelected(Uri articleUri);
    }
}
