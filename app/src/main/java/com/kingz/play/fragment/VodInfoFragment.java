package com.kingz.play.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import com.base.BaseActivity;
import com.base.BaseFragment;
import com.base.IPresenter;
import com.kingz.customdemo.R;
import com.kingz.play.presenter.VodInfoPresenter;
import com.kingz.play.view.IPlayerView;
import com.kingz.utils.ZLog;

/**
 * author：KingZ
 * date：2019/7/30
 * description：影片简单样式详情的Fragment
 */
public class VodInfoFragment extends BaseFragment implements IPlayerView,View.OnClickListener{
    private static final String TAG = "VodInfoFragment";
    private VodInfoPresenter vodInfoPresenter;
    private ScrollView mScrollView;
    private TextView nameTv;        //影片名字
    private TextView scoreTv;       //评分
    private TextView descTv;        //评分
    private CheckBox collectChk;    //收藏按钮

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vodInfoPresenter = new VodInfoPresenter(this);
        vodInfoPresenter.onCreate();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vod_detailinfo, container, false);
        initViews(rootView);
        onPresenterCreateView();
        return rootView;
    }

    private void initViews(View rootView) {
        mScrollView = rootView.findViewById(R.id.vod_detail_scrollview);
        nameTv = rootView.findViewById(R.id.tv_name_video);
        scoreTv = rootView.findViewById(R.id.tv_score_video);
        descTv = rootView.findViewById(R.id.tv_detail_video);
        rootView.findViewById(R.id.iv_detail_arrow_more).setOnClickListener(this);
        collectChk = rootView.findViewById(R.id.chk_collect);
        collectChk.setOnClickListener(this);
    }

    private void onPresenterCreateView() {
        if(vodInfoPresenter != null){
            vodInfoPresenter.onCreateView();
        }
        //直接显示假数据页面
        showVideoInfo();
    }

    /**
     * 显示影片信息
     */
    public void showVideoInfo(){
        mScrollView.setVisibility(View.VISIBLE);
        nameTv.setText("测试影片1测试影片1测试影片1测试影片1测试影片1");
        scoreTv.setText("9.9");
        descTv.setText("香港邵氏电影公司(Shaw Brothers)在60年代由何梦华导演拍摄了四部《西游记》系列电影");
        syncCollectState();
    }

    private void syncCollectState() {
        collectChk.setChecked(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (vodInfoPresenter != null) {
            vodInfoPresenter.onDestroyView();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_detail_arrow_more:
                ZLog.d(TAG,"onClick --- 影片详情UI暂时未实现");
                break;
            case R.id.episode_more:
                ZLog.d(TAG,"onClick --- 更多剧集UI暂时未实现");
                break;
            case R.id.chk_collect:
                collect();
                break;
        }
    }

    private void collect() {
        if (!collectChk.isChecked()) {
            ZLog.d(TAG,"onClick ---取消收藏");
        }else{
            ZLog.d(TAG,"onClick ---添加收藏");
        }
    }

        @Override
    public void setPresenter(IPresenter presenter) {
    }

    @Override
    public void showLoading() {
        //do showLoadding
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(View.OnClickListener listener) {

    }

    @Override
    public void showEmpty(View.OnClickListener listener) {

    }

    @Override
    public boolean isShown() {
        return getActivity() != null && ((BaseActivity) getActivity()).isActivityShow() && isVisible();
    }

    @Override
    public void showMessage(String tips) {

    }

    @Override
    public View getPlayView() {
        return null;
    }

    @Override
    public void showPlayLoadingTips(String tips) {

    }

    @Override
    public void showPlayingView() {

    }

    @Override
    public void showPlayCompleteTips(String tips) {

    }

    @Override
    public void showPlayErrorTips(String tips) {

    }

    @Override
    public void updateTitleView() {

    }

    @Override
    public void showPlayStateView() {

    }

    @Override
    public void showPauseStateView() {

    }

    @Override
    public void showFlowTipsView() {

    }

    @Override
    public void switchVisibleState() {

    }

    @Override
    public void updatePlayProgressView(boolean isDrag, int postion) {

    }

    @Override
    public void repostControllersDismissTask(boolean enable) {

    }

    @Override
    public void dismissPop() {

    }
}
