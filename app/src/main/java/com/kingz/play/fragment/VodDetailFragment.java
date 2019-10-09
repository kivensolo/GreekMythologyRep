package com.kingz.play.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.BaseFragment;
import com.kingz.customdemo.R;
import com.kingz.play.VideoInfo;
import com.mplayer.exo_player.DetailPageActivty;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vod_detail, container, false);
        nameScoreTextview = rootView.findViewById(R.id.tv_name_video);
        newEpisodeTv = rootView.findViewById(R.id.tv_episode_desc);
        actorTv = rootView.findViewById(R.id.tv_actor);
        directorTv = rootView.findViewById(R.id.tv_director);
        detailTv = rootView.findViewById(R.id.video_detail);
        rootView.findViewById(R.id.arrow_down).setOnClickListener(this);
        showUI();
        return rootView;
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
        switch (v.getId()) {
            case R.id.arrow_down:
                if (getActivity() != null) {
                    ((DetailPageActivty) getActivity()).showOrDismissVideoDetail(false, videoInfo);
                }
                break;
        }
    }
}
