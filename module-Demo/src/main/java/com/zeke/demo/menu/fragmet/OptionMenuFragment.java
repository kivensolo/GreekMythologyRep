package com.zeke.demo.menu.fragmet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;

import com.zeke.demo.R;

/**
 * @ProjectName: AndroidMenuDemo
 * @Package: com.anand.menudemo.fragmet
 * @ClassName: OptionMenuFragment
 * @Description: TODO
 * @Author: Anand
 * @CreateDate: 2019/9/2 22:00
 * @UpdateUser: Anand
 * @UpdateDate: 2019/9/2 22:00
 * @UpdateRemark: TODO:更新说明
 * @Version: 1.0
 */
public class OptionMenuFragment extends BaseFragmet {

    private static final String TAG = "OptionMenuFragment";

    private final int MAIN_GROUP = 0;
    private final int SINGLE_CHECK_GROUP = 1;
    private final int ALL_CHECK_GROUP = 2;

    //item_id
    private final int MENU_SEARCH = 0;
    private final int MENU_SHARE = 1;
    private final int MENU_COLLECT = 2;
    private final int MENU_PREVIOUS = 3;
    private final int MENU_NEXT = 4;

    private final int MENU_SINGLE_CHECK = 5;
    private final int MENU_SINGLE_01 = 51;
    private final int MENU_SINGLE_02 = 52;
    private final int MENU_SINGLE_03 = 53;

    private final int MENU_ALL_CHECK = 6;
    private final int MENU_ALL_01 = 61;
    private final int MENU_ALL_02 = 62;
    private final int MENU_ALL_03 = 63;


    private MenuItem searchMenuItem;            //搜索按钮
    private MenuItem shareMenuItem;             //分享按钮
    private MenuItem collectMenuItem;           //收藏按钮
    private MenuItem previousMenuItem;          //上一步按钮
    private MenuItem nextMenuItem;              //下一步按钮
    private SubMenu singleSubMenu;
    private SubMenu allSubMenu;               //复选按钮组

    private boolean isShowNext = true;                 //是否显示下一步

    public static OptionMenuFragment newInstance(){
        OptionMenuFragment fragment = new OptionMenuFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_option_menu,container,false);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.option_menu,menu);
        //搜索菜单
        searchMenuItem = menu.add(MAIN_GROUP,MENU_SEARCH,1,"搜索菜单");
        searchMenuItem.setIcon(android.R.drawable.ic_menu_search);
        SearchView searchView = new SearchView(getContext());
        SearchView.SearchAutoComplete searchTv = searchView.findViewById(R.id.search_src_text);
        searchTv.setTextColor(getResources().getColor(android.R.color.white));
        searchTv.setHint("搜索…");
        ImageView searchCloseButton = searchView.findViewById(R.id.search_close_btn);
        searchCloseButton.setColorFilter(getResources().getColor(android.R.color.white));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "点击了搜索按钮", Toast.LENGTH_SHORT).show();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getActivity(), "提交内容为："+query, Toast.LENGTH_SHORT).show();
                //TODO 处理搜索的结果逻辑
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG,"输入内容:"+newText);
                return false;
            }
        });
        searchMenuItem.setActionView(searchView);
        searchMenuItem.setOnActionExpandListener(onActionExpandListener);
        searchMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS|MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        //分享菜单
        shareMenuItem = menu.add(MAIN_GROUP,MENU_SHARE,1,"分享菜单");
        shareMenuItem.setIcon(android.R.drawable.ic_menu_share);
        ShareActionProvider shareProvider = new ShareActionProvider(getContext());
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("text/plain");//设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_SUBJECT, "标题");//添加分享内容标题
        share_intent.putExtra(Intent.EXTRA_TEXT, "内容");//添加分享内容
        shareProvider.setShareIntent(share_intent);
        MenuItemCompat.setActionProvider(shareMenuItem,shareProvider);
        shareMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        //收藏菜单
        collectMenuItem = menu.add(MAIN_GROUP,MENU_COLLECT,1,"收藏菜单");
        collectMenuItem.setIcon(android.R.drawable.btn_star_big_on);
        collectMenuItem.setActionView(R.layout.layout_collect);
        collectMenuItem.setOnActionExpandListener(onActionExpandListener);
        collectMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER|MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        //上一步菜单
        previousMenuItem = menu.add(MAIN_GROUP,MENU_PREVIOUS,1,"这是上一步的菜单展示效果");
        previousMenuItem.setTitleCondensed("上一步");
        previousMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //下一步菜单
        nextMenuItem = menu.add(MAIN_GROUP,MENU_NEXT,1,"这是下一步的菜单展示效果");
        nextMenuItem.setTitleCondensed("下一步");
        nextMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //单选菜单
        singleSubMenu = menu.addSubMenu(MAIN_GROUP, MENU_SINGLE_CHECK, 1, "单选按钮");
        singleSubMenu.add(SINGLE_CHECK_GROUP,MENU_SINGLE_01,1,"单选按钮01").setChecked(true);
        singleSubMenu.add(SINGLE_CHECK_GROUP,MENU_SINGLE_02,1,"单选按钮02");
        singleSubMenu.add(SINGLE_CHECK_GROUP,MENU_SINGLE_03,1,"单选按钮03");
        //设置菜单项为单选菜单项，互斥的
        singleSubMenu.setGroupCheckable(SINGLE_CHECK_GROUP,true,true);
        //复选菜单
        allSubMenu = menu.addSubMenu(MAIN_GROUP,MENU_ALL_CHECK,1,"复选按钮");
        allSubMenu.add(ALL_CHECK_GROUP,MENU_ALL_01,1,"复选按钮01").setChecked(true);
        allSubMenu.add(ALL_CHECK_GROUP,MENU_ALL_02,1,"复选按钮02");
        allSubMenu.add(ALL_CHECK_GROUP,MENU_ALL_03,1,"复选按钮03");
        //设置菜单项为复选菜单项
        allSubMenu.setGroupCheckable(ALL_CHECK_GROUP,true,false);
    }

    /**
     * 监听折叠按钮的展开关闭事件
     */
    MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
        @Override
        public boolean onMenuItemActionExpand(MenuItem item) {
            Log.i(TAG, "打开了折叠视图");
            if(previousMenuItem != null && nextMenuItem != null){
                previousMenuItem.setVisible(false);
                nextMenuItem.setVisible(false);
            }
            return true;
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem item) {
            Log.i(TAG, "关闭了折叠视图");
            if(previousMenuItem != null && nextMenuItem != null){
                previousMenuItem.setVisible(true);
                nextMenuItem.setVisible(true);
            }
            return true;
        }
    };

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(menu != null){
            if(previousMenuItem != null && nextMenuItem != null){
                //上一步下一步可点击逻辑效果
                if(isShowNext){
                    previousMenuItem.setEnabled(false);
                    nextMenuItem.setEnabled(true);
                }else {
                    previousMenuItem.setEnabled(true);
                    nextMenuItem.setEnabled(false);
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case MENU_SEARCH:
                //判断收藏菜单是否打开，是的话关闭收藏菜单的拓展
                if(collectMenuItem != null && collectMenuItem.isActionViewExpanded()){
                    collectMenuItem.collapseActionView();
                }
                return true;
            case MENU_PREVIOUS:
                isShowNext = true;
                //通知系统刷新Menu
                getActivity().invalidateOptionsMenu();
                Toast.makeText(getActivity(), "点击上一页菜单", Toast.LENGTH_SHORT).show();
                return true;
            case MENU_NEXT:
                isShowNext = false;
                //通知系统刷新Menu
                getActivity().invalidateOptionsMenu();
                Toast.makeText(getActivity(), "点击下一页菜单", Toast.LENGTH_SHORT).show();
                return true;
            case MENU_SINGLE_01:
            case MENU_SINGLE_02:
            case MENU_SINGLE_03:
                item.setChecked(true);
                Toast.makeText(getActivity(), "单选按钮选中："+item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            case MENU_ALL_01:
            case MENU_ALL_02:
            case MENU_ALL_03:
                item.setChecked(!item.isChecked());
                if(allSubMenu != null){
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < allSubMenu.size(); i++) {
                        if(allSubMenu.getItem(i).isChecked()){
                            sb.append(allSubMenu.getItem(i).getTitle());
                        }
                    }
                    Toast.makeText(getActivity(), "多选按钮选中："+sb.toString(), Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 重置菜单
     */
    private void resetMenuItem(){
        //重置搜索菜单
        if(searchMenuItem != null && searchMenuItem.isActionViewExpanded()){
            searchMenuItem.collapseActionView();
        }
        //重置分享菜单
        if(shareMenuItem != null && shareMenuItem.isActionViewExpanded()){
            shareMenuItem.collapseActionView();
        }
        //重置收藏菜单
        if(collectMenuItem != null && collectMenuItem.isActionViewExpanded()){
            collectMenuItem.collapseActionView();
        }
    }

    //Fragment有点坑，居然没onMenuOpened事件

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        Log.i(TAG, "onOptionsMenuClosed: ");
    }
}
