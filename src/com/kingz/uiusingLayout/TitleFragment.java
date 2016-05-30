package com.kingz.uiusingLayout;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.kingz.uiusingListViews.R;

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
    boolean mDualPane;
    int mCurCheckPosition = 0;
    OnArticleSelectedListener mListener;

    // Container Activity must implement this interface
    public interface OnArticleSelectedListener {
        public void onArticleSelected(Uri articleUri);
    }

    //-------------Created  (Start)
    @Override
    public void onAttach(Activity activity) {
        //在片段已与 Activity 关联时调用（Activity 传递到此方法内）
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

     /**
     * @param inflater
     * @param container  片段布局将插入到的父ViewGroup（来自 Activity 的布局）
     * @param savedInstanceState savedInstanceState 参数是在恢复片段时，提供上一片段实例相关数据的 Bundle（处理片段生命周期部分对恢复状态做了详细阐述）。
     * @return View
     */
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


    private String[] TITLES = {"体育新闻","娱乐新闻","科技新闻","内涵段子"};
    /**
     * 在 Activity 的 onCreate() 方法已返回时调用
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Populate list with our static array of titles.
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1, TITLES));

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.content_fragment);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showDetails(mCurCheckPosition);
        }

    }
    //-------------Created  (End)

    //-------------Started
    @Override
    public void onStart() {
        super.onStart();
    }

    //-------------Resumed
    @Override
    public void onResume() {
        super.onResume();
    }

    //-------------Paused
    @Override
    public void onPause() {
        super.onPause();
    }

    //-------------Stopped
    @Override
    public void onStop() {
        super.onStop();
    }

    //-------------Destroyed (Start)
    @Override
    public void onDestroyView() {
        //在删除与片段关联的视图层次结构时调用。
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        //在取消片段与 Activity 的关联时调用
        super.onDetach();
    }
   //-------------Destroyed (End)

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
         // Append the clicked item's row ID with the content provider Uri
        //Uri noteUri = ContentUris.withAppendedId(ArticleColumns.CONTENT_URI, id);
        // Send the event and Uri to the host activity
        //mListener.onArticleSelected(noteUri);
    }

     /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            ContentFragment details = (ContentFragment)getFragmentManager().findFragmentById(R.id.content_fragment);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = ContentFragment.newInstance(index);
                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (index == 0) {
                    ft.replace(R.id.content_fragment, details);
                } else {
                    //ft.replace(R.id.a_item, details);
                }
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            //Intent intent = new Intent();
            //intent.setClass(getActivity(), DetailsActivity.class);
            //intent.putExtra("index", index);
            //startActivity(intent);
        }
    }
}
