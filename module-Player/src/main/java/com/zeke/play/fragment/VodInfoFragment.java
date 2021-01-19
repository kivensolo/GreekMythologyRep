package com.zeke.play.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kingz.module.common.base.BaseActivity;
import com.kingz.module.common.base.BaseFragment;
import com.kingz.module.common.base.IPresenter;
import com.zeke.kangaroo.utils.ZLog;
import com.zeke.module_player.R;
import com.zeke.play.DetailPageActivty;
import com.zeke.play.VideoInfo;
import com.zeke.play.presenter.VodInfoPresenter;
import com.zeke.play.view.IPlayerView;

/**
 * author：KingZ
 * date：2019/7/30
 * description：影片简单样式详情的Fragment
 */
public class VodInfoFragment extends BaseFragment implements IPlayerView {
    private static final String TAG = "VodInfoFragment";
    private VodInfoPresenter vodInfoPresenter;
    private ScrollView mScrollView;
    private TextView nameTv;        //影片名字
    private TextView scoreTv;       //评分
    private TextView descTv;        //评分
    private CheckBox collectChk;    //收藏按钮
    private VideoInfo videoInfo;

    // 剧集
//    private EpisodeAdapter episodeAdapter;
    private RecyclerView episodeRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vodInfoPresenter = new VodInfoPresenter(this);
        vodInfoPresenter.onCreate();
    }

    private void initViews(View rootView) {
        mScrollView = rootView.findViewById(R.id.vod_detail_scrollview);
        nameTv = rootView.findViewById(R.id.tv_name_video);
        scoreTv = rootView.findViewById(R.id.tv_score_video);
        descTv = rootView.findViewById(R.id.tv_detail_video);
        rootView.findViewById(R.id.iv_detail_arrow_more).setOnClickListener(this);
        collectChk = rootView.findViewById(R.id.chk_collect);
        collectChk.setOnClickListener(this);

        episodeRecyclerView = rootView.findViewById(R.id.episode_recycler);
        episodeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //TODo 进行剧集数据适配器数据绑定
//        episodeAdapter = new EpisodeAdapter(mediaParams);
//        episodeRecyclerView.setAdapter(episodeAdapter);
    }

    private void onPresenterCreateView() {
        if(vodInfoPresenter != null){
            vodInfoPresenter.onCreateView();
        }
        showVideoInfo();
    }

    /**
     * 显示影片信息 (假数据)
     */
    public void showVideoInfo(){
        this.videoInfo = new VideoInfo();
        mScrollView.setVisibility(View.VISIBLE);
        nameTv.setText(videoInfo.videoName);
        scoreTv.setText(videoInfo.userScore);
        descTv.setText(videoInfo.summary);
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
        int id = v.getId();
        if (id == R.id.iv_detail_arrow_more) {
            if (getActivity() != null) {
                //通过Activity来与Fragment交互
                ((DetailPageActivty) getActivity()).showOrDismissVideoDetail(true, videoInfo);
            }
        } else if (id == R.id.episode_more) {
            ZLog.d(TAG, "onClick --- 更多剧集UI暂时未实现");
            //打开剧集Activity  REQUEST_EPISODE
//                Intent episodeIntent = new Intent(getContext(), EpisodeActivity.class);
//                episodeIntent.putExtra("MediaParams", mediaParams);
//                startActivityForResult(episodeIntent, REQUEST_EPISODE);
        } else if (id == R.id.chk_collect) {
            collect();
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
    public void showPlayBufferTips() {

    }

    @Override
    public void dismissPlayBufferTips() {

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.detailpage_vodinfo_layout;
    }

    @Override
    public void onCreateViewReady() {
        super.onCreateViewReady();
        initViews(getRootView());
        onPresenterCreateView();
    }

    @Override
    public void onViewCreated() {
    }
}
