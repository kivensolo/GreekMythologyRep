package com.kingz.play.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.kingz.play.VideoInfo;
import com.kingz.play.presenter.VodInfoPresenter;
import com.kingz.play.view.IPlayerView;
import com.kingz.utils.ZLog;
import com.mplayer.exo_player.DetailPageActivty;

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
        switch (v.getId()) {
            case R.id.iv_detail_arrow_more:
                if (getActivity() != null) {
                    //通过Activity来与Fragment交互
                    ((DetailPageActivty) getActivity()).showOrDismissVideoDetail(true, videoInfo);
                }
                break;
            case R.id.episode_more:
                ZLog.d(TAG,"onClick --- 更多剧集UI暂时未实现");
                //打开剧集Activity  REQUEST_EPISODE
//                Intent episodeIntent = new Intent(getContext(), EpisodeActivity.class);
//                episodeIntent.putExtra("MediaParams", mediaParams);
//                startActivityForResult(episodeIntent, REQUEST_EPISODE);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
