package com.zeke.play.fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kingz.module.common.base.BaseFragment
import com.zeke.module_player.R
import com.zeke.play.VideoInfo
import com.zeke.play.activities.DetailPageActivty

/**
 * author：KingZ
 * date：2019/10/9
 * description：视频简介Fragment
 */
class VodDetailFragment : BaseFragment(), View.OnClickListener {
    //影片名字 评分
    private var nameScoreTextview : TextView? = null
    //最新，总集数
    private var newEpisodeTv : TextView? = null
    //演员
    private var actorTv : TextView? = null
    //导演
    private var directorTv : TextView? = null
    //影片简介
    private var detailTv : TextView? = null
    //视频简介信息
    private var videoInfo : VideoInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        videoInfo = arguments?.getSerializable(DETAIL_ARG_KEY) as VideoInfo
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId():Int  = R.layout.fragment_vod_detail

    override fun onCreateViewReady() {
        super.onCreateViewReady()
        nameScoreTextview = rootView?.findViewById(R.id.tv_name_video)
        newEpisodeTv = rootView?.findViewById(R.id.tv_episode_desc)
        actorTv = rootView?.findViewById(R.id.tv_actor)
        directorTv = rootView?.findViewById(R.id.tv_director)
        detailTv = rootView?.findViewById(R.id.video_detail)
        rootView?.findViewById<View>(R.id.arrow_down)?.setOnClickListener(this)
        showUI()
    }

    private fun showUI() {
        if (videoInfo != null) {
            val spannableString =
                SpannableString(videoInfo!!.videoName + "   " + videoInfo!!.userScore)
            val foregroundColorSpan =
                ForegroundColorSpan(ContextCompat.getColor(context!!,R.color.accent_A200))
            spannableString.setSpan(
                foregroundColorSpan,
                (videoInfo!!.videoName + "   ").length,
                spannableString.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            nameScoreTextview!!.text = spannableString
            //剧集显示
            newEpisodeTv!!.visibility = View.VISIBLE
            newEpisodeTv!!.text = String.format(
                context!!.getString(R.string.txt_tv_new),
                (videoInfo!!.newEpisodeIndex + 1).toString(),
                videoInfo!!.episodeCount.toString()
            )
            //主演
            actorTv!!.text = String.format(context!!.getString(R.string.txt_actor),"古天乐")
            directorTv!!.text = String.format(context!!.getString(R.string.txt_director),"吴炫辉")
            detailTv!!.text = String.format(context!!.getString(R.string.txt_detail),
                if (videoInfo!!.summary == null) "" else videoInfo!!.summary
            )
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.arrow_down) {
            if (activity != null) {
                (activity as DetailPageActivty?)!!.showOrDismissVideoDetail(false, videoInfo)
            }
        }
    }

    override fun onViewCreated() {}

    companion object {
        const val DETAIL_ARG_KEY = "VideoInfo"
        @JvmStatic
        fun newInstance(videoInfo: VideoInfo?): VodDetailFragment {
            val vodDetailFragment = VodDetailFragment()
            val bundle = Bundle()
            bundle.putSerializable(DETAIL_ARG_KEY, videoInfo)
            vodDetailFragment.arguments = bundle
            return vodDetailFragment
        }
    }
}