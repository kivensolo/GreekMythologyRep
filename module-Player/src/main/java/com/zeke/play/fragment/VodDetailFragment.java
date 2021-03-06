package com.zeke.play.fragment;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.kingz.module.common.base.BaseFragment;
import com.zeke.module_player.R;
import com.zeke.play.DetailPageActivty;
import com.zeke.play.VideoInfo;

/**
 * author：KingZ
 * date：2019/10/9
 * description：视频简介Fragment
 */
public class VodDetailFragment extends BaseFragment implements View.OnClickListener{
    private TextView nameScoreTextview;  //影片名字 评分
    private TextView newEpisodeTv;  //最新，总集数
    private TextView actorTv; //演员
    private TextView directorTv; //导演
    private TextView detailTv; //影片简介
    private VideoInfo videoInfo; //视频简介信息
    public static final String DETAIL_ARG_KEY = "VideoInfo";

    public static VodDetailFragment newInstance(VideoInfo videoInfo) {
        VodDetailFragment vodDetailFragment = new VodDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DETAIL_ARG_KEY, videoInfo);
        vodDetailFragment.setArguments(bundle);
        return vodDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            videoInfo = (VideoInfo) getArguments().getSerializable(DETAIL_ARG_KEY);
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_vod_detail;
    }

    @Override
    public void onCreateViewReady() {
        super.onCreateViewReady();
        View rootView = getRootView();
        if(rootView != null){
            nameScoreTextview = rootView.findViewById(R.id.tv_name_video);
            newEpisodeTv = rootView.findViewById(R.id.tv_episode_desc);
            actorTv = rootView.findViewById(R.id.tv_actor);
            directorTv = rootView.findViewById(R.id.tv_director);
            detailTv = rootView.findViewById(R.id.video_detail);
            rootView.findViewById(R.id.arrow_down).setOnClickListener(this);
        }
        showUI();
    }

    private void showUI() {
        if (videoInfo != null) {
            SpannableString spannableString = new SpannableString(videoInfo.videoName + "   " + videoInfo.userScore);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.accent_A200));
            spannableString.setSpan(foregroundColorSpan, (videoInfo.videoName + "   ").length(), spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            nameScoreTextview.setText(spannableString);

            //剧集显示
            newEpisodeTv.setVisibility(View.VISIBLE);
            newEpisodeTv.setText(String.format(getContext().getString(R.string.txt_tv_new), String.valueOf(videoInfo.newEpisodeIndex + 1),
                    String.valueOf(videoInfo.episodeCount)));
            //主演
            actorTv.setText(String.format(getContext().getString(R.string.txt_actor), "古天乐"));
            directorTv.setText(String.format(getContext().getString(R.string.txt_director), "吴炫辉"));
            detailTv.setText(String.format(getContext().getString(R.string.txt_detail), videoInfo.summary == null ? "" : videoInfo.summary));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.arrow_down) {
            if (getActivity() != null) {
                ((DetailPageActivty) getActivity()).showOrDismissVideoDetail(false, videoInfo);
            }
        }
    }

    @Override
    public void onViewCreated() {

    }
}
